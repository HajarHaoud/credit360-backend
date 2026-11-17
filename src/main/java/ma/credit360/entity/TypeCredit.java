package ma.credit360.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ma.credit360.entity.enums.FamilleCredit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "types_credits")
public class TypeCredit {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false)
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FamilleCredit famille;

    @Column(nullable = false)
    private Double taux;

    @Column(nullable = false)
    private Double montantMin;

    @Column(nullable = false)
    private Double montantMax;

    @Column(nullable = false)
    private Integer dureeMin;

    @Column(nullable = false)
    private Integer dureeMax;

    @Column(nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "typeCredit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordre ASC")
    @JsonIgnore
    private List<PieceRequise> piecesRequises = new ArrayList<>();

    public TypeCredit() {}

    public TypeCredit(String id, String libelle, FamilleCredit famille, Double taux, Double montantMin, Double montantMax, Integer dureeMin, Integer dureeMax) {
        this.id = id;
        this.libelle = libelle;
        this.famille = famille;
        this.taux = taux;
        this.montantMin = montantMin;
        this.montantMax = montantMax;
        this.dureeMin = dureeMin;
        this.dureeMax = dureeMax;
        this.actif = true;
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

    public List<PieceRequise> getPiecesRequises() { return piecesRequises; }
    public void setPiecesRequises(List<PieceRequise> piecesRequises) { this.piecesRequises = piecesRequises; }
}
