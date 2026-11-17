package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.UtilisateurService;
import ma.credit360.web.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/utilisateurs")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
@Tag(name = "1.1 Administration Utilisateurs", description = "Gestion des comptes et habilitations utilisateurs (Admin)")
public class AdminUtilisateurController {

    private final UtilisateurService utilisateurService;

    public AdminUtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    @Operation(summary = "Lister les utilisateurs")
    public ResponseEntity<List<UserDto>> lister() {
        return ResponseEntity.ok(utilisateurService.listerTous());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un utilisateur par ID")
    public ResponseEntity<UserDto> obtenir(@PathVariable("id") Long id) {
        return ResponseEntity.ok(utilisateurService.obtenirParId(id));
    }

    @PostMapping
    @Operation(summary = "Créer un utilisateur")
    public ResponseEntity<UserDto> creer(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurService.creer(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier les informations d'un utilisateur")
    public ResponseEntity<UserDto> modifier(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(utilisateurService.modifier(id, request));
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Activer ou désactiver un compte")
    public ResponseEntity<UserDto> modifierStatut(@PathVariable("id") Long id, @RequestBody UpdateStatutRequest request) {
        return ResponseEntity.ok(utilisateurService.modifierStatut(id, request.isActif()));
    }

    @PatchMapping("/{id}/role")
    @Operation(summary = "Modifier le rôle d'un utilisateur")
    public ResponseEntity<UserDto> modifierRole(@PathVariable("id") Long id, @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(utilisateurService.modifierRole(id, request.getRole()));
    }

    @PostMapping("/{id}/reinitialiser-mot-de-passe")
    @Operation(summary = "Réinitialiser administrativement le mot de passe")
    public ResponseEntity<Map<String, String>> reinitialiserMotDePasse(@PathVariable("id") Long id, @Valid @RequestBody ResetUserPasswordRequest request) {
        utilisateurService.reinitialiserMotDePasse(id, request.getMotDePasse());
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }
}
