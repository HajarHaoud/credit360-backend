package ma.credit360.repository;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.PieceDeposee;
import ma.credit360.entity.PieceRequise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PieceDeposeeRepository extends JpaRepository<PieceDeposee, Long> {
    List<PieceDeposee> findByDossier(Dossier dossier);
    Optional<PieceDeposee> findByDossierAndPieceRequise(Dossier dossier, PieceRequise pieceRequise);
    void deleteByDossierAndPieceRequise(Dossier dossier, PieceRequise pieceRequise);
    long countByDossier(Dossier dossier);
}
