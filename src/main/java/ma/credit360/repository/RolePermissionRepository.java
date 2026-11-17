package ma.credit360.repository;

import ma.credit360.entity.RolePermission;
import ma.credit360.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRole(Role role);
    void deleteByRole(Role role);
}
