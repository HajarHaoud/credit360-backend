package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.DocumentService;
import ma.credit360.web.dto.DocumentDto;
import ma.credit360.web.dto.GenerateDocumentRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dossiers/{reference}/documents")
@Tag(name = "8. Documents et Génération PDF", description = "Génération dynamique de fiches, offres, contrats et attestations au format PDF")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    @Operation(summary = "Lister les documents générés pour le dossier")
    public ResponseEntity<List<DocumentDto>> listerDocuments(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(documentService.listerDocuments(reference));
    }

    @PostMapping("/generer")
    @Operation(summary = "Générer un document officiel (PDF) pour le dossier")
    public ResponseEntity<DocumentDto> genererDocument(
            @PathVariable("reference") String reference,
            @Valid @RequestBody GenerateDocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.genererDocument(reference, request));
    }

    @GetMapping("/{documentId}")
    @Operation(summary = "Consulter les métadonnées d'un document généré")
    public ResponseEntity<DocumentDto> obtenirDocument(
            @PathVariable("reference") String reference,
            @PathVariable("documentId") Long documentId) {
        return ResponseEntity.ok(documentService.obtenirDocument(reference, documentId));
    }

    @GetMapping("/{documentId}/fichier")
    @Operation(summary = "Télécharger ou visualiser le fichier PDF généré")
    public ResponseEntity<Resource> telechargerDocument(
            @PathVariable("reference") String reference,
            @PathVariable("documentId") Long documentId) {
        Resource resource = documentService.telechargerDocument(reference, documentId);
        DocumentDto doc = documentService.obtenirDocument(reference, documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getNom() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{documentId}")
    @Operation(summary = "Supprimer un document généré")
    public ResponseEntity<Void> supprimerDocument(
            @PathVariable("reference") String reference,
            @PathVariable("documentId") Long documentId) {
        documentService.supprimerDocument(reference, documentId);
        return ResponseEntity.noContent().build();
    }
}
