package ma.credit360.repository;

import ma.credit360.entity.AlerteImpaye;
import ma.credit360.entity.enums.ActionImpaye;
import ma.credit360.entity.enums.StatutImpaye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlerteImpayeRepository extends JpaRepository<AlerteImpaye, Long> {

    List<AlerteImpaye> findAllByOrderByJoursRetardDesc();

    List<AlerteImpaye> findByStatut(StatutImpaye statut);

    List<AlerteImpaye> findByAction(ActionImpaye action);

    long countByStatutNot(StatutImpaye statut);

    @Query("SELECT COALESCE(SUM(a.montantImpaye), 0.0) FROM AlerteImpaye a WHERE a.statut <> 'REGULARISE'")
    Double totalMontantImpayes();
}
