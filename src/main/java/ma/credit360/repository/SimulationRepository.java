package ma.credit360.repository;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    List<Simulation> findByDossierOrderByDateSimulationDesc(Dossier dossier);
    List<Simulation> findByDossierReferenceOrderByDateSimulationDesc(String reference);
}
