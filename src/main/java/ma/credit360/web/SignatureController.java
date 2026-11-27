package ma.credit360.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.credit360.entity.Dossier;
import ma.credit360.entity.enums.TypeSignataire;
import ma.credit360.service.SignatureService;
import ma.credit360.web.dto.DossierDetailDto;
import ma.credit360.web.dto.SignatureParcoursDto;
import ma.credit360.web.dto.ValiderOtpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dossiers/{reference}")
@Tag(name = "17. Signature Digitale et Mise en Place", description = "Signature électronique tripartite (Client, Conseiller, Responsable), OTP et déblocage")
public class SignatureController {

    private final SignatureService signatureService;

    public SignatureController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @GetMapping("/signatures")
    @Operation(summary = "Consulter l'état d'avancement du parcours de signature tripartite")
    public ResponseEntity<SignatureParcoursDto> obtenirParcours(@PathVariable("reference") String reference) {
        return ResponseEntity.ok(signatureService.initialiserOuObtenirParcours(reference));
    }

    @PostMapping("/signatures/otp")
    @Operation(summary = "Demander l'envoi d'un code OTP sécurisé par SMS/Email pour signature")
    public ResponseEntity<Map<String, String>> envoyerOtp(
            @PathVariable("reference") String reference,
            @RequestParam(name = "signataire", required = false) TypeSignataire signataire) {
        if (signataire == null) {
            throw new IllegalArgumentException("Le paramètre 'signataire' (CLIENT, CONSEILLER, RESPONSABLE) est obligatoire");
        }
        String otp = signatureService.envoyerOtp(reference, signataire);
        return ResponseEntity.ok(Map.of(
                "message", "Code OTP généré et transmis au " + signataire.getLibelle(),
                "codeOtpTest", otp // Fourni pour simplifier les tests Postman
        ));
    }

    @PostMapping("/signatures/valider")
    @Operation(summary = "Valider le code OTP et apposer la signature électronique certifiée")
    public ResponseEntity<SignatureParcoursDto> validerOtpEtSigner(
            @PathVariable("reference") String reference,
            @Valid @RequestBody(required = false) ValiderOtpRequest request) {
        if (request == null || request.getTypeSignataire() == null || request.getCodeOtp() == null || request.getCodeOtp().isBlank()) {
            throw new IllegalArgumentException("Le type de signataire et le code OTP sont obligatoires");
        }
        return ResponseEntity.ok(signatureService.validerOtpEtSigner(reference, request));
    }

    @PostMapping("/mise-en-place")
    @PreAuthorize("hasAnyRole('CONSEILLER', 'RESPONSABLE', 'ADMINISTRATEUR')")
    @Operation(summary = "Étape 5 : Exécuter la mise en place du crédit (déblocage des fonds)")
    public ResponseEntity<DossierDetailDto> mettreEnPlace(@PathVariable("reference") String reference) {
        Dossier misEnPlace = signatureService.mettreEnPlaceCredit(reference);
        return ResponseEntity.ok(new DossierDetailDto(misEnPlace));
    }
}
