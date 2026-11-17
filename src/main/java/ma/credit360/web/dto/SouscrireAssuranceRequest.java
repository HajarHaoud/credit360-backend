package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import ma.credit360.entity.enums.TypeAssurance;

public class SouscrireAssuranceRequest {
    private String referenceDossier;

    @NotNull(message = "Le nom du client est obligatoire")
    private String client;

    private String cin;

    @NotNull(message = "Le type d'assurance est obligatoire")
    private TypeAssurance typeAssurance;

    @NotNull(message = "Le capital assuré est obligatoire")
    private Double capitalAssure;

    private Double tauxPrimePourcent;

    public SouscrireAssuranceRequest() {}

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public TypeAssurance getTypeAssurance() { return typeAssurance; }
    public void setTypeAssurance(TypeAssurance typeAssurance) { this.typeAssurance = typeAssurance; }

    public Double getCapitalAssure() { return capitalAssure; }
    public void setCapitalAssure(Double capitalAssure) { this.capitalAssure = capitalAssure; }

    public Double getTauxPrimePourcent() { return tauxPrimePourcent; }
    public void setTauxPrimePourcent(Double tauxPrimePourcent) { this.tauxPrimePourcent = tauxPrimePourcent; }
}
