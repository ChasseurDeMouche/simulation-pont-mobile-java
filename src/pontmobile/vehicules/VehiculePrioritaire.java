package pontmobile.vehicules;

import pontmobile.Config;
import pontmobile.modele.Pont;
import pontmobile.modele.SensVehicule;

/**
 * Véhicule prioritaire (ambulance, pompiers). Pendant son passage, les
 * véhicules de son sens s'arrêtent sur la bande d'urgence ; il roule plus vite.
 */
public abstract class VehiculePrioritaire extends Vehicule {

    /** Crée un véhicule prioritaire. */
    protected VehiculePrioritaire(String identifiant, Pont pont, SensVehicule sens) {
        super(identifiant, pont, sens);
    }

    @Override
    public int getLongueur() {
        return 1;
    }

    /**
     * Traversée prioritaire : entrée, passage rapide sur tous les segments,
     * puis sortie (qui permet aux autres véhicules de repartir).
     */
    @Override
    public void run() {
        try {
            pont.entrerPrioritaire(this);
            for (int i = 1; i < pont.getNbSegments(); i++) {
                Thread.sleep(Config.DUREE_SEGMENT / 2);
                pont.avancerPrioritaire(this);
            }
            Thread.sleep(Config.DUREE_SEGMENT / 2);
            pont.sortirPrioritaire(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
