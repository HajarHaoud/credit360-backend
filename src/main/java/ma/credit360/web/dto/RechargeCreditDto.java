package ma.credit360.web.dto;

import ma.credit360.entity.RechargeCredit;

import java.time.LocalDateTime;

public class RechargeCreditDto {
    private Long id;
    private String referenceOperation;
    private String client;
    private String cin;
    private String telephone;
    private Double creditExistant;
    private Double soldeRestant;
    private Double nouveauFinancement;
    private Double remboursementAncienCredit;
    private Double fraisEtAssurance;
    private Double soldeAVerserClient;
    private Integer nouvelleDuree;
    private Double nouvelleMensualite;
    private Double nouveauTaux;
    private String statut;
    private String statutLibelle;
    private String valideParNom;
    private LocalDateTime dateDemande;
    private LocalDateTime dateValidation;

    public RechargeCreditDto() {}

    public RechargeCreditDto(RechargeCredit r) {
        this.id = r.getId();
        this.referenceOperation = r.getReferenceOperation();
        this.client = r.getClient();
        this.cin = r.getCin();
        this.telephone = r.getTelephone();
        this.creditExistant = r.getCreditExistant();
        this.soldeRestant = r.getSoldeRestant();
        this.nouveauFinancement = r.getNouveauFinancement();
        this.remboursementAncienCredit = r.getRemboursementAncienCredit();
        this.fraisEtAssurance = r.getFraisEtAssurance();
        this.soldeAVerserClient = r.getSoldeAVerserClient();
        this.nouvelleDuree = r.getNouvelleDuree();
        this.nouvelleMensualite = r.getNouvelleMensualite();
        this.nouveauTaux = r.getNouveauTaux();
        this.statut = r.getStatut().name();
        this.statutLibelle = r.getStatut().getLibelle();
        this.valideParNom = r.getValidePar() != null ? r.getValidePar().getNom() : null;
        this.dateDemande = r.getDateDemande();
        this.dateValidation = r.getDateValidation();
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

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getStatutLibelle() { return statutLibelle; }
    public void setStatutLibelle(String statutLibelle) { this.statutLibelle = statutLibelle; }

    public String getValideParNom() { return valideParNom; }
    public void setValideParNom(String valideParNom) { this.valideParNom = valideParNom; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }
}
