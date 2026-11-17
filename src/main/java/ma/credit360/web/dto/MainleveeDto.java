package ma.credit360.web.dto;

import ma.credit360.entity.Mainlevee;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MainleveeDto {
    private Long id;
    private String referenceMainlevee;
    private String referenceCredit;
    private String client;
    private String cin;
    private String typeGarantie;
    private String designationBien;
    private Double montantInitial;
    private LocalDate dateOctroi;
    private LocalDate dateSoldage;
    private String statut;
    private String statutLibelle;
    private Long documentPdfId;
    private String valideParNom;
    private LocalDateTime dateDemande;
    private LocalDateTime dateValidation;
    private LocalDateTime dateDelivrance;

    public MainleveeDto() {}

    public MainleveeDto(Mainlevee m) {
        this.id = m.getId();
        this.referenceMainlevee = m.getReferenceMainlevee();
        this.referenceCredit = m.getReferenceCredit();
        this.client = m.getClient();
        this.cin = m.getCin();
        this.typeGarantie = m.getTypeGarantie();
        this.designationBien = m.getDesignationBien();
        this.montantInitial = m.getMontantInitial();
        this.dateOctroi = m.getDateOctroi();
        this.dateSoldage = m.getDateSoldage();
        this.statut = m.getStatut().name();
        this.statutLibelle = m.getStatut().getLibelle();
        this.documentPdfId = m.getDocumentPdfId();
        this.valideParNom = m.getValidePar() != null ? m.getValidePar().getNom() : null;
        this.dateDemande = m.getDateDemande();
        this.dateValidation = m.getDateValidation();
        this.dateDelivrance = m.getDateDelivrance();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceMainlevee() { return referenceMainlevee; }
    public void setReferenceMainlevee(String referenceMainlevee) { this.referenceMainlevee = referenceMainlevee; }

    public String getReferenceCredit() { return referenceCredit; }
    public void setReferenceCredit(String referenceCredit) { this.referenceCredit = referenceCredit; }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTypeGarantie() { return typeGarantie; }
    public void setTypeGarantie(String typeGarantie) { this.typeGarantie = typeGarantie; }

    public String getDesignationBien() { return designationBien; }
    public void setDesignationBien(String designationBien) { this.designationBien = designationBien; }

    public Double getMontantInitial() { return montantInitial; }
    public void setMontantInitial(Double montantInitial) { this.montantInitial = montantInitial; }

    public LocalDate getDateOctroi() { return dateOctroi; }
    public void setDateOctroi(LocalDate dateOctroi) { this.dateOctroi = dateOctroi; }

    public LocalDate getDateSoldage() { return dateSoldage; }
    public void setDateSoldage(LocalDate dateSoldage) { this.dateSoldage = dateSoldage; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getStatutLibelle() { return statutLibelle; }
    public void setStatutLibelle(String statutLibelle) { this.statutLibelle = statutLibelle; }

    public Long getDocumentPdfId() { return documentPdfId; }
    public void setDocumentPdfId(Long documentPdfId) { this.documentPdfId = documentPdfId; }

    public String getValideParNom() { return valideParNom; }
    public void setValideParNom(String valideParNom) { this.valideParNom = valideParNom; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }

    public LocalDateTime getDateDelivrance() { return dateDelivrance; }
    public void setDateDelivrance(LocalDateTime dateDelivrance) { this.dateDelivrance = dateDelivrance; }
}
