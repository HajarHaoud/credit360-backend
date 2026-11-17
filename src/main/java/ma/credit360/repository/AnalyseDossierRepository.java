package ma.credit360.repository;

import ma.credit360.entity.AnalyseDossier;
import ma.credit360.entity.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalyseDossierRepository extends JpaRepository<AnalyseDossier, Long> {
    Optional<AnalyseDossier> findByDossier(Dossier dossier);
    Optional<AnalyseDossier> findByDossierReference(String reference);
}
