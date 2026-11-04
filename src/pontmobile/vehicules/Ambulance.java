package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.SensVehicule;

/** Véhicule prioritaire de type ambulance. */
public class Ambulance extends VehiculePrioritaire {

    /** Crée une ambulance. */
    public Ambulance(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }
}
