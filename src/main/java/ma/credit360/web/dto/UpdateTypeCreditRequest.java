package ma.credit360.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ma.credit360.entity.enums.FamilleCredit;

public class UpdateTypeCreditRequest {
    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;

    @NotNull(message = "La famille est obligatoire")
    private FamilleCredit famille;

    @NotNull(message = "Le taux est obligatoire")
    @Positive(message = "Le taux doit être positif")
    private Double taux;

    @NotNull(message = "Le montant minimum est obligatoire")
    @Positive(message = "Le montant minimum doit être positif")
    private Double montantMin;

    @NotNull(message = "Le montant maximum est obligatoire")
    @Positive(message = "Le montant maximum doit être positif")
    private Double montantMax;

    @NotNull(message = "La durée minimale est obligatoire")
    @Positive(message = "La durée minimale doit être positive")
    private Integer dureeMin;

    @NotNull(message = "La durée maximale est obligatoire")
    @Positive(message = "La durée maximale doit être positive")
    private Integer dureeMax;

    public UpdateTypeCreditRequest() {}

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public FamilleCredit getFamille() { return famille; }
    public void setFamille(FamilleCredit famille) { this.famille = famille; }

    public Double getTaux() { return taux; }
    public void setTaux(Double taux) { this.taux = taux; }

    public Double getMontantMin() { return montantMin; }
    public void setMontantMin(Double montantMin) { this.montantMin = montantMin; }

    public Double getMontantMax() { return montantMax; }
    public void setMontantMax(Double montantMax) { this.montantMax = montantMax; }

    public Integer getDureeMin() { return dureeMin; }
    public void setDureeMin(Integer dureeMin) { this.dureeMin = dureeMin; }

    public Integer getDureeMax() { return dureeMax; }
    public void setDureeMax(Integer dureeMax) { this.dureeMax = dureeMax; }
}
