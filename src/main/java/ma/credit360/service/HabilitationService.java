package ma.credit360.service;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.SeuilHabilitation;
import ma.credit360.entity.enums.Role;
import ma.credit360.repository.SeuilHabilitationRepository;
import ma.credit360.web.dto.HabilitationCheckDto;
import ma.credit360.web.dto.SaveSeuilHabilitationRequest;
import ma.credit360.web.dto.SeuilHabilitationDto;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabilitationService {

    private final SeuilHabilitationRepository seuilHabilitationRepository;
    private final DossierService dossierService;
    private final AuthService authService;
    private final AuditService auditService;

    public HabilitationService(SeuilHabilitationRepository seuilHabilitationRepository,
                              DossierService dossierService,
                              AuthService authService,
                              AuditService auditService) {
        this.seuilHabilitationRepository = seuilHabilitationRepository;
        this.dossierService = dossierService;
        this.authService = authService;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<SeuilHabilitationDto> listerTous() {
        return seuilHabilitationRepository.findAll().stream()
                .map(SeuilHabilitationDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public SeuilHabilitationDto creer(SaveSeuilHabilitationRequest request) {
        SeuilHabilitation s = new SeuilHabilitation(
                request.getRole(),
                request.getMontantMinimum(),
                request.getMontantMaximum(),
                request.isAutoriseValidation()
        );
        s = seuilHabilitationRepository.save(s);
        auditService.tracer("CREATION_SEUIL_HABILITATION", "HABILITATION", s.getId().toString(), "Nouveau seuil pour " + s.getRole());
        return new SeuilHabilitationDto(s);
    }

    @Transactional
    public SeuilHabilitationDto modifier(Long id, SaveSeuilHabilitationRequest request) {
        SeuilHabilitation s = seuilHabilitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seuil d'habilitation introuvable: " + id));

        s.setRole(request.getRole());
        s.setMontantMinimum(request.getMontantMinimum());
        s.setMontantMaximum(request.getMontantMaximum());
        s.setAutoriseValidation(request.isAutoriseValidation());
        s = seuilHabilitationRepository.save(s);

        auditService.tracer("MODIF_SEUIL_HABILITATION", "HABILITATION", s.getId().toString(), "Modification du seuil pour " + s.getRole());
        return new SeuilHabilitationDto(s);
    }

    @Transactional
    public void supprimer(Long id) {
        SeuilHabilitation s = seuilHabilitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seuil d'habilitation introuvable: " + id));
        seuilHabilitationRepository.delete(s);
        auditService.tracer("SUPPR_SEUIL_HABILITATION", "HABILITATION", id.toString(), "Suppression du seuil");
    }

    @Transactional(readOnly = true)
    public HabilitationCheckDto verifierHabilitationPourDossier(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        Role userRole = authService.getCurrentUser().getRole();
        double montant = dossier.getMontant();

        if (userRole == Role.ADMINISTRATEUR) {
            return new HabilitationCheckDto(reference, montant, "ADMINISTRATEUR", true, "Autorisation totale administrateur");
        }

        Optional<SeuilHabilitation> seuilOpt = seuilHabilitationRepository.findFirstByRole(userRole);
        if (seuilOpt.isEmpty()) {
            return new HabilitationCheckDto(reference, montant, "RESPONSABLE", false, "Aucun seuil défini pour votre rôle (" + userRole + ")");
        }

        SeuilHabilitation seuil = seuilOpt.get();
        if (!seuil.isAutoriseValidation()) {
            return new HabilitationCheckDto(reference, montant, "RESPONSABLE", false, "Votre rôle n'est pas habilité à valider les dossiers");
        }

        boolean autorise = montant >= seuil.getMontantMinimum() && montant <= seuil.getMontantMaximum();
        String message = autorise
                ? "Dossier éligible à la validation par " + userRole + " (seuil max: " + seuil.getMontantMaximum() + " DH)"
                : "Montant (" + montant + " DH) supérieur au seuil autorisé pour " + userRole + " (" + seuil.getMontantMaximum() + " DH). Escalade requise.";

        String roleRequis = autorise ? userRole.name() : (userRole == Role.ANALYSTE ? "RESPONSABLE" : "ADMINISTRATEUR");

        return new HabilitationCheckDto(reference, montant, roleRequis, autorise, message);
    }

    public boolean estHabilite(Role role, Double montant) {
        if (role == Role.ADMINISTRATEUR) return true;
        if (role == Role.CONSEILLER) return false;

        Optional<SeuilHabilitation> seuilOpt = seuilHabilitationRepository.findFirstByRole(role);
        if (seuilOpt.isEmpty()) return false;

        SeuilHabilitation seuil = seuilOpt.get();
        return seuil.isAutoriseValidation() && montant <= seuil.getMontantMaximum();
    }
}
