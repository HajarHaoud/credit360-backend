package ma.credit360.web.dto;

import java.util.List;
import java.util.Map;

public class EvolutionDto {
    private List<String> periodes;
    private Map<String, List<Long>> series;

    public EvolutionDto() {}

    public EvolutionDto(List<String> periodes, Map<String, List<Long>> series) {
        this.periodes = periodes;
        this.series = series;
    }

    public List<String> getPeriodes() { return periodes; }
    public void setPeriodes(List<String> periodes) { this.periodes = periodes; }

    public Map<String, List<Long>> getSeries() { return series; }
    public void setSeries(Map<String, List<Long>> series) { this.series = series; }
}
