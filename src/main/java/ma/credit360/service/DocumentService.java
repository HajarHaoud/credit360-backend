package ma.credit360.service;

import ma.credit360.entity.*;
import ma.credit360.entity.enums.TypeDocument;
import ma.credit360.repository.AnalyseDossierRepository;
import ma.credit360.repository.DecisionRepository;
import ma.credit360.repository.DocumentDossierRepository;
import ma.credit360.service.pdf.SimplePdfBuilder;
import ma.credit360.web.dto.DocumentDto;
import ma.credit360.web.dto.GenerateDocumentRequest;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentDossierRepository documentDossierRepository;
    private final AnalyseDossierRepository analyseDossierRepository;
    private final DecisionRepository decisionRepository;
    private final DossierService dossierService;
    private final AuditService auditService;
    private final Path uploadDir;

    public DocumentService(DocumentDossierRepository documentDossierRepository,
                           AnalyseDossierRepository analyseDossierRepository,
                           DecisionRepository decisionRepository,
                           DossierService dossierService,
                           AuditService auditService,
                           @Value("${credit360.storage.upload-dir:./uploads}") String uploadDirPath) {
        this.documentDossierRepository = documentDossierRepository;
        this.analyseDossierRepository = analyseDossierRepository;
        this.decisionRepository = decisionRepository;
        this.dossierService = dossierService;
        this.auditService = auditService;
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> listerDocuments(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        return documentDossierRepository.findByDossierOrderByDateGenerationDesc(dossier).stream()
                .map(DocumentDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DocumentDto obtenirDocument(String reference, Long documentId) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        DocumentDossier doc = documentDossierRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable: " + documentId));

        return new DocumentDto(doc);
    }

    @Transactional
    public DocumentDto genererDocument(String reference, GenerateDocumentRequest request) {
        if (request == null || request.getTypeDocument() == null) {
            throw new IllegalArgumentException("Le type de document est obligatoire (OFFRE_DE_PRET, CONTRAT, ATTESTATION, FICHE_SYNTHESE)");
        }

        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        TypeDocument type = request.getTypeDocument();
        String filename = type.name().toLowerCase() + "_" + dossier.getReference() + "_" + System.currentTimeMillis() + ".pdf";

        try {
            byte[] pdfBytes = genererContenuPdf(dossier, type);

            Path docDir = this.uploadDir.resolve(dossier.getReference()).resolve("documents");
            Files.createDirectories(docDir);
            Path filePath = docDir.resolve(filename);
            Files.write(filePath, pdfBytes);

            DocumentDossier doc = new DocumentDossier(
                    dossier,
                    filename,
                    type,
                    "PDF",
                    filePath.toString()
            );

            doc = documentDossierRepository.save(doc);
            auditService.tracer("GENERATION_DOCUMENT", "DOCUMENT", dossier.getReference(), "Génération du document " + type.name());

            return new DocumentDto(doc);

        } catch (IOException e) {
            throw new BusinessException("Erreur lors de la génération du document PDF: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Resource telechargerDocument(String reference, Long documentId) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        DocumentDossier doc = documentDossierRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable: " + documentId));

        try {
            Path filePath = Paths.get(doc.getCheminFichier());
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Le fichier PDF n'existe pas sur le disque");
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Chemin du document invalide");
        }
    }

    @Transactional
    public void supprimerDocument(String reference, Long documentId) {
        Dossier dossier = dossierService.trouverParReference(reference);
        dossierService.verifierAccesDossier(dossier);

        DocumentDossier doc = documentDossierRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable: " + documentId));

        try {
            Files.deleteIfExists(Paths.get(doc.getCheminFichier()));
        } catch (Exception ignored) {}

        documentDossierRepository.delete(doc);
        auditService.tracer("SUPPR_DOCUMENT", "DOCUMENT", dossier.getReference(), "Suppression du document: " + doc.getNom());
    }

    private byte[] genererContenuPdf(Dossier d, TypeDocument type) throws IOException {
        SimplePdfBuilder pdf = new SimplePdfBuilder();

        // En-tete Banque Populaire
        pdf.fillRect(40, 770, 515, 35, 0.88f, 0.35f, 0.06f); // Orange BP (#e05a10)
        pdf.addText("CREDIT 360 - BANQUE POPULAIRE", 55, 785, 14, true);
        pdf.addText("Grandir. Ensemble.", 420, 785, 10, false);

        // Titre du document
        pdf.addText(titreParType(type), 55, 735, 16, true);
        pdf.addText("Reference: " + d.getReference(), 55, 715, 11, false);
        pdf.addText("Date d'edition: " + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 380, 715, 10, false);

        pdf.drawLine(55, 705, 540, 705);

        // Section 1: Informations Client
        pdf.fillRect(55, 680, 485, 18, 0.93f, 0.94f, 0.96f);
        pdf.addText("1. IDENTIFICATION DU CLIENT", 60, 685, 11, true);

        pdf.addText("Nom & Prenom: " + d.getNom() + " " + d.getPrenom(), 65, 660, 10, false);
        pdf.addText("CIN: " + d.getCin(), 340, 660, 10, false);
        pdf.addText("Telephone: " + d.getTelephone(), 65, 642, 10, false);
        pdf.addText("Email: " + d.getEmail(), 340, 642, 10, false);
        pdf.addText("Profession: " + (d.getProfession() != null ? d.getProfession() : "-"), 65, 624, 10, false);
        pdf.addText("Adresse: " + (d.getAdresse() != null ? d.getAdresse() : "-"), 340, 624, 10, false);

        // Section 2: Financement demande
        pdf.fillRect(55, 595, 485, 18, 0.93f, 0.94f, 0.96f);
        pdf.addText("2. CARACTERISTIQUES DU FINANCEMENT", 60, 600, 11, true);

        String typeLib = d.getTypeCredit() != null ? d.getTypeCredit().getLibelle() : "-";
        double taux = d.getTypeCredit() != null ? d.getTypeCredit().getTaux() : 4.5;
        pdf.addText("Produit: " + typeLib, 65, 575, 10, false);
        pdf.addText("Montant: " + String.format("%,.2f", d.getMontant()) + " DH", 340, 575, 10, true);
        pdf.addText("Duree: " + d.getDuree() + " mois", 65, 557, 10, false);
        pdf.addText("Taux nominal: " + taux + "%", 340, 557, 10, false);
        pdf.addText("Statut actuel: " + d.getStatut().getLibelle(), 65, 539, 10, false);

        // Section 3: Analyse & Etude de Risque
        pdf.fillRect(55, 510, 485, 18, 0.93f, 0.94f, 0.96f);
        pdf.addText("3. EVALUATION FINANCIERE ET RISQUE", 60, 515, 11, true);

        Optional<AnalyseDossier> anOpt = analyseDossierRepository.findByDossier(d);
        if (anOpt.isPresent()) {
            AnalyseDossier a = anOpt.get();
            pdf.addText("Revenu mensuel justifie: " + String.format("%,.2f", a.getRevenuMensuel()) + " DH", 65, 490, 10, false);
            pdf.addText("Charges declarees: " + String.format("%,.2f", a.getChargesMensuelles()) + " DH", 340, 490, 10, false);
            pdf.addText("Mensualite estimee: " + String.format("%,.2f", a.getMensualiteEstimee()) + " DH", 65, 472, 10, false);
            pdf.addText("Taux d'endettement: " + a.getTauxEndettement() + "% (Seuil max: 45%)", 340, 472, 10, true);
            pdf.addText("Capacite de remboursement: " + String.format("%,.2f", a.getCapaciteRemboursement()) + " DH", 65, 454, 10, false);
            pdf.addText("Eligibilite: " + (Boolean.TRUE.equals(a.getEligible()) ? "FAVORABLE" : "RESERVE"), 340, 454, 10, true);
        } else {
            pdf.addText("Revenu mensuel: " + String.format("%,.2f", d.getRevenuMensuel()) + " DH", 65, 490, 10, false);
            pdf.addText("Charges mensuelles: " + String.format("%,.2f", d.getChargesMensuelles()) + " DH", 340, 490, 10, false);
            pdf.addText("Analyse financiere detaillee en cours d'instruction", 65, 472, 10, false);
        }

        // Section 4: Decision
        pdf.fillRect(55, 425, 485, 18, 0.93f, 0.94f, 0.96f);
        pdf.addText("4. AVIS ET DECISION DU COMITE", 60, 430, 11, true);

        Optional<Decision> decOpt = decisionRepository.findFirstByDossierOrderByDateDecisionDesc(d);
        if (decOpt.isPresent()) {
            Decision dec = decOpt.get();
            pdf.addText("Decision: " + dec.getTypeDecision().name(), 65, 405, 11, true);
            pdf.addText("Montant accorde: " + String.format("%,.2f", dec.getMontantAccorde()) + " DH", 340, 405, 10, false);
            pdf.addText("Duree accordee: " + dec.getDureeAccordee() + " mois", 65, 387, 10, false);
            pdf.addText("Taux accorde: " + dec.getTauxAccorde() + "%", 340, 387, 10, false);
            pdf.addText("Motif: " + (dec.getMotif() != null ? dec.getMotif() : "Conforme aux regles d'octroi"), 65, 369, 10, false);
            String decideur = dec.getDecidePar() != null ? dec.getDecidePar().getNom() : "Comite de credit";
            pdf.addText("Signe par: " + decideur, 65, 351, 10, false);
        } else {
            pdf.addText("Statut du comite: En cours d'analyse et de validation.", 65, 405, 10, false);
        }

        // Cadres signatures
        pdf.drawRect(65, 230, 200, 80);
        pdf.addText("Pour le Conseiller / Agence", 75, 295, 9, true);
        if (d.getCreePar() != null) {
            pdf.addText(d.getCreePar().getNom(), 75, 275, 9, false);
        }

        pdf.drawRect(330, 230, 200, 80);
        pdf.addText("Pour le Comite des Engagements", 340, 295, 9, true);

        // Footer
        pdf.drawLine(55, 90, 540, 90);
        pdf.addText("Credit 360 - Systeme Integre de Gestion des Demandes de Credit - Banque Populaire", 95, 75, 8, false);
        pdf.addText("Document a caractere strictement confidentiel et reserve a l'usage bancaire interne", 105, 63, 8, false);

        return pdf.build();
    }

    private String titreParType(TypeDocument type) {
        return switch (type) {
            case FICHE_ANALYSE -> "FICHE D'ANALYSE FINANCIERE ET DE RISQUE";
            case OFFRE_CREDIT -> "OFFRE PREALABLE DE CREDIT";
            case RECAPITULATIF -> "RECAPITULATIF GENERAL DU DOSSIER";
            case DECISION_OCTROI -> "NOTIFICATION DE DECISION D'OCTROI";
            case CONTRAT_CREDIT -> "PROJET DE CONTRAT DE CREDIT";
            case ATTESTATION_MISE_EN_PLACE -> "ATTESTATION DE MISE EN PLACE DU FINANCEMENT";
            case LETTRE_MISE_EN_DEMEURE -> "LETTRE DE MISE EN DEMEURE";
            case CERTIFICAT_MAINLEVEE -> "CERTIFICAT DE MAINLEVEE DE SURETE";
            case POLICE_ASSURANCE -> "ATTESTATION DE COUVERTURE D'ASSURANCE";
            case ATTESTATION_REMBOURSEMENT -> "ATTESTATION DE REMBOURSEMENT DE FINANCEMENT";
            default -> type.name().replace('_', ' ');
        };
    }
}
