package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.modele.SensVehicule;

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

