package ma.credit360.entity.enums;

public enum StatutDossier {
    A_COMPLETER("À compléter"),
    EN_ANALYSE("En attente d'analyse"),
    EN_COURS("En cours d'analyse"),
    COMPLEMENT_REQUIS("Complément requis"),
    ACCEPTE("Accepté"),
    SIGNE("Signé"),
    MIS_EN_PLACE("Mis en place"),
    SOLDE("Soldé"),
    REFUSE("Refusé"),
    ANNULE("Annulé");

    private final String libelle;

    StatutDossier(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
