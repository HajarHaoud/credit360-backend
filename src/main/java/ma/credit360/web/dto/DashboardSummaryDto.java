package ma.credit360.web.dto;

public class DashboardSummaryDto {
    private long totalDossiers;
    private long aCompleter;
    private long enAnalyse;
    private long enCours;
    private long complementRequis;
    private long valides;
    private long refuses;
    private long impayes;
    private long creditsSoldes;
    private Double encoursTotal;

    public DashboardSummaryDto() {}

    public DashboardSummaryDto(long totalDossiers, long aCompleter, long enAnalyse, long enCours, long complementRequis, long valides, long refuses, long impayes, long creditsSoldes, Double encoursTotal) {
        this.totalDossiers = totalDossiers;
        this.aCompleter = aCompleter;
        this.enAnalyse = enAnalyse;
        this.enCours = enCours;
        this.complementRequis = complementRequis;
        this.valides = valides;
        this.refuses = refuses;
        this.impayes = impayes;
        this.creditsSoldes = creditsSoldes;
        this.encoursTotal = encoursTotal;
    }

    public long getImpayes() { return impayes; }
    public void setImpayes(long impayes) { this.impayes = impayes; }

    public long getCreditsSoldes() { return creditsSoldes; }
    public void setCreditsSoldes(long creditsSoldes) { this.creditsSoldes = creditsSoldes; }

    public long getTotalDossiers() { return totalDossiers; }
    public void setTotalDossiers(long totalDossiers) { this.totalDossiers = totalDossiers; }

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

    public Double getEncoursTotal() { return encoursTotal; }
    public void setEncoursTotal(Double encoursTotal) { this.encoursTotal = encoursTotal; }
}
