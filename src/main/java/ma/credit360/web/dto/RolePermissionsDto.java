package ma.credit360.web.dto;

import ma.credit360.entity.enums.Role;
import java.util.List;

public class RolePermissionsDto {
    private Role role;
    private List<String> permissions;

    public RolePermissionsDto() {}

    public RolePermissionsDto(Role role, List<String> permissions) {
        this.role = role;
        this.permissions = permissions;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}
