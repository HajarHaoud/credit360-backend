package ma.credit360.web.dto;

public class RepartitionDto {
    private String libelle;
    private long total;
    private double pourcentage;

    public RepartitionDto() {}

    public RepartitionDto(String libelle, long total, double pourcentage) {
        this.libelle = libelle;
        this.total = total;
        this.pourcentage = pourcentage;
    }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public double getPourcentage() { return pourcentage; }
    public void setPourcentage(double pourcentage) { this.pourcentage = pourcentage; }
}
