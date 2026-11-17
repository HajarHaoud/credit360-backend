package ma.credit360.entity.enums;

public enum StatutAssurance {
    PROPOSEE("Proposition générée"),
    SOUSCRITE("Souscrite et signée"),
    ACTIVE("Couverture active"),
    RESILIEE("Contrat résilié");

    private final String libelle;

    StatutAssurance(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
