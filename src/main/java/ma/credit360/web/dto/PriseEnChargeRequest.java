package ma.credit360.web.dto;

public class PriseEnChargeRequest {
    private String commentaire;

    public PriseEnChargeRequest() {}
    public PriseEnChargeRequest(String commentaire) { this.commentaire = commentaire; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
