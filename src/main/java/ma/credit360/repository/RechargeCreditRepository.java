package ma.credit360.repository;

import ma.credit360.entity.RechargeCredit;
import ma.credit360.entity.enums.StatutRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RechargeCreditRepository extends JpaRepository<RechargeCredit, Long> {

    Optional<RechargeCredit> findByReferenceOperation(String referenceOperation);

    List<RechargeCredit> findAllByOrderByDateDemandeDesc();

    List<RechargeCredit> findByStatut(StatutRecharge statut);

    long countByStatut(StatutRecharge statut);
}
