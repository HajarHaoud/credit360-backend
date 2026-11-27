package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.credit360.entity.PieceDeposee;
import ma.credit360.service.PieceService;
import ma.credit360.web.dto.ChecklistDto;
import ma.credit360.web.dto.FichierPieceDto;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/dossiers/{reference}")
@Tag(name = "5. Gestion des Pièces Justificatives", description = "Checklist, dépôt, suppression et soumission du dossier")
public class PieceController {

    private final PieceService pieceService;

    public PieceController(PieceService pieceService) {
        this.pieceService = pieceService;
    }

    @GetMapping("/pieces")
    @Operation(summary = "Consulter la checklist des pièces justificatives du dossier")
    public ResponseEntity<ChecklistDto> obtenirChecklist(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(pieceService.obtenirChecklist(reference));
    }

    @PostMapping(value = "/pieces/{pieceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Déposer ou remplacer une pièce justificative (PDF, JPG, PNG <= 5Mo)")
    public ResponseEntity<FichierPieceDto> deposerPiece(
            @PathVariable("reference") String reference,
            @PathVariable("pieceId") Long pieceId,
            @RequestParam(name = "fichier", required = false) MultipartFile fichier) {
        if (fichier == null || fichier.isEmpty()) {
            throw new IllegalArgumentException("Le paramètre 'fichier' est obligatoire et ne peut pas être vide");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pieceService.deposerPiece(reference, pieceId, fichier));
    }

    @DeleteMapping("/pieces/{pieceId}")
    @Operation(summary = "Supprimer une pièce justificative déposée")
    public ResponseEntity<Void> supprimerPiece(
            @PathVariable("reference") String reference,
            @PathVariable("pieceId") Long pieceId) {
        pieceService.supprimerPiece(reference, pieceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pieces/{pieceId}/fichier")
    @Operation(summary = "Télécharger ou visualiser le fichier d'une pièce justificative")
    public ResponseEntity<Resource> telechargerFichier(
            @PathVariable("reference") String reference,
            @PathVariable("pieceId") Long pieceId) {
        Resource resource = pieceService.telechargerFichier(reference, pieceId);
        PieceDeposee pd = pieceService.obtenirPieceDeposee(reference, pieceId);

        String contentType = pd.getTypeContenu() != null ? pd.getTypeContenu() : "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pd.getNomFichier() + "\"")
                .body(resource);
    }

    @PostMapping("/soumettre")
    @Operation(summary = "Soumettre le dossier constitué à l'analyse financière")
    public ResponseEntity<Map<String, String>> soumettreDossier(@PathVariable("reference") String reference) {
        pieceService.soumettreDossier(reference);
        return ResponseEntity.ok(Map.of("message", "Dossier " + reference + " soumis avec succès à l'analyse"));
    }
}
