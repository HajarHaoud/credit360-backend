package ma.credit360.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.credit360.entity.enums.TypeSignataire;

public class ValiderOtpRequest {
    @NotNull(message = "Le type de signataire est obligatoire")
    private TypeSignataire typeSignataire;

    @NotBlank(message = "Le code OTP est obligatoire")
    private String codeOtp;

    public ValiderOtpRequest() {}

    public TypeSignataire getTypeSignataire() { return typeSignataire; }
    public void setTypeSignataire(TypeSignataire typeSignataire) { this.typeSignataire = typeSignataire; }

    public String getCodeOtp() { return codeOtp; }
    public void setCodeOtp(String codeOtp) { this.codeOtp = codeOtp; }
}
