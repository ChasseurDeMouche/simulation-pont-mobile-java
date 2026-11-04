package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.SensVehicule;

/** Base des ambulances et véhicules de pompiers prioritaires. */
public abstract class VehiculesPrioritaire extends Vehicule {

    /** Crée un véhicule prioritaire. */
    protected VehiculePrioritaire(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }

    /** Un véhicule prioritaire occupe un segment. */
    @Override
    public int getLongueurEnSegments() {
        return 1;
    }
}
