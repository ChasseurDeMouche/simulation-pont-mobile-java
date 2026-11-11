package pontmobile.modele;

import java.util.TimerTask;

/**
 * Tâche planifiée avec un Timer à chaque fermeture complète du pont.
 * Elle s'exécute une minute plus tard : elle autorise à nouveau l'ouverture
 * et déclenche l'ouverture si des bateaux attendent.
 */
public class TacheAutorisationOuverture extends TimerTask {

    private final Pont pont;

    /**
     * @param pont le pont à prévenir quand le délai est écoulé
     */
    public TacheAutorisationOuverture(Pont pont) {
        this.pont = pont;
    }

    /** Appelée par le Timer quand l'heure de prochaine ouverture est atteinte. */
    @Override
    public void run() {
        pont.autoriserOuverture();
    }
}
