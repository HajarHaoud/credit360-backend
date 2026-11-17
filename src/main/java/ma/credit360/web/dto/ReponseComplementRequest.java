package ma.credit360.web.dto;

public class ReponseComplementRequest {
    private String commentaire;

    public ReponseComplementRequest() {}
    public ReponseComplementRequest(String commentaire) { this.commentaire = commentaire; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
