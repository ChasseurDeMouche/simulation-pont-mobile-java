package fr.iutgon.r305.pontmobile.vehicules;

import fr.iutgon.r305.pontmobile.modele.Pont;
import fr.iutgon.r305.pontmobile.modele.SensVehicule;

/** Classe de base d'un thread représentant un véhicule terrestre. */
public abstract class Vehicule implements Runnable {

    private final String id;
    private final Pont pont;
    private final SensVehicule sens;

    /** Crée un véhicule avec un identifiant, un pont et un sens. */
    protected Vehicule(String id, Pont pont, SensVehicule sens) {
        if (id == null || pont == null || sens == null) {
            throw new IllegalArgumentException("L'id, le pont et le sens sont obligatoires.");
        }
        this.id = id;
        this.pont = pont;
        this.sens = sens;
    }

    /** Retourne l'identifiant du véhicule. */
    public String getId() {
        return id;
    }

    /** Retourne le pont utilisé par le véhicule. */
    public Pont getPont() {
        return pont;
    }

    /** Retourne le sens de circulation. */
    public SensVehicule getSens() {
        return sens;
    }

    /** Retourne le nombre de segments occupés par ce type de véhicule. */
    public abstract int getLongueurEnSegments();

    /** Indique si le véhicule interdit la présence du sens opposé. */
    public boolean bloqueSensOppose() {
        return false;
    }

    /** Délègue la traversée au gestionnaire commun du pont. */
    @Override
    public void run() {
        try {
            pont.traverser(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
