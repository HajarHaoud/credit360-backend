package ma.credit360.service;

import jakarta.servlet.http.HttpServletRequest;
import ma.credit360.entity.JournalAudit;
import ma.credit360.entity.Utilisateur;
import ma.credit360.repository.JournalAuditRepository;
import ma.credit360.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    private final JournalAuditRepository journalAuditRepository;
    private final HttpServletRequest request;

    public AuditService(JournalAuditRepository journalAuditRepository, HttpServletRequest request) {
        this.journalAuditRepository = journalAuditRepository;
        this.request = request;
    }

    @Transactional
    public void tracer(String action, String entite, String reference, String details) {
        Long userId = null;
        String userName = "SYSTÈME";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            userId = principal.getId();
            userName = principal.getNom();
        }

        String ip = getClientIp();
        JournalAudit audit = new JournalAudit(userId, userName, action, entite, reference, details, ip);
        journalAuditRepository.save(audit);
    }

    @Transactional
    public void tracerUtilisateur(Utilisateur user, String action, String entite, String reference, String details) {
        String ip = getClientIp();
        JournalAudit audit = new JournalAudit(user.getId(), user.getNom(), action, entite, reference, details, ip);
        journalAuditRepository.save(audit);
    }

    @Transactional(readOnly = true)
    public List<JournalAudit> listerTous() {
        return journalAuditRepository.findAllByOrderByDateActionDesc();
    }

    @Transactional(readOnly = true)
    public JournalAudit trouverParId(Long id) {
        return journalAuditRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<JournalAudit> trouverParUtilisateur(Long userId) {
        return journalAuditRepository.findByUtilisateurIdOrderByDateActionDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<JournalAudit> trouverParReference(String reference) {
        return journalAuditRepository.findByReferenceOrderByDateActionDesc(reference);
    }

    private String getClientIp() {
        if (request == null) return "127.0.0.1";
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank()) {
            return xf.split(",")[0].trim();
        }
        return request.getRemoteAddr() != null ? request.getRemoteAddr() : "127.0.0.1";
    }
}
