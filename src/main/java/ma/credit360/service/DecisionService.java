package ma.credit360.service;

import ma.credit360.entity.*;
import ma.credit360.entity.enums.StatutDossier;
import ma.credit360.entity.enums.TypeDecision;
import ma.credit360.entity.enums.TypeNotification;
import ma.credit360.repository.DecisionRepository;
import ma.credit360.repository.DossierRepository;
import ma.credit360.repository.HistoriqueStatutRepository;
import ma.credit360.web.dto.DecisionRequest;
import ma.credit360.web.dto.DecisionResponseDto;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import ma.credit360.web.exception.UnauthorizedOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DecisionService {

    private final DecisionRepository decisionRepository;
    private final DossierRepository dossierRepository;
    private final HistoriqueStatutRepository historiqueStatutRepository;
    private final DossierService dossierService;
    private final HabilitationService habilitationService;
    private final AuthService authService;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public DecisionService(DecisionRepository decisionRepository,
                           DossierRepository dossierRepository,
                           HistoriqueStatutRepository historiqueStatutRepository,
                           DossierService dossierService,
                           HabilitationService habilitationService,
                           AuthService authService,
                           AuditService auditService,
                           NotificationService notificationService) {
        this.decisionRepository = decisionRepository;
        this.dossierRepository = dossierRepository;
        this.historiqueStatutRepository = historiqueStatutRepository;
        this.dossierService = dossierService;
        this.habilitationService = habilitationService;
        this.authService = authService;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    @Transactional
    public DecisionResponseDto enregistrerDecision(String reference, DecisionRequest request) {
        if (request == null || request.getTypeDecision() == null) {
            throw new IllegalArgumentException("Le type de décision (VALIDATION, REFUS, AJOURNEMENT, DEMANDE_COMPLEMENT) est obligatoire");
        }

        Dossier dossier = dossierService.trouverParReference(reference);
        Utilisateur currentUser = authService.getCurrentUser();

        // Vérification de l'étape du dossier
        if (dossier.getStatut() == StatutDossier.A_COMPLETER) {
            throw new BusinessException("Impossible d'enregistrer une décision : le dossier est en cours de constitution et n'a pas encore été soumis à l'analyse");
        }

        if (dossier.getStatut() == StatutDossier.ACCEPTE || dossier.getStatut() == StatutDossier.REFUSE || dossier.getStatut() == StatutDossier.ANNULE) {
            throw new BusinessException("Impossible de prendre une décision sur un dossier clôturé (" + dossier.getStatut().getLibelle() + ")");
        }

        if (request.getTypeDecision() == TypeDecision.REFUS && (request.getMotif() == null || request.getMotif().isBlank())) {
            throw new IllegalArgumentException("Un motif explicatif est obligatoire pour tout refus de crédit");
        }

        double montantCible = request.getMontantAccorde() != null ? request.getMontantAccorde() : dossier.getMontant();

        if (request.getTypeDecision() == TypeDecision.VALIDATION) {
            if (!habilitationService.estHabilite(currentUser.getRole(), montantCible)) {
                throw new UnauthorizedOperationException("Votre rôle (" + currentUser.getRole() + ") n'est pas habilité à valider un montant de "
                        + montantCible + " DH. Escalade nécessaire vers un niveau hiérarchique supérieur.");
            }
        }

        Decision decision = new Decision();
        decision.setDossier(dossier);
        decision.setTypeDecision(request.getTypeDecision());
        decision.setMontantAccorde(request.getMontantAccorde() != null ? request.getMontantAccorde() : dossier.getMontant());
        decision.setDureeAccordee(request.getDureeAccordee() != null ? request.getDureeAccordee() : dossier.getDuree());
        Double tauxParDefaut = (dossier.getTypeCredit() != null && dossier.getTypeCredit().getTaux() != null) ? dossier.getTypeCredit().getTaux() : 5.0;
        decision.setTauxAccorde(request.getTauxAccorde() != null ? request.getTauxAccorde() : tauxParDefaut);
        decision.setMotif(request.getMotif());
        decision.setDecidePar(currentUser);
        decision = decisionRepository.save(decision);

        StatutDossier ancienStatut = dossier.getStatut();
        StatutDossier nouveauStatut;

        switch (request.getTypeDecision()) {
            case VALIDATION -> nouveauStatut = StatutDossier.ACCEPTE;
            case REFUS -> nouveauStatut = StatutDossier.REFUSE;
            case AJOURNEMENT -> nouveauStatut = StatutDossier.EN_COURS;
            case DEMANDE_COMPLEMENT -> nouveauStatut = StatutDossier.COMPLEMENT_REQUIS;
            default -> nouveauStatut = ancienStatut;
        }

        dossier.setStatut(nouveauStatut);
        dossierRepository.save(dossier);

        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                ancienStatut,
                nouveauStatut,
                "DECISION_" + request.getTypeDecision().name(),
                currentUser,
                "Décision prise: " + request.getTypeDecision().name() + " - " + (request.getMotif() != null ? request.getMotif() : "Conforme")
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("DECISION_CREDIT", "DECISION", dossier.getReference(), "Décision: " + request.getTypeDecision().name() + " par " + currentUser.getNom());

        // Notifications
        if (dossier.getCreePar() != null) {
            String titre = (request.getTypeDecision() == TypeDecision.VALIDATION) ? "Dossier Validé !" : "Décision sur le dossier";
            String msg = "Le dossier " + dossier.getReference() + " a reçu une décision: " + request.getTypeDecision().name() + ".";
            notificationService.envoyerNotification(dossier.getCreePar(), titre, msg, TypeNotification.DECISION_PRISE, dossier.getReference());
        }

        return new DecisionResponseDto(decision);
    }

    @Transactional(readOnly = true)
    public List<DecisionResponseDto> listerDecisions(String reference) {
        return decisionRepository.findByDossierReferenceOrderByDateDecisionDesc(reference).stream()
                .map(DecisionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DecisionResponseDto obtenirDecisionCourante(String reference) {
        Decision d = decisionRepository.findFirstByDossierReferenceOrderByDateDecisionDesc(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Aucune décision enregistrée pour ce dossier"));
        return new DecisionResponseDto(d);
    }
}
