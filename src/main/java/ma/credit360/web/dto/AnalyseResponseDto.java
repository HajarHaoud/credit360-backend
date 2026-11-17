package ma.credit360.web.dto;

import ma.credit360.entity.AnalyseDossier;
import java.time.LocalDateTime;

public class AnalyseResponseDto {
    private Long id;
    private String referenceDossier;
    private Double revenuMensuel;
    private Double chargesMensuelles;
    private Double mensualiteEstimee;
    private Double tauxEndettement;
    private Double capaciteRemboursement;
    private Boolean eligible;
    private String observations;
    private String analysteNom;
    private LocalDateTime dateAnalyse;
    private boolean termine;

    public AnalyseResponseDto() {}

    public AnalyseResponseDto(AnalyseDossier entity) {
        this.id = entity.getId();
        if (entity.getDossier() != null) {
            this.referenceDossier = entity.getDossier().getReference();
        }
        this.revenuMensuel = entity.getRevenuMensuel();
        this.chargesMensuelles = entity.getChargesMensuelles();
        this.mensualiteEstimee = entity.getMensualiteEstimee();
        this.tauxEndettement = entity.getTauxEndettement();
        this.capaciteRemboursement = entity.getCapaciteRemboursement();
        this.eligible = entity.getEligible();
        this.observations = entity.getObservations();
        if (entity.getAnalyste() != null) {
            this.analysteNom = entity.getAnalyste().getNom();
        }
        this.dateAnalyse = entity.getDateAnalyse();
        this.termine = entity.isTermine();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

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

    public String getAnalysteNom() { return analysteNom; }
    public void setAnalysteNom(String analysteNom) { this.analysteNom = analysteNom; }

    public LocalDateTime getDateAnalyse() { return dateAnalyse; }
    public void setDateAnalyse(LocalDateTime dateAnalyse) { this.dateAnalyse = dateAnalyse; }

    public boolean isTermine() { return termine; }
    public void setTermine(boolean termine) { this.termine = termine; }
}
