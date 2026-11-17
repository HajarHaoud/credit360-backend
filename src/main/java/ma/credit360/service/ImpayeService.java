package ma.credit360.service;

import ma.credit360.entity.AlerteImpaye;
import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.ActionImpaye;
import ma.credit360.entity.enums.StatutImpaye;
import ma.credit360.repository.AlerteImpayeRepository;
import ma.credit360.service.pdf.SimplePdfBuilder;
import ma.credit360.web.dto.AlerteImpayeDto;
import ma.credit360.web.dto.CountDto;
import ma.credit360.web.dto.TraiterImpayeRequest;
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
public class ImpayeService {

    private final AlerteImpayeRepository alerteImpayeRepository;
    private final AuthService authService;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public ImpayeService(AlerteImpayeRepository alerteImpayeRepository,
                         AuthService authService,
                         AuditService auditService,
                         NotificationService notificationService) {
        this.alerteImpayeRepository = alerteImpayeRepository;
        this.authService = authService;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<AlerteImpayeDto> listerTous() {
        return alerteImpayeRepository.findAllByOrderByJoursRetardDesc().stream()
                .map(AlerteImpayeDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlerteImpayeDto> listerParStatut(StatutImpaye statut) {
        return alerteImpayeRepository.findByStatut(statut).stream()
                .map(AlerteImpayeDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlerteImpayeDto> listerParAction(ActionImpaye action) {
        return alerteImpayeRepository.findByAction(action).stream()
                .map(AlerteImpayeDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlerteImpayeDto obtenirParId(Long id) {
        AlerteImpaye alerte = trouverParId(id);
        return new AlerteImpayeDto(alerte);
    }

    public AlerteImpaye trouverParId(Long id) {
        return alerteImpayeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerte impayé introuvable avec l'ID: " + id));
    }

    @Transactional
    public AlerteImpayeDto traiterImpaye(Long id, TraiterImpayeRequest request) {
        AlerteImpaye alerte = trouverParId(id);
        Utilisateur currentUser = authService.getCurrentUser();

        if (request.getNouveauStatut() != null) {
            alerte.setStatut(request.getNouveauStatut());
        }
        if (request.getNouvelleAction() != null) {
            alerte.setAction(request.getNouvelleAction());
        }
        if (request.getCommentaire() != null) {
            alerte.setCommentaire(request.getCommentaire());
        }

        alerte.setDateDerniereAction(LocalDateTime.now());
        AlerteImpaye saved = alerteImpayeRepository.save(alerte);

        auditService.tracer("TRAITEMENT_IMPAYE", "IMPAYES", String.valueOf(id),
                "Alerte traitée pour " + alerte.getClient() + " par " + currentUser.getNom() + " : " + alerte.getAction().getLibelle());

        return new AlerteImpayeDto(saved);
    }

    @Transactional
    public byte[] genererLettreMiseEnDemeure(Long id) {
        AlerteImpaye alerte = trouverParId(id);
        Utilisateur currentUser = authService.getCurrentUser();

        try {
            SimplePdfBuilder pdf = new SimplePdfBuilder();

            // En-tête Banque Populaire
            pdf.fillRect(50, 780, 500, 30, 0.85f, 0.40f, 0.05f);
            pdf.addText("BANQUE POPULAIRE - DIRECTION DES ENGAGEMENTS ET DU RECOUVREMENT", 60, 790, 11, true);

            pdf.addText("LETTRE DE MISE EN DEMEURE", 200, 740, 16, true);
            pdf.drawLine(50, 730, 550, 730);

            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            pdf.addText("Date d'emission : " + dateStr, 50, 710, 10, false);
            pdf.addText("Reference Dossier : " + (alerte.getReferenceDossier() != null ? alerte.getReferenceDossier() : "DOS-" + alerte.getId()), 50, 695, 10, false);
            pdf.addText("Objet : Mise en demeure valant sommation de payer", 50, 680, 11, true);

            // Destinataire
            pdf.drawRect(320, 610, 230, 60);
            pdf.addText("A l'attention de :", 330, 655, 10, true);
            pdf.addText(alerte.getClient(), 330, 640, 11, true);
            if (alerte.getCin() != null) {
                pdf.addText("CIN : " + alerte.getCin(), 330, 625, 10, false);
            }

            // Corps de la lettre
            pdf.addText("Madame, Monsieur,", 50, 580, 11, false);

            pdf.addText("Sauf erreur ou omission de notre part, nous constatons qu'a ce jour,", 50, 555, 10, false);
            pdf.addText("votre compte de pret (" + alerte.getCredit() + ") presente un retard de paiement non regularise.", 50, 540, 10, false);

            pdf.drawRect(50, 460, 500, 65);
            pdf.addText("RECAPITULATIF DE LA CREANCE EN SOUFFRANCE :", 60, 505, 10, true);
            pdf.addText("- Nombre de jours de retard : " + alerte.getJoursRetard() + " jours", 70, 490, 10, false);
            pdf.addText("- Montant des echeances impayees : " + String.format("%,.2f", alerte.getMontantImpaye()) + " DH", 70, 475, 10, true);

            pdf.addText("Nous vous mettons formellement en demeure de proceder au reglement integral", 50, 435, 10, false);
            pdf.addText("de cette somme dans un delai de 8 (huit) jours a compter de la presente.", 50, 420, 10, true);

            pdf.addText("A defaut de reglement dans le delai imparti, la Banque Populaire se verra", 50, 390, 10, false);
            pdf.addText("dans l'obligation d'engager a votre encontre les poursuites judiciaires prevues", 50, 375, 10, false);
            pdf.addText("par la legislation bancaire marocaine, avec exigibilite immediate du capital restant du.", 50, 360, 10, false);

            pdf.addText("Veuillez agreer, Madame, Monsieur, nos salutations distinguees.", 50, 320, 10, false);

            pdf.addText("Pour la Banque Populaire", 350, 270, 10, true);
            pdf.addText(currentUser.getNom(), 350, 255, 10, false);
            pdf.addText("Departement Contentieux et Recouvrement", 350, 240, 9, false);

            // Pied de page
            pdf.drawLine(50, 100, 550, 100);
            pdf.addText("Banque Populaire - Societe Anonyme a Directoire et Conseil de Surveillance", 110, 85, 8, false);
            pdf.addText("Ce document officiel est genere conformement aux regles bancaires de Bank Al-Maghrib.", 100, 73, 8, false);

            alerte.setLettreGeneree(true);
            alerte.setDateDerniereAction(LocalDateTime.now());
            alerteImpayeRepository.save(alerte);

            auditService.tracer("GENERATION_LETTRE_MISE_EN_DEMEURE", "IMPAYES", String.valueOf(id),
                    "Lettre de mise en demeure générée pour " + alerte.getClient());

            return pdf.build();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération de la lettre de mise en demeure: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public CountDto compterImpayes() {
        long count = alerteImpayeRepository.countByStatutNot(StatutImpaye.REGULARISE);
        return new CountDto(count);
    }
}
