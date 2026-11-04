package pontmobile.bateau;

/**
 * Représente un bateau circulant sous le pont.
 * Chaque bateau s'exécute dans un thread indépendant.
 */
public class Bateau extends Thread {

    private int idBateau;
    private SensBateau sens;
    private GestionnaireBateaux gestionnaire;

    public Bateau(int idBateau, SensBateau sens, GestionnaireBateaux gestionnaire) {
        this.idBateau = idBateau;
        this.sens = sens;
        this.gestionnaire = gestionnaire;
    }

    public int getIdBateau() {
        return idBateau;
    }

    public SensBateau getSens() {
        return sens;
    }

    @Override
    public void run() {
        try {
            // Le bateau signale son arrivée et entre dans la file FIFO
            gestionnaire.enregistrerEtTraverser(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[BATEAU " + idBateau + "] Interrompu.");
        }
    }
}