package ma.credit360.web.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class DemandeComplementRequest {
    @NotBlank(message = "Le motif est obligatoire")
    private String motif;

    private List<Long> piecesConcernees;
    private String commentaire;

    public DemandeComplementRequest() {}

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public List<Long> getPiecesConcernees() { return piecesConcernees; }
    public void setPiecesConcernees(List<Long> piecesConcernees) { this.piecesConcernees = piecesConcernees; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
