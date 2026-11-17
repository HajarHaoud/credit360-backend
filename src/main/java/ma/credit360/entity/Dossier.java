package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.StatutDossier;
import java.time.LocalDateTime;

@Entity
@Table(name = "dossiers")
public class Dossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String reference;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, length = 30)
    private String cin;

    @Column(nullable = false, length = 30)
    private String telephone;

    @Column(nullable = false)
    private String email;

    private String profession;
    private String adresse;

    @Column(nullable = false)
    private Double revenuMensuel;

    private Double chargesMensuelles = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_credit_id", nullable = false)
    private TypeCredit typeCredit;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private Integer duree;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDossier statut = StatutDossier.A_COMPLETER;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cree_par_id")
    private Utilisateur creePar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "analyste_affecte_id")
    private Utilisateur analysteAffecte;

    private LocalDateTime dateCreation;
    private LocalDateTime dateMaj;

    public Dossier() {}

    @PrePersist
    public void onPrePersist() {
        this.dateCreation = LocalDateTime.now();
        this.dateMaj = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutDossier.A_COMPLETER;
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.dateMaj = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

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

    public TypeCredit getTypeCredit() { return typeCredit; }
    public void setTypeCredit(TypeCredit typeCredit) { this.typeCredit = typeCredit; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }

    public StatutDossier getStatut() { return statut; }
    public void setStatut(StatutDossier statut) { this.statut = statut; }

    public Utilisateur getCreePar() { return creePar; }
    public void setCreePar(Utilisateur creePar) { this.creePar = creePar; }

    public Utilisateur getAnalysteAffecte() { return analysteAffecte; }
    public void setAnalysteAffecte(Utilisateur analysteAffecte) { this.analysteAffecte = analysteAffecte; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateMaj() { return dateMaj; }
    public void setDateMaj(LocalDateTime dateMaj) { this.dateMaj = dateMaj; }
}
