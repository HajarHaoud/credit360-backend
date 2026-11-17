package ma.credit360.service;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.HistoriqueStatut;
import ma.credit360.entity.TypeCredit;
import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.Role;
import ma.credit360.entity.enums.StatutDossier;
import ma.credit360.repository.*;
import ma.credit360.web.dto.*;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import ma.credit360.web.exception.UnauthorizedOperationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DossierService {

    private final DossierRepository dossierRepository;
    private final TypeCreditRepository typeCreditRepository;
    private final PieceRequiseRepository pieceRequiseRepository;
    private final PieceDeposeeRepository pieceDeposeeRepository;
    private final HistoriqueStatutRepository historiqueStatutRepository;
    private final AuthService authService;
    private final AuditService auditService;

    public DossierService(DossierRepository dossierRepository,
                          TypeCreditRepository typeCreditRepository,
                          PieceRequiseRepository pieceRequiseRepository,
                          PieceDeposeeRepository pieceDeposeeRepository,
                          HistoriqueStatutRepository historiqueStatutRepository,
                          AuthService authService,
                          AuditService auditService) {
        this.dossierRepository = dossierRepository;
        this.typeCreditRepository = typeCreditRepository;
        this.pieceRequiseRepository = pieceRequiseRepository;
        this.pieceDeposeeRepository = pieceDeposeeRepository;
        this.historiqueStatutRepository = historiqueStatutRepository;
        this.authService = authService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public PageDto<DossierItemDto> rechercherDossiers(String query, String statutStr, String typeId, int page, int taille) {
        Utilisateur currentUser = authService.getCurrentUser();
        Utilisateur filtreConseiller = (currentUser.getRole() == Role.CONSEILLER) ? currentUser : null;

        StatutDossier statut = null;
        if (statutStr != null && !statutStr.isBlank()) {
            try {
                statut = StatutDossier.valueOf(statutStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignore invalid enum and search all or match libelle
                for (StatutDossier sd : StatutDossier.values()) {
                    if (sd.getLibelle().equalsIgnoreCase(statutStr) || sd.name().equalsIgnoreCase(statutStr)) {
                        statut = sd;
                        break;
                    }
                }
            }
        }

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), taille, Sort.by(Sort.Direction.DESC, "dateMaj"));
        Page<Dossier> resultPage = dossierRepository.searchDossiers(
                filtreConseiller,
                statut,
                (typeId != null && !typeId.isBlank()) ? typeId : null,
                (query != null && !query.isBlank()) ? query.trim() : null,
                pageable
        );

        List<DossierItemDto> dtos = resultPage.getContent().stream()
                .map(DossierItemDto::new)
                .collect(Collectors.toList());

        return PageDto.from(resultPage, dtos);
    }

    @Transactional(readOnly = true)
    public List<DossierItemDto> obtenirDossiersRecents() {
        Utilisateur currentUser = authService.getCurrentUser();
        List<Dossier> dossiers;
        if (currentUser.getRole() == Role.CONSEILLER) {
            dossiers = dossierRepository.findTop5ByCreeParOrderByDateMajDesc(currentUser);
        } else {
            dossiers = dossierRepository.findTop5ByOrderByDateMajDesc();
        }
        return dossiers.stream().map(DossierItemDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DossierStatsDto obtenirStatistiques() {
        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser.getRole() == Role.CONSEILLER) {
            long total = dossierRepository.countByCreePar(currentUser);
            long aCompleter = dossierRepository.countByStatutAndCreePar(StatutDossier.A_COMPLETER, currentUser);
            long enAnalyse = dossierRepository.countByStatutAndCreePar(StatutDossier.EN_ANALYSE, currentUser);
            long enCours = dossierRepository.countByStatutAndCreePar(StatutDossier.EN_COURS, currentUser);
            long complement = dossierRepository.countByStatutAndCreePar(StatutDossier.COMPLEMENT_REQUIS, currentUser);
            long valides = dossierRepository.countByStatutAndCreePar(StatutDossier.ACCEPTE, currentUser);
            long refuses = dossierRepository.countByStatutAndCreePar(StatutDossier.REFUSE, currentUser);
            long annules = dossierRepository.countByStatutAndCreePar(StatutDossier.ANNULE, currentUser);
            return new DossierStatsDto(total, aCompleter, enAnalyse, enCours, complement, valides, refuses, annules);
        } else {
            long total = dossierRepository.count();
            long aCompleter = dossierRepository.countByStatut(StatutDossier.A_COMPLETER);
            long enAnalyse = dossierRepository.countByStatut(StatutDossier.EN_ANALYSE);
            long enCours = dossierRepository.countByStatut(StatutDossier.EN_COURS);
            long complement = dossierRepository.countByStatut(StatutDossier.COMPLEMENT_REQUIS);
            long valides = dossierRepository.countByStatut(StatutDossier.ACCEPTE);
            long refuses = dossierRepository.countByStatut(StatutDossier.REFUSE);
            long annules = dossierRepository.countByStatut(StatutDossier.ANNULE);
            return new DossierStatsDto(total, aCompleter, enAnalyse, enCours, complement, valides, refuses, annules);
        }
    }

    @Transactional(readOnly = true)
    public DossierDetailDto obtenirParReference(String reference) {
        Dossier dossier = trouverParReference(reference);
        verifierAccesDossier(dossier);

        boolean modifiable = (dossier.getStatut() == StatutDossier.A_COMPLETER || dossier.getStatut() == StatutDossier.COMPLEMENT_REQUIS);
        int requises = pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(dossier.getTypeCredit().getId()).size();
        int deposees = pieceDeposeeRepository.findByDossier(dossier).size();

        return new DossierDetailDto(dossier, modifiable, requises, deposees);
    }

    @Transactional
    public DossierDetailDto creer(CreateDossierRequest request) {
        Utilisateur currentUser = authService.getCurrentUser();
        TypeCredit typeCredit = typeCreditRepository.findById(request.getTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Type de crédit introuvable: " + request.getTypeId()));

        if (!typeCredit.isActif()) {
            throw new BusinessException("Ce type de crédit n'est pas actif pour la création de nouveaux dossiers");
        }

        if (request.getMontant() < typeCredit.getMontantMin() || request.getMontant() > typeCredit.getMontantMax()) {
            throw new BusinessException("Le montant (" + request.getMontant() + " DH) doit être compris entre "
                    + typeCredit.getMontantMin() + " et " + typeCredit.getMontantMax() + " DH");
        }

        if (request.getDuree() < typeCredit.getDureeMin() || request.getDuree() > typeCredit.getDureeMax()) {
            throw new BusinessException("La durée (" + request.getDuree() + " mois) doit être comprise entre "
                    + typeCredit.getDureeMin() + " et " + typeCredit.getDureeMax() + " mois");
        }

        Dossier dossier = new Dossier();
        dossier.setReference(genererReferenceUnique());
        dossier.setNom(request.getNom());
        dossier.setPrenom(request.getPrenom());
        dossier.setCin(request.getCin().toUpperCase());
        dossier.setTelephone(request.getTelephone());
        dossier.setEmail(request.getEmail());
        dossier.setProfession(request.getProfession());
        dossier.setAdresse(request.getAdresse());
        dossier.setRevenuMensuel(request.getRevenuMensuel());
        dossier.setChargesMensuelles(request.getChargesMensuelles() != null ? request.getChargesMensuelles() : 0.0);
        dossier.setTypeCredit(typeCredit);
        dossier.setMontant(request.getMontant());
        dossier.setDuree(request.getDuree());
        dossier.setStatut(StatutDossier.A_COMPLETER);
        dossier.setCreePar(currentUser);

        dossier = dossierRepository.save(dossier);

        // Historique de création
        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                null,
                StatutDossier.A_COMPLETER,
                "CREATION",
                currentUser,
                "Création initiale de la demande de crédit"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("CREATION_DOSSIER", "DOSSIER", dossier.getReference(), "Dossier créé pour le client " + dossier.getNom() + " " + dossier.getPrenom());

        int requises = pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(typeCredit.getId()).size();
        return new DossierDetailDto(dossier, true, requises, 0);
    }

    @Transactional
    public DossierDetailDto modifier(String reference, UpdateDossierRequest request) {
        Dossier dossier = trouverParReference(reference);
        verifierAccesDossier(dossier);

        if (dossier.getStatut() != StatutDossier.A_COMPLETER && dossier.getStatut() != StatutDossier.COMPLEMENT_REQUIS) {
            throw new BusinessException("Ce dossier ne peut plus être modifié dans son statut actuel (" + dossier.getStatut().getLibelle() + ")");
        }

        if (request.getNom() != null) dossier.setNom(request.getNom());
        if (request.getPrenom() != null) dossier.setPrenom(request.getPrenom());
        if (request.getTelephone() != null) dossier.setTelephone(request.getTelephone());
        if (request.getEmail() != null) dossier.setEmail(request.getEmail());
        if (request.getProfession() != null) dossier.setProfession(request.getProfession());
        if (request.getAdresse() != null) dossier.setAdresse(request.getAdresse());
        if (request.getRevenuMensuel() != null) dossier.setRevenuMensuel(request.getRevenuMensuel());
        if (request.getChargesMensuelles() != null) dossier.setChargesMensuelles(request.getChargesMensuelles());

        TypeCredit tc = dossier.getTypeCredit();
        if (request.getMontant() != null) {
            if (request.getMontant() < tc.getMontantMin() || request.getMontant() > tc.getMontantMax()) {
                throw new BusinessException("Le montant doit être compris entre " + tc.getMontantMin() + " et " + tc.getMontantMax() + " DH");
            }
            dossier.setMontant(request.getMontant());
        }
        if (request.getDuree() != null) {
            if (request.getDuree() < tc.getDureeMin() || request.getDuree() > tc.getDureeMax()) {
                throw new BusinessException("La durée doit être comprise entre " + tc.getDureeMin() + " et " + tc.getDureeMax() + " mois");
            }
            dossier.setDuree(request.getDuree());
        }

        dossier = dossierRepository.save(dossier);

        Utilisateur currentUser = authService.getCurrentUser();
        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                dossier.getStatut(),
                dossier.getStatut(),
                "MODIFICATION",
                currentUser,
                "Modification des informations du dossier"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("MODIF_DOSSIER", "DOSSIER", dossier.getReference(), "Informations du dossier mises à jour");

        int requises = pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(tc.getId()).size();
        int deposees = pieceDeposeeRepository.findByDossier(dossier).size();
        return new DossierDetailDto(dossier, true, requises, deposees);
    }

    @Transactional
    public DossierDetailDto annuler(String reference) {
        Dossier dossier = trouverParReference(reference);
        verifierAccesDossier(dossier);

        if (dossier.getStatut() == StatutDossier.ACCEPTE || dossier.getStatut() == StatutDossier.REFUSE) {
            throw new BusinessException("Impossible d'annuler un dossier déjà statué définitivement (" + dossier.getStatut().getLibelle() + ")");
        }

        StatutDossier ancienStatut = dossier.getStatut();
        dossier.setStatut(StatutDossier.ANNULE);
        dossier = dossierRepository.save(dossier);

        Utilisateur currentUser = authService.getCurrentUser();
        HistoriqueStatut hist = new HistoriqueStatut(
                dossier,
                ancienStatut,
                StatutDossier.ANNULE,
                "ANNULATION",
                currentUser,
                "Annulation de la demande de crédit"
        );
        historiqueStatutRepository.save(hist);

        auditService.tracer("ANNULATION_DOSSIER", "DOSSIER", dossier.getReference(), "Dossier annulé");

        int requises = pieceRequiseRepository.findByTypeCreditIdOrderByOrdreAsc(dossier.getTypeCredit().getId()).size();
        int deposees = pieceDeposeeRepository.findByDossier(dossier).size();
        return new DossierDetailDto(dossier, false, requises, deposees);
    }

    public Dossier trouverParReference(String reference) {
        return dossierRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Dossier introuvable avec la référence: " + reference));
    }

    public void verifierAccesDossier(Dossier dossier) {
        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser.getRole() == Role.CONSEILLER) {
            if (dossier.getCreePar() != null && !dossier.getCreePar().getId().equals(currentUser.getId())) {
                throw new UnauthorizedOperationException("Vous n'êtes pas autorisé à accéder au dossier d'un autre conseiller");
            }
        }
    }

    private synchronized String genererReferenceUnique() {
        long count = dossierRepository.count() + 1;
        String ref;
        do {
            ref = String.format("CRD%05d", count);
            count++;
        } while (dossierRepository.existsByReference(ref));
        return ref;
    }
}
