package ma.credit360.web.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePieceRequiseRequest {
    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;
    private boolean obligatoire = true;
    private int ordre = 1;

    public CreatePieceRequiseRequest() {}

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public boolean isObligatoire() { return obligatoire; }
    public void setObligatoire(boolean obligatoire) { this.obligatoire = obligatoire; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }
}
