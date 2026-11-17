package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.WorkflowService;
import ma.credit360.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "6. Workflow d'Analyse", description = "Prise en charge, analyse financière, demandes de complément et historique")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/api/analyses/a-traiter")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Lister les dossiers en attente d'affectation pour analyse")
    public ResponseEntity<List<DossierItemDto>> listerDossiersATraiter() {
        return ResponseEntity.ok(workflowService.listerDossiersATraiter());
    }

    @GetMapping("/api/analyses/mes-dossiers")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Lister les dossiers affectés à l'analyste connecté")
    public ResponseEntity<List<DossierItemDto>> listerMesDossiers() {
        return ResponseEntity.ok(workflowService.listerMesDossiers());
    }

    @PostMapping("/api/dossiers/{reference}/prendre-en-charge")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Prendre en charge un dossier pour analyse")
    public ResponseEntity<Map<String, String>> prendreEnCharge(
            @PathVariable("reference") String reference,
            @RequestBody(required = false) PriseEnChargeRequest request) {
        workflowService.prendreEnCharge(reference, request);
        return ResponseEntity.ok(Map.of("message", "Dossier " + reference + " pris en charge"));
    }

    @PostMapping("/api/dossiers/{reference}/liberer")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Libérer l'affectation d'un dossier")
    public ResponseEntity<Map<String, String>> liberer(@PathVariable("reference") String reference) {
        workflowService.liberer(reference);
        return ResponseEntity.ok(Map.of("message", "Affectation du dossier " + reference + " libérée"));
    }

    @GetMapping("/api/dossiers/{reference}/analyse")
    @Operation(summary = "Consulter les données de l'analyse financière du dossier")
    public ResponseEntity<AnalyseResponseDto> obtenirAnalyse(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(workflowService.obtenirOuInitialiserAnalyse(reference));
    }

    @PostMapping("/api/dossiers/{reference}/analyse")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Enregistrer les données de l'analyse financière")
    public ResponseEntity<AnalyseResponseDto> enregistrerAnalyse(
            @PathVariable("reference") String reference,
            @RequestBody AnalyseRequest request) {
        return ResponseEntity.ok(workflowService.enregistrerAnalyse(reference, request));
    }

    @PutMapping("/api/dossiers/{reference}/analyse")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Mettre à jour l'analyse financière")
    public ResponseEntity<AnalyseResponseDto> mettreAJourAnalyse(
            @PathVariable("reference") String reference,
            @RequestBody AnalyseRequest request) {
        return ResponseEntity.ok(workflowService.enregistrerAnalyse(reference, request));
    }

    @PostMapping("/api/dossiers/{reference}/analyse/terminer")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Marquer l'analyse comme terminée")
    public ResponseEntity<AnalyseResponseDto> terminerAnalyse(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(workflowService.terminerAnalyse(reference));
    }

    @PostMapping("/api/dossiers/{reference}/complements")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Demander des pièces ou informations complémentaires")
    public ResponseEntity<ComplementDto> demanderComplement(
            @PathVariable("reference") String reference,
            @Valid @RequestBody DemandeComplementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workflowService.demanderComplement(reference, request));
    }

    @GetMapping("/api/dossiers/{reference}/complements")
    @Operation(summary = "Consulter la liste des demandes de complément du dossier")
    public ResponseEntity<List<ComplementDto>> listerComplements(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(workflowService.listerComplements(reference));
    }

    @PostMapping("/api/dossiers/{reference}/complements/{complementId}/repondre")
    @Operation(summary = "Répondre à une demande de complément")
    public ResponseEntity<ComplementDto> repondreComplement(
            @PathVariable("reference") String reference,
            @PathVariable("complementId") Long complementId,
            @RequestBody(required = false) ReponseComplementRequest request) {
        return ResponseEntity.ok(workflowService.repondreComplement(reference, complementId, request));
    }

    @PostMapping("/api/dossiers/{reference}/complements/{complementId}/cloturer")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Clôturer une demande de complément vérifiée")
    public ResponseEntity<ComplementDto> cloturerComplement(
            @PathVariable("reference") String reference,
            @PathVariable("complementId") Long complementId) {
        return ResponseEntity.ok(workflowService.cloturerComplement(reference, complementId));
    }

    @GetMapping("/api/dossiers/{reference}/historique")
    @Operation(summary = "Consulter l'historique complet des événements du dossier")
    public ResponseEntity<List<HistoriqueDto>> listerHistorique(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(workflowService.listerHistorique(reference));
    }

    @GetMapping("/api/dossiers/{reference}/historique/{id}")
    @Operation(summary = "Consulter un événement précis de l'historique")
    public ResponseEntity<HistoriqueDto> obtenirHistoriqueItem(
            @PathVariable("reference") String reference,
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(workflowService.obtenirHistoriqueItem(reference, id));
    }
}
