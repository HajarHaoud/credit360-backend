package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class UpdateRolePermissionsRequest {
    @NotNull(message = "La liste des permissions est obligatoire")
    private List<String> permissions;

    public UpdateRolePermissionsRequest() {}
    public UpdateRolePermissionsRequest(List<String> permissions) { this.permissions = permissions; }

    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}
