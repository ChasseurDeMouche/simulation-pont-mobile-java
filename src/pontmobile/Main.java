package pontmobile;

import java.util.ArrayList;
import java.util.List;

import pontmobile.bateau.Bateau;
import pontmobile.modele.Pont;
import pontmobile.modele.SensBateau;
import pontmobile.modele.SensVehicule;
import pontmobile.vehicules.Ambulance;
import pontmobile.vehicules.Camion;
import pontmobile.vehicules.ConvoiExceptionnel;
import pontmobile.vehicules.VehiculePompiers;
import pontmobile.vehicules.Voiture;

/**
 * Point d'entrée : lance un scénario de démonstration qui montre toutes les
 * règles du sujet (segments, camions, convoi, bateaux FIFO, délai d'une
 * minute entre deux ouvertures, véhicules prioritaires).
 */
public class Main {

    /** Tous les threads lancés, pour attendre leur fin. */
    private static final List<Thread> threads = new ArrayList<>();

    /**
     * Attend un certain temps puis démarre un thread.
     * @param attenteMs temps d'attente avant le démarrage
     * @param t         le thread (véhicule ou bateau) à démarrer
     */
    private static void lancer(long attenteMs, Thread t) throws InterruptedException {
        Thread.sleep(attenteMs);
        threads.add(t);
        t.start();
    }

    /**
     * Scénario de la simulation.
     * @param args non utilisés
     */
    public static void main(String[] args) throws InterruptedException {
        Pont pont = new Pont(Config.NB_SEGMENTS);
        SensVehicule gd = SensVehicule.GAUCHE_DROITE;
        SensVehicule dg = SensVehicule.DROITE_GAUCHE;

        Journal.log("=== Debut de la simulation du pont mobile (" + Config.NB_SEGMENTS + " segments) ===");

        // 1) Circulation dans les deux sens : voitures et camions
        lancer(0, new Voiture("V1", pont, gd));
        lancer(0, new Voiture("V2", pont, dg));
        lancer(300, new Camion("C1", pont, gd));
        lancer(700, new Voiture("V3", pont, gd));
        lancer(300, new Camion("C2", pont, dg));

        // 2) Une ambulance arrive : les véhicules G->D se rangent
        lancer(1500, new Ambulance("A1", pont, gd));

        // 3) Un convoi exceptionnel bloque l'entrée D->G
        lancer(1500, new ConvoiExceptionnel("X1", pont, gd));
        lancer(300, new Voiture("V4", pont, dg));
        lancer(300, new Voiture("V5", pont, gd));

        // 4) Deux bateaux arrivent : ouverture dès que le pont est vide
        lancer(5000, new Bateau("B1", SensBateau.NORD_SUD, pont));
        lancer(300, new Bateau("B2", SensBateau.SUD_NORD, pont));
        lancer(500, new Voiture("V6", pont, dg)); // bloquée pendant l'ouverture

        // 5) Un bateau arrive après la fermeture : il attend le Timer (1 minute)
        lancer(12000, new Bateau("B3", SensBateau.SUD_NORD, pont));
        lancer(1000, new Camion("C3", pont, gd));
        lancer(500, new Voiture("V7", pont, dg));

        // 6) Les pompiers passent pendant que des véhicules sont sur le pont
        lancer(1500, new VehiculePompiers("P1", pont, dg));
        lancer(300, new Bateau("B4", SensBateau.NORD_SUD, pont));

        for (Thread t : threads) {
            t.join();
        }
        Journal.log("=== Fin de la simulation : tous les vehicules et bateaux sont passes ===");
    }
}
