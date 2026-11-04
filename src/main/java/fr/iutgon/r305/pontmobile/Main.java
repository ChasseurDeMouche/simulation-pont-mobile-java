package fr.iutgon.r305.pontmobile;

import fr.iutgon.r305.pontmobile.modele.Pont;

/** Point d'entrée provisoire du projet. */
public class Main {

    /** Lance l'affichage de l'état initial du socle. */
    public static void main(String[] args) {
        Pont pont = new Pont(8);
        System.out.println("Simulation du pont mobile - socle commun");
        System.out.println("Nombre de segments : " + pont.getNombreSegments());
        System.out.println("État initial : " + pont.getEtat());
        System.out.println("La simulation des threads reste à implémenter.");
    }
}
