package ma.credit360.web.dto;

public class PieceChecklistItemDto {
    private Long id;
    private String libelle;
    private boolean obligatoire;
    private int ordre;
    private FichierPieceDto fichier;

    public PieceChecklistItemDto() {}

    public PieceChecklistItemDto(Long id, String libelle, boolean obligatoire, int ordre, FichierPieceDto fichier) {
        this.id = id;
        this.libelle = libelle;
        this.obligatoire = obligatoire;
        this.ordre = ordre;
        this.fichier = fichier;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public boolean isObligatoire() { return obligatoire; }
    public void setObligatoire(boolean obligatoire) { this.obligatoire = obligatoire; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }

    public FichierPieceDto getFichier() { return fichier; }
    public void setFichier(FichierPieceDto fichier) { this.fichier = fichier; }
}
