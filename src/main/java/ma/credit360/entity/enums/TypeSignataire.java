package ma.credit360.entity.enums;

public enum TypeSignataire {
    CLIENT("Emprunteur / Client"),
    CONSEILLER("Conseiller Commercial"),
    RESPONSABLE("Responsable Engagements");

    private final String libelle;

    TypeSignataire(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
