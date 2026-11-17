package ma.credit360.web.dto;

import java.util.List;

public class SignatureParcoursDto {
    private String referenceDossier;
    private String statutDossier;
    private boolean completementSigne;
    private List<SignatureDto> signataires;

    public SignatureParcoursDto() {}

    public SignatureParcoursDto(String referenceDossier, String statutDossier, boolean completementSigne, List<SignatureDto> signataires) {
        this.referenceDossier = referenceDossier;
        this.statutDossier = statutDossier;
        this.completementSigne = completementSigne;
        this.signataires = signataires;
    }

    public String getReferenceDossier() { return referenceDossier; }
    public void setReferenceDossier(String referenceDossier) { this.referenceDossier = referenceDossier; }

    public String getStatutDossier() { return statutDossier; }
    public void setStatutDossier(String statutDossier) { this.statutDossier = statutDossier; }

    public boolean isCompletementSigne() { return completementSigne; }
    public void setCompletementSigne(boolean completementSigne) { this.completementSigne = completementSigne; }

    public List<SignatureDto> getSignataires() { return signataires; }
    public void setSignataires(List<SignatureDto> signataires) { this.signataires = signataires; }
}
