package ma.credit360.web.dto;

import ma.credit360.entity.ParametreSysteme;

public class ParametreDto {
    private Long id;
    private String cle;
    private String valeur;
    private String description;
    private String type;

    public ParametreDto() {}

    public ParametreDto(ParametreSysteme entity) {
        this.id = entity.getId();
        this.cle = entity.getCle();
        this.valeur = entity.getValeur();
        this.description = entity.getDescription();
        this.type = entity.getType();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCle() { return cle; }
    public void setCle(String cle) { this.cle = cle; }

    public String getValeur() { return valeur; }
    public void setValeur(String valeur) { this.valeur = valeur; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
