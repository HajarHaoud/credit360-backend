package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import ma.credit360.entity.enums.TypeDecision;

public class DecisionRequest {
    @NotNull(message = "Le type de décision est obligatoire")
    private TypeDecision typeDecision;

    private Double montantAccorde;
    private Integer dureeAccordee;
    private Double tauxAccorde;
    private String motif;

    public DecisionRequest() {}

    public TypeDecision getTypeDecision() { return typeDecision; }
    public void setTypeDecision(TypeDecision typeDecision) { this.typeDecision = typeDecision; }

    public Double getMontantAccorde() { return montantAccorde; }
    public void setMontantAccorde(Double montantAccorde) { this.montantAccorde = montantAccorde; }

    public Integer getDureeAccordee() { return dureeAccordee; }
    public void setDureeAccordee(Integer dureeAccordee) { this.dureeAccordee = dureeAccordee; }

    public Double getTauxAccorde() { return tauxAccorde; }
    public void setTauxAccorde(Double tauxAccorde) { this.tauxAccorde = tauxAccorde; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
}
