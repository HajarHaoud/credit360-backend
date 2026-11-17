package ma.credit360.web.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class ReorderPiecesRequest {
    @NotEmpty(message = "La liste des identifiants est obligatoire")
    private List<Long> piecesOrdre;

    public ReorderPiecesRequest() {}
    public ReorderPiecesRequest(List<Long> piecesOrdre) { this.piecesOrdre = piecesOrdre; }

    public List<Long> getPiecesOrdre() { return piecesOrdre; }
    public void setPiecesOrdre(List<Long> piecesOrdre) { this.piecesOrdre = piecesOrdre; }
}
