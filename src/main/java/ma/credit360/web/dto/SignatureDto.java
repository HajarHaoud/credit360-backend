package ma.credit360.web.dto;

import ma.credit360.entity.SignatureDigitale;

import java.time.LocalDateTime;

public class SignatureDto {
    private Long id;
    private String referenceDossier;
    private String typeSignataire;
    private String typeSignataireLibelle;
    private String nomSignataire;
    private String cin;
    private String telephone;
    private String statut;
    private String statutLibelle;
    private LocalDateTime dateSignature;
    private String empreinteSha256;

    public SignatureDto() {}

    public SignatureDto(SignatureDigitale s) {
        this.id = s.getId();
        this.referenceDossier = s.getDossier() != null ? s.getDossier().getReference() : null;
        this.typeSignataire = s.getTypeSignataire().name();
        this.typeSignataireLibelle = s.getTypeSignataire().getLibelle();
        this.nomSignataire = s.getNomSignataire();
        this.cin = s.getCin();
        this.telephone = s.getTelephone();
        this.statut = s.getStatut().name();
        this.statutLibelle = s.getStatut().getLibelle();
        this.dateSignature = s.getDateSignature();
        this.empreinteSha256 = s.getEmpreinteSha256();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getTypeSignataire() { return typeSignataire; }
    public void setTypeSignataire(String typeSignataire) { this.typeSignataire = typeSignataire; }

    public String getTypeSignataireLibelle() { return typeSignataireLibelle; }
    public void setTypeSignataireLibelle(String typeSignataireLibelle) { this.typeSignataireLibelle = typeSignataireLibelle; }

    public String getNomSignataire() { return nomSignataire; }
    public void setNomSignataire(String nomSignataire) { this.nomSignataire = nomSignataire; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getStatutLibelle() { return statutLibelle; }
    public void setStatutLibelle(String statutLibelle) { this.statutLibelle = statutLibelle; }

    public LocalDateTime getDateSignature() { return dateSignature; }
    public void setDateSignature(LocalDateTime dateSignature) { this.dateSignature = dateSignature; }

    public String getEmpreinteSha256() { return empreinteSha256; }
    public void setEmpreinteSha256(String empreinteSha256) { this.empreinteSha256 = empreinteSha256; }
}
