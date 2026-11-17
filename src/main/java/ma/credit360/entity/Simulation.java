package ma.credit360.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulations")
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id")
    private Dossier dossier;

    @Column(nullable = false)
    private String typeCreditId;

    private String typeCreditLibelle;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private Integer dureeMois;

    @Column(nullable = false)
    private Double tauxAnnuel;

    private Double revenuMensuel;
    private Double chargesMensuelles;
    private Double mensualite;
    private Double coutTotal;
    private Double coutInterets;
    private Double tauxEndettement;
    private Double capaciteRemboursement;
    private boolean eligible;
    private String motif;

    @Column(nullable = false)
    private LocalDateTime dateSimulation;

    public Simulation() {}

    @PrePersist
    public void onPrePersist() {
        this.dateSimulation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

    public String getTypeCreditId() { return typeCreditId; }
    public void setTypeCreditId(String typeCreditId) { this.typeCreditId = typeCreditId; }

    public String getTypeCreditLibelle() { return typeCreditLibelle; }
    public void setTypeCreditLibelle(String typeCreditLibelle) { this.typeCreditLibelle = typeCreditLibelle; }

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

    public Double getMensualite() { return mensualite; }
    public void setMensualite(Double mensualite) { this.mensualite = mensualite; }

    public Double getCoutTotal() { return coutTotal; }
    public void setCoutTotal(Double coutTotal) { this.coutTotal = coutTotal; }

    public Double getCoutInterets() { return coutInterets; }
    public void setCoutInterets(Double coutInterets) { this.coutInterets = coutInterets; }

    public Double getTauxEndettement() { return tauxEndettement; }
    public void setTauxEndettement(Double tauxEndettement) { this.tauxEndettement = tauxEndettement; }

    public Double getCapaciteRemboursement() { return capaciteRemboursement; }
    public void setCapaciteRemboursement(Double capaciteRemboursement) { this.capaciteRemboursement = capaciteRemboursement; }

    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public LocalDateTime getDateSimulation() { return dateSimulation; }
    public void setDateSimulation(LocalDateTime dateSimulation) { this.dateSimulation = dateSimulation; }
}
