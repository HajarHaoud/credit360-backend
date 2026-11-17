package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.entity.enums.Role;
import ma.credit360.service.ParametreService;
import ma.credit360.web.dto.ParametreDto;
import ma.credit360.web.dto.RolePermissionsDto;
import ma.credit360.web.dto.UpdateParametreRequest;
import ma.credit360.web.dto.UpdateRolePermissionsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
@Tag(name = "11. Administration des Paramètres", description = "Paramètres système, rôles et gestion fine des permissions (Admin)")
public class AdminParametreController {

    private final ParametreService parametreService;

    public AdminParametreController(ParametreService parametreService) {
        this.parametreService = parametreService;
    }

    @GetMapping("/parametres")
    @Operation(summary = "Lister les paramètres fonctionnels et techniques du système")
    public ResponseEntity<List<ParametreDto>> listerParametres() {
        return ResponseEntity.ok(parametreService.listerParametres());
    }

    @GetMapping("/parametres/{cle}")
    @Operation(summary = "Consulter la valeur d'un paramètre système")
    public ResponseEntity<ParametreDto> obtenirParametre(@PathVariable("cle") String cle) {
        return ResponseEntity.ok(parametreService.obtenirParametre(cle));
    }

    @PutMapping("/parametres/{cle}")
    @Operation(summary = "Modifier la valeur d'un paramètre système")
    public ResponseEntity<ParametreDto> modifierParametre(
            @PathVariable("cle") String cle,
            @Valid @RequestBody UpdateParametreRequest request) {
        return ResponseEntity.ok(parametreService.modifierParametre(cle, request));
    }

    @GetMapping("/roles")
    @Operation(summary = "Lister les rôles applicatifs configurés")
    public ResponseEntity<List<String>> listerRoles() {
        return ResponseEntity.ok(parametreService.listerRoles());
    }

    @GetMapping("/permissions")
    @Operation(summary = "Lister l'ensemble des permissions fonctionnelles du système")
    public ResponseEntity<List<String>> listerPermissions() {
        return ResponseEntity.ok(parametreService.listerPermissions());
    }

    @GetMapping("/roles/{role}/permissions")
    @Operation(summary = "Consulter les permissions associées à un rôle")
    public ResponseEntity<RolePermissionsDto> obtenirPermissionsRole(@PathVariable("role") Role role) {
        return ResponseEntity.ok(parametreService.obtenirPermissionsRole(role));
    }

    @PutMapping("/roles/{role}/permissions")
    @Operation(summary = "Mettre à jour les permissions associées à un rôle")
    public ResponseEntity<RolePermissionsDto> modifierPermissionsRole(
            @PathVariable("role") Role role,
            @Valid @RequestBody UpdateRolePermissionsRequest request) {
        return ResponseEntity.ok(parametreService.modifierPermissionsRole(role, request));
    }
}
