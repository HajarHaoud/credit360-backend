package ma.credit360.repository;

import ma.credit360.entity.Mainlevee;
import ma.credit360.entity.enums.StatutMainlevee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MainleveeRepository extends JpaRepository<Mainlevee, Long> {

    Optional<Mainlevee> findByReferenceMainlevee(String referenceMainlevee);

    Optional<Mainlevee> findByReferenceCredit(String referenceCredit);

    List<Mainlevee> findAllByOrderByDateSoldageDesc();

    List<Mainlevee> findByStatut(StatutMainlevee statut);

    long countByStatut(StatutMainlevee statut);
}
