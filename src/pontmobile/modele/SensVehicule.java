package pontmobile.modele;

/**
 * Sens de circulation d'un véhicule sur le pont.
 * Chaque sens possède sa propre voie de N segments.
 */
public enum SensVehicule {
    GAUCHE_DROITE("G->D"),
    DROITE_GAUCHE("D->G");

    /** Libellé court pour l'affichage. */
    private final String libelle;

    SensVehicule(String libelle) {
        this.libelle = libelle;
    }

    /** @return le sens opposé */
    public SensVehicule oppose() {
        return this == GAUCHE_DROITE ? DROITE_GAUCHE : GAUCHE_DROITE;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
