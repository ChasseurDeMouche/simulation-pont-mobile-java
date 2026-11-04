package fr.iutgon.r305.pontmobile.bateaux;

import fr.iutgon.r305.pontmobile.modele.DirectionBateau;
import fr.iutgon.r305.pontmobile.modele.Pont;

/** Thread représentant un bateau attendant son tour sous le pont. */
public class Bateau implements Runnable {

    private final String id;
    private final Pont pont;
    private final DirectionBateau direction;

    /** Crée un bateau identifié et associé à une direction. */
    public Bateau(String id, Pont pont, DirectionBateau direction) {
        if (id == null || pont == null || direction == null) {
            throw new IllegalArgumentException("L'id, le pont et la direction sont obligatoires.");
        }
        this.id = id;
        this.pont = pont;
        this.direction = direction;
    }

    /** Retourne l'identifiant du bateau. */
    public String getId() {
        return id;
    }

    /** Retourne le sens du bateau. */
    public DirectionBateau getDirection() {
        return direction;
    }

    /** Délègue l'attente et le passage au gestionnaire du pont. */
    @Override
    public void run() {
        try {
            pont.demanderPassage(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
