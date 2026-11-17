package ma.credit360.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analyses_dossiers")
public class AnalyseDossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dossier_id", nullable = false, unique = true)
    private Dossier dossier;

    private Double revenuMensuel;
    private Double chargesMensuelles;
    private Double mensualiteEstimee;
    private Double tauxEndettement;
    private Double capaciteRemboursement;
    private Boolean eligible;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "analyste_id")
    private Utilisateur analyste;

    private LocalDateTime dateAnalyse;
    private boolean termine = false;

    public AnalyseDossier() {}

    @PrePersist
    public void onPrePersist() {
        this.dateAnalyse = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

    public Double getRevenuMensuel() { return revenuMensuel; }
    public void setRevenuMensuel(Double revenuMensuel) { this.revenuMensuel = revenuMensuel; }

    public Double getChargesMensuelles() { return chargesMensuelles; }
    public void setChargesMensuelles(Double chargesMensuelles) { this.chargesMensuelles = chargesMensuelles; }

    public Double getMensualiteEstimee() { return mensualiteEstimee; }
    public void setMensualiteEstimee(Double mensualiteEstimee) { this.mensualiteEstimee = mensualiteEstimee; }

    public Double getTauxEndettement() { return tauxEndettement; }
    public void setTauxEndettement(Double tauxEndettement) { this.tauxEndettement = tauxEndettement; }

    public Double getCapaciteRemboursement() { return capaciteRemboursement; }
    public void setCapaciteRemboursement(Double capaciteRemboursement) { this.capaciteRemboursement = capaciteRemboursement; }

    public Boolean getEligible() { return eligible; }
    public void setEligible(Boolean eligible) { this.eligible = eligible; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public Utilisateur getAnalyste() { return analyste; }
    public void setAnalyste(Utilisateur analyste) { this.analyste = analyste; }

    public LocalDateTime getDateAnalyse() { return dateAnalyse; }
    public void setDateAnalyse(LocalDateTime dateAnalyse) { this.dateAnalyse = dateAnalyse; }

    public boolean isTermine() { return termine; }
    public void setTermine(boolean termine) { this.termine = termine; }
}
