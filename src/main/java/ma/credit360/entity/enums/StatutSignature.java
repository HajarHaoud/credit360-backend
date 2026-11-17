package ma.credit360.entity.enums;

public enum StatutSignature {
    EN_ATTENTE("En attente de signature"),
    SIGNE("Signé avec succès"),
    REFUSE("Signature refusée");

    private final String libelle;

    StatutSignature(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
