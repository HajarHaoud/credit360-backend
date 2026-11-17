package ma.credit360.web.dto;

import ma.credit360.entity.Notification;
import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private String titre;
    private String message;
    private String type;
    private String referenceDossier;
    private boolean lue;
    private LocalDateTime dateCreation;

    public NotificationDto() {}

    public NotificationDto(Notification entity) {
        this.id = entity.getId();
        this.titre = entity.getTitre();
        this.message = entity.getMessage();
        this.type = entity.getType().name();
        this.referenceDossier = entity.getReferenceDossier();
        this.lue = entity.isLue();
        this.dateCreation = entity.getDateCreation();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
