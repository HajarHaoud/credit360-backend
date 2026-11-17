package ma.credit360.web.dto;

import jakarta.validation.constraints.NotNull;
import ma.credit360.entity.enums.TypeDocument;

public class GenerateDocumentRequest {
    @NotNull(message = "Le type de document est obligatoire")
    private TypeDocument typeDocument;

    private String format = "PDF";

    public GenerateDocumentRequest() {}

    public GenerateDocumentRequest(TypeDocument typeDocument, String format) {
        this.typeDocument = typeDocument;
        this.format = format;
    }

    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}
