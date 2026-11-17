package ma.credit360.repository;

import ma.credit360.entity.ComplementDossier;
import ma.credit360.entity.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplementDossierRepository extends JpaRepository<ComplementDossier, Long> {
    List<ComplementDossier> findByDossierOrderByDateDemandeDesc(Dossier dossier);
    List<ComplementDossier> findByDossierReferenceOrderByDateDemandeDesc(String reference);
}
