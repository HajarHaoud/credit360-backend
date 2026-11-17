package ma.credit360.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SimulationRequest {
    @NotBlank(message = "Le type de crédit est obligatoire")
    private String typeCreditId;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double montant;

    @NotNull(message = "La durée en mois est obligatoire")
    @Positive(message = "La durée doit être positive")
    private Integer dureeMois;

    private Double tauxAnnuel;

    @NotNull(message = "Le revenu mensuel est obligatoire")
    @Positive(message = "Le revenu mensuel doit être positif")
    private Double revenuMensuel;

    private Double chargesMensuelles = 0.0;

    public SimulationRequest() {}

    public String getTypeCreditId() { return typeCreditId; }
    public void setTypeCreditId(String typeCreditId) { this.typeCreditId = typeCreditId; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public Integer getDureeMois() { return dureeMois; }
    public void setDureeMois(Integer dureeMois) { this.dureeMois = dureeMois; }

    public Double getTauxAnnuel() { return tauxAnnuel; }
    public void setTauxAnnuel(Double tauxAnnuel) { this.tauxAnnuel = tauxAnnuel; }

    public Double getRevenuMensuel() { return revenuMensuel; }
    public void setRevenuMensuel(Double revenuMensuel) { this.revenuMensuel = revenuMensuel; }

    public Double getChargesMensuelles() { return chargesMensuelles; }
    public void setChargesMensuelles(Double chargesMensuelles) { this.chargesMensuelles = chargesMensuelles; }
}
