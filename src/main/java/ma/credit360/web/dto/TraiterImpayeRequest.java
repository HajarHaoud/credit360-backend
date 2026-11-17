package ma.credit360.web.dto;

import ma.credit360.entity.enums.ActionImpaye;
import ma.credit360.entity.enums.StatutImpaye;

public class TraiterImpayeRequest {
    private StatutImpaye nouveauStatut;
    private ActionImpaye nouvelleAction;
    private String commentaire;

    public TraiterImpayeRequest() {}

    public StatutImpaye getNouveauStatut() { return nouveauStatut; }
    public void setNouveauStatut(StatutImpaye nouveauStatut) { this.nouveauStatut = nouveauStatut; }

    public ActionImpaye getNouvelleAction() { return nouvelleAction; }
    public void setNouvelleAction(ActionImpaye nouvelleAction) { this.nouvelleAction = nouvelleAction; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
