package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.credit360.service.NotificationService;
import ma.credit360.web.dto.CountDto;
import ma.credit360.web.dto.NotificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "10. Notifications", description = "Gestion des alertes et notifications applicatives internes")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @Operation(summary = "Lister les notifications de l'utilisateur connecté")
    public ResponseEntity<List<NotificationDto>> lister() {
        return ResponseEntity.ok(notificationService.listerMesNotifications());
    }

    @GetMapping("/non-lues/count")
    @Operation(summary = "Obtenir le nombre de notifications non lues")
    public ResponseEntity<CountDto> compterNonLues() {
        return ResponseEntity.ok(notificationService.compterNonLues());
    }

    @PatchMapping("/{id}/lue")
    @Operation(summary = "Marquer une notification comme lue")
    public ResponseEntity<Map<String, String>> marquerCommeLue(@PathVariable("id") Long id) {
        notificationService.marquerCommeLue(id);
        return ResponseEntity.ok(Map.of("message", "Notification marquée comme lue"));
    }

    @PatchMapping("/lire-tout")
    @Operation(summary = "Marquer toutes les notifications comme lues")
    public ResponseEntity<Map<String, String>> marquerToutCommeLu() {
        notificationService.marquerToutCommeLu();
        return ResponseEntity.ok(Map.of("message", "Toutes les notifications ont été marquées comme lues"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une notification personnelle")
    public ResponseEntity<Void> supprimer(@PathVariable("id") Long id) {
        notificationService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
