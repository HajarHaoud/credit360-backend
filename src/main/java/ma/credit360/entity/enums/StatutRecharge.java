package ma.credit360.entity.enums;

public enum StatutRecharge {
    SIMULEE("Simulation effectuée"),
    VALIDEE("Opération validée"),
    DEBLOQUEE("Fonds débloqués"),
    ANNULEE("Opération annulée");

    private final String libelle;

    StatutRecharge(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
