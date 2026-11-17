package ma.credit360.web.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateParametreRequest {
    @NotBlank(message = "La valeur est obligatoire")
    private String valeur;
    private String description;

    public UpdateParametreRequest() {}
    public UpdateParametreRequest(String valeur, String description) {
        this.valeur = valeur;
        this.description = description;
    }

    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
