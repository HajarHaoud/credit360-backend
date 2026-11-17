package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.TypeNotification;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Utilisateur destinataire;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification type;

    private String referenceDossier;

    @Column(nullable = false)
    private boolean lue = false;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    public Notification() {}

    public Notification(Utilisateur destinataire, String titre, String message, TypeNotification type, String referenceDossier) {
        this.destinataire = destinataire;
        this.titre = titre;
        this.message = message;
        this.type = type;
        this.referenceDossier = referenceDossier;
        this.lue = false;
        this.dateCreation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getDestinataire() { return destinataire; }
    public void setDestinataire(Utilisateur destinataire) { this.destinataire = destinataire; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public TypeNotification getType() { return type; }
    public void setType(TypeNotification type) { this.type = type; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}
