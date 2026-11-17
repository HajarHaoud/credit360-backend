package ma.credit360.repository;

import ma.credit360.entity.Decision;
import ma.credit360.entity.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long> {
    List<Decision> findByDossierOrderByDateDecisionDesc(Dossier dossier);
    Optional<Decision> findFirstByDossierOrderByDateDecisionDesc(Dossier dossier);
    List<Decision> findByDossierReferenceOrderByDateDecisionDesc(String reference);
    Optional<Decision> findFirstByDossierReferenceOrderByDateDecisionDesc(String reference);
}
