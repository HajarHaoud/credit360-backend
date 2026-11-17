package ma.credit360.web.dto;

import ma.credit360.entity.AssuranceCredit;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssuranceCreditDto {
    private Long id;
    private String referenceContrat;
    private String referenceDossier;
    private String client;
    private String cin;
    private String typeAssurance;
    private String typeAssuranceLibelle;
    private Double capitalAssure;
    private Double tauxPrimePourcent;
    private Double primeMensuelle;
    private String statut;
    private String statutLibelle;
    private String compagnieAssurance;
    private LocalDate dateEffet;
    private LocalDateTime dateSouscription;

    public AssuranceCreditDto() {}

    public AssuranceCreditDto(AssuranceCredit a) {
        this.id = a.getId();
        this.referenceContrat = a.getReferenceContrat();
        this.referenceDossier = a.getReferenceDossier();
        this.client = a.getClient();
        this.cin = a.getCin();
        this.typeAssurance = a.getTypeAssurance().name();
        this.typeAssuranceLibelle = a.getTypeAssurance().getLibelle();
        this.capitalAssure = a.getCapitalAssure();
        this.tauxPrimePourcent = a.getTauxPrimePourcent();
        this.primeMensuelle = a.getPrimeMensuelle();
        this.statut = a.getStatut().name();
        this.statutLibelle = a.getStatut().getLibelle();
        this.compagnieAssurance = a.getCompagnieAssurance();
        this.dateEffet = a.getDateEffet();
        this.dateSouscription = a.getDateSouscription();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceContrat() { return referenceContrat; }
    public void setReferenceContrat(String referenceContrat) { this.referenceContrat = referenceContrat; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTypeAssurance() { return typeAssurance; }
    public void setTypeAssurance(String typeAssurance) { this.typeAssurance = typeAssurance; }

    public String getTypeAssuranceLibelle() { return typeAssuranceLibelle; }
    public void setTypeAssuranceLibelle(String typeAssuranceLibelle) { this.typeAssuranceLibelle = typeAssuranceLibelle; }

    public Double getCapitalAssure() { return capitalAssure; }
    public void setCapitalAssure(Double capitalAssure) { this.capitalAssure = capitalAssure; }

    public Double getTauxPrimePourcent() { return tauxPrimePourcent; }
    public void setTauxPrimePourcent(Double tauxPrimePourcent) { this.tauxPrimePourcent = tauxPrimePourcent; }

    public Double getPrimeMensuelle() { return primeMensuelle; }
    public void setPrimeMensuelle(Double primeMensuelle) { this.primeMensuelle = primeMensuelle; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getStatutLibelle() { return statutLibelle; }
    public void setStatutLibelle(String statutLibelle) { this.statutLibelle = statutLibelle; }

    public String getCompagnieAssurance() { return compagnieAssurance; }
    public void setCompagnieAssurance(String compagnieAssurance) { this.compagnieAssurance = compagnieAssurance; }

    public LocalDate getDateEffet() { return dateEffet; }
    public void setDateEffet(LocalDate dateEffet) { this.dateEffet = dateEffet; }

    public LocalDateTime getDateSouscription() { return dateSouscription; }
    public void setDateSouscription(LocalDateTime dateSouscription) { this.dateSouscription = dateSouscription; }
}
