package pontmobile.vehicules;

import pontmobile.modele.Pont;
import pontmobile.modele.SensVehicule;

/**
 * Véhicule de pompiers : véhicule prioritaire.
 */
public class VehiculePompiers extends VehiculePrioritaire {

    /** Crée un véhicule de pompiers. */
    public VehiculePompiers(String identifiant, Pont pont, SensVehicule sens) {
        super(identifiant, pont, sens);
    }
}
