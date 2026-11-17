package ma.credit360.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pieces_deposees")
public class PieceDeposee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    private Dossier dossier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "piece_requise_id", nullable = false)
    private PieceRequise pieceRequise;

    @Column(nullable = false)
    private String nomFichier;

    @Column(nullable = false)
    private Long taille;

    @Column(nullable = false)
    private String typeContenu;

    @Column(nullable = false)
    private String cheminFichier;

    @Column(nullable = false)
    private LocalDateTime dateDepot;

    public PieceDeposee() {}

    public PieceDeposee(Dossier dossier, PieceRequise pieceRequise, String nomFichier, Long taille, String typeContenu, String cheminFichier) {
        this.dossier = dossier;
        this.pieceRequise = pieceRequise;
        this.nomFichier = nomFichier;
        this.taille = taille;
        this.typeContenu = typeContenu;
        this.cheminFichier = cheminFichier;
        this.dateDepot = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

    public PieceRequise getPieceRequise() { return pieceRequise; }
    public void setPieceRequise(PieceRequise pieceRequise) { this.pieceRequise = pieceRequise; }

    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }

    public Long getTaille() { return taille; }
    public void setTaille(Long taille) { this.taille = taille; }

    public String getTypeContenu() { return typeContenu; }
    public void setTypeContenu(String typeContenu) { this.typeContenu = typeContenu; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public LocalDateTime getDateDepot() { return dateDepot; }
    public void setDateDepot(LocalDateTime dateDepot) { this.dateDepot = dateDepot; }
}
