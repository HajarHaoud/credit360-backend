package ma.credit360.service;

import ma.credit360.entity.ParametreSysteme;
import ma.credit360.entity.RolePermission;
import ma.credit360.entity.enums.Role;
import ma.credit360.repository.ParametreSystemeRepository;
import ma.credit360.repository.RolePermissionRepository;
import ma.credit360.web.dto.ParametreDto;
import ma.credit360.web.dto.RolePermissionsDto;
import ma.credit360.web.dto.UpdateParametreRequest;
import ma.credit360.web.dto.UpdateRolePermissionsRequest;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParametreService {

    private final ParametreSystemeRepository parametreSystemeRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final AuditService auditService;

    public ParametreService(ParametreSystemeRepository parametreSystemeRepository,
                            RolePermissionRepository rolePermissionRepository,
                            AuditService auditService) {
        this.parametreSystemeRepository = parametreSystemeRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<ParametreDto> listerParametres() {
        return parametreSystemeRepository.findAll().stream()
                .map(ParametreDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParametreDto obtenirParametre(String cle) {
        ParametreSysteme p = parametreSystemeRepository.findByCle(cle)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètre introuvable avec la clé: " + cle));
        return new ParametreDto(p);
    }

    @Transactional
    public ParametreDto modifierParametre(String cle, UpdateParametreRequest request) {
        ParametreSysteme p = parametreSystemeRepository.findByCle(cle)
                .orElseThrow(() -> new ResourceNotFoundException("Paramètre introuvable avec la clé: " + cle));

        p.setValeur(request.getValeur());
        if (request.getDescription() != null) {
            p.setDescription(request.getDescription());
        }
        p = parametreSystemeRepository.save(p);

        auditService.tracer("MODIF_PARAMETRE", "PARAMETRE", cle, "Nouvelle valeur: " + request.getValeur());
        return new ParametreDto(p);
    }

    public List<String> listerRoles() {
        return Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toList());
    }

    public List<String> listerPermissions() {
        return List.of(
                "DOSSIER_CREER",
                "DOSSIER_CONSULTER_PROPRE",
                "DOSSIER_CONSULTER_TOUS",
                "DOSSIER_MODIFIER",
                "DOSSIER_ANNULER",
                "PIECE_DEPOSER",
                "PIECE_SUPPRIMER",
                "PIECE_TELECHARGER",
                "DOSSIER_SOUMETTRE",
                "WORKFLOW_PRISE_EN_CHARGE",
                "WORKFLOW_LIBERER",
                "ANALYSE_EXECUTER",
                "COMPLEMENT_DEMANDER",
                "DECISION_VALIDER",
                "DECISION_REFUSER",
                "DOCUMENTS_GENERER",
                "DASHBOARD_CONSULTER",
                "EXPORT_RAPPORTS",
                "ADMIN_UTILISATEURS",
                "ADMIN_REFERENTIEL",
                "ADMIN_HABILITATIONS",
                "ADMIN_PARAMETRES",
                "ADMIN_AUDIT"
        );
    }

    @Transactional(readOnly = true)
    public RolePermissionsDto obtenirPermissionsRole(Role role) {
        List<String> perms = rolePermissionRepository.findByRole(role).stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toList());
        return new RolePermissionsDto(role, perms);
    }

    @Transactional
    public RolePermissionsDto modifierPermissionsRole(Role role, UpdateRolePermissionsRequest request) {
        rolePermissionRepository.deleteByRole(role);
        for (String perm : request.getPermissions()) {
            RolePermission rp = new RolePermission(role, perm);
            rolePermissionRepository.save(rp);
        }

        auditService.tracer("MODIF_PERMISSIONS_ROLE", "SECURITE", role.name(), "Permissions mises à jour pour " + role.name());
        return new RolePermissionsDto(role, request.getPermissions());
    }
}
