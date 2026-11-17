package ma.credit360.web.dto;

public class HabilitationCheckDto {
    private String reference;
    private Double montantDossier;
    private String roleRequis;
    private boolean autorise;
    private String message;

    public HabilitationCheckDto() {}

    public HabilitationCheckDto(String reference, Double montantDossier, String roleRequis, boolean autorise, String message) {
        this.reference = reference;
        this.montantDossier = montantDossier;
        this.roleRequis = roleRequis;
        this.autorise = autorise;
        this.message = message;
    }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Double getMontantDossier() { return montantDossier; }
    public void setMontantDossier(Double montantDossier) { this.montantDossier = montantDossier; }

    public String getRoleRequis() { return roleRequis; }
    public void setRoleRequis(String roleRequis) { this.roleRequis = roleRequis; }

    public boolean isAutorise() { return autorise; }
    public void setAutorise(boolean autorise) { this.autorise = autorise; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
