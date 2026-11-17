package ma.credit360.web.dto;

import ma.credit360.entity.SeuilHabilitation;
import ma.credit360.entity.enums.Role;

public class SeuilHabilitationDto {
    private Long id;
    private Role role;
    private Double montantMinimum;
    private Double montantMaximum;
    private boolean autoriseValidation;

    public SeuilHabilitationDto() {}

    public SeuilHabilitationDto(SeuilHabilitation entity) {
        this.id = entity.getId();
        this.role = entity.getRole();
        this.montantMinimum = entity.getMontantMinimum();
        this.montantMaximum = entity.getMontantMaximum();
        this.autoriseValidation = entity.isAutoriseValidation();
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
