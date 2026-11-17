package ma.credit360.web.dto;

import ma.credit360.entity.PieceDeposee;
import java.time.LocalDateTime;

public class FichierPieceDto {
    private Long id;
    private String nom;
    private Long taille;
    private String typeContenu;
    private LocalDateTime dateDepot;

    public FichierPieceDto() {}

    public FichierPieceDto(PieceDeposee entity) {
        this.id = entity.getId();
        this.nom = entity.getNomFichier();
        this.taille = entity.getTaille();
        this.typeContenu = entity.getTypeContenu();
        this.dateDepot = entity.getDateDepot();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Long getTaille() { return taille; }
    public void setTaille(Long taille) { this.taille = taille; }

    public String getTypeContenu() { return typeContenu; }
    public void setTypeContenu(String typeContenu) { this.typeContenu = typeContenu; }

    public LocalDateTime getDateDepot() { return dateDepot; }
    public void setDateDepot(LocalDateTime dateDepot) { this.dateDepot = dateDepot; }
}
