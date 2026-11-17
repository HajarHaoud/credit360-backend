package ma.credit360.repository;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.StatutDossier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long>, JpaSpecificationExecutor<Dossier> {

    Optional<Dossier> findByReference(String reference);

    boolean existsByReference(String reference);

    long countByStatut(StatutDossier statut);

    long countByStatutAndCreePar(StatutDossier statut, Utilisateur creePar);

    long countByCreePar(Utilisateur creePar);

    List<Dossier> findTop5ByOrderByDateMajDesc();

    List<Dossier> findTop5ByCreeParOrderByDateMajDesc(Utilisateur creePar);

    List<Dossier> findByStatutAndAnalysteAffecteIsNullOrderByDateMajDesc(StatutDossier statut);

    List<Dossier> findByAnalysteAffecteOrderByDateMajDesc(Utilisateur analyste);

    @Query("SELECT d FROM Dossier d WHERE " +
           "(:conseiller IS NULL OR d.creePar = :conseiller) AND " +
           "(:statut IS NULL OR d.statut = :statut) AND " +
           "(:typeId IS NULL OR d.typeCredit.id = :typeId) AND " +
           "(:query IS NULL OR LOWER(d.reference) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.nom) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.prenom) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.cin) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Dossier> searchDossiers(@Param("conseiller") Utilisateur conseiller,
                                 @Param("statut") StatutDossier statut,
                                 @Param("typeId") String typeId,
                                 @Param("query") String query,
                                 Pageable pageable);
}
