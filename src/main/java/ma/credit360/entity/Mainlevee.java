package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.StatutMainlevee;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mainlevees")
public class Mainlevee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String referenceMainlevee; // MLV00001

    @Column(nullable = false)
    private String referenceCredit; // CRD...

    @Column(nullable = false)
    private String client;

    private String cin;

    @Column(nullable = false)
    private String typeGarantie; // Hypothèque immobilière, Gage de véhicule, Nantissement

    private String designationBien; // ex: Titre foncier N° 12345/R, Véhicule matricule 45678-A-26

    @Column(nullable = false)
    private Double montantInitial;

    @Column(nullable = false)
    private LocalDate dateOctroi;

    @Column(nullable = false)
    private LocalDate dateSoldage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutMainlevee statut = StatutMainlevee.CREDIT_SOLDE;

    private Long documentPdfId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "valide_par_id")
    private Utilisateur validePar;

    private LocalDateTime dateDemande;
    private LocalDateTime dateValidation;
    private LocalDateTime dateDelivrance;

    public Mainlevee() {}

    public Mainlevee(String referenceMainlevee, String referenceCredit, String client, String cin,
                     String typeGarantie, String designationBien, Double montantInitial,
                     LocalDate dateOctroi, LocalDate dateSoldage) {
        this.referenceMainlevee = referenceMainlevee;
        this.referenceCredit = referenceCredit;
        this.client = client;
        this.cin = cin;
        this.typeGarantie = typeGarantie;
        this.designationBien = designationBien;
        this.montantInitial = montantInitial;
        this.dateOctroi = dateOctroi;
        this.dateSoldage = dateSoldage;
        this.statut = StatutMainlevee.CREDIT_SOLDE;
        this.dateDemande = LocalDateTime.now();
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

    public StatutMainlevee getStatut() { return statut; }
    public void setStatut(StatutMainlevee statut) { this.statut = statut; }

    public Long getDocumentPdfId() { return documentPdfId; }
    public void setDocumentPdfId(Long documentPdfId) { this.documentPdfId = documentPdfId; }

    public Utilisateur getValidePar() { return validePar; }
    public void setValidePar(Utilisateur validePar) { this.validePar = validePar; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }

    public LocalDateTime getDateDelivrance() { return dateDelivrance; }
    public void setDateDelivrance(LocalDateTime dateDelivrance) { this.dateDelivrance = dateDelivrance; }
}
