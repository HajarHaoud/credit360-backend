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
        Utilisateur user = getCurrentUser();

        if (!passwordEncoder.matches(request.getAncienMotDePasse(), user.getMotDePasse())) {
            throw new BusinessException("L'ancien mot de passe est incorrect");
        }

        if (!request.getNouveauMotDePasse().equals(request.getConfirmationMotDePasse())) {
            throw new BusinessException("Le nouveau mot de passe et sa confirmation ne correspondent pas");
        }

        user.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateurRepository.save(user);

        auditService.tracerUtilisateur(user, "CHANGEMENT_MDP", "AUTH", user.getEmail(), "Mot de passe modifié avec succès");
    }

    @Transactional
    public void forgotPassword(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email).orElse(null);
        if (user != null) {
            auditService.tracerUtilisateur(user, "DEMANDE_RESET_MDP", "AUTH", email, "Demande de réinitialisation de mot de passe");
        }
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec cet email"));

        user.setMotDePasse(passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateurRepository.save(user);

        auditService.tracerUtilisateur(user, "RESET_MDP", "AUTH", user.getEmail(), "Réinitialisation effectuée avec succès");
    }
}
