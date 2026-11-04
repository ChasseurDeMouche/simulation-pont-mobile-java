package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.SensVehicule;

/** Véhicule prioritaire de type véhicule de pompiers. */
public class VehiculePompiers extends VehiculePrioritaires {

    /** Crée un véhicule de pompiers. */
    public VehiculePompiers(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }
}
