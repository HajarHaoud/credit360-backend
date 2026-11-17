package ma.credit360.repository;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.SignatureDigitale;
import ma.credit360.entity.enums.TypeSignataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SignatureDigitaleRepository extends JpaRepository<SignatureDigitale, Long> {

    List<SignatureDigitale> findByDossier(Dossier dossier);

    Optional<SignatureDigitale> findByDossierAndTypeSignataire(Dossier dossier, TypeSignataire typeSignataire);

    long countByDossierAndStatut(Dossier dossier, ma.credit360.entity.enums.StatutSignature statut);
}
