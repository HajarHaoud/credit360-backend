package ma.credit360.web.dto;

import ma.credit360.entity.Simulation;

public class SimulationResponse {
    private Long id;
    private Double montant;
    private Integer dureeMois;
    private Double tauxAnnuel;
    private Double mensualite;
    private Double coutTotal;
    private Double coutInterets;
    private Double tauxEndettement;
    private Double capaciteRemboursement;
    private boolean eligible;
    private String motif;

    public SimulationResponse() {}

    public SimulationResponse(Simulation entity) {
        this.id = entity.getId();
        this.montant = entity.getMontant();
        this.dureeMois = entity.getDureeMois();
        this.tauxAnnuel = entity.getTauxAnnuel();
        this.mensualite = entity.getMensualite();
        this.coutTotal = entity.getCoutTotal();
        this.coutInterets = entity.getCoutInterets();
        this.tauxEndettement = entity.getTauxEndettement();
        this.capaciteRemboursement = entity.getCapaciteRemboursement();
        this.eligible = entity.isEligible();
        this.motif = entity.getMotif();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public Integer getDureeMois() { return dureeMois; }
    public void setDureeMois(Integer dureeMois) { this.dureeMois = dureeMois; }

    public Double getTauxAnnuel() { return tauxAnnuel; }
    public void setTauxAnnuel(Double tauxAnnuel) { this.tauxAnnuel = tauxAnnuel; }

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
}
