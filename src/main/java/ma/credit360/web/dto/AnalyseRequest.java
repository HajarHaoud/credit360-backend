package ma.credit360.web.dto;

public class AnalyseRequest {
    private Double revenuMensuel;
    private Double chargesMensuelles;
    private Double mensualiteEstimee;
    private Double tauxEndettement;
    private Double capaciteRemboursement;
    private Boolean eligible;
    private String observations;

    public AnalyseRequest() {}

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
}
