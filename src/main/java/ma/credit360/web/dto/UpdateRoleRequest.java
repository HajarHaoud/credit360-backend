package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import ma.credit360.entity.enums.Role;

public class UpdateRoleRequest {
    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    public UpdateRoleRequest() {}
    public UpdateRoleRequest(Role role) { this.role = role; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
