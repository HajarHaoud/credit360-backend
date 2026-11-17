package ma.credit360.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "pieces_requises")
public class PieceRequise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @Column(nullable = false)
    private boolean obligatoire = true;

    @Column(nullable = false)
    private int ordre = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_credit_id", nullable = false)
    @JsonIgnore
    private TypeCredit typeCredit;

    public PieceRequise() {}

    public PieceRequise(String libelle, boolean obligatoire, int ordre, TypeCredit typeCredit) {
        this.libelle = libelle;
        this.obligatoire = obligatoire;
        this.ordre = ordre;
        this.typeCredit = typeCredit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public boolean isObligatoire() { return obligatoire; }
    public void setObligatoire(boolean obligatoire) { this.obligatoire = obligatoire; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }

    public TypeCredit getTypeCredit() { return typeCredit; }
    public void setTypeCredit(TypeCredit typeCredit) { this.typeCredit = typeCredit; }
}
