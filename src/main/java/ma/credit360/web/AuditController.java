package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.credit360.entity.JournalAudit;
import ma.credit360.service.AuditService;
import ma.credit360.web.dto.JournalAuditDto;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/audit")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
@Tag(name = "12. Audit et Traçabilité", description = "Journalisation des opérations sensibles, connexions et intégrité")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @Operation(summary = "Consulter le journal global des pistes d'audit")
    public ResponseEntity<List<JournalAuditDto>> listerTous() {
        return ResponseEntity.ok(auditService.listerTous().stream()
                .map(JournalAuditDto::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter le détail d'un événement d'audit")
    public ResponseEntity<JournalAuditDto> obtenirParId(@PathVariable("id") Long id) {
        JournalAudit audit = auditService.trouverParId(id);
        if (audit == null) {
            throw new ResourceNotFoundException("Événement d'audit introuvable: " + id);
        }
        return ResponseEntity.ok(new JournalAuditDto(audit));
    }

    @GetMapping("/utilisateur/{userId}")
    @Operation(summary = "Consulter l'historique d'audit d'un utilisateur")
    public ResponseEntity<List<JournalAuditDto>> listerParUtilisateur(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(auditService.trouverParUtilisateur(userId).stream()
                .map(JournalAuditDto::new)
                .collect(Collectors.toList()));
    }

    @GetMapping("/dossier/{reference}")
    @Operation(summary = "Consulter l'historique d'audit d'un dossier")
    public ResponseEntity<List<JournalAuditDto>> listerParDossier(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(auditService.trouverParReference(reference).stream()
                .map(JournalAuditDto::new)
                .collect(Collectors.toList()));
    }
}
