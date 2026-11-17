package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.MainleveeService;
import ma.credit360.web.dto.DemandeMainleveeRequest;
import ma.credit360.web.dto.MainleveeDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mainlevees")
@Tag(name = "15. Mainlevées et Clôture", description = "Détection des crédits soldés et délivrance des certificats officiels de mainlevée")
public class MainleveeController {

    private final MainleveeService mainleveeService;

    public MainleveeController(MainleveeService mainleveeService) {
        this.mainleveeService = mainleveeService;
    }

    @GetMapping
    @Operation(summary = "Lister les dossiers de mainlevée et crédits soldés")
    public ResponseEntity<List<MainleveeDto>> lister() {
        return ResponseEntity.ok(mainleveeService.listerToutes());
    }

    @GetMapping("/soldes")
    @Operation(summary = "Lister uniquement les crédits récemment soldés en attente de mainlevée")
    public ResponseEntity<List<MainleveeDto>> listerSoldes() {
        return ResponseEntity.ok(mainleveeService.listerSoldes());
    }

    @GetMapping("/{referenceMainlevee}")
    @Operation(summary = "Consulter le détail d'une mainlevée par référence")
    public ResponseEntity<MainleveeDto> obtenirParReference(@PathVariable("referenceMainlevee") String referenceMainlevee) {
        return ResponseEntity.ok(mainleveeService.obtenirParReference(referenceMainlevee));
    }

    @PostMapping
    @Operation(summary = "Créer une demande de mainlevée sur crédit soldé")
    public ResponseEntity<MainleveeDto> creerDemande(@Valid @RequestBody DemandeMainleveeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mainleveeService.creerDemande(request));
    }

    @PostMapping("/{referenceMainlevee}/valider")
    @Operation(summary = "Valider la mainlevée (Habilité Engagements)")
    public ResponseEntity<MainleveeDto> valider(@PathVariable("referenceMainlevee") String referenceMainlevee) {
        return ResponseEntity.ok(mainleveeService.valider(referenceMainlevee));
    }

    @PostMapping("/{referenceMainlevee}/delivrer")
    @Operation(summary = "Confirmer la délivrance et remise de la mainlevée au client")
    public ResponseEntity<MainleveeDto> delivrer(@PathVariable("referenceMainlevee") String referenceMainlevee) {
        return ResponseEntity.ok(mainleveeService.delivrerAuClient(referenceMainlevee));
    }

    @GetMapping(value = "/{referenceMainlevee}/certificat", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Générer et télécharger le certificat officiel de mainlevée au format PDF")
    public ResponseEntity<Resource> telechargerCertificat(@PathVariable("referenceMainlevee") String referenceMainlevee) {
        byte[] pdfBytes = mainleveeService.genererCertificatPdf(referenceMainlevee);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"Certificat_Mainlevee_" + referenceMainlevee + ".pdf\"")
                .body(resource);
    }
}
