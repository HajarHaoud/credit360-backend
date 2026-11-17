package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.DossierService;
import ma.credit360.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dossiers")
@Tag(name = "4. Gestion des Dossiers", description = "Création, recherche, modification et suivi des dossiers de crédit")
public class DossierController {

    private final DossierService dossierService;

    public DossierController(DossierService dossierService) {
        this.dossierService = dossierService;
    }

    @GetMapping
    @Operation(summary = "Liste paginée et recherche des dossiers de crédit")
    public ResponseEntity<PageDto<DossierItemDto>> rechercherDossiers(
            @Parameter(description = "Recherche libre sur référence, nom, prénom ou CIN")
            @RequestParam(name = "q", required = false) String q,
            @Parameter(description = "Filtrer par statut")
            @RequestParam(name = "statut", required = false) String statut,
            @Parameter(description = "Filtrer par identifiant du type de crédit")
            @RequestParam(name = "type", required = false) String type,
            @Parameter(description = "Numéro de page (1-based)")
            @RequestParam(name = "page", defaultValue = "1") int page,
            @Parameter(description = "Nombre d'éléments par page")
            @RequestParam(name = "taille", defaultValue = "10") int taille) {

        return ResponseEntity.ok(dossierService.rechercherDossiers(q, statut, type, page, taille));
    }

    @GetMapping("/recents")
    @Operation(summary = "Obtenir les 5 dossiers les plus récemment mis à jour")
    public ResponseEntity<List<DossierItemDto>> obtenirDossiersRecents() {
        return ResponseEntity.ok(dossierService.obtenirDossiersRecents());
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtenir les compteurs statistiques des dossiers par statut")
    public ResponseEntity<DossierStatsDto> obtenirStatistiques() {
        return ResponseEntity.ok(dossierService.obtenirStatistiques());
    }

    @GetMapping("/{reference}")
    @Operation(summary = "Consulter le détail complet d'un dossier par sa référence")
    public ResponseEntity<DossierDetailDto> obtenirParReference(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(dossierService.obtenirParReference(reference));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CONSEILLER', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Créer un nouveau dossier de crédit")
    public ResponseEntity<DossierDetailDto> creerDossier(@Valid @RequestBody CreateDossierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dossierService.creer(request));
    }

    @PutMapping("/{reference}")
    @PreAuthorize("hasAnyRole('CONSEILLER', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Modifier les informations d'un dossier de crédit")
    public ResponseEntity<DossierDetailDto> modifierDossier(@PathVariable("reference") String reference, @Valid @RequestBody UpdateDossierRequest request) {
        return ResponseEntity.ok(dossierService.modifier(reference, request));
    }

    @PatchMapping("/{reference}/annuler")
    @Operation(summary = "Annuler une demande de crédit")
    public ResponseEntity<DossierDetailDto> annulerDossier(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(dossierService.annuler(reference));
    }
}
