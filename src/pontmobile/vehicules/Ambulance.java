package pontmobile.vehicules;

import pontmobile.modele.Pont;
import pontmobile.modele.SensVehicule;

/**
 * Ambulance : véhicule prioritaire.
 */
public class Ambulance extends VehiculePrioritaire {

    /** Crée une ambulance. */
    public Ambulance(String identifiant, Pont pont, SensVehicule sens) {
        super(identifiant, pont, sens);
    }
}
