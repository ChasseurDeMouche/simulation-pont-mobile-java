package pontmobile;

import pontmobile.modele.*;
import pontmobile.bateau.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Pont pont = new Pont(8);
        GestionnaireBateaux gestionnaire = new GestionnaireBateaux(pont);

        System.out.println("--- Début de la simulation des bateaux ---");

        // Lancement de 3 bateaux en parallèle
        Bateau b1 = new Bateau(1, SensBateau.NORD_SUD, gestionnaire);
        Bateau b2 = new Bateau(2, SensBateau.SUD_NORD, gestionnaire);
        Bateau b3 = new Bateau(3, SensBateau.NORD_SUD, gestionnaire);

        b1.start();
        b2.start();
        b3.start();
    }
}
