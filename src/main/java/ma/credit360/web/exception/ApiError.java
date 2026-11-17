package ma.credit360.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private LocalDateTime horodatage;
    private int statut;
    private String erreur;
    private String message;
    private String chemin;
    private Map<String, String> erreursValidation;

    public ApiError() {
        this.horodatage = LocalDateTime.now();
    }

    public ApiError(int statut, String erreur, String message, String chemin) {
        this.horodatage = LocalDateTime.now();
        this.statut = statut;
        this.erreur = erreur;
        this.message = message;
        this.chemin = chemin;
    }

    public ApiError(int statut, String erreur, String message, String chemin, Map<String, String> erreursValidation) {
        this(statut, erreur, message, chemin);
        this.erreursValidation = erreursValidation;
    }

    public LocalDateTime getHorodatage() { return horodatage; }
    public void setHorodatage(LocalDateTime horodatage) { this.horodatage = horodatage; }

    public int getStatut() { return statut; }
    public void setStatut(int statut) { this.statut = statut; }

    public String getErreur() { return erreur; }
    public void setErreur(String erreur) { this.erreur = erreur; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getChemin() { return chemin; }
    public void setChemin(String chemin) { this.chemin = chemin; }

    public Map<String, String> getErreursValidation() { return erreursValidation; }
    public void setErreursValidation(Map<String, String> erreursValidation) { this.erreursValidation = erreursValidation; }
}
