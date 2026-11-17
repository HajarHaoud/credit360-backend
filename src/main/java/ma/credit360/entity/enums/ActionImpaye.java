package ma.credit360.entity.enums;

public enum ActionImpaye {
    RELANCE("Relance"),
    MISE_EN_DEMEURE("Mise en demeure"),
    PROCEDURE("Procédure");

    private final String libelle;

    ActionImpaye(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
