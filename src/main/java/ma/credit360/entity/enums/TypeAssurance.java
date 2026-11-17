package ma.credit360.entity.enums;

public enum TypeAssurance {
    DECES_INVALIDITE("Décès et Invalidité Absolue et Définitive (DIAD)"),
    PERTE_EMPLOI("Garantie Perte d'Emploi"),
    MULTIRISQUE_HABITATION("Multirisque Habitation Emprunteur");

    private final String libelle;

    TypeAssurance(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
