package ma.credit360.web.dto;

import ma.credit360.entity.HistoriqueStatut;
import java.time.LocalDateTime;

public class HistoriqueDto {
    private Long id;
    private String referenceDossier;
    private String ancienStatut;
    private String nouveauStatut;
    private String action;
    private String utilisateur;
    private LocalDateTime dateAction;
    private String commentaire;

    public HistoriqueDto() {}

    public HistoriqueDto(HistoriqueStatut entity) {
        this.id = entity.getId();
        if (entity.getDossier() != null) {
            this.referenceDossier = entity.getDossier().getReference();
        }
        this.ancienStatut = entity.getAncienStatut() != null ? entity.getAncienStatut().name() : null;
        this.nouveauStatut = entity.getNouveauStatut().name();
        this.action = entity.getAction();
        if (entity.getUtilisateur() != null) {
            this.utilisateur = entity.getUtilisateur().getNom();
        }
        this.dateAction = entity.getDateAction();
        this.commentaire = entity.getCommentaire();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getAncienStatut() { return ancienStatut; }
    public void setAncienStatut(String ancienStatut) { this.ancienStatut = ancienStatut; }

    public String getNouveauStatut() { return nouveauStatut; }
    public void setNouveauStatut(String nouveauStatut) { this.nouveauStatut = nouveauStatut; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
