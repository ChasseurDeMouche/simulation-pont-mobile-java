package pontmobile;

/**
 * Paramètres de la simulation, regroupés au même endroit pour pouvoir
 * les modifier facilement. Toutes les durées sont en millisecondes.
 */
public final class Config {

    /** Nombre de segments du pont (S1 à SN). */
    public static final int NB_SEGMENTS = 8;

    /** Temps passé par un véhicule normal sur un segment. */
    public static final long DUREE_SEGMENT = 600;

    /** Durée d'une manœuvre du pont (ouverture ou fermeture). */
    public static final long DUREE_MANOEUVRE = 2000;

    /** Durée de la traversée d'un bateau sous le pont. */
    public static final long DUREE_TRAVERSEE_BATEAU = 2500;

    /** Délai minimal entre une fermeture complète et la prochaine ouverture (1 minute). */
    public static final long DELAI_ENTRE_OUVERTURES = 60_000;

    /** Classe utilitaire : pas d'instance. */
    private Config() {
    }
}
