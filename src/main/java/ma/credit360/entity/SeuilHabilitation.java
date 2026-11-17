package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.Role;

@Entity
@Table(name = "seuils_habilitations")
public class SeuilHabilitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Double montantMinimum;

    @Column(nullable = false)
    private Double montantMaximum;

    @Column(nullable = false)
    private boolean autoriseValidation = true;

    public SeuilHabilitation() {}

    public SeuilHabilitation(Role role, Double montantMinimum, Double montantMaximum, boolean autoriseValidation) {
        this.role = role;
        this.montantMinimum = montantMinimum;
        this.montantMaximum = montantMaximum;
        this.autoriseValidation = autoriseValidation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Double getMontantMinimum() { return montantMinimum; }
    public void setMontantMinimum(Double montantMinimum) { this.montantMinimum = montantMinimum; }

    public Double getMontantMaximum() { return montantMaximum; }
    public void setMontantMaximum(Double montantMaximum) { this.montantMaximum = montantMaximum; }

    public boolean isAutoriseValidation() { return autoriseValidation; }
    public void setAutoriseValidation(boolean autoriseValidation) { this.autoriseValidation = autoriseValidation; }
}
