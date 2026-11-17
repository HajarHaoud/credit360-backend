package ma.credit360.web.dto;

import java.util.List;

public class SimulationParamsDto {
    private Double seuilEndettementMax;
    private List<TypeCreditDto> typesCredits;

    public SimulationParamsDto() {}

    public SimulationParamsDto(Double seuilEndettementMax, List<TypeCreditDto> typesCredits) {
        this.seuilEndettementMax = seuilEndettementMax;
        this.typesCredits = typesCredits;
    }

    public Double getSeuilEndettementMax() { return seuilEndettementMax; }
    public void setSeuilEndettementMax(Double seuilEndettementMax) { this.seuilEndettementMax = seuilEndettementMax; }

    public List<TypeCreditDto> getTypesCredits() { return typesCredits; }
    public void setTypesCredits(List<TypeCreditDto> typesCredits) { this.typesCredits = typesCredits; }
}
