package pontmobile;

/**
 * Affichage des messages de la simulation dans la console.
 * Les méthodes sont synchronisées pour que les lignes écrites
 * par des threads différents ne se mélangent pas.
 */
public final class Journal {

    /** Instant de démarrage, pour afficher le temps écoulé. */
    private static final long DEBUT = System.currentTimeMillis();

    /** Classe utilitaire : pas d'instance. */
    private Journal() {
    }

    /**
     * Affiche un message précédé du temps écoulé depuis le début.
     * @param message texte à afficher
     */
    public static synchronized void log(String message) {
        double secondes = (System.currentTimeMillis() - DEBUT) / 1000.0;
        System.out.printf("[%5.1f s] %s%n", secondes, message);
    }

    /**
     * Affiche un texte tel quel (utilisé pour dessiner le pont).
     * @param texte texte à afficher
     */
    public static synchronized void brut(String texte) {
        System.out.print(texte);
        System.out.flush();
    }
}
