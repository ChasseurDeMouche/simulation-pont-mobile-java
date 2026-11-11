package pontmobile.modele;

/**
 * Les quatre états du pont mobile.
 * Transitions autorisées : FERME -> OUVERTURE -> OUVERT -> FERMETURE -> FERME.
 */
public enum EtatPont {
    /** Le pont accueille les véhicules, les bateaux ne passent pas. */
    FERME,
    /** Le pont s'ouvre : aucun véhicule ne peut entrer. */
    OUVERTURE,
    /** Le pont est ouvert : les bateaux passent un par un. */
    OUVERT,
    /** Le pont se referme : aucun véhicule ne peut entrer. */
    FERMETURE
}
