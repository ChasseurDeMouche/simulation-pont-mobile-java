package fr.iutgon.r305.pontmobile.vehicules;

import fr.iutgon.r305.pontmobile.modele.Pont;
import fr.iutgon.r305.pontmobile.modele.SensVehicule;

/** Véhicule prioritaire de type ambulance. */
public class Ambulance extends VehiculePrioritaire {

    /** Crée une ambulance. */
    public Ambulance(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }
}
