package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.SensVehicule;

/** Classe de base d'un thread représentant un véhicule terrestre. */
public abstract class Vehicule implements Runnable {

    private final String id;
    private final Pont pont;
    private final SensVehicule sens;
    public abstract int getLongueurEnSegments();

    /** Crée un véhicule avec un identifiant, un pont et un sens. */
    protected Vehicule(String id, Pont pont, SensVehicule sens) {
        if (id == null || pont == null || sens == null) {
            throw new IllegalArgumentException("L'id, le pont et le sens sont obligatoires.");
        }
        this.id = id;
        this.pont = pont;
        this.sens = sens;
    }


    public String getId() {
        return id;
    }


    public Pont getPont() {
        return pont;
    }


    public SensVehicule getSens() {
        return sens;
    }

    /** Indique si le véhicule interdit la présence du sens opposé. */
    public boolean bloqueSensOppose() {
        return false;
    }

}
