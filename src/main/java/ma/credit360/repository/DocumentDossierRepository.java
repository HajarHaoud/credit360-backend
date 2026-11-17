package ma.credit360.repository;

import ma.credit360.entity.DocumentDossier;
import ma.credit360.entity.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentDossierRepository extends JpaRepository<DocumentDossier, Long> {
    List<DocumentDossier> findByDossierOrderByDateGenerationDesc(Dossier dossier);
    List<DocumentDossier> findByDossierReferenceOrderByDateGenerationDesc(String reference);
}
