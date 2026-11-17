package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class DemandeMainleveeRequest {
    @NotNull(message = "La référence du crédit est obligatoire")
    private String referenceCredit;

    @NotNull(message = "Le nom du client est obligatoire")
    private String client;

    private String cin;

    @NotNull(message = "Le type de garantie est obligatoire")
    private String typeGarantie;

    private String designationBien;

    @NotNull(message = "Le montant initial est obligatoire")
    private Double montantInitial;

    private LocalDate dateOctroi;
    private LocalDate dateSoldage;

    public DemandeMainleveeRequest() {}

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
}
