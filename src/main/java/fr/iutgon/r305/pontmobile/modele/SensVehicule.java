package fr.iutgon.r305.pontmobile.modele;

/** Sens de déplacement d'un véhicule sur le pont. */
public enum SensVehicule {
    GAUCHE_VERS_DROITE(1),
    DROITE_VERS_GAUCHE(-1);

    private final int pas;

    SensVehicule(int pas) {
        this.pas = pas;
    }

    /** Retourne le premier segment rencontré en entrant sur le pont. */
    public int getPremierSegment(int nombreSegments) {
        if (this == GAUCHE_VERS_DROITE) {
            return 1;
        }
        return nombreSegments;
    }

    /** Retourne le sens de progression dans les numéros de segments. */
    public int getPas() {
        return pas;
    }
}
