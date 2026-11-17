package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.SimulationService;
import ma.credit360.web.dto.SimulationParamsDto;
import ma.credit360.web.dto.SimulationRequest;
import ma.credit360.web.dto.SimulationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "3. Simulation de Crédit", description = "Calcul financier des mensualités, capacité de remboursement et scénarios")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/api/simulations/parametres")
    @Operation(summary = "Obtenir les paramètres de calcul de simulation")
    public ResponseEntity<SimulationParamsDto> obtenirParametres() {
        return ResponseEntity.ok(simulationService.obtenirParametres());
    }

    @PostMapping("/api/simulations")
    @Operation(summary = "Calculer une simulation financière")
    public ResponseEntity<SimulationResponse> calculer(@Valid @RequestBody SimulationRequest request) {
        return ResponseEntity.ok(simulationService.calculer(request));
    }

    @GetMapping("/api/simulations/{id}")
    @Operation(summary = "Consulter une simulation enregistrée par ID")
    public ResponseEntity<SimulationResponse> obtenirParId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(simulationService.obtenirParId(id));
    }

    @GetMapping("/api/dossiers/{reference}/simulations")
    @Operation(summary = "Consulter l'historique des simulations d'un dossier")
    public ResponseEntity<List<SimulationResponse>> listerSimulationsDossier(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(simulationService.listerSimulationsDossier(reference));
    }

    @PostMapping("/api/dossiers/{reference}/simulations")
    @Operation(summary = "Enregistrer une simulation attachée à un dossier de crédit")
    public ResponseEntity<SimulationResponse> enregistrerPourDossier(@PathVariable("reference") String reference, @Valid @RequestBody SimulationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(simulationService.enregistrerPourDossier(reference, request));
    }
}
