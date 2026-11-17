package ma.credit360.web.dto;

import ma.credit360.entity.Dossier;
import java.time.LocalDateTime;

public class DossierDetailDto {
    private Long id;
    private String reference;
    private String nom;
    private String prenom;
    private String cin;
    private String telephone;
    private String email;
    private String profession;
    private String adresse;
    private Double revenuMensuel;
    private Double chargesMensuelles;
    private String typeId;
    private String typeLibelle;
    private Double typeTaux;
    private Double montant;
    private Integer duree;
    private String statut;
    private String statutLibelle;
    private LocalDateTime dateCreation;
    private LocalDateTime dateMaj;
    private UserDto creePar;
    private UserDto analysteAffecte;
    private boolean modifiable;
    private int piecesRequisesCount;
    private int piecesDeposeesCount;

    public DossierDetailDto() {}

    public DossierDetailDto(Dossier d) {
        this(d, false, 0, 0);
    }

    public DossierDetailDto(Dossier d, boolean modifiable, int piecesRequisesCount, int piecesDeposeesCount) {
        this.id = d.getId();
        this.reference = d.getReference();
        this.nom = d.getNom();
        this.prenom = d.getPrenom();
        this.cin = d.getCin();
        this.telephone = d.getTelephone();
        this.email = d.getEmail();
        this.profession = d.getProfession();
        this.adresse = d.getAdresse();
        this.revenuMensuel = d.getRevenuMensuel();
        this.chargesMensuelles = d.getChargesMensuelles();
        if (d.getTypeCredit() != null) {
            this.typeId = d.getTypeCredit().getId();
            this.typeLibelle = d.getTypeCredit().getLibelle();
            this.typeTaux = d.getTypeCredit().getTaux();
        }
        this.montant = d.getMontant();
        this.duree = d.getDuree();
        this.statut = d.getStatut().name();
        this.statutLibelle = d.getStatut().getLibelle();
        this.dateCreation = d.getDateCreation();
        this.dateMaj = d.getDateMaj();
        if (d.getCreePar() != null) {
            this.creePar = new UserDto(d.getCreePar().getId(), d.getCreePar().getNom(), d.getCreePar().getFonction(), d.getCreePar().getEmail(), d.getCreePar().getRole().name(), d.getCreePar().isActif());
        }
        if (d.getAnalysteAffecte() != null) {
            this.analysteAffecte = new UserDto(d.getAnalysteAffecte().getId(), d.getAnalysteAffecte().getNom(), d.getAnalysteAffecte().getFonction(), d.getAnalysteAffecte().getEmail(), d.getAnalysteAffecte().getRole().name(), d.getAnalysteAffecte().isActif());
        }
        this.modifiable = modifiable;
        this.piecesRequisesCount = piecesRequisesCount;
        this.piecesDeposeesCount = piecesDeposeesCount;
    }

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

    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }

    public String getTypeLibelle() { return typeLibelle; }
    public void setTypeLibelle(String typeLibelle) { this.typeLibelle = typeLibelle; }

    public Double getTypeTaux() { return typeTaux; }
    public void setTypeTaux(Double typeTaux) { this.typeTaux = typeTaux; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public Integer getDuree() { return duree; }
    public void setDuree(Integer duree) { this.duree = duree; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getStatutLibelle() { return statutLibelle; }
    public void setStatutLibelle(String statutLibelle) { this.statutLibelle = statutLibelle; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateMaj() { return dateMaj; }
    public void setDateMaj(LocalDateTime dateMaj) { this.dateMaj = dateMaj; }

    public UserDto getCreePar() { return creePar; }
    public void setCreePar(UserDto creePar) { this.creePar = creePar; }

    public UserDto getAnalysteAffecte() { return analysteAffecte; }
    public void setAnalysteAffecte(UserDto analysteAffecte) { this.analysteAffecte = analysteAffecte; }

    public boolean isModifiable() { return modifiable; }
    public void setModifiable(boolean modifiable) { this.modifiable = modifiable; }

    public int getPiecesRequisesCount() { return piecesRequisesCount; }
    public void setPiecesRequisesCount(int piecesRequisesCount) { this.piecesRequisesCount = piecesRequisesCount; }

    public int getPiecesDeposeesCount() { return piecesDeposeesCount; }
    public void setPiecesDeposeesCount(int piecesDeposeesCount) { this.piecesDeposeesCount = piecesDeposeesCount; }
}
