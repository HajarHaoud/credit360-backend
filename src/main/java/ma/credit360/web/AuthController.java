package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.service.AuthService;
import ma.credit360.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Authentification", description = "Endpoints d'authentification et gestion de session")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Vérifie les identifiants et délivre un token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Profil de l'utilisateur connecté", description = "Récupère les informations de l'utilisateur authentifié")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(authService.getMe());
    }

    @PostMapping("/logout")
    @Operation(summary = "Déconnexion", description = "Invalide la session côté client")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Changer le mot de passe", description = "Permet à l'utilisateur connecté de modifier son mot de passe")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Demande de réinitialisation", description = "Initie une demande de réinitialisation de mot de passe")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody(required = false) Map<String, String> request) {
        if (request == null || !request.containsKey("email") || request.get("email") == null || request.get("email").isBlank()) {
            throw new IllegalArgumentException("L'adresse email est obligatoire");
        }
        String email = request.get("email");
        String resetToken = authService.forgotPassword(email);
        java.util.Map<String, String> resp = new java.util.HashMap<>();
        resp.put("message", "Si l'adresse existe, un email a été envoyé pour réinitialiser le mot de passe");
        if (resetToken != null) {
            resp.put("tokenTest", resetToken);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Réinitialiser le mot de passe", description = "Réinitialise le mot de passe avec le token de validation")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }
}
