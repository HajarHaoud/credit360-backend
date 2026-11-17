package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.StatutRecharge;

import java.time.LocalDateTime;

@Entity
@Table(name = "recharges_credits")
public class RechargeCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String referenceOperation; // ex: RCH00001

    @Column(nullable = false)
    private String client; // ex: M. Ahmed

    private String cin;
    private String telephone;

    @Column(nullable = false)
    private Double creditExistant; // 120 000 DH

    @Column(nullable = false)
    private Double soldeRestant; // 60 000 DH

    @Column(nullable = false)
    private Double nouveauFinancement; // 200 000 DH

    @Column(nullable = false)
    private Double remboursementAncienCredit; // 60 000 DH

    @Column(nullable = false)
    private Double fraisEtAssurance; // 10 000 DH

    @Column(nullable = false)
    private Double soldeAVerserClient; // 130 000 DH

    private Integer nouvelleDuree; // en mois (ex: 84)
    private Double nouvelleMensualite;
    private Double nouveauTaux;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRecharge statut = StatutRecharge.SIMULEE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "valide_par_id")
    private Utilisateur validePar;

    @Column(nullable = false)
    private LocalDateTime dateDemande = LocalDateTime.now();

    private LocalDateTime dateValidation;

    public RechargeCredit() {}

    public RechargeCredit(String referenceOperation, String client, String cin, String telephone,
                          Double creditExistant, Double soldeRestant, Double nouveauFinancement,
                          Double fraisEtAssurance, Integer nouvelleDuree, Double nouveauTaux) {
        this.referenceOperation = referenceOperation;
        this.client = client;
        this.cin = cin;
        this.telephone = telephone;
        this.creditExistant = creditExistant;
        this.soldeRestant = soldeRestant;
        this.nouveauFinancement = nouveauFinancement;
        this.remboursementAncienCredit = soldeRestant;
        this.fraisEtAssurance = fraisEtAssurance;
        this.soldeAVerserClient = nouveauFinancement - soldeRestant - fraisEtAssurance;
        this.nouvelleDuree = nouvelleDuree;
        this.nouveauTaux = nouveauTaux;
        this.statut = StatutRecharge.SIMULEE;
        this.dateDemande = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceOperation() { return referenceOperation; }
    public void setReferenceOperation(String referenceOperation) { this.referenceOperation = referenceOperation; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Double getCreditExistant() { return creditExistant; }
    public void setCreditExistant(Double creditExistant) { this.creditExistant = creditExistant; }

    public Double getSoldeRestant() { return soldeRestant; }
    public void setSoldeRestant(Double soldeRestant) { this.soldeRestant = soldeRestant; }

    public Double getNouveauFinancement() { return nouveauFinancement; }
    public void setNouveauFinancement(Double nouveauFinancement) { this.nouveauFinancement = nouveauFinancement; }

    public Double getRemboursementAncienCredit() { return remboursementAncienCredit; }
    public void setRemboursementAncienCredit(Double remboursementAncienCredit) { this.remboursementAncienCredit = remboursementAncienCredit; }

    public Double getFraisEtAssurance() { return fraisEtAssurance; }
    public void setFraisEtAssurance(Double fraisEtAssurance) { this.fraisEtAssurance = fraisEtAssurance; }

    public Double getSoldeAVerserClient() { return soldeAVerserClient; }
    public void setSoldeAVerserClient(Double soldeAVerserClient) { this.soldeAVerserClient = soldeAVerserClient; }

    public Integer getNouvelleDuree() { return nouvelleDuree; }
    public void setNouvelleDuree(Integer nouvelleDuree) { this.nouvelleDuree = nouvelleDuree; }

    public Double getNouvelleMensualite() { return nouvelleMensualite; }
    public void setNouvelleMensualite(Double nouvelleMensualite) { this.nouvelleMensualite = nouvelleMensualite; }

    public Double getNouveauTaux() { return nouveauTaux; }
    public void setNouveauTaux(Double nouveauTaux) { this.nouveauTaux = nouveauTaux; }

    public StatutRecharge getStatut() { return statut; }
    public void setStatut(StatutRecharge statut) { this.statut = statut; }

    public Utilisateur getValidePar() { return validePar; }
    public void setValidePar(Utilisateur validePar) { this.validePar = validePar; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }
}
