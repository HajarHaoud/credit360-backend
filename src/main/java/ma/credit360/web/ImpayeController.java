package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.credit360.entity.enums.ActionImpaye;
import ma.credit360.entity.enums.StatutImpaye;
import ma.credit360.service.ImpayeService;
import ma.credit360.web.dto.AlerteImpayeDto;
import ma.credit360.web.dto.CountDto;
import ma.credit360.web.dto.TraiterImpayeRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/impayes")
@Tag(name = "13. Alertes et Impayés", description = "Gestion des retards de paiement (30j, 60j, 90j), actions de relance et lettres de mise en demeure")
public class ImpayeController {

    private final ImpayeService impayeService;

    public ImpayeController(ImpayeService impayeService) {
        this.impayeService = impayeService;
    }

    @GetMapping
    @Operation(summary = "Lister les alertes d'impayés avec filtres optionnels")
    public ResponseEntity<List<AlerteImpayeDto>> lister(
            @RequestParam(name = "statut", required = false) StatutImpaye statut,
            @RequestParam(name = "action", required = false) ActionImpaye action) {
        if (statut != null) {
            return ResponseEntity.ok(impayeService.listerParStatut(statut));
        }
        if (action != null) {
            return ResponseEntity.ok(impayeService.listerParAction(action));
        }
        return ResponseEntity.ok(impayeService.listerTous());
    }

    @GetMapping("/count")
    @Operation(summary = "Nombre d'alertes impayées actives")
    public ResponseEntity<CountDto> compter() {
        return ResponseEntity.ok(impayeService.compterImpayes());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter le détail d'une alerte impayé")
    public ResponseEntity<AlerteImpayeDto> obtenirParId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(impayeService.obtenirParId(id));
    }

    @PatchMapping("/{id}/traiter")
    @Operation(summary = "Traiter une alerte d'impayé (changer statut ou action)")
    public ResponseEntity<AlerteImpayeDto> traiter(
            @PathVariable("id") Long id,
            @RequestBody TraiterImpayeRequest request) {
        return ResponseEntity.ok(impayeService.traiterImpaye(id, request));
    }

    @GetMapping(value = "/{id}/lettre-mise-en-demeure", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Générer et télécharger la lettre officielle de mise en demeure au format PDF")
    public ResponseEntity<Resource> telechargerLettreMiseEnDemeure(@PathVariable("id") Long id) {
        byte[] pdfBytes = impayeService.genererLettreMiseEnDemeure(id);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"Mise_en_demeure_Alerte_" + id + ".pdf\"")
                .body(resource);
    }
}
