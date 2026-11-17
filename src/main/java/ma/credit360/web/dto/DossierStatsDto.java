package ma.credit360.web.dto;

public class DossierStatsDto {
    private long total;
    private long aCompleter;
    private long enAnalyse;
    private long enCours;
    private long complementRequis;
    private long valides;
    private long refuses;
    private long annules;

    public DossierStatsDto() {}

    public DossierStatsDto(long total, long aCompleter, long enAnalyse, long enCours, long complementRequis, long valides, long refuses, long annules) {
        this.total = total;
        this.aCompleter = aCompleter;
        this.enAnalyse = enAnalyse;
        this.enCours = enCours;
        this.complementRequis = complementRequis;
        this.valides = valides;
        this.refuses = refuses;
        this.annules = annules;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public long getACompleter() { return aCompleter; }
    public void setACompleter(long aCompleter) { this.aCompleter = aCompleter; }

    public long getEnAnalyse() { return enAnalyse; }
    public void setEnAnalyse(long enAnalyse) { this.enAnalyse = enAnalyse; }

    public long getEnCours() { return enCours; }
    public void setEnCours(long enCours) { this.enCours = enCours; }

    public long getComplementRequis() { return complementRequis; }
    public void setComplementRequis(long complementRequis) { this.complementRequis = complementRequis; }

    public long getValides() { return valides; }
    public void setValides(long valides) { this.valides = valides; }

    public long getRefuses() { return refuses; }
    public void setRefuses(long refuses) { this.refuses = refuses; }

    public long getAnnules() { return annules; }
    public void setAnnules(long annules) { this.annules = annules; }
}
