package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.TypeCreditService;
import ma.credit360.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "2. Référentiel des Crédits", description = "Gestion des types de crédit et des pièces requises")
public class TypeCreditController {

    private final TypeCreditService typeCreditService;

    public TypeCreditController(TypeCreditService typeCreditService) {
        this.typeCreditService = typeCreditService;
    }

    @GetMapping("/api/types-credits")
    @Operation(summary = "Lister les types de crédit actifs")
    public ResponseEntity<List<TypeCreditDto>> listerActifs() {
        return ResponseEntity.ok(typeCreditService.listerActifs());
    }

    @GetMapping("/api/types-credits/{id}")
    @Operation(summary = "Consulter le détail d'un type de crédit")
    public ResponseEntity<TypeCreditDto> obtenirParId(@PathVariable("id") String id) {
        return ResponseEntity.ok(typeCreditService.obtenirParId(id));
    }

    @PostMapping("/api/admin/types-credits")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Créer un nouveau type de crédit (Admin)")
    public ResponseEntity<TypeCreditDto> creerTypeCredit(@Valid @RequestBody CreateTypeCreditRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(typeCreditService.creer(request));
    }

    @PutMapping("/api/admin/types-credits/{id}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Modifier un type de crédit (Admin)")
    public ResponseEntity<TypeCreditDto> modifierTypeCredit(@PathVariable("id") String id, @Valid @RequestBody UpdateTypeCreditRequest request) {
        return ResponseEntity.ok(typeCreditService.modifier(id, request));
    }

    @PatchMapping("/api/admin/types-credits/{id}/statut")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Activer ou désactiver un type de crédit (Admin)")
    public ResponseEntity<TypeCreditDto> modifierStatut(@PathVariable("id") String id, @RequestBody Map<String, Boolean> body) {
        boolean actif = body.getOrDefault("actif", true);
        return ResponseEntity.ok(typeCreditService.modifierStatut(id, actif));
    }

    @GetMapping("/api/types-credits/{typeId}/pieces-requises")
    @Operation(summary = "Lister les pièces justificatives requises pour un type de crédit")
    public ResponseEntity<List<PieceRequiseDto>> listerPiecesRequises(@PathVariable("typeId") String typeId) {
        return ResponseEntity.ok(typeCreditService.listerPiecesRequises(typeId));
    }

    @PostMapping("/api/admin/types-credits/{typeId}/pieces-requises")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Ajouter une pièce requise à un type de crédit (Admin)")
    public ResponseEntity<PieceRequiseDto> ajouterPieceRequise(@PathVariable("typeId") String typeId, @Valid @RequestBody CreatePieceRequiseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(typeCreditService.ajouterPieceRequise(typeId, request));
    }

    @PutMapping("/api/admin/pieces-requises/{pieceId}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Modifier une pièce requise (Admin)")
    public ResponseEntity<PieceRequiseDto> modifierPieceRequise(@PathVariable("pieceId") Long pieceId, @Valid @RequestBody UpdatePieceRequiseRequest request) {
        return ResponseEntity.ok(typeCreditService.modifierPieceRequise(pieceId, request));
    }

    @DeleteMapping("/api/admin/pieces-requises/{pieceId}")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Supprimer une pièce requise (Admin)")
    public ResponseEntity<Void> supprimerPieceRequise(@PathVariable("pieceId") Long pieceId) {
        typeCreditService.supprimerPieceRequise(pieceId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/admin/pieces-requises/{pieceId}/obligatoire")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Modifier le caractère obligatoire d'une pièce (Admin)")
    public ResponseEntity<PieceRequiseDto> modifierObligatoire(@PathVariable("pieceId") Long pieceId, @RequestBody Map<String, Boolean> body) {
        boolean obligatoire = body.getOrDefault("obligatoire", true);
        return ResponseEntity.ok(typeCreditService.modifierObligatoire(pieceId, obligatoire));
    }

    @PatchMapping("/api/admin/types-credits/{typeId}/pieces-requises/ordre")
    @PreAuthorize("hasRole('ADMINISTRATEUR')")
    @Operation(summary = "Réorganiser l'ordre des pièces requises (Admin)")
    public ResponseEntity<List<PieceRequiseDto>> reordonnerPieces(@PathVariable("typeId") String typeId, @Valid @RequestBody ReorderPiecesRequest request) {
        return ResponseEntity.ok(typeCreditService.reordonnerPieces(typeId, request.getPiecesOrdre()));
    }
}
