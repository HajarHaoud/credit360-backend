package ma.credit360.service;

import ma.credit360.entity.RechargeCredit;
import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.StatutRecharge;
import ma.credit360.repository.RechargeCreditRepository;
import ma.credit360.web.dto.RechargeCreditDto;
import ma.credit360.web.dto.SimulerRechargeRequest;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RechargeService {

    private final RechargeCreditRepository rechargeCreditRepository;
    private final AuthService authService;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public RechargeService(RechargeCreditRepository rechargeCreditRepository,
                           AuthService authService,
                           AuditService auditService,
                           NotificationService notificationService) {
        this.rechargeCreditRepository = rechargeCreditRepository;
        this.authService = authService;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<RechargeCreditDto> listerToutes() {
        return rechargeCreditRepository.findAllByOrderByDateDemandeDesc().stream()
                .map(RechargeCreditDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RechargeCreditDto obtenirParReference(String referenceOperation) {
        RechargeCredit recharge = rechargeCreditRepository.findByReferenceOperation(referenceOperation)
                .orElseThrow(() -> new ResourceNotFoundException("Opération de recharge introuvable: " + referenceOperation));
        return new RechargeCreditDto(recharge);
    }

    @Transactional
    public RechargeCreditDto simulerEtEnregistrer(SimulerRechargeRequest request) {
        if (request.getNouveauFinancement() <= request.getSoldeRestant()) {
            throw new BusinessException("Le nouveau financement (" + request.getNouveauFinancement()
                    + " DH) doit être supérieur au solde restant dû (" + request.getSoldeRestant() + " DH)");
        }

        double frais = request.getFraisEtAssurance() != null ? request.getFraisEtAssurance() : 10000.0;
        int duree = request.getNouvelleDuree() != null ? request.getNouvelleDuree() : 84;
        double taux = request.getNouveauTaux() != null ? request.getNouveauTaux() : 5.5;

        long seq = rechargeCreditRepository.count() + 1;
        String ref = String.format("RCH%05d", seq);

        RechargeCredit recharge = new RechargeCredit(
                ref,
                request.getClient(),
                request.getCin(),
                request.getTelephone(),
                request.getCreditExistant(),
                request.getSoldeRestant(),
                request.getNouveauFinancement(),
                frais,
                duree,
                taux
        );

        // Calcul mensualité
        double tMensuel = (taux / 100.0) / 12.0;
        double mens = (request.getNouveauFinancement() * tMensuel) / (1 - Math.pow(1 + tMensuel, -duree));
        recharge.setNouvelleMensualite(Math.round(mens * 100.0) / 100.0);

        RechargeCredit saved = rechargeCreditRepository.save(recharge);
        auditService.tracer("SIMULATION_RECHARGE", "RECHARGES", ref,
                "Simulation recharge client " + request.getClient() + " : Net à verser = " + recharge.getSoldeAVerserClient() + " DH");

        return new RechargeCreditDto(saved);
    }

    @Transactional
    public RechargeCreditDto validerOperation(String referenceOperation) {
        RechargeCredit recharge = rechargeCreditRepository.findByReferenceOperation(referenceOperation)
                .orElseThrow(() -> new ResourceNotFoundException("Opération de recharge introuvable: " + referenceOperation));

        Utilisateur currentUser = authService.getCurrentUser();

        recharge.setStatut(StatutRecharge.VALIDEE);
        recharge.setValidePar(currentUser);
        recharge.setDateValidation(LocalDateTime.now());

        RechargeCredit saved = rechargeCreditRepository.save(recharge);

        auditService.tracer("VALIDATION_RECHARGE", "RECHARGES", referenceOperation,
                "Opération validée par " + currentUser.getNom() + " : Déblocage net = " + recharge.getSoldeAVerserClient() + " DH");

        notificationService.envoyerNotification(
                currentUser,
                "Recharge de crédit validée",
                "L'opération " + referenceOperation + " pour " + recharge.getClient() + " a été validée. Solde à verser : " + recharge.getSoldeAVerserClient() + " DH.",
                ma.credit360.entity.enums.TypeNotification.SYSTEME,
                referenceOperation
        );

        return new RechargeCreditDto(saved);
    }
}
