package pontmobile.vehicules;

import pontmobile.Config;
import pontmobile.modele.Pont;
import pontmobile.modele.SensVehicule;

/**
 * Classe de base d'un véhicule terrestre. Chaque véhicule est un thread.
 * Un véhicule normal entre sur le pont, avance segment par segment, puis sort.
 */
public abstract class Vehicule extends Thread {

    private final String identifiant;
    protected final Pont pont;
    private final SensVehicule sens;

    /**
     * Position du segment occupé par l'avant du véhicule, comptée dans son
     * sens de marche (0 = premier segment). Modifiée uniquement par le Pont.
     */
    private int positionTete = -1;

    /**
     * @param identifiant nom court affiché sur le pont (3 caractères max)
     * @param pont        le pont à traverser
     * @param sens        le sens de circulation
     */
    protected Vehicule(String identifiant, Pont pont, SensVehicule sens) {
        this.identifiant = identifiant;
        this.pont = pont;
        this.sens = sens;
        setName(identifiant);
    }

    /** @return le nombre de segments occupés par le véhicule */
    public abstract int getLongueur();

    /** @return vrai si le véhicule interdit la présence du sens opposé (convoi) */
    public boolean bloqueSensOppose() {
        return false;
    }

    /**
     * Traversée d'un véhicule normal : entrer, puis avancer d'un segment après
     * chaque temps de parcours, jusqu'à la sortie complète du pont.
     */
    @Override
    public void run() {
        try {
            pont.entrer(this);
            do {
                Thread.sleep(Config.DUREE_SEGMENT);
            } while (pont.avancer(this));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public SensVehicule getSens() {
        return sens;
    }

    public int getPositionTete() {
        return positionTete;
    }

    public void setPositionTete(int positionTete) {
        this.positionTete = positionTete;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + identifiant;
    }
}
