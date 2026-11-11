package pontmobile.vehicules;

import pontmobile.modele.Pont;
import pontmobile.modele.SensVehicule;

/**
 * Camion : occupe deux segments consécutifs. Il réserve ses deux segments
 * en même temps pour entrer (jamais un seul en attendant l'autre).
 */
public class Camion extends Vehicule {

    /** Crée un camion. */
    public Camion(String identifiant, Pont pont, SensVehicule sens) {
        super(identifiant, pont, sens);
    }

    @Override
    public int getLongueur() {
        return 2;
    }
}
