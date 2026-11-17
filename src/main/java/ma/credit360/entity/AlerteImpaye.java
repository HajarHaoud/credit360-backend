package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.ActionImpaye;
import ma.credit360.entity.enums.StatutImpaye;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertes_impayes")
public class AlerteImpaye {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String client;

    private String cin;
    private String telephone;
    private String email;

    @Column(nullable = false)
    private String credit; // CONSO, AUTO, IMMO, PRO

    private String referenceDossier;

    @Column(nullable = false)
    private Integer joursRetard; // 30, 60, 90+

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutImpaye statut; // 1er impayé, 2ème impayé, 3ème impayé

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionImpaye action; // Relance, Mise en demeure, Procédure

    @Column(nullable = false)
    private Double montantImpaye;

    private Double capitalRestantDu;

    @Column(nullable = false)
    private LocalDate dateConstat;

    private LocalDateTime dateDerniereAction;

    private boolean lettreGeneree = false;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    public AlerteImpaye() {}

    public AlerteImpaye(String client, String cin, String telephone, String email, String credit,
                        String referenceDossier, Integer joursRetard, StatutImpaye statut,
                        ActionImpaye action, Double montantImpaye, Double capitalRestantDu) {
        this.client = client;
        this.cin = cin;
        this.telephone = telephone;
        this.email = email;
        this.credit = credit;
        this.referenceDossier = referenceDossier;
        this.joursRetard = joursRetard;
        this.statut = statut;
        this.action = action;
        this.montantImpaye = montantImpaye;
        this.capitalRestantDu = capitalRestantDu;
        this.dateConstat = LocalDate.now().minusDays(joursRetard);
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

    public StatutImpaye getStatut() { return statut; }
    public void setStatut(StatutImpaye statut) { this.statut = statut; }

    public ActionImpaye getAction() { return action; }
    public void setAction(ActionImpaye action) { this.action = action; }

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
