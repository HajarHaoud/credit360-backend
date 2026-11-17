package ma.credit360.web.dto;

import ma.credit360.entity.AlerteImpaye;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlerteImpayeDto {
    private Long id;
    private String client;
    private String cin;
    private String telephone;
    private String email;
    private String credit;
    private String referenceDossier;
    private Integer joursRetard;
    private String statut;
    private String statutLibelle;
    private String action;
    private String actionLibelle;
    private Double montantImpaye;
    private Double capitalRestantDu;
    private LocalDate dateConstat;
    private LocalDateTime dateDerniereAction;
    private boolean lettreGeneree;
    private String commentaire;

    public AlerteImpayeDto() {}

    public AlerteImpayeDto(AlerteImpaye a) {
        this.id = a.getId();
        this.client = a.getClient();
        this.cin = a.getCin();
        this.telephone = a.getTelephone();
        this.email = a.getEmail();
        this.credit = a.getCredit();
        this.referenceDossier = a.getReferenceDossier();
        this.joursRetard = a.getJoursRetard();
        this.statut = a.getStatut().name();
        this.statutLibelle = a.getStatut().getLibelle();
        this.action = a.getAction().name();
        this.actionLibelle = a.getAction().getLibelle();
        this.montantImpaye = a.getMontantImpaye();
        this.capitalRestantDu = a.getCapitalRestantDu();
        this.dateConstat = a.getDateConstat();
        this.dateDerniereAction = a.getDateDerniereAction();
        this.lettreGeneree = a.isLettreGeneree();
        this.commentaire = a.getCommentaire();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCredit() { return credit; }
    public void setCredit(String credit) { this.credit = credit; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public Integer getJoursRetard() { return joursRetard; }
    public void setJoursRetard(Integer joursRetard) { this.joursRetard = joursRetard; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getStatutLibelle() { return statutLibelle; }
    public void setStatutLibelle(String statutLibelle) { this.statutLibelle = statutLibelle; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getActionLibelle() { return actionLibelle; }
    public void setActionLibelle(String actionLibelle) { this.actionLibelle = actionLibelle; }

    public Double getMontantImpaye() { return montantImpaye; }
    public void setMontantImpaye(Double montantImpaye) { this.montantImpaye = montantImpaye; }

    public Double getCapitalRestantDu() { return capitalRestantDu; }
    public void setCapitalRestantDu(Double capitalRestantDu) { this.capitalRestantDu = capitalRestantDu; }

    public LocalDate getDateConstat() { return dateConstat; }
    public void setDateConstat(LocalDate dateConstat) { this.dateConstat = dateConstat; }

    public LocalDateTime getDateDerniereAction() { return dateDerniereAction; }
    public void setDateDerniereAction(LocalDateTime dateDerniereAction) { this.dateDerniereAction = dateDerniereAction; }

    public boolean isLettreGeneree() { return lettreGeneree; }
    public void setLettreGeneree(boolean lettreGeneree) { this.lettreGeneree = lettreGeneree; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
