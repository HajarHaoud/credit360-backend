package ma.credit360.repository;

import ma.credit360.entity.PieceRequise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceRequiseRepository extends JpaRepository<PieceRequise, Long> {
    List<PieceRequise> findByTypeCreditIdOrderByOrdreAsc(String typeCreditId);
    List<PieceRequise> findByTypeCreditIdAndObligatoireTrue(String typeCreditId);
}
