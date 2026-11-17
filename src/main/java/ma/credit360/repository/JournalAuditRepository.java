package ma.credit360.repository;

import ma.credit360.entity.JournalAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalAuditRepository extends JpaRepository<JournalAudit, Long> {
    List<JournalAudit> findAllByOrderByDateActionDesc();
    List<JournalAudit> findByUtilisateurIdOrderByDateActionDesc(Long utilisateurId);
    List<JournalAudit> findByReferenceOrderByDateActionDesc(String reference);
}
