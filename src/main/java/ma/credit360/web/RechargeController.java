package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.RechargeService;
import ma.credit360.web.dto.RechargeCreditDto;
import ma.credit360.web.dto.SimulerRechargeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recharges")
@Tag(name = "14. Recharges et Nouveau Financement", description = "Rachat d'anciens crédits, calcul du net à verser et validation de recharge")
public class RechargeController {

    private final RechargeService rechargeService;

    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @GetMapping
    @Operation(summary = "Lister les opérations de recharge et nouveau financement")
    public ResponseEntity<List<RechargeCreditDto>> lister() {
        return ResponseEntity.ok(rechargeService.listerToutes());
    }

    @GetMapping("/{referenceOperation}")
    @Operation(summary = "Consulter le détail d'une opération de recharge")
    public ResponseEntity<RechargeCreditDto> obtenirParReference(@PathVariable("referenceOperation") String referenceOperation) {
        return ResponseEntity.ok(rechargeService.obtenirParReference(referenceOperation));
    }

    @PostMapping("/simuler")
    @Operation(summary = "Simuler et enregistrer une demande de recharge de crédit")
    public ResponseEntity<RechargeCreditDto> simuler(@Valid @RequestBody SimulerRechargeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rechargeService.simulerEtEnregistrer(request));
    }

    @PostMapping("/{referenceOperation}/valider")
    @PreAuthorize("hasAnyRole('RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Valider définitivement l'opération de recharge (déblocage du net client)")
    public ResponseEntity<RechargeCreditDto> valider(@PathVariable("referenceOperation") String referenceOperation) {
        return ResponseEntity.ok(rechargeService.validerOperation(referenceOperation));
    }
}
