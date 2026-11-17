package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SimulerRechargeRequest {
    @NotNull(message = "Le nom du client est obligatoire")
    private String client;

    private String cin;
    private String telephone;

    @NotNull(message = "Le montant du crédit existant est obligatoire")
    @Positive
    private Double creditExistant;

    @NotNull(message = "Le solde restant dû est obligatoire")
    @Positive
    private Double soldeRestant;

    @NotNull(message = "Le nouveau financement demandé est obligatoire")
    @Positive
    private Double nouveauFinancement;

    private Double fraisEtAssurance;
    private Integer nouvelleDuree;
    private Double nouveauTaux;

    public SimulerRechargeRequest() {}

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

    public Double getFraisEtAssurance() { return fraisEtAssurance; }
    public void setFraisEtAssurance(Double fraisEtAssurance) { this.fraisEtAssurance = fraisEtAssurance; }

    public Integer getNouvelleDuree() { return nouvelleDuree; }
    public void setNouvelleDuree(Integer nouvelleDuree) { this.nouvelleDuree = nouvelleDuree; }

    public Double getNouveauTaux() { return nouveauTaux; }
    public void setNouveauTaux(Double nouveauTaux) { this.nouveauTaux = nouveauTaux; }
}
