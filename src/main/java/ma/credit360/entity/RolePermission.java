package ma.credit360.entity;

import jakarta.persistence.*;
import ma.credit360.entity.enums.Role;

@Entity
@Table(name = "roles_permissions")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String permission;

    public RolePermission() {}

    public RolePermission(Role role, String permission) {
        this.role = role;
        this.permission = permission;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
}
