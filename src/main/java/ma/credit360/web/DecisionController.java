package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.DecisionService;
import ma.credit360.service.HabilitationService;
import ma.credit360.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "7. Décision et Habilitations", description = "Comité de crédit, validation, refus et seuils d'octroi")
public class DecisionController {

    private final DecisionService decisionService;
    private final HabilitationService habilitationService;

    public DecisionController(DecisionService decisionService, HabilitationService habilitationService) {
        this.decisionService = decisionService;
        this.habilitationService = habilitationService;
    }

    @GetMapping("/api/dossiers/{reference}/decisions")
    @Operation(summary = "Historique des décisions prises sur le dossier")
    public ResponseEntity<List<DecisionResponseDto>> listerDecisions(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(decisionService.listerDecisions(reference));
    }

    @PostMapping("/api/dossiers/{reference}/decisions")
    @PreAuthorize("hasAnyRole('ANALYSTE', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Enregistrer une décision (Validation, Refus, Ajournement)")
    public ResponseEntity<DecisionResponseDto> enregistrerDecision(
            @PathVariable("reference") String reference,
            @Valid @RequestBody(required = false) DecisionRequest request) {
        if (request == null || request.getTypeDecision() == null) {
            throw new IllegalArgumentException("Le corps de la requête et le type de décision (VALIDATION, REFUS, AJOURNEMENT, DEMANDE_COMPLEMENT) sont obligatoires");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(decisionService.enregistrerDecision(reference, request));
    }

    @GetMapping("/api/dossiers/{reference}/decision-courante")
    @Operation(summary = "Consulter la dernière décision en vigueur sur le dossier")
    public ResponseEntity<DecisionResponseDto> obtenirDecisionCourante(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(decisionService.obtenirDecisionCourante(reference));
    }

    @GetMapping("/api/admin/habilitations")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Consulter la grille des seuils d'habilitation (Admin)")
    public ResponseEntity<List<SeuilHabilitationDto>> listerHabilitations() {
        return ResponseEntity.ok(habilitationService.listerTous());
    }

    @PostMapping("/api/admin/habilitations")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Créer un seuil d'habilitation par rôle (Admin)")
    public ResponseEntity<SeuilHabilitationDto> creerHabilitation(@Valid @RequestBody SaveSeuilHabilitationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habilitationService.creer(request));
    }

    @PutMapping("/api/admin/habilitations/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Modifier un seuil d'habilitation (Admin)")
    public ResponseEntity<SeuilHabilitationDto> modifierHabilitation(@PathVariable("id") Long id, @Valid @RequestBody SaveSeuilHabilitationRequest request) {
        return ResponseEntity.ok(habilitationService.modifier(id, request));
    }

    @DeleteMapping("/api/admin/habilitations/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Supprimer un seuil d'habilitation (Admin)")
    public ResponseEntity<Void> supprimerHabilitation(@PathVariable("id") Long id) {
        habilitationService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/dossiers/{reference}/habilitations")
    @Operation(summary = "Vérifier le niveau d'habilitation et l'éligibilité de validation pour ce dossier")
    public ResponseEntity<HabilitationCheckDto> verifierHabilitation(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(habilitationService.verifierHabilitationPourDossier(reference));
    }
}
