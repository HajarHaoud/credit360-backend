package ma.credit360.repository;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.HistoriqueStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {
    List<HistoriqueStatut> findByDossierOrderByDateActionDesc(Dossier dossier);
    List<HistoriqueStatut> findByDossierReferenceOrderByDateActionDesc(String reference);
}
