package ma.credit360.web.dto;

import ma.credit360.entity.ComplementDossier;
import java.time.LocalDateTime;

public class ComplementDto {
    private Long id;
    private String referenceDossier;
    private String motif;
    private String commentaire;
    private String piecesConcernees;
    private String statut;
    private LocalDateTime dateDemande;
    private LocalDateTime dateReponse;
    private LocalDateTime dateCloture;
    private String demandeurNom;
    private String reponseCommentaire;

    public ComplementDto() {}

    public ComplementDto(ComplementDossier entity) {
        this.id = entity.getId();
        if (entity.getDossier() != null) {
            this.referenceDossier = entity.getDossier().getReference();
        }
        this.motif = entity.getMotif();
        this.commentaire = entity.getCommentaire();
        this.piecesConcernees = entity.getPiecesConcernees();
        this.statut = entity.getStatut().name();
        this.dateDemande = entity.getDateDemande();
        this.dateReponse = entity.getDateReponse();
        this.dateCloture = entity.getDateCloture();
        if (entity.getDemandePar() != null) {
            this.demandeurNom = entity.getDemandePar().getNom();
        }
        this.reponseCommentaire = entity.getReponseCommentaire();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getPiecesConcernees() { return piecesConcernees; }
    public void setPiecesConcernees(String piecesConcernees) { this.piecesConcernees = piecesConcernees; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public LocalDateTime getDateReponse() { return dateReponse; }
    public void setDateReponse(LocalDateTime dateReponse) { this.dateReponse = dateReponse; }

    public LocalDateTime getDateCloture() { return dateCloture; }
    public void setDateCloture(LocalDateTime dateCloture) { this.dateCloture = dateCloture; }

    public String getDemandeurNom() { return demandeurNom; }
    public void setDemandeurNom(String demandeurNom) { this.demandeurNom = demandeurNom; }

    public String getReponseCommentaire() { return reponseCommentaire; }
    public void setReponseCommentaire(String reponseCommentaire) { this.reponseCommentaire = reponseCommentaire; }
}
