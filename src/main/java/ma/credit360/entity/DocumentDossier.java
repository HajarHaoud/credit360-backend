package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.TypeDocument;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents_dossiers")
public class DocumentDossier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dossier_id", nullable = false)
    private Dossier dossier;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument typeDocument;

    @Column(nullable = false)
    private String format = "PDF";

    @Column(nullable = false)
    private LocalDateTime dateGeneration;

    @Column(nullable = false)
    private String cheminFichier;

    public DocumentDossier() {}

    public DocumentDossier(Dossier dossier, String nom, TypeDocument typeDocument, String format, String cheminFichier) {
        this.dossier = dossier;
        this.nom = nom;
        this.typeDocument = typeDocument;
        this.format = format;
        this.cheminFichier = cheminFichier;
        this.dateGeneration = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dossier getDossier() { return dossier; }
    public void setDossier(Dossier dossier) { this.dossier = dossier; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public LocalDateTime getDateGeneration() { return dateGeneration; }
    public void setDateGeneration(LocalDateTime dateGeneration) { this.dateGeneration = dateGeneration; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }
}
