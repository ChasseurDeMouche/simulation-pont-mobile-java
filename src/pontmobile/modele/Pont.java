package pontmobile.modele;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Représente le pont mobile bidirectionnel.
 * Gère les états de transition, le nombre de véhicules présents sur les segments,
 * ainsi que le délai minimal d'une minute entre deux ouvertures[cite: 1, 2, 4].
 */
public class Pont {

    private int nombreSegments;
    private EtatPont etat;
    private int vehiculesSurPont; // Compteur de véhicules actuellement engagés
    private LocalDateTime heureDerniereFermeture;
    private boolean ouvertureDemandeeParBateaux;

    /**
     * Constructeur du pont.
     * @param nombreSegments Nombre de segments consécutifs composant le pont[cite: 2].
     */
    public Pont(int nombreSegments) {
        this.nombreSegments = nombreSegments;
        this.etat = EtatPont.FERME;
        this.vehiculesSurPont = 0;
        this.heureDerniereFermeture = LocalDateTime.now().minusMinutes(2); // Permet une ouverture immédiate au lancement
        this.ouvertureDemandeeParBateaux = false;
    }

    public int getNombreSegments() {
        return nombreSegments;
    }

    public synchronized EtatPont getEtat() {
        return etat;
    }

    /**
     * Un véhicule demande à entrer sur le pont.
     * Bloqué si le pont n'est pas FERME ou si une ouverture est demandée[cite: 2].
     */
    public synchronized void entrerVehicule() throws InterruptedException {
        while (etat != EtatPont.FERME || ouvertureDemandeeParBateaux) {
            wait(); // Attend que le pont soit fermé et disponible
        }
        vehiculesSurPont++;
    }

    /**
     * Un véhicule quitte complètement le pont.
     */
    public synchronized void sortirVehicule() {
        vehiculesSurPont--;
        if (vehiculesSurPont == 0 && ouvertureDemandeeParBateaux) {
            notifyAll(); // Réveille les threads en attente (ex: la procédure d'ouverture)
        }
    }

    /**
     * Déclenche la procédure d'ouverture du pont pour laisser passer les bateaux.
     * Respecte la règle d'attente d'une minute et attend que le pont se vide des véhicules[cite: 2, 4].
     */
    public synchronized void demanderOuverture() throws InterruptedException {
        ouvertureDemandeeParBateaux = true;

        long secondesDepuisFermeture = Duration.between(heureDerniereFermeture, LocalDateTime.now()).getSeconds();
        if (secondesDepuisFermeture < 60) {
            long attenteMillis = (60 - secondesDepuisFermeture) * 1000;
            System.out.println("[PONT] En attente du délai réglementaire avant prochaine ouverture (" + (60 - secondesDepuisFermeture) + "s)...");
            Thread.sleep(attenteMillis);
        }

        // 2. Attendre que tous les véhicules engagés aient quitté le pont[cite: 2]
        while (vehiculesSurPont > 0) {
            System.out.println("[PONT] En attente de la sortie des " + vehiculesSurPont + " derniers véhicules...");
            wait();
        }

        // 3. Passage à l'état OUVERTURE puis OUVERT[cite: 1, 2, 3]
        etat = EtatPont.OUVERTURE;
        System.out.println("[PONT] État actuel : OUVERTURE en cours...");
        Thread.sleep(1000); // Simulation du temps mécanique d'ouverture

        etat = EtatPont.OUVERT;
        System.out.println("[PONT] État actuel : OUVERT. Les bateaux peuvent traverser[cite: 3].");
        notifyAll();
    }

    /**
     * Déclenche la fermeture du pont une fois que tous les bateaux ont traversé[cite: 3].
     */
    public synchronized void fermerPont() throws InterruptedException {
        etat = EtatPont.FERMETURE;
        System.out.println("[PONT] État actuel : FERMETURE en cours...");
        Thread.sleep(1000); // Simulation du temps mécanique de fermeture

        etat = EtatPont.FERME;
        heureDerniereFermeture = LocalDateTime.now(); // Mémorise l'heure exacte de fermeture complète[cite: 4]
        ouvertureDemandeeParBateaux = false;
        System.out.println("[PONT] État actuel : FERMÉ. Les véhicules peuvent reprendre.");

        notifyAll(); // Libère les véhicules en attente d'entrée
    }
}