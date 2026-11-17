package ma.credit360.service;

import ma.credit360.entity.*;
import ma.credit360.entity.enums.Role;
import ma.credit360.entity.enums.StatutComplement;
import ma.credit360.entity.enums.StatutDossier;
import ma.credit360.entity.enums.TypeNotification;
import ma.credit360.repository.AnalyseDossierRepository;
import ma.credit360.repository.ComplementDossierRepository;
import ma.credit360.repository.DossierRepository;
import ma.credit360.repository.HistoriqueStatutRepository;
import ma.credit360.web.dto.*;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowService {

    private final DossierRepository dossierRepository;
    private final AnalyseDossierRepository analyseDossierRepository;
    private final ComplementDossierRepository complementDossierRepository;
    private final HistoriqueStatutRepository historiqueStatutRepository;
    private final DossierService dossierService;
    private final AuthService authService;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public WorkflowService(DossierRepository dossierRepository,
                           AnalyseDossierRepository analyseDossierRepository,
                           ComplementDossierRepository complementDossierRepository,
                           HistoriqueStatutRepository historiqueStatutRepository,
                           DossierService dossierService,
                           AuthService authService,
                           AuditService auditService,
                           NotificationService notificationService) {
        this.dossierRepository = dossierRepository;
        this.analyseDossierRepository = analyseDossierRepository;
        this.complementDossierRepository = complementDossierRepository;
        this.historiqueStatutRepository = historiqueStatutRepository;
        this.dossierService = dossierService;
        this.authService = authService;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<DossierItemDto> listerDossiersATraiter() {
        return dossierRepository.findByStatutAndAnalysteAffecteIsNullOrderByDateMajDesc(StatutDossier.EN_ANALYSE).stream()
                .map(DossierItemDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DossierItemDto> listerMesDossiers() {
        Utilisateur currentUser = authService.getCurrentUser();
        return dossierRepository.findByAnalysteAffecteOrderByDateMajDesc(currentUser).stream()
                .map(DossierItemDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void prendreEnCharge(String reference, PriseEnChargeRequest request) {
        Dossier dossier = dossierService.trouverParReference(reference);
        Utilisateur currentUser = authService.getCurrentUser();

        if (dossier.getAnalysteAffecte() != null && !dossier.getAnalysteAffecte().getId().equals(currentUser.getId())) {
            throw new BusinessException("Ce dossier est déjà pris en charge par l'analyste " + dossier.getAnalysteAffecte().getNom());
        }

        StatutDossier ancienStatut = dossier.getStatut();
        dossier.setAnalysteAffecte(currentUser);
        dossier.setStatut(StatutDossier.EN_COURS);
        dossierRepository.save(dossier);

        String commentaire = (request != null && request.getCommentaire() != null && !request.getCommentaire().isBlank())
                ? request.getCommentaire() : "Dossier pris en charge pour analyse approfondie";

        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                ancienStatut,
                StatutDossier.EN_COURS,
                "PRISE_EN_CHARGE",
                currentUser,
                commentaire
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("PRISE_EN_CHARGE", "WORKFLOW", dossier.getReference(), "Dossier pris en charge par " + currentUser.getNom());

        if (dossier.getCreePar() != null) {
            notificationService.envoyerNotification(
                    dossier.getCreePar(),
                    "Dossier pris en charge",
                    "Votre dossier " + dossier.getReference() + " a été pris en charge par l'analyste " + currentUser.getNom() + ".",
                    TypeNotification.AFFECTATION,
                    dossier.getReference()
            );
        }
    }

    @Transactional
    public void liberer(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        Utilisateur currentUser = authService.getCurrentUser();

        if (dossier.getAnalysteAffecte() == null) {
            throw new BusinessException("Ce dossier n'est affecté à aucun analyste");
        }

        if (currentUser.getRole() != Role.RESPONSABLE && currentUser.getRole() != Role.ADMINISTRATEUR
                && !dossier.getAnalysteAffecte().getId().equals(currentUser.getId())) {
            throw new BusinessException("Vous ne pouvez libérer que vos propres dossiers");
        }

        StatutDossier ancienStatut = dossier.getStatut();
        dossier.setAnalysteAffecte(null);
        dossier.setStatut(StatutDossier.EN_ANALYSE);
        dossierRepository.save(dossier);

        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                ancienStatut,
                StatutDossier.EN_ANALYSE,
                "LIBERATION",
                currentUser,
                "Dossier libéré et remis en attente d'affectation"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("LIBERATION_DOSSIER", "WORKFLOW", dossier.getReference(), "Dossier libéré");
    }

    @Transactional
    public AnalyseResponseDto obtenirOuInitialiserAnalyse(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        AnalyseDossier analyse = analyseDossierRepository.findByDossier(dossier).orElse(null);

        if (analyse == null) {
            analyse = new AnalyseDossier();
            analyse.setDossier(dossier);
            analyse.setRevenuMensuel(dossier.getRevenuMensuel());
            analyse.setChargesMensuelles(dossier.getChargesMensuelles() != null ? dossier.getChargesMensuelles() : 0.0);

            // Estimation de la mensualité
            double taux = dossier.getTypeCredit() != null ? dossier.getTypeCredit().getTaux() : 5.0;
            double r = (taux / 100.0) / 12.0;
            double mensualite = (dossier.getMontant() * r) / (1 - Math.pow(1 + r, -dossier.getDuree()));
            mensualite = round(mensualite, 2);
            analyse.setMensualiteEstimee(mensualite);

            double charges = analyse.getChargesMensuelles();
            double revenu = analyse.getRevenuMensuel();
            double tauxEndettement = round(((mensualite + charges) / revenu) * 100.0, 2);
            double capacite = round((revenu * 0.45) - charges, 2);

            analyse.setTauxEndettement(tauxEndettement);
            analyse.setCapaciteRemboursement(capacite);
            analyse.setEligible(tauxEndettement <= 45.0 && mensualite <= capacite);
            analyse.setObservations("Analyse financière initialisée automatiquement. À vérifier avec les pièces justificatives.");
            analyse.setAnalyste(authService.getCurrentUser());
            analyse.setDateAnalyse(LocalDateTime.now());
            analyse = analyseDossierRepository.save(analyse);
        }

        return new AnalyseResponseDto(analyse);
    }

    @Transactional
    public AnalyseResponseDto enregistrerAnalyse(String reference, AnalyseRequest request) {
        Dossier dossier = dossierService.trouverParReference(reference);
        Utilisateur currentUser = authService.getCurrentUser();

        AnalyseDossier analyse = analyseDossierRepository.findByDossier(dossier)
                .orElseGet(() -> {
                    AnalyseDossier a = new AnalyseDossier();
                    a.setDossier(dossier);
                    return a;
                });

        if (request.getRevenuMensuel() != null) analyse.setRevenuMensuel(request.getRevenuMensuel());
        if (request.getChargesMensuelles() != null) analyse.setChargesMensuelles(request.getChargesMensuelles());
        if (request.getMensualiteEstimee() != null) analyse.setMensualiteEstimee(request.getMensualiteEstimee());
        if (request.getTauxEndettement() != null) analyse.setTauxEndettement(request.getTauxEndettement());
        if (request.getCapaciteRemboursement() != null) analyse.setCapaciteRemboursement(request.getCapaciteRemboursement());
        if (request.getEligible() != null) analyse.setEligible(request.getEligible());
        if (request.getObservations() != null) analyse.setObservations(request.getObservations());

        analyse.setAnalyste(currentUser);
        analyse.setDateAnalyse(LocalDateTime.now());
        analyse = analyseDossierRepository.save(analyse);

        auditService.tracer("ENREGISTREMENT_ANALYSE", "ANALYSE", dossier.getReference(), "Données d'analyse mises à jour");
        return new AnalyseResponseDto(analyse);
    }

    @Transactional
    public AnalyseResponseDto terminerAnalyse(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        AnalyseDossier analyse = analyseDossierRepository.findByDossier(dossier)
                .orElseThrow(() -> new BusinessException("Aucune analyse enregistrée pour ce dossier"));

        analyse.setTermine(true);
        analyse = analyseDossierRepository.save(analyse);

        Utilisateur currentUser = authService.getCurrentUser();
        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                dossier.getStatut(),
                dossier.getStatut(),
                "ANALYSE_TERMINEE",
                currentUser,
                "Analyse financière et étude de risque clôturées"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("CLOTURE_ANALYSE", "ANALYSE", dossier.getReference(), "Analyse marquée comme terminée");
        return new AnalyseResponseDto(analyse);
    }

    @Transactional
    public ComplementDto demanderComplement(String reference, DemandeComplementRequest request) {
        Dossier dossier = dossierService.trouverParReference(reference);
        Utilisateur currentUser = authService.getCurrentUser();

        ComplementDossier comp = new ComplementDossier();
        comp.setDossier(dossier);
        comp.setMotif(request.getMotif());
        comp.setCommentaire(request.getCommentaire());
        if (request.getPiecesConcernees() != null && !request.getPiecesConcernees().isEmpty()) {
            comp.setPiecesConcernees(request.getPiecesConcernees().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        comp.setStatut(StatutComplement.EN_ATTENTE);
        comp.setDemandePar(currentUser);
        comp = complementDossierRepository.save(comp);

        StatutDossier ancienStatut = dossier.getStatut();
        dossier.setStatut(StatutDossier.COMPLEMENT_REQUIS);
        dossierRepository.save(dossier);

        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                ancienStatut,
                StatutDossier.COMPLEMENT_REQUIS,
                "DEMANDE_COMPLEMENT",
                currentUser,
                "Demande de complément: " + request.getMotif() + " (" + request.getCommentaire() + ")"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("DEMANDE_COMPLEMENT", "WORKFLOW", dossier.getReference(), "Demande de complément émise: " + request.getMotif());

        if (dossier.getCreePar() != null) {
            notificationService.envoyerNotification(
                    dossier.getCreePar(),
                    "Complément requis pour le dossier " + dossier.getReference(),
                    "L'analyste " + currentUser.getNom() + " a demandé des compléments : " + request.getMotif(),
                    TypeNotification.COMPLEMENT_DEMANDE,
                    dossier.getReference()
            );
        }

        return new ComplementDto(comp);
    }

    @Transactional(readOnly = true)
    public List<ComplementDto> listerComplements(String reference) {
        return complementDossierRepository.findByDossierReferenceOrderByDateDemandeDesc(reference).stream()
                .map(ComplementDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComplementDto repondreComplement(String reference, Long complementId, ReponseComplementRequest request) {
        Dossier dossier = dossierService.trouverParReference(reference);
        ComplementDossier comp = complementDossierRepository.findById(complementId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande de complément introuvable: " + complementId));

        comp.setStatut(StatutComplement.REPONDU);
        comp.setDateReponse(LocalDateTime.now());
        if (request != null && request.getCommentaire() != null) {
            comp.setReponseCommentaire(request.getCommentaire());
        }
        comp = complementDossierRepository.save(comp);

        Utilisateur currentUser = authService.getCurrentUser();
        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                dossier.getStatut(),
                dossier.getStatut(),
                "REPONSE_COMPLEMENT",
                currentUser,
                "Réponse au complément apportée: " + (request != null ? request.getCommentaire() : "")
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("REPONSE_COMPLEMENT", "WORKFLOW", dossier.getReference(), "Réponse au complément fournie");

        if (comp.getDemandePar() != null) {
            notificationService.envoyerNotification(
                    comp.getDemandePar(),
                    "Réponse reçue sur complément",
                    "Le conseiller a répondu à la demande de complément sur le dossier " + dossier.getReference(),
                    TypeNotification.COMPLEMENT_DEMANDE,
                    dossier.getReference()
            );
        }

        return new ComplementDto(comp);
    }

    @Transactional
    public ComplementDto cloturerComplement(String reference, Long complementId) {
        Dossier dossier = dossierService.trouverParReference(reference);
        ComplementDossier comp = complementDossierRepository.findById(complementId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande de complément introuvable: " + complementId));

        comp.setStatut(StatutComplement.CLOTURE);
        comp.setDateCloture(LocalDateTime.now());
        comp = complementDossierRepository.save(comp);

        // Repasser le dossier en cours d'analyse
        StatutDossier ancien = dossier.getStatut();
        dossier.setStatut(StatutDossier.EN_COURS);
        dossierRepository.save(dossier);

        Utilisateur currentUser = authService.getCurrentUser();
        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                ancien,
                StatutDossier.EN_COURS,
                "CLOTURE_COMPLEMENT",
                currentUser,
                "Complément vérifié et clôturé avec succès"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("CLOTURE_COMPLEMENT", "WORKFLOW", dossier.getReference(), "Complément clôturé");
        return new ComplementDto(comp);
    }

    @Transactional(readOnly = true)
    public List<HistoriqueDto> listerHistorique(String reference) {
        return historiqueStatutRepository.findByDossierReferenceOrderByDateActionDesc(reference).stream()
                .map(HistoriqueDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistoriqueDto obtenirHistoriqueItem(String reference, Long id) {
        HistoriqueStatut hist = historiqueStatutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Élément d'historique introuvable: " + id));
        return new HistoriqueDto(hist);
    }

    private double round(double val, int places) {
        return BigDecimal.valueOf(val).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }
}
