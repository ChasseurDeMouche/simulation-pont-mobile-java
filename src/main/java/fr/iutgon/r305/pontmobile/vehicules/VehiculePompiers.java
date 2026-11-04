package fr.iutgon.r305.pontmobile.vehicules;

import fr.iutgon.r305.pontmobile.modele.Pont;
import fr.iutgon.r305.pontmobile.modele.SensVehicule;

/** Véhicule prioritaire de type véhicule de pompiers. */
public class VehiculePompiers extends VehiculePrioritaire {

    /** Crée un véhicule de pompiers. */
    public VehiculePompiers(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }
}
