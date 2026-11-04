package pontmobile.vehicules;

import fr.iutgon.r305.pontmobile.modele.Pont;
import fr.iutgon.r305.pontmobile.modele.SensVehicule;

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
