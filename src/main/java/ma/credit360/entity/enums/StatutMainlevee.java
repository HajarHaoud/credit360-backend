package ma.credit360.entity.enums;

public enum StatutMainlevee {
    CREDIT_SOLDE("Crédit Soldé"),
    DEMANDEE("Mainlevée Demandée"),
    VALIDEE("Mainlevée Validée"),
    DELIVREE("Délivrée au Client");

    private final String libelle;

    StatutMainlevee(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
