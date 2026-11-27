package ma.credit360.service;

import ma.credit360.entity.Utilisateur;
import ma.credit360.repository.UtilisateurRepository;
import ma.credit360.security.JwtTokenProvider;
import ma.credit360.security.UserPrincipal;
import ma.credit360.web.dto.ChangePasswordRequest;
import ma.credit360.web.dto.LoginRequest;
import ma.credit360.web.dto.LoginResponse;
import ma.credit360.web.dto.ResetPasswordRequest;
import ma.credit360.web.dto.UserDto;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuditService auditService;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       AuditService auditService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auditService = auditService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    auditService.tracer("ECHEC_CONNEXION", "AUTH", request.getEmail(), "Email inexistant");
                    return new BadCredentialsException("Email ou mot de passe incorrect");
                });

        if (!user.isActif()) {
            auditService.tracerUtilisateur(user, "ECHEC_CONNEXION", "AUTH", user.getEmail(), "Compte désactivé");
            throw new BusinessException("Ce compte utilisateur est désactivé. Veuillez contacter un administrateur.");
        }

        if (!passwordEncoder.matches(request.getMotDePasse(), user.getMotDePasse())) {
            auditService.tracerUtilisateur(user, "ECHEC_CONNEXION", "AUTH", user.getEmail(), "Mot de passe incorrect");
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        String token = tokenProvider.generateToken(user);
        auditService.tracerUtilisateur(user, "CONNEXION", "AUTH", user.getEmail(), "Connexion réussie");

        UserDto userDto = new UserDto(
                user.getId(),
                user.getNom(),
                user.getFonction(),
                user.getEmail(),
                user.getRole().name().toLowerCase(),
                user.isActif()
        );

        return new LoginResponse(token, userDto);
    }

    public Utilisateur getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            return utilisateurRepository.findById(principal.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur connecté introuvable"));
        }
        throw new BusinessException("Aucun utilisateur connecté");
    }

    public UserDto getMe() {
        Utilisateur user = getCurrentUser();
        return new UserDto(
                user.getId(),
                user.getNom(),
                user.getFonction(),
                user.getEmail(),
                user.getRole().name().toLowerCase(),
                user.isActif()
        );
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        if (request == null || request.getAncienMotDePasse() == null || request.getNouveauMotDePasse() == null) {
            throw new IllegalArgumentException("L'ancien et le nouveau mot de passe sont obligatoires");
        }

        Utilisateur user = getCurrentUser();

        if (!passwordEncoder.matches(request.getAncienMotDePasse(), user.getMotDePasse())) {
            throw new BusinessException("L'ancien mot de passe est incorrect");
        }

        // Faille de validation : interdire de réutiliser le même mot de passe
        if (request.getNouveauMotDePasse().equals(request.getAncienMotDePasse()) ||
            passwordEncoder.matches(request.getNouveauMotDePasse(), user.getMotDePasse())) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit être différent de l'ancien mot de passe");
        }

        if (!request.getNouveauMotDePasse().equals(request.getConfirmationMotDePasse())) {
            throw new BusinessException("Le nouveau mot de passe et sa confirmation ne correspondent pas");
        }

        user.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateurRepository.save(user);

        auditService.tracerUtilisateur(user, "CHANGEMENT_MDP", "AUTH", user.getEmail(), "Mot de passe modifié avec succès");
    }

    @Transactional
    public String forgotPassword(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        Utilisateur user = utilisateurRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String token = UUID.randomUUID().toString().replace("-", "");
            user.setResetToken(token);
            user.setResetTokenExpiresAt(LocalDateTime.now().plusHours(1));
            utilisateurRepository.save(user);
            auditService.tracerUtilisateur(user, "DEMANDE_RESET_MDP", "AUTH", email, "Demande de réinitialisation de mot de passe");
            return token;
        }
        return null;
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (request == null || request.getToken() == null || request.getToken().isBlank()) {
            throw new IllegalArgumentException("Le token de réinitialisation est obligatoire");
        }

        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec cet email"));

        // Faille de sécurité : validation stricte du token de réinitialisation
        boolean tokenValide = false;

        // 1. Vérification si le token correspond au resetToken stocké et valide
        if (user.getResetToken() != null && user.getResetToken().equals(request.getToken())) {
            if (user.getResetTokenExpiresAt() != null && user.getResetTokenExpiresAt().isAfter(LocalDateTime.now())) {
                tokenValide = true;
            }
        }

        // 2. Ou si le token est un JWT valide émis pour cet utilisateur
        if (!tokenValide && tokenProvider.validateToken(request.getToken())) {
            String emailFromToken = tokenProvider.getEmailFromToken(request.getToken());
            if (user.getEmail().equalsIgnoreCase(emailFromToken)) {
                tokenValide = true;
            }
        }

        if (!tokenValide) {
            auditService.tracerUtilisateur(user, "ECHEC_RESET_MDP", "AUTH", user.getEmail(), "Tentative de réinitialisation avec un token invalide");
            throw new BadCredentialsException("Token de réinitialisation invalide ou expiré");
        }

        // Vérification que le nouveau mot de passe est différent
        if (passwordEncoder.matches(request.getNouveauMotDePasse(), user.getMotDePasse())) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit être différent de l'ancien mot de passe");
        }

        user.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        user.setResetToken(null);
        user.setResetTokenExpiresAt(null);
        utilisateurRepository.save(user);

        auditService.tracerUtilisateur(user, "RESET_MDP", "AUTH", user.getEmail(), "Réinitialisation effectuée avec succès");
    }
}
