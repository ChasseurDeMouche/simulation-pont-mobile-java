package pontmobile.vehicules;

import pontmobile.modele.Pont;
import pontmobile.modele.SensVehicule;

/**
 * Convoi exceptionnel : occupe quatre segments consécutifs et, à cause de
 * sa largeur, ne peut pas croiser de véhicules du sens opposé.
 */
public class ConvoiExceptionnel extends Vehicule {

    /** Crée un convoi exceptionnel. */
    public ConvoiExceptionnel(String identifiant, Pont pont, SensVehicule sens) {
        super(identifiant, pont, sens);
    }

    @Override
    public int getLongueur() {
        return 4;
    }

    /** Le convoi bloque l'entrée du sens opposé. */
    @Override
    public boolean bloqueSensOppose() {
        return true;
    }
}
