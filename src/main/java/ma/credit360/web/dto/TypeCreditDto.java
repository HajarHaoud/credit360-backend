package ma.credit360.web.dto;

import ma.credit360.entity.TypeCredit;
import ma.credit360.entity.enums.FamilleCredit;

public class TypeCreditDto {
    private String id;
    private String libelle;
    private FamilleCredit famille;
    private Double taux;
    private Double montantMin;
    private Double montantMax;
    private Integer dureeMin;
    private Integer dureeMax;
    private boolean actif;

    public TypeCreditDto() {}

    public TypeCreditDto(TypeCredit entity) {
        this.id = entity.getId();
        this.libelle = entity.getLibelle();
        this.famille = entity.getFamille();
        this.taux = entity.getTaux();
        this.montantMin = entity.getMontantMin();
        this.montantMax = entity.getMontantMax();
        this.dureeMin = entity.getDureeMin();
        this.dureeMax = entity.getDureeMax();
        this.actif = entity.isActif();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}
