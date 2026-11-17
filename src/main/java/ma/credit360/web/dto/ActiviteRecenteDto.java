package ma.credit360.web.dto;

import java.time.LocalDateTime;

public class ActiviteRecenteDto {
    private Long id;
    private String action;
    private String reference;
    private String utilisateur;
    private String description;
    private LocalDateTime dateAction;

    public ActiviteRecenteDto() {}

    public ActiviteRecenteDto(Long id, String action, String reference, String utilisateur, String description, LocalDateTime dateAction) {
        this.id = id;
        this.action = action;
        this.reference = reference;
        this.utilisateur = utilisateur;
        this.description = description;
        this.dateAction = dateAction;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }
}
