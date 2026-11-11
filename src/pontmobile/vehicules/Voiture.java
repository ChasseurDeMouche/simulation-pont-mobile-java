package pontmobile.vehicules;

import pontmobile.modele.Pont;
import pontmobile.modele.SensVehicule;

/**
 * Voiture : occupe un seul segment à la fois.
 */
public class Voiture extends Vehicule {

    /** Crée une voiture. */
    public Voiture(String identifiant, Pont pont, SensVehicule sens) {
        super(identifiant, pont, sens);
    }

    @Override
    public int getLongueur() {
        return 1;
    }
}
