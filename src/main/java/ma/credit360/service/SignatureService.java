package ma.credit360.service;

import ma.credit360.entity.Dossier;
import ma.credit360.entity.HistoriqueStatut;
import ma.credit360.entity.SignatureDigitale;
import ma.credit360.entity.Utilisateur;
import ma.credit360.entity.enums.StatutDossier;
import ma.credit360.entity.enums.StatutSignature;
import ma.credit360.entity.enums.TypeNotification;
import ma.credit360.entity.enums.TypeSignataire;
import ma.credit360.repository.DossierRepository;
import ma.credit360.repository.HistoriqueStatutRepository;
import ma.credit360.repository.SignatureDigitaleRepository;
import ma.credit360.web.dto.SignatureDto;
import ma.credit360.web.dto.SignatureParcoursDto;
import ma.credit360.web.dto.ValiderOtpRequest;
import ma.credit360.web.exception.BusinessException;
import ma.credit360.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SignatureService {

    private final SignatureDigitaleRepository signatureDigitaleRepository;
    private final DossierRepository dossierRepository;
    private final HistoriqueStatutRepository historiqueStatutRepository;
    private final DossierService dossierService;
    private final AuthService authService;
    private final AuditService auditService;
    private final NotificationService notificationService;

    public SignatureService(SignatureDigitaleRepository signatureDigitaleRepository,
                            DossierRepository dossierRepository,
                            HistoriqueStatutRepository historiqueStatutRepository,
                            DossierService dossierService,
                            AuthService authService,
                            AuditService auditService,
                            NotificationService notificationService) {
        this.signatureDigitaleRepository = signatureDigitaleRepository;
        this.dossierRepository = dossierRepository;
        this.historiqueStatutRepository = historiqueStatutRepository;
        this.dossierService = dossierService;
        this.authService = authService;
        this.auditService = auditService;
        this.notificationService = notificationService;
    }

    @Transactional
    public SignatureParcoursDto initialiserOuObtenirParcours(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);
        List<SignatureDigitale> signatures = signatureDigitaleRepository.findByDossier(dossier);

        if (signatures.isEmpty()) {
            // Créer les 3 signataires requis
            SignatureDigitale sigClient = new SignatureDigitale(
                    dossier,
                    TypeSignataire.CLIENT,
                    dossier.getPrenom() + " " + dossier.getNom(),
                    dossier.getCin(),
                    dossier.getTelephone()
            );

            Utilisateur conseiller = dossier.getCreePar() != null ? dossier.getCreePar() : authService.getCurrentUser();
            SignatureDigitale sigConseiller = new SignatureDigitale(
                    dossier,
                    TypeSignataire.CONSEILLER,
                    conseiller.getNom(),
                    "CP-BANQUE",
                    conseiller.getEmail()
            );

            SignatureDigitale sigResp = new SignatureDigitale(
                    dossier,
                    TypeSignataire.RESPONSABLE,
                    "Karim Tazi (Responsable Engagements)",
                    "DIR-ENG",
                    "responsable@credit360.ma"
            );

            signatures = List.of(
                    signatureDigitaleRepository.save(sigClient),
                    signatureDigitaleRepository.save(sigConseiller),
                    signatureDigitaleRepository.save(sigResp)
            );
        }

        boolean completementSigne = signatures.stream().allMatch(s -> s.getStatut() == StatutSignature.SIGNE);

        List<SignatureDto> dtos = signatures.stream().map(SignatureDto::new).collect(Collectors.toList());
        return new SignatureParcoursDto(dossier.getReference(), dossier.getStatut().name(), completementSigne, dtos);
    }

    @Transactional
    public String envoyerOtp(String reference, TypeSignataire typeSignataire) {
        Dossier dossier = dossierService.trouverParReference(reference);
        SignatureDigitale sig = signatureDigitaleRepository.findByDossierAndTypeSignataire(dossier, typeSignataire)
                .orElseThrow(() -> new ResourceNotFoundException("Signataire " + typeSignataire + " introuvable pour ce dossier"));

        String otp = String.format("%06d", new Random().nextInt(900000) + 100000);
        sig.setCodeOtp(otp);
        sig.setDateEnvoiOtp(LocalDateTime.now());
        signatureDigitaleRepository.save(sig);

        auditService.tracer("ENVOI_OTP_SIGNATURE", "SIGNATURE", reference,
                "Code OTP envoyé au signataire " + typeSignataire.getLibelle() + " (" + sig.getNomSignataire() + ")");

        return otp; // En environnement réel envoyé par SMS, ici retourné pour test API fluide
    }

    @Transactional
    public SignatureParcoursDto validerOtpEtSigner(String reference, ValiderOtpRequest request) {
        Dossier dossier = dossierService.trouverParReference(reference);
        SignatureDigitale sig = signatureDigitaleRepository.findByDossierAndTypeSignataire(dossier, request.getTypeSignataire())
                .orElseThrow(() -> new ResourceNotFoundException("Signataire " + request.getTypeSignataire() + " introuvable"));

        if (sig.getStatut() == StatutSignature.SIGNE) {
            throw new BusinessException("Ce signataire a déjà signé le dossier.");
        }

        // Si aucun code OTP n'a été demandé, ou si le code fourni est "123456" (code de bypass démo) ou le code exact
        if (sig.getCodeOtp() != null && !sig.getCodeOtp().equals(request.getCodeOtp()) && !"123456".equals(request.getCodeOtp())) {
            throw new BusinessException("Code OTP invalide. Veuillez vérifier le code reçu.");
        }

        sig.setStatut(StatutSignature.SIGNE);
        sig.setDateSignature(LocalDateTime.now());
        sig.setAdresseIp("127.0.0.1 (Authentifié)");

        // Génération d'une empreinte cryptographique SHA-256
        String donneesSignature = reference + ":" + sig.getTypeSignataire().name() + ":" + sig.getNomSignataire() + ":" + LocalDateTime.now();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(donneesSignature.getBytes(StandardCharsets.UTF_8));
            sig.setEmpreinteSha256(HexFormat.of().formatHex(hash));
        } catch (NoSuchAlgorithmException ignored) {
            sig.setEmpreinteSha256("SHA256-" + System.currentTimeMillis());
        }

        signatureDigitaleRepository.save(sig);

        auditService.tracer("SIGNATURE_ELECTRONIQUE", "SIGNATURE", reference,
                "Signature apposée par " + sig.getTypeSignataire().getLibelle() + " (" + sig.getNomSignataire() + ")");

        // Vérifier si tous les 3 signataires ont signé
        List<SignatureDigitale> allSignatures = signatureDigitaleRepository.findByDossier(dossier);
        boolean toutSigne = allSignatures.stream().allMatch(s -> s.getStatut() == StatutSignature.SIGNE);

        if (toutSigne && dossier.getStatut() != StatutDossier.SIGNE) {
            StatutDossier anc = dossier.getStatut();
            dossier.setStatut(StatutDossier.SIGNE);
            dossierRepository.save(dossier);

            historiqueStatutRepository.save(new HistoriqueStatut(
                    dossier, anc, StatutDossier.SIGNE, "SIGNATURE_DIGITALE_COMPLETE", authService.getCurrentUser(),
                    "Contrat signé numériquement par toutes les parties (Client, Conseiller, Responsable)"
            ));

            if (dossier.getCreePar() != null) {
                notificationService.envoyerNotification(
                        dossier.getCreePar(),
                        "Dossier 100% signé",
                        "Le contrat du dossier " + reference + " a été signé par l'ensemble des parties. Prêt pour la mise en place.",
                        TypeNotification.DECISION_PRISE,
                        reference
                );
            }
        }

        List<SignatureDto> dtos = allSignatures.stream().map(SignatureDto::new).collect(Collectors.toList());
        return new SignatureParcoursDto(dossier.getReference(), dossier.getStatut().name(), toutSigne, dtos);
    }

    @Transactional
    public Dossier mettreEnPlaceCredit(String reference) {
        Dossier dossier = dossierService.trouverParReference(reference);

        if (dossier.getStatut() != StatutDossier.SIGNE && dossier.getStatut() != StatutDossier.ACCEPTE) {
            throw new BusinessException("Le dossier doit être au préalable accepté et signé pour être mis en place (statut actuel: "
                    + dossier.getStatut().getLibelle() + ")");
        }

        StatutDossier anc = dossier.getStatut();
        dossier.setStatut(StatutDossier.MIS_EN_PLACE);
        Dossier saved = dossierRepository.save(dossier);

        historiqueStatutRepository.save(new HistoriqueStatut(
                dossier, anc, StatutDossier.MIS_EN_PLACE, "MISE_EN_PLACE", authService.getCurrentUser(),
                "Fonds débloqués et virement exécuté au profit du client."
        ));

        auditService.tracer("MISE_EN_PLACE_CREDIT", "DOSSIER", reference, "Crédit mis en place, déblocage des fonds exécuté");

        return saved;
    }
}
