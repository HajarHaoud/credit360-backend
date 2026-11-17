package ma.credit360.web.dto;

import ma.credit360.entity.Decision;
import java.time.LocalDateTime;

public class DecisionResponseDto {
    private Long id;
    private String referenceDossier;
    private String typeDecision;
    private Double montantAccorde;
    private Integer dureeAccordee;
    private Double tauxAccorde;
    private String motif;
    private String decideParNom;
    private LocalDateTime dateDecision;

    public DecisionResponseDto() {}

    public DecisionResponseDto(Decision entity) {
        this.id = entity.getId();
        if (entity.getDossier() != null) {
            this.referenceDossier = entity.getDossier().getReference();
        }
        this.typeDecision = entity.getTypeDecision().name();
        this.montantAccorde = entity.getMontantAccorde();
        this.dureeAccordee = entity.getDureeAccordee();
        this.tauxAccorde = entity.getTauxAccorde();
        this.motif = entity.getMotif();
        if (entity.getDecidePar() != null) {
            this.decideParNom = entity.getDecidePar().getNom();
        }
        this.dateDecision = entity.getDateDecision();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getTypeDecision() { return typeDecision; }
    public void setTypeDecision(String typeDecision) { this.typeDecision = typeDecision; }

    public Double getMontantAccorde() { return montantAccorde; }
    public void setMontantAccorde(Double montantAccorde) { this.montantAccorde = montantAccorde; }

    public Integer getDureeAccordee() { return dureeAccordee; }
    public void setDureeAccordee(Integer dureeAccordee) { this.dureeAccordee = dureeAccordee; }

    public Double getTauxAccorde() { return tauxAccorde; }
    public void setTauxAccorde(Double tauxAccorde) { this.tauxAccorde = tauxAccorde; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getDecideParNom() { return decideParNom; }
    public void setDecideParNom(String decideParNom) { this.decideParNom = decideParNom; }

    public LocalDateTime getDateDecision() { return dateDecision; }
    public void setDateDecision(LocalDateTime dateDecision) { this.dateDecision = dateDecision; }
}
