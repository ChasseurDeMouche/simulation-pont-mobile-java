package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.SensVehicule;

/** Véhicule occupant deux segments consécutifs. */
public class Camion extends Vehicule {

    /** Crée un camion. */
    public Camion(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }

    /** Un camion occupe deux segments. */
    @Override
    public int getLongueurEnSegments() {
        return 2;
    }
}
