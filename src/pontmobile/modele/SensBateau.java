package pontmobile.modele;

/**
 * Sens de circulation d'un bateau sous le pont.
 */
public enum SensBateau {
    NORD_SUD("Nord->Sud"),
    SUD_NORD("Sud->Nord");

    /** Libellé court pour l'affichage. */
    private final String libelle;

    SensBateau(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
