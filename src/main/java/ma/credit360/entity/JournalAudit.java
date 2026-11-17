package ma.credit360.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journaux_audit")
public class JournalAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long utilisateurId;
    private String utilisateurNom;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entite;

    private String reference;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    private LocalDateTime dateAction;

    private String adresseIp;

    public JournalAudit() {}

    public JournalAudit(Long utilisateurId, String utilisateurNom, String action, String entite, String reference, String details, String adresseIp) {
        this.utilisateurId = utilisateurId;
        this.utilisateurNom = utilisateurNom;
        this.action = action;
        this.entite = entite;
        this.reference = reference;
        this.details = details;
        this.dateAction = LocalDateTime.now();
        this.adresseIp = adresseIp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public String getUtilisateurNom() { return utilisateurNom; }
    public void setUtilisateurNom(String utilisateurNom) { this.utilisateurNom = utilisateurNom; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntite() { return entite; }
    public void setEntite(String entite) { this.entite = entite; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }

    public String getAdresseIp() { return adresseIp; }
    public void setAdresseIp(String adresseIp) { this.adresseIp = adresseIp; }
}
