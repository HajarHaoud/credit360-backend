package ma.credit360.web.dto;

import ma.credit360.entity.Dossier;
import java.time.LocalDateTime;

public class DossierItemDto {
    private String reference;
    private String clientNom;
    private String clientPrenom;
    private String cin;
    private String typeId;
    private String typeLibelle;
    private Double montant;
    private Integer duree;
    private String statut;
    private String statutLibelle;
    private LocalDateTime dateMaj;
    private LocalDateTime dateCreation;
    private String creeParNom;
    private String analysteNom;

    public DossierItemDto() {}

    public DossierItemDto(Dossier d) {
        this.reference = d.getReference();
        this.clientNom = d.getNom();
        this.clientPrenom = d.getPrenom();
        this.cin = d.getCin();
        this.typeId = d.getTypeCredit() != null ? d.getTypeCredit().getId() : null;
        this.typeLibelle = d.getTypeCredit() != null ? d.getTypeCredit().getLibelle() : null;
        this.montant = d.getMontant();
        this.duree = d.getDuree();
        this.statut = d.getStatut().name();
        this.statutLibelle = d.getStatut().getLibelle();
        this.dateMaj = d.getDateMaj();
        this.dateCreation = d.getDateCreation();
        this.creeParNom = d.getCreePar() != null ? d.getCreePar().getNom() : null;
        this.analysteNom = d.getAnalysteAffecte() != null ? d.getAnalysteAffecte().getNom() : null;
    }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }

    public String getClientPrenom() { return clientPrenom; }
    public void setClientPrenom(String clientPrenom) { this.clientPrenom = clientPrenom; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }

    public String getTypeLibelle() { return typeLibelle; }
    public void setTypeLibelle(String typeLibelle) { this.typeLibelle = typeLibelle; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getStatutLibelle() { return statutLibelle; }
    public void setStatutLibelle(String statutLibelle) { this.statutLibelle = statutLibelle; }

    public LocalDateTime getDateMaj() { return dateMaj; }
    public void setDateMaj(LocalDateTime dateMaj) { this.dateMaj = dateMaj; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getCreeParNom() { return creeParNom; }
    public void setCreeParNom(String creeParNom) { this.creeParNom = creeParNom; }

    public String getAnalysteNom() { return analysteNom; }
    public void setAnalysteNom(String analysteNom) { this.analysteNom = analysteNom; }
}
