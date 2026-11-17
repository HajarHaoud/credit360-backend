package ma.credit360.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

public class UpdateDossierRequest {
    private String nom;
    private String prenom;
    private String telephone;

    @Email(message = "Format d'email invalide")
    private String email;

    private String profession;
    private String adresse;

    @Positive(message = "Le revenu mensuel doit être positif")
    private Double revenuMensuel;

    private Double chargesMensuelles;

    @Positive(message = "Le montant doit être positif")
    private Double montant;

    @Positive(message = "La durée doit être positive")
    private Integer duree;

    public UpdateDossierRequest() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Double getRevenuMensuel() { return revenuMensuel; }
    public void setRevenuMensuel(Double revenuMensuel) { this.revenuMensuel = revenuMensuel; }

    public Double getChargesMensuelles() { return chargesMensuelles; }
    public void setChargesMensuelles(Double chargesMensuelles) { this.chargesMensuelles = chargesMensuelles; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }
}
