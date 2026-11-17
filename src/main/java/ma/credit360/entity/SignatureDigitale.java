package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.StatutSignature;
import ma.credit360.entity.enums.TypeSignataire;

import java.time.LocalDateTime;

@Entity
@Table(name = "signatures_digitales")
public class SignatureDigitale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dossier_id", nullable = false)
    private Dossier dossier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeSignataire typeSignataire;

    @Column(nullable = false)
    private String nomSignataire;

    private String cin;
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSignature statut = StatutSignature.EN_ATTENTE;

    private String codeOtp;
    private LocalDateTime dateEnvoiOtp;
    private LocalDateTime dateSignature;
    private String adresseIp;
    private String empreinteSha256;
    private Long documentPdfId;

    public SignatureDigitale() {}

    public SignatureDigitale(Dossier dossier, TypeSignataire typeSignataire, String nomSignataire, String cin, String telephone) {
        this.dossier = dossier;
        this.typeSignataire = typeSignataire;
        this.nomSignataire = nomSignataire;
        this.cin = cin;
        this.telephone = telephone;
        this.statut = StatutSignature.EN_ATTENTE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

    public TypeSignataire getTypeSignataire() { return typeSignataire; }
    public void setTypeSignataire(TypeSignataire typeSignataire) { this.typeSignataire = typeSignataire; }

    public String getNomSignataire() { return nomSignataire; }
    public void setNomSignataire(String nomSignataire) { this.nomSignataire = nomSignataire; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public StatutSignature getStatut() { return statut; }
    public void setStatut(StatutSignature statut) { this.statut = statut; }

    public String getCodeOtp() { return codeOtp; }
    public void setCodeOtp(String codeOtp) { this.codeOtp = codeOtp; }

    public LocalDateTime getDateEnvoiOtp() { return dateEnvoiOtp; }
    public void setDateEnvoiOtp(LocalDateTime dateEnvoiOtp) { this.dateEnvoiOtp = dateEnvoiOtp; }

    public LocalDateTime getDateSignature() { return dateSignature; }
    public void setDateSignature(LocalDateTime dateSignature) { this.dateSignature = dateSignature; }

    public String getAdresseIp() { return adresseIp; }
    public void setAdresseIp(String adresseIp) { this.adresseIp = adresseIp; }

    public String getEmpreinteSha256() { return empreinteSha256; }
    public void setEmpreinteSha256(String empreinteSha256) { this.empreinteSha256 = empreinteSha256; }

    public Long getDocumentPdfId() { return documentPdfId; }
    public void setDocumentPdfId(Long documentPdfId) { this.documentPdfId = documentPdfId; }
}
