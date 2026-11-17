package ma.credit360.web.dto;

import ma.credit360.entity.PieceRequise;

public class PieceRequiseDto {
    private Long id;
    private String libelle;
    private boolean obligatoire;
    private int ordre;

    public PieceRequiseDto() {}

    public PieceRequiseDto(PieceRequise entity) {
        this.id = entity.getId();
        this.libelle = entity.getLibelle();
        this.obligatoire = entity.isObligatoire();
        this.ordre = entity.getOrdre();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public boolean isObligatoire() { return obligatoire; }
    public void setObligatoire(boolean obligatoire) { this.obligatoire = obligatoire; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }
}
