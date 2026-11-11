package pontmobile.modele;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;

import pontmobile.Config;
import pontmobile.Journal;
import pontmobile.bateau.Bateau;
import pontmobile.vehicules.Vehicule;

/**
 * Le pont mobile : ressource partagée par tous les threads.
 *
 * Cette classe est un moniteur : toutes les méthodes qui lisent ou modifient
 * l'état partagé sont synchronisées, et les threads attendent avec wait()
 * qu'une condition devienne vraie. À chaque changement, notifyAll() réveille
 * les threads en attente, qui revérifient leur condition.
 *
 * Organisation des segments : chaque sens a sa propre voie de N segments.
 * voies[sens][i] contient le véhicule qui occupe le i-ème segment rencontré
 * dans son sens de marche (i = 0 : entrée du pont), ou null si le segment est libre.
 */
public class Pont {

    private static final DateTimeFormatter HEURE = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final int nbSegments;
    private EtatPont etat = EtatPont.FERME;

    // ----- Véhicules -----
    /** Occupation des segments, une ligne par sens (indice = sens.ordinal()). */
    private final Vehicule[][] voies;
    /** Nombre de véhicules présents sur le pont dans chaque sens. */
    private final int[] nbVehicules = new int[2];
    /** Nombre de convois qui bloquent l'entrée de chaque sens. */
    private final int[] convoisBloquants = new int[2];
    /** Indique si un convoi exceptionnel est sur le pont dans chaque sens. */
    private final boolean[] convoiSurPont = new boolean[2];
    /** Véhicules prioritaires en train de traverser. */
    private final List<Vehicule> prioritaires = new ArrayList<>();

    // ----- Bateaux et ouvertures -----
    /** File FIFO commune à tous les bateaux, quelle que soit leur direction. */
    private final Queue<Bateau> fileBateaux = new LinkedList<>();
    /** Vrai quand une ouverture est prise en charge : plus aucun véhicule n'entre. */
    private boolean ouvertureDemandee = false;
    /** Faux pendant la minute qui suit une fermeture complète. */
    private boolean ouvertureAutorisee = true;
    /** Heure à partir de laquelle une nouvelle ouverture est autorisée. */
    private LocalTime prochaineOuverture;
    /** Timer qui autorise l'ouverture suivante (thread démon). */
    private final Timer timer = new Timer("Timer-ouverture", true);

    /**
     * Crée un pont fermé et vide.
     * @param nbSegments nombre de segments du pont
     */
    public Pont(int nbSegments) {
        this.nbSegments = nbSegments;
        this.voies = new Vehicule[2][nbSegments];
    }

    /** @return le nombre de segments du pont */
    public int getNbSegments() {
        return nbSegments;
    }

    // =====================================================================
    //  Véhicules normaux : voitures, camions, convois exceptionnels
    // =====================================================================

    /**
     * Fait entrer un véhicule sur le pont. Le thread attend tant que l'entrée
     * est impossible, puis réserve en une seule fois les L premiers segments
     * de sa voie (L = longueur du véhicule).
     * @param v le véhicule qui veut entrer
     * @throws InterruptedException si le thread est interrompu pendant l'attente
     */
    public synchronized void entrer(Vehicule v) throws InterruptedException {
        int s = v.getSens().ordinal();
        int longueur = v.getLongueur();

        if (v.bloqueSensOppose()) {
            // Règle du convoi : l'entrée opposée est bloquée dès son arrivée.
            convoisBloquants[1 - s]++;
            Journal.log(v + " arrive " + v.getSens() + " -> entrée " + v.getSens().oppose() + " bloquée");
        } else {
            Journal.log(v + " arrive " + v.getSens());
        }

        while (!peutEntrer(v)) {
            wait();
        }

        for (int i = 0; i < longueur; i++) {
            voies[s][i] = v;
        }
        v.setPositionTete(longueur - 1);
        nbVehicules[s]++;
        if (v.bloqueSensOppose()) {
            convoiSurPont[s] = true;
        }
        Journal.log(v + " entre sur le pont");
        afficher();
    }

    /**
     * Teste si un véhicule normal peut entrer maintenant.
     * @param v le véhicule qui veut entrer
     * @return vrai si toutes les conditions d'entrée sont réunies
     */
    private boolean peutEntrer(Vehicule v) {
        int s = v.getSens().ordinal();

        // Pont fermé et aucune ouverture en préparation.
        if (etat != EtatPont.FERME || ouvertureDemandee) {
            return false;
        }
        // Un véhicule prioritaire passe dans ce sens.
        if (prioritaireDansLeSens(s)) {
            return false;
        }
        if (v.bloqueSensOppose()) {
            // Un convoi attend que le sens opposé soit vide.
            if (nbVehicules[1 - s] > 0) {
                return false;
            }
        } else if (convoisBloquants[s] > 0) {
            // Un convoi du sens opposé bloque cette entrée.
            return false;
        }
        // Les L premiers segments doivent être libres (réservation simultanée).
        for (int i = 0; i < v.getLongueur(); i++) {
            if (voies[s][i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Fait avancer un véhicule d'un segment. Le véhicule réserve le segment
     * suivant (en attendant qu'il soit libre) et libère son dernier segment.
     * Si le véhicule est déjà au bout du pont, il sort.
     * @param v le véhicule qui avance
     * @return vrai si le véhicule est toujours sur le pont, faux s'il est sorti
     * @throws InterruptedException si le thread est interrompu pendant l'attente
     */
    public synchronized boolean avancer(Vehicule v) throws InterruptedException {
        int s = v.getSens().ordinal();

        // Pendant le passage d'un prioritaire, le véhicule reste arrêté sur la
        // bande d'urgence en gardant ses segments.
        while (prioritaireDansLeSens(s)) {
            wait();
        }

        int tete = v.getPositionTete();
        if (tete == nbSegments - 1) {
            sortir(v);
            return false;
        }

        // Attendre que le segment suivant soit libre.
        while (voies[s][tete + 1] != null || prioritaireDansLeSens(s)) {
            wait();
        }

        voies[s][tete + 1] = v;                   // réserve le segment suivant
        voies[s][tete + 1 - v.getLongueur()] = null; // libère le segment quitté
        v.setPositionTete(tete + 1);
        notifyAll();
        afficher();
        return true;
    }

    /**
     * Sortie complète d'un véhicule : libère tous ses segments.
     * Appelée uniquement depuis avancer() (le verrou est déjà pris).
     * @param v le véhicule qui sort
     */
    private void sortir(Vehicule v) {
        int s = v.getSens().ordinal();
        int tete = v.getPositionTete();
        for (int i = tete - v.getLongueur() + 1; i <= tete; i++) {
            voies[s][i] = null;
        }
        nbVehicules[s]--;
        Journal.log(v + " a quitté le pont");

        if (v.bloqueSensOppose()) {
            convoiSurPont[s] = false;
            convoisBloquants[1 - s]--;
            if (convoisBloquants[1 - s] == 0) {
                Journal.log("Entrée " + v.getSens().oppose() + " débloquée");
            }
        }
        notifyAll();
        afficher();
    }

    // =====================================================================
    //  Véhicules prioritaires : ambulances et pompiers
    // =====================================================================

    /**
     * Entrée d'un véhicule prioritaire. Il n'a pas besoin de segments libres :
     * les véhicules de son sens se rangent sur la bande d'urgence.
     * Il attend seulement que le pont soit fermé (sécurité) et qu'aucun convoi
     * ne roule en sens opposé (largeur insuffisante).
     * @param v le véhicule prioritaire
     * @throws InterruptedException si le thread est interrompu pendant l'attente
     */
    public synchronized void entrerPrioritaire(Vehicule v) throws InterruptedException {
        int s = v.getSens().ordinal();
        Journal.log(v + " arrive " + v.getSens() + " (PRIORITAIRE)");

        while (etat != EtatPont.FERME || ouvertureDemandee || convoiSurPont[1 - s]) {
            wait();
        }

        prioritaires.add(v);
        nbVehicules[s]++;
        v.setPositionTete(0);

        List<String> ranges = vehiculesSurVoie(s);
        if (ranges.isEmpty()) {
            Journal.log(v + " entre sur le pont, voie " + v.getSens() + " libre");
        } else {
            Journal.log(v + " entre sur le pont : " + ranges
                    + " se rangent sur la bande d'urgence (segments conservés)");
        }
        afficher();
    }

    /**
     * Fait avancer un véhicule prioritaire d'un segment (affichage uniquement,
     * car il roule sur la voie libérée par les véhicules rangés).
     * @param v le véhicule prioritaire
     */
    public synchronized void avancerPrioritaire(Vehicule v) {
        v.setPositionTete(v.getPositionTete() + 1);
        afficher();
    }

    /**
     * Sortie d'un véhicule prioritaire : les véhicules arrêtés reprennent
     * leur progression là où ils s'étaient arrêtés.
     * @param v le véhicule prioritaire
     */
    public synchronized void sortirPrioritaire(Vehicule v) {
        int s = v.getSens().ordinal();
        prioritaires.remove(v);
        nbVehicules[s]--;
        Journal.log(v + " a quitté le pont");

        if (!prioritaireDansLeSens(s)) {
            List<String> ranges = vehiculesSurVoie(s);
            if (!ranges.isEmpty()) {
                Journal.log(ranges + " quittent la bande d'urgence et reprennent leur progression");
            }
        }
        notifyAll();
        afficher();
    }

    /**
     * @param s indice du sens
     * @return vrai si un véhicule prioritaire traverse dans ce sens
     */
    private boolean prioritaireDansLeSens(int s) {
        for (Vehicule p : prioritaires) {
            if (p.getSens().ordinal() == s) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param s indice du sens
     * @return les identifiants des véhicules normaux présents sur cette voie
     */
    private List<String> vehiculesSurVoie(int s) {
        List<String> noms = new ArrayList<>();
        for (Vehicule v : voies[s]) {
            if (v != null && !noms.contains(v.getIdentifiant())) {
                noms.add(v.getIdentifiant());
            }
        }
        return noms;
    }

    // =====================================================================
    //  Bateaux, ouverture et fermeture du pont
    // =====================================================================

    /**
     * Enregistre l'arrivée d'un bateau dans la file FIFO commune.
     * @param b le bateau qui arrive
     */
    public synchronized void arriveeBateau(Bateau b) {
        fileBateaux.add(b);
        Journal.log(b + " arrive (" + b.getSens() + "), rang " + fileBateaux.size() + " dans la file");
        if (!ouvertureAutorisee) {
            Journal.log("   prochaine ouverture autorisée à " + prochaineOuverture.format(HEURE));
        }
        notifyAll();
    }

    /**
     * Le bateau attend son tour (premier de la file) et que le pont soit ouvert.
     * Si le pont est fermé et qu'une ouverture est autorisée, c'est ce bateau
     * qui lance l'ouverture : blocage des entrées, attente de la sortie des
     * véhicules, puis OUVERTURE -> OUVERT.
     * @param b le bateau qui veut passer
     * @throws InterruptedException si le thread est interrompu pendant l'attente
     */
    public void attendrePassage(Bateau b) throws InterruptedException {
        synchronized (this) {
            while (fileBateaux.peek() != b || !(etat == EtatPont.OUVERT
                    || (etat == EtatPont.FERME && ouvertureAutorisee))) {
                wait();
            }
            if (etat == EtatPont.OUVERT) {
                return; // le pont est déjà ouvert, le bateau peut passer
            }

            // Prise en charge de l'ouverture : plus aucun véhicule n'entre.
            ouvertureDemandee = true;
            Journal.log("Ouverture demandée : entrées du pont bloquées");
            while (nbVehicules[0] + nbVehicules[1] > 0) {
                wait(); // les véhicules engagés terminent leur traversée
            }
            changerEtat(EtatPont.OUVERTURE);
        }

        Thread.sleep(Config.DUREE_MANOEUVRE); // manœuvre d'ouverture (hors verrou)

        synchronized (this) {
            ouvertureDemandee = false;
            changerEtat(EtatPont.OUVERT);
            notifyAll();
        }
    }

    /**
     * Fin de la traversée d'un bateau : il sort de la file. Si la file est vide,
     * le pont se referme, l'heure de fermeture est mémorisée et un Timer est
     * programmé pour autoriser la prochaine ouverture une minute plus tard.
     * @param b le bateau qui a traversé
     * @throws InterruptedException si le thread est interrompu pendant la fermeture
     */
    public void finPassage(Bateau b) throws InterruptedException {
        synchronized (this) {
            fileBateaux.poll();
            Journal.log(b + " a passé le pont");
            if (!fileBateaux.isEmpty()) {
                notifyAll(); // au tour du bateau suivant
                return;
            }
            changerEtat(EtatPont.FERMETURE);
        }

        Thread.sleep(Config.DUREE_MANOEUVRE); // manœuvre de fermeture (hors verrou)

        synchronized (this) {
            changerEtat(EtatPont.FERME);
            LocalTime heureFermeture = LocalTime.now();
            prochaineOuverture = heureFermeture.plusNanos(Config.DELAI_ENTRE_OUVERTURES * 1_000_000);
            ouvertureAutorisee = false;
            Journal.log("Fermeture complète à " + heureFermeture.format(HEURE)
                    + " -> prochaine ouverture autorisée à " + prochaineOuverture.format(HEURE));
            timer.schedule(new TacheAutorisationOuverture(this), Config.DELAI_ENTRE_OUVERTURES);
            notifyAll(); // les véhicules peuvent de nouveau entrer
        }
    }

    /**
     * Appelée par le Timer quand le délai entre deux ouvertures est écoulé.
     * Vérifie s'il y a des bateaux en attente et, si oui, réveille le premier
     * pour qu'il déclenche l'ouverture.
     */
    public synchronized void autoriserOuverture() {
        ouvertureAutorisee = true;
        if (fileBateaux.isEmpty()) {
            Journal.log("Timer : ouverture de nouveau autorisée (aucun bateau en attente)");
        } else {
            Journal.log("Timer : ouverture autorisée, " + fileBateaux.size()
                    + " bateau(x) en attente -> ouverture déclenchée");
        }
        notifyAll();
    }

    /**
     * Change l'état du pont et l'affiche.
     * @param nouvelEtat le nouvel état
     */
    private void changerEtat(EtatPont nouvelEtat) {
        etat = nouvelEtat;
        Journal.log("PONT : " + nouvelEtat);
        afficher();
    }

    // =====================================================================
    //  Affichage
    // =====================================================================

    /**
     * Dessine l'état actuel du pont : les deux voies segment par segment,
     * les véhicules prioritaires, l'état du pont et la file des bateaux.
     */
    private void afficher() {
        StringBuilder sb = new StringBuilder();
        sb.append("            ");
        for (int i = 1; i <= nbSegments; i++) {
            sb.append(String.format(" S%-3d", i));
        }
        sb.append('\n');

        for (SensVehicule sens : SensVehicule.values()) {
            int s = sens.ordinal();
            sb.append("     ").append(sens).append("  |");
            for (int col = 0; col < nbSegments; col++) {
                Vehicule v = voies[s][indiceDansLaVoie(sens, col)];
                sb.append(String.format(" %-3s|", v == null ? "" : v.getIdentifiant()));
            }
            if (prioritaireDansLeSens(s)) {
                sb.append("  <- bande d'urgence");
            }
            sb.append('\n');

            if (prioritaireDansLeSens(s)) {
                sb.append("     urg.  |");
                for (int col = 0; col < nbSegments; col++) {
                    String id = "";
                    for (Vehicule p : prioritaires) {
                        if (p.getSens() == sens && indiceDansLaVoie(sens, col) == p.getPositionTete()) {
                            id = p.getIdentifiant();
                        }
                    }
                    sb.append(String.format(" %-3s|", id));
                }
                sb.append('\n');
            }
        }

        sb.append("     Pont : ").append(etat).append("   Bateaux en attente : ");
        List<String> bateaux = new ArrayList<>();
        for (Bateau b : fileBateaux) {
            bateaux.add(b.getIdentifiant());
        }
        sb.append(bateaux).append("\n\n");
        Journal.brut(sb.toString());
    }

    /**
     * Convertit une colonne affichée (S1 à gauche) en indice dans la voie
     * d'un sens (0 = entrée du pont pour ce sens).
     */
    private int indiceDansLaVoie(SensVehicule sens, int colonne) {
        return sens == SensVehicule.GAUCHE_DROITE ? colonne : nbSegments - 1 - colonne;
    }
}
