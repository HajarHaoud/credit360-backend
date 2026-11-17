package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.TypeDecision;
import java.time.LocalDateTime;

@Entity
@Table(name = "decisions")
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    private Dossier dossier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDecision typeDecision;

    private Double montantAccorde;
    private Integer dureeAccordee;
    private Double tauxAccorde;

    @Column(columnDefinition = "TEXT")
    private String motif;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "decide_par_id")
    private Utilisateur decidePar;

    @Column(nullable = false)
    private LocalDateTime dateDecision;

    public Decision() {}

    @PrePersist
    public void onPrePersist() {
        this.dateDecision = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

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

    public Utilisateur getDecidePar() { return decidePar; }
    public void setDecidePar(Utilisateur decidePar) { this.decidePar = decidePar; }

    public LocalDateTime getDateDecision() { return dateDecision; }
    public void setDateDecision(LocalDateTime dateDecision) { this.dateDecision = dateDecision; }
}
