package ma.credit360.web.dto;

public class UpdateStatutRequest {
    private boolean actif;

    public UpdateStatutRequest() {}
    public UpdateStatutRequest(boolean actif) { this.actif = actif; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}
