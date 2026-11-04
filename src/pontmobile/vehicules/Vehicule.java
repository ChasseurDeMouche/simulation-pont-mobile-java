package pontmobile.vehicules;

import pontmobile.Pont;
import pontmobile.modele.SensVehicule;

/** Classe de base d'un thread représentant un véhicule terrestre. */
public abstract class Vehicule extends Thread {

    private final String identifiant;
    private final Pont pont;
    private final SensVehicule sens;

    protected Vehicule(String identifiant, Pont pont, SensVehicule sens) {
        if (identifiant == null || pont == null || sens == null) {
            throw new IllegalArgumentException(
                    "L'identifiant, le pont et le sens sont obligatoires."
            );
        }

        this.identifiant = identifiant;
        this.pont = pont;
        this.sens = sens;
        setName(identifiant);
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public Pont getPont() {
        return pont;
    }

    public SensVehicule getSens() {
        return sens;
    }

    public abstract int getLongueurEnSegments();

    /** Chaque sous-classe définit sa traversée dans cette méthode. */
    @Override
    @Override
    public void run() {
        // Traversée du camion à coder
    }

    /** Indique si le véhicule bloque les entrées du sens opposé. */
    public boolean bloqueSensOppose() {
        return false;
    }
}