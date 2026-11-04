package fr.iutgon.r305.pontmobile.vehicules;

import fr.iutgon.r305.pontmobile.modele.Pont;
import fr.iutgon.r305.pontmobile.modele.SensVehicule;

/** Véhicule occupant un segment à la fois. */
public class Voiture extends Vehicule {

    /** Crée une voiture. */
    public Voiture(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }

    /** Une voiture occupe un segment. */
    @Override
    public int getLongueurEnSegments() {
        return 1;
    }
}
