package ma.credit360.repository;

import ma.credit360.entity.AssuranceCredit;
import ma.credit360.entity.enums.StatutAssurance;
import ma.credit360.entity.enums.TypeAssurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssuranceCreditRepository extends JpaRepository<AssuranceCredit, Long> {

    Optional<AssuranceCredit> findByReferenceContrat(String referenceContrat);

    List<AssuranceCredit> findByReferenceDossier(String referenceDossier);

    List<AssuranceCredit> findAllByOrderByReferenceContratDesc();

    List<AssuranceCredit> findByStatut(StatutAssurance statut);

    List<AssuranceCredit> findByTypeAssurance(TypeAssurance typeAssurance);
}
