package ma.credit360.service;

import ma.credit360.entity.AssuranceCredit;
import ma.credit360.entity.Dossier;
import ma.credit360.entity.enums.StatutAssurance;
import ma.credit360.entity.enums.TypeAssurance;
import ma.credit360.repository.AssuranceCreditRepository;
import ma.credit360.web.dto.AssuranceCreditDto;
import ma.credit360.web.dto.SouscrireAssuranceRequest;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssuranceService {

    private final AssuranceCreditRepository assuranceCreditRepository;
    private final DossierService dossierService;
    private final AuditService auditService;

    public AssuranceService(AssuranceCreditRepository assuranceCreditRepository,
                            DossierService dossierService,
                            AuditService auditService) {
        this.assuranceCreditRepository = assuranceCreditRepository;
        this.dossierService = dossierService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<AssuranceCreditDto> listerToutes() {
        return assuranceCreditRepository.findAllByOrderByReferenceContratDesc().stream()
                .map(AssuranceCreditDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssuranceCreditDto> listerParDossier(String referenceDossier) {
        return assuranceCreditRepository.findByReferenceDossier(referenceDossier).stream()
                .map(AssuranceCreditDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AssuranceCreditDto obtenirParReference(String referenceContrat) {
        AssuranceCredit a = assuranceCreditRepository.findByReferenceContrat(referenceContrat)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat d'assurance introuvable: " + referenceContrat));
        return new AssuranceCreditDto(a);
    }

    @Transactional
    public List<AssuranceCreditDto> proposerPourDossier(String referenceDossier) {
        Dossier dossier = dossierService.trouverParReference(referenceDossier);

        // Si des assurances existent déjà pour ce dossier, les renvoyer
        List<AssuranceCredit> existantes = assuranceCreditRepository.findByReferenceDossier(referenceDossier);
        if (!existantes.isEmpty()) {
            return existantes.stream().map(AssuranceCreditDto::new).collect(Collectors.toList());
        }

        long count = assuranceCreditRepository.count();

        // 1. Assurance Décès-Invalidité (DIAD)
        String refDiad = String.format("ASS%05d", count + 1);
        double tauxDiad = 0.35;
        AssuranceCredit diad = new AssuranceCredit(
                refDiad,
                dossier.getReference(),
                dossier.getPrenom() + " " + dossier.getNom(),
                dossier.getCin(),
                TypeAssurance.DECES_INVALIDITE,
                dossier.getMontant(),
                tauxDiad
        );
        assuranceCreditRepository.save(diad);

        // 2. Multirisque Habitation si Immobilier
        if (dossier.getTypeCredit().getFamille() == ma.credit360.entity.enums.FamilleCredit.IMMOBILIER) {
            String refMrh = String.format("ASS%05d", count + 2);
            double tauxMrh = 0.15;
            AssuranceCredit mrh = new AssuranceCredit(
                    refMrh,
                    dossier.getReference(),
                    dossier.getPrenom() + " " + dossier.getNom(),
                    dossier.getCin(),
                    TypeAssurance.MULTIRISQUE_HABITATION,
                    dossier.getMontant(),
                    tauxMrh
            );
            assuranceCreditRepository.save(mrh);
        }

        auditService.tracer("PROPOSITION_ASSURANCE", "ASSURANCES", referenceDossier,
                "Proposition d'assurance générée automatiquement pour le dossier " + referenceDossier);

        return assuranceCreditRepository.findByReferenceDossier(referenceDossier).stream()
                .map(AssuranceCreditDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssuranceCreditDto souscrire(SouscrireAssuranceRequest request) {
        long seq = assuranceCreditRepository.count() + 1;
        String ref = String.format("ASS%05d", seq);
        double taux = request.getTauxPrimePourcent() != null ? request.getTauxPrimePourcent() : 0.35;

        AssuranceCredit a = new AssuranceCredit(
                ref,
                request.getReferenceDossier(),
                request.getClient(),
                request.getCin(),
                request.getTypeAssurance(),
                request.getCapitalAssure(),
                taux
        );
        a.setStatut(StatutAssurance.SOUSCRITE);
        a.setDateSouscription(LocalDateTime.now());
        a.setDateEffet(LocalDate.now());

        AssuranceCredit saved = assuranceCreditRepository.save(a);
        auditService.tracer("SOUSCRIPTION_ASSURANCE", "ASSURANCES", ref,
                "Souscription assurance " + a.getTypeAssurance().getLibelle() + " pour " + request.getClient());

        return new AssuranceCreditDto(saved);
    }

    @Transactional
    public AssuranceCreditDto activer(String referenceContrat) {
        AssuranceCredit a = assuranceCreditRepository.findByReferenceContrat(referenceContrat)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat d'assurance introuvable: " + referenceContrat));

        a.setStatut(StatutAssurance.ACTIVE);
        a.setDateEffet(LocalDate.now());

        AssuranceCredit saved = assuranceCreditRepository.save(a);
        auditService.tracer("ACTIVATION_ASSURANCE", "ASSURANCES", referenceContrat, "Contrat d'assurance activé");

        return new AssuranceCreditDto(saved);
    }
}
