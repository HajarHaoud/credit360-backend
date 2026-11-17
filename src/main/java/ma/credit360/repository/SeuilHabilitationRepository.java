package ma.credit360.repository;

import ma.credit360.entity.SeuilHabilitation;
import ma.credit360.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeuilHabilitationRepository extends JpaRepository<SeuilHabilitation, Long> {
    List<SeuilHabilitation> findByRole(Role role);
    Optional<SeuilHabilitation> findFirstByRole(Role role);
}
