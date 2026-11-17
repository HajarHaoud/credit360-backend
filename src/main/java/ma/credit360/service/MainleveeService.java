package ma.credit360.service;

import ma.credit360.entity.Mainlevee;
import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.StatutMainlevee;
import ma.credit360.repository.MainleveeRepository;
import ma.credit360.service.pdf.SimplePdfBuilder;
import ma.credit360.web.dto.DemandeMainleveeRequest;
import ma.credit360.web.dto.MainleveeDto;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainleveeService {

    private final MainleveeRepository mainleveeRepository;
    private final AuthService authService;
    private final AuditService auditService;

    public MainleveeService(MainleveeRepository mainleveeRepository,
                            AuthService authService,
                            AuditService auditService) {
        this.mainleveeRepository = mainleveeRepository;
        this.authService = authService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<MainleveeDto> listerToutes() {
        return mainleveeRepository.findAllByOrderByDateSoldageDesc().stream()
                .map(MainleveeDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MainleveeDto> listerSoldes() {
        return mainleveeRepository.findByStatut(StatutMainlevee.CREDIT_SOLDE).stream()
                .map(MainleveeDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MainleveeDto obtenirParReference(String referenceMainlevee) {
        Mainlevee m = mainleveeRepository.findByReferenceMainlevee(referenceMainlevee)
                .orElseThrow(() -> new ResourceNotFoundException("Mainlevée introuvable: " + referenceMainlevee));
        return new MainleveeDto(m);
    }

    @Transactional
    public MainleveeDto creerDemande(DemandeMainleveeRequest request) {
        long seq = mainleveeRepository.count() + 1;
        String ref = String.format("MLV%05d", seq);

        LocalDate dateOctroi = request.getDateOctroi() != null ? request.getDateOctroi() : LocalDate.now().minusYears(5);
        LocalDate dateSolde = request.getDateSoldage() != null ? request.getDateSoldage() : LocalDate.now().minusDays(10);

        Mainlevee m = new Mainlevee(
                ref,
                request.getReferenceCredit(),
                request.getClient(),
                request.getCin(),
                request.getTypeGarantie(),
                request.getDesignationBien(),
                request.getMontantInitial(),
                dateOctroi,
                dateSolde
        );

        Mainlevee saved = mainleveeRepository.save(m);
        auditService.tracer("DEMANDE_MAINLEVEE", "MAINLEVEES", ref, "Création mainlevée pour crédit " + request.getReferenceCredit());

        return new MainleveeDto(saved);
    }

    @Transactional
    public MainleveeDto valider(String referenceMainlevee) {
        Mainlevee m = mainleveeRepository.findByReferenceMainlevee(referenceMainlevee)
                .orElseThrow(() -> new ResourceNotFoundException("Mainlevée introuvable: " + referenceMainlevee));

        Utilisateur currentUser = authService.getCurrentUser();
        m.setStatut(StatutMainlevee.VALIDEE);
        m.setValidePar(currentUser);
        m.setDateValidation(LocalDateTime.now());

        Mainlevee saved = mainleveeRepository.save(m);
        auditService.tracer("VALIDATION_MAINLEVEE", "MAINLEVEES", referenceMainlevee, "Mainlevée validée par " + currentUser.getNom());

        return new MainleveeDto(saved);
    }

    @Transactional
    public MainleveeDto delivrerAuClient(String referenceMainlevee) {
        Mainlevee m = mainleveeRepository.findByReferenceMainlevee(referenceMainlevee)
                .orElseThrow(() -> new ResourceNotFoundException("Mainlevée introuvable: " + referenceMainlevee));

        m.setStatut(StatutMainlevee.DELIVREE);
        m.setDateDelivrance(LocalDateTime.now());

        Mainlevee saved = mainleveeRepository.save(m);
        auditService.tracer("DELIVRANCE_MAINLEVEE", "MAINLEVEES", referenceMainlevee, "Mainlevée délivrée au client");

        return new MainleveeDto(saved);
    }

    @Transactional
    public byte[] genererCertificatPdf(String referenceMainlevee) {
        Mainlevee m = mainleveeRepository.findByReferenceMainlevee(referenceMainlevee)
                .orElseThrow(() -> new ResourceNotFoundException("Mainlevée introuvable: " + referenceMainlevee));

        Utilisateur currentUser = authService.getCurrentUser();

        try {
            SimplePdfBuilder pdf = new SimplePdfBuilder();

            pdf.fillRect(50, 780, 500, 30, 0.85f, 0.40f, 0.05f);
            pdf.addText("BANQUE POPULAIRE - ATTESTATION OFFICIELLE DE MAINLEVEE", 80, 790, 11, true);

            pdf.addText("CERTIFICAT DE MAINLEVEE ET QUITTANCE DEFINITIVE", 110, 740, 14, true);
            pdf.drawLine(50, 730, 550, 730);

            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            pdf.addText("Ref Mainlevee : " + m.getReferenceMainlevee(), 50, 710, 10, true);
            pdf.addText("Date de delivrance : " + dateStr, 400, 710, 10, false);
            pdf.addText("Ref Pret Solde : " + m.getReferenceCredit(), 50, 695, 10, false);

            pdf.drawRect(50, 570, 500, 110);
            pdf.addText("IDENTIFICATION DU BENEFICIAIRE ET DU FINANCEMENT :", 60, 665, 10, true);
            pdf.addText("- Emprunteur : " + m.getClient(), 70, 650, 10, false);
            if (m.getCin() != null) {
                pdf.addText("- CIN : " + m.getCin(), 70, 635, 10, false);
            }
            pdf.addText("- Montant initial du pret : " + String.format("%,.2f", m.getMontantInitial()) + " DH", 70, 620, 10, false);
            pdf.addText("- Date d'octroi : " + m.getDateOctroi().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 70, 605, 10, false);
            pdf.addText("- Date de soldage complet : " + m.getDateSoldage().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 70, 590, 10, true);

            pdf.drawRect(50, 470, 500, 85);
            pdf.addText("DESCRIPTION DE LA SURETE ET GARANTIE LIBEREE :", 60, 540, 10, true);
            pdf.addText("- Nature de la surete : " + m.getTypeGarantie(), 70, 525, 10, false);
            pdf.addText("- Designation du bien : " + (m.getDesignationBien() != null ? m.getDesignationBien() : "Garantie conventionnelle enregistree"), 70, 510, 10, false);
            pdf.addText("- Statut : CREDIT TOTALEMENT SOLDE EN CAPITAL ET INTERETS", 70, 485, 10, true);

            pdf.addText("La Banque Populaire certifie par la presente donner pleine et entiere mainlevee", 50, 430, 10, false);
            pdf.addText("de toute inscription hypothécaire, gage ou surete prise a son profit en garantie", 50, 415, 10, false);
            pdf.addText("du remboursement dudit credit, le compte etant integralement cloture.", 50, 400, 10, false);

            pdf.addText("Fait a Casablanca, pour servir et valoir ce que de droit.", 50, 360, 10, false);

            pdf.addText("Pour la Banque Populaire - Direction des Engagements", 250, 310, 10, true);
            pdf.addText("Signataire habilite : " + currentUser.getNom(), 250, 295, 10, false);
            pdf.drawRect(250, 200, 250, 80);
            pdf.addText("[ Cachet Electronique et Signature Certifiee ]", 270, 240, 9, false);

            pdf.drawLine(50, 100, 550, 100);
            pdf.addText("Banque Populaire - Registre Central des Suretes Mobilieres et Hypotheques", 100, 85, 8, false);

            m.setStatut(StatutMainlevee.VALIDEE);
            m.setDateValidation(LocalDateTime.now());
            mainleveeRepository.save(m);

            auditService.tracer("GENERATION_CERTIFICAT_MAINLEVEE", "MAINLEVEES", referenceMainlevee,
                    "Certificat de mainlevée généré pour " + m.getClient());

            return pdf.build();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du certificat de mainlevée: " + e.getMessage(), e);
        }
    }
}
