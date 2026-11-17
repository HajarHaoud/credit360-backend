package ma.credit360.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "parametres_systeme")
public class ParametreSysteme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String cle;

    @Column(nullable = false, length = 500)
    private String valeur;

    private String description;

    @Column(nullable = false, length = 50)
    private String type = "STRING";

    public ParametreSysteme() {}

    public ParametreSysteme(String cle, String valeur, String description, String type) {
        this.cle = cle;
        this.valeur = valeur;
        this.description = description;
        this.type = type;
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
