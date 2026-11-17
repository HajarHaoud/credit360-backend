package ma.credit360.service;

import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.Role;
import ma.credit360.repository.UtilisateurRepository;
import ma.credit360.web.dto.*;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;
    private final AuthService authService;

    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              PasswordEncoder passwordEncoder,
                              AuditService auditService,
                              AuthService authService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<UserDto> listerTous() {
        return utilisateurRepository.findAll().stream()
                .map(u -> new UserDto(u.getId(), u.getNom(), u.getFonction(), u.getEmail(), u.getRole().name(), u.isActif()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto obtenirParId(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID: " + id));
        return new UserDto(u.getId(), u.getNom(), u.getFonction(), u.getEmail(), u.getRole().name(), u.isActif());
    }

    @Transactional
    public UserDto creer(CreateUserRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Un utilisateur existe déjà avec cet email: " + request.getEmail());
        }

        Utilisateur u = new Utilisateur(
                request.getNom(),
                request.getEmail(),
                passwordEncoder.encode(request.getMotDePasse()),
                request.getRole(),
                request.getFonction()
        );

        u = utilisateurRepository.save(u);
        auditService.tracer("CREATION_UTILISATEUR", "UTILISATEUR", u.getId().toString(), "Création du compte: " + u.getEmail() + " (" + u.getRole() + ")");

        return new UserDto(u.getId(), u.getNom(), u.getFonction(), u.getEmail(), u.getRole().name(), u.isActif());
    }

    @Transactional
    public UserDto modifier(Long id, UpdateUserRequest request) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID: " + id));

        if (!u.getEmail().equalsIgnoreCase(request.getEmail()) && utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Un utilisateur existe déjà avec cet email: " + request.getEmail());
        }

        u.setNom(request.getNom());
        u.setEmail(request.getEmail());
        u.setFonction(request.getFonction());
        u = utilisateurRepository.save(u);

        auditService.tracer("MODIFICATION_UTILISATEUR", "UTILISATEUR", u.getId().toString(), "Modification des données du compte: " + u.getEmail());
        return new UserDto(u.getId(), u.getNom(), u.getFonction(), u.getEmail(), u.getRole().name(), u.isActif());
    }

    @Transactional
    public UserDto modifierStatut(Long id, boolean actif) {
        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser.getId().equals(id) && !actif) {
            throw new BusinessException("Impossible de désactiver votre propre compte administrateur.");
        }

        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID: " + id));

        u.setActif(actif);
        u = utilisateurRepository.save(u);

        auditService.tracer("MODIF_STATUT_UTILISATEUR", "UTILISATEUR", u.getId().toString(), "Statut modifié: " + (actif ? "ACTIF" : "INACTIF"));
        return new UserDto(u.getId(), u.getNom(), u.getFonction(), u.getEmail(), u.getRole().name(), u.isActif());
    }

    @Transactional
    public UserDto modifierRole(Long id, Role role) {
        Utilisateur currentUser = authService.getCurrentUser();
        if (currentUser.getId().equals(id) && role != Role.ADMINISTRATEUR) {
            throw new BusinessException("Impossible de révoquer vos propres droits administrateur.");
        }

        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID: " + id));

        u.setRole(role);
        u = utilisateurRepository.save(u);

        auditService.tracer("MODIF_ROLE_UTILISATEUR", "UTILISATEUR", u.getId().toString(), "Rôle modifié en: " + role.name());
        return new UserDto(u.getId(), u.getNom(), u.getFonction(), u.getEmail(), u.getRole().name(), u.isActif());
    }

    @Transactional
    public void reinitialiserMotDePasse(Long id, String nouveauMdp) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID: " + id));

        u.setMotDePasse(passwordEncoder.encode(nouveauMdp));
        utilisateurRepository.save(u);

        auditService.tracer("ADMIN_RESET_MDP", "UTILISATEUR", u.getId().toString(), "Réinitialisation administrative du mot de passe");
    }
}
