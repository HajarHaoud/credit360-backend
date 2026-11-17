package ma.credit360.web.dto;

import ma.credit360.entity.DocumentDossier;
import java.time.LocalDateTime;

public class DocumentDto {
    private Long id;
    private String nom;
    private String typeDocument;
    private String format;
    private LocalDateTime dateGeneration;

    public DocumentDto() {}

    public DocumentDto(DocumentDossier entity) {
        this.id = entity.getId();
        this.nom = entity.getNom();
        this.typeDocument = entity.getTypeDocument().name();
        this.format = entity.getFormat();
        this.dateGeneration = entity.getDateGeneration();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTypeDocument() { return typeDocument; }
    public void setTypeDocument(String typeDocument) { this.typeDocument = typeDocument; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public LocalDateTime getDateGeneration() { return dateGeneration; }
    public void setDateGeneration(LocalDateTime dateGeneration) { this.dateGeneration = dateGeneration; }
}
