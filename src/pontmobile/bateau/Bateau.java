package pontmobile.bateau;

import pontmobile.Config;
import pontmobile.Journal;
import pontmobile.modele.Pont;
import pontmobile.modele.SensBateau;

/**
 * Bateau circulant sous le pont. Chaque bateau est un thread.
 * Il s'inscrit dans la file FIFO, attend son tour et l'ouverture complète
 * du pont, traverse, puis libère le passage.
 */
public class Bateau extends Thread {

    private final String identifiant;
    private final SensBateau sens;
    private final Pont pont;

    /**
     * @param identifiant nom court du bateau
     * @param sens        direction (nord -> sud ou sud -> nord)
     * @param pont        le pont sous lequel il passe
     */
    public Bateau(String identifiant, SensBateau sens, Pont pont) {
        this.identifiant = identifiant;
        this.sens = sens;
        this.pont = pont;
        setName(identifiant);
    }

    /** Cycle de vie du bateau : arrivée, attente, traversée, sortie. */
    @Override
    public void run() {
        try {
            pont.arriveeBateau(this);
            pont.attendrePassage(this);
            Journal.log(this + " traverse sous le pont (" + sens + ")");
            Thread.sleep(Config.DUREE_TRAVERSEE_BATEAU);
            pont.finPassage(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public SensBateau getSens() {
        return sens;
    }

    @Override
    public String toString() {
        return "Bateau " + identifiant;
    }
}
