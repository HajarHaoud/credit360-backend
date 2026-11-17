package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.AssuranceService;
import ma.credit360.web.dto.AssuranceCreditDto;
import ma.credit360.web.dto.SouscrireAssuranceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assurances")
@Tag(name = "16. Assurances Emprunteur", description = "Proposition automatique selon le crédit, souscription digitale et suivi des garanties")
public class AssuranceController {

    private final AssuranceService assuranceService;

    public AssuranceController(AssuranceService assuranceService) {
        this.assuranceService = assuranceService;
    }

    @GetMapping
    @Operation(summary = "Lister tous les contrats et propositions d'assurance")
    public ResponseEntity<List<AssuranceCreditDto>> lister() {
        return ResponseEntity.ok(assuranceService.listerToutes());
    }

    @GetMapping("/dossier/{referenceDossier}")
    @Operation(summary = "Lister les assurances associées à un dossier de crédit")
    public ResponseEntity<List<AssuranceCreditDto>> listerParDossier(@PathVariable("referenceDossier") String referenceDossier) {
        return ResponseEntity.ok(assuranceService.listerParDossier(referenceDossier));
    }

    @PostMapping("/dossier/{referenceDossier}/proposer")
    @Operation(summary = "Générer la proposition d'assurance automatique selon le type de crédit")
    public ResponseEntity<List<AssuranceCreditDto>> proposerPourDossier(@PathVariable("referenceDossier") String referenceDossier) {
        return ResponseEntity.ok(assuranceService.proposerPourDossier(referenceDossier));
    }

    @PostMapping("/souscrire")
    @Operation(summary = "Souscrire une assurance pour un crédit")
    public ResponseEntity<AssuranceCreditDto> souscrire(@Valid @RequestBody SouscrireAssuranceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assuranceService.souscrire(request));
    }

    @PostMapping("/{referenceContrat}/activer")
    @Operation(summary = "Activer la couverture du contrat d'assurance")
    public ResponseEntity<AssuranceCreditDto> activer(@PathVariable("referenceContrat") String referenceContrat) {
        return ResponseEntity.ok(assuranceService.activer(referenceContrat));
    }
}
