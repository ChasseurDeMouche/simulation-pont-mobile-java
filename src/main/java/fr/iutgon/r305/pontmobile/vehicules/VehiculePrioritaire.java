package fr.iutgon.r305.pontmobile.vehicules;

import fr.iutgon.r305.pontmobile.modele.Pont;
import fr.iutgon.r305.pontmobile.modele.SensVehicule;

/** Base des ambulances et véhicules de pompiers prioritaires. */
public abstract class VehiculePrioritaire extends Vehicule {

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
