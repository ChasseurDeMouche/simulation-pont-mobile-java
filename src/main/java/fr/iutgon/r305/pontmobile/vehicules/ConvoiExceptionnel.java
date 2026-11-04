package fr.iutgon.r305.pontmobile.vehicules;

import fr.iutgon.r305.pontmobile.modele.Pont;
import fr.iutgon.r305.pontmobile.modele.SensVehicule;

/** Véhicule long occupant quatre segments et bloquant le sens opposé. */
public class ConvoiExceptionnel extends Vehicule {

    /** Crée un convoi exceptionnel. */
    public ConvoiExceptionnel(String id, Pont pont, SensVehicule sens) {
        super(id, pont, sens);
    }

    /** Un convoi exceptionnel occupe quatre segments. */
    @Override
    public int getLongueurEnSegments() {
        return 4;
    }

    /** Le convoi ne peut pas croiser des véhicules du sens opposé. */
    @Override
    public boolean bloqueSensOppose() {
        return true;
    }
}
