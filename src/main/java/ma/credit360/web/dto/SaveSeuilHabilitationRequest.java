package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import ma.credit360.entity.enums.Role;

public class SaveSeuilHabilitationRequest {
    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    @NotNull(message = "Le montant minimum est obligatoire")
    @PositiveOrZero(message = "Le montant minimum doit être positif ou nul")
    private Double montantMinimum;

    @NotNull(message = "Le montant maximum est obligatoire")
    private Double montantMaximum;

    private boolean autoriseValidation = true;

    public SaveSeuilHabilitationRequest() {}

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Double getMontantMinimum() { return montantMinimum; }
    public void setMontantMinimum(Double montantMinimum) { this.montantMinimum = montantMinimum; }

    public Double getMontantMaximum() { return montantMaximum; }
    public void setMontantMaximum(Double montantMaximum) { this.montantMaximum = montantMaximum; }

    public boolean isAutoriseValidation() { return autoriseValidation; }
    public void setAutoriseValidation(boolean autoriseValidation) { this.autoriseValidation = autoriseValidation; }
}
