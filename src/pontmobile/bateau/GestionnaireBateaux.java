package pontmobile.bateau;

import java.util.LinkedList;
import java.util.Queue;
import pontmobile.modele.Pont;
import pontmobile.modele.EtatPont;

/**
 * Gère la file d'attente FIFO commune pour l'ensemble des bateaux
 * et coordonne leur traversée un par un.
 */
public class GestionnaireBateaux {

    public Pont pont;
    private Queue<Bateau> fileAttenteFIFO;

    public GestionnaireBateaux(Pont pont) {
        this.pont = pont;
        this.fileAttenteFIFO = new LinkedList<>();
    }

    /**
     * Enregistre un bateau dans la file FIFO et gère sa traversée.
     */
    public synchronized void enregistrerEtTraverser(Bateau bateau) throws InterruptedException {
        // 1. Arrivée du bateau : enregistrement FIFO
        fileAttenteFIFO.add(bateau);
        System.out.println("[BATEAU " + bateau.getIdBateau() + "] Arrivé (" + bateau.getSens() + "). Ajouté à la file FIFO. Rang : " + fileAttenteFIFO.size());

        // 2. Attente de son tour dans la file FIFO
        while (fileAttenteFIFO.peek() != bateau) {
            wait();
        }

        // 3. Si le pont n'est pas encore ouvert, on demande l'ouverture
        if (pont.getEtat() != EtatPont.OUVERT) {
            pont.demanderOuverture();
        }

        // 4. Traversée sous le pont (un seul bateau à la fois)
        System.out.println("-> [BATEAU " + bateau.getIdBateau() + "] En train de traverser (" + bateau.getSens() + ")...");
        Thread.sleep(2000); // Temps de traversée du bateau
        System.out.println("<- [BATEAU " + bateau.getIdBateau() + "] Traversée terminée.");

        // 5. Retrait de la file et notification du bateau suivant
        fileAttenteFIFO.poll();
        notifyAll();

        // 6. Si la file est vide, on referme le pont
        if (fileAttenteFIFO.isEmpty()) {
            System.out.println("[BATEAUX] Tous les bateaux ont traversé. Fermeture du pont.");
            pont.fermerPont();
        }
    }

    public synchronized int getNombreBateauxEnAttente() {
        return fileAttenteFIFO.size();
    }
}
