package ma.credit360.web.dto;

import ma.credit360.entity.JournalAudit;
import java.time.LocalDateTime;

public class JournalAuditDto {
    private Long id;
    private Long utilisateurId;
    private String utilisateurNom;
    private String action;
    private String entite;
    private String reference;
    private String details;
    private LocalDateTime dateAction;
    private String adresseIp;

    public JournalAuditDto() {}

    public JournalAuditDto(JournalAudit entity) {
        this.id = entity.getId();
        this.utilisateurId = entity.getUtilisateurId();
        this.utilisateurNom = entity.getUtilisateurNom();
        this.action = entity.getAction();
        this.entite = entity.getEntite();
        this.reference = entity.getReference();
        this.details = entity.getDetails();
        this.dateAction = entity.getDateAction();
        this.adresseIp = entity.getAdresseIp();
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
