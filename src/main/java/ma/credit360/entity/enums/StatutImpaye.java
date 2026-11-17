package ma.credit360.entity.enums;

public enum StatutImpaye {
    PREMIER_IMPAYE("1er impayé"),
    DEUXIEME_IMPAYE("2ème impayé"),
    TROISIEME_IMPAYE("3ème impayé"),
    REGULARISE("Régularisé"),
    CONTENTIEUX("Procédure contentieuse");

    private final String libelle;

    StatutImpaye(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
