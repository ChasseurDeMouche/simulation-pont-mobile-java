package fr.iutgon.r305.pontmobile.modele;

import fr.iutgon.r305.pontmobile.bateaux.Bateau;
import fr.iutgon.r305.pontmobile.vehicules.Vehicule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Ressource partagée qui coordonnera les véhicules, les bateaux et les segments. */
public class Pont {

    private final List<Segment> segments;
    private EtatPont etat;
    private boolean ouvertureDemandee;

    /** Crée un pont contenant le nombre de segments indiqué. */
    public Pont(int nombreSegments) {
        if (nombreSegments < 1) {
            throw new IllegalArgumentException("Le pont doit contenir au moins un segment.");
        }
        segments = new ArrayList<>();
        for (int numero = 1; numero <= nombreSegments; numero++) {
            segments.add(new Segment(numero));
        }
        etat = EtatPont.FERME;
    }

    /** Retourne l'état actuel du pont. */
    public synchronized EtatPont getEtat() {
        return etat;
    }

    /** Retourne le nombre de segments du pont. */
    public int getNombreSegments() {
        return segments.size();
    }

    /** Retourne une vue non modifiable de la liste des segments. */
    public List<Segment> getSegments() {
        return Collections.unmodifiableList(segments);
    }

    /** Demande une ouverture et bloque les nouvelles entrées terrestres. */
    public synchronized void demanderOuverture() {
        ouvertureDemandee = true;
        notifyAll();
        // TODO : attendre que les véhicules déjà engagés aient quitté le pont,
        // puis effectuer les transitions OUVERTURE et OUVERT.
    }

    /** Indique si une nouvelle entrée terrestre est actuellement autorisée. */
    public synchronized boolean entreeVehiculeAutorisee() {
        return etat == EtatPont.FERME && !ouvertureDemandee;
    }

    /** Modifie l'état du pont en respectant la transition autorisée suivante. */
    public synchronized void passerEtatSuivant() {
        switch (etat) {
            case FERME:
                etat = EtatPont.OUVERTURE;
                break;
            case OUVERTURE:
                etat = EtatPont.OUVERT;
                break;
            case OUVERT:
                etat = EtatPont.FERMETURE;
                break;
            case FERMETURE:
                etat = EtatPont.FERME;
                ouvertureDemandee = false;
                break;
            default:
                throw new IllegalStateException("État du pont inconnu.");
        }
        notifyAll();
    }

    /**
     * Réserve à l'entrée tous les segments consécutifs d'un véhicule, ou aucun.
     * Le premier numéro est le segment d'entrée du véhicule. Pour la progression
     * d'un véhicule déjà engagé, utiliser avancerVehicule.
     */
    public synchronized boolean reserverSegments(Vehicule vehicule, int premierNumero) {
        if (vehicule == null || !entreeVehiculeAutorisee()) {
            return false;
        }

        List<Segment> aReserver = new ArrayList<>();
        int numero = premierNumero;
        for (int i = 0; i < vehicule.getLongueurEnSegments(); i++) {
            if (numero < 1 || numero > segments.size()) {
                return false;
            }
            Segment segment = segments.get(numero - 1);
            if (!segment.estLibre()) {
                return false;
            }
            aReserver.add(segment);
            numero += vehicule.getSens().getPas();
        }

        // L'accès aux réservations doit passer par le verrou du Pont.
        for (Segment segment : aReserver) {
            if (!segment.reserver(vehicule)) {
                for (Segment dejaReserve : aReserver) {
                    if (dejaReserve.getOccupant() == vehicule) {
                        dejaReserve.liberer(vehicule);
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Déplace un véhicule déjà engagé vers les segments suivants.
     * Cette méthode devra rester autorisée pendant qu'une ouverture attend que
     * les véhicules présents terminent leur traversée.
     */
    public synchronized boolean avancerVehicule(Vehicule vehicule, int premierNouveauSegment) {
        throw new UnsupportedOperationException("TODO : déplacer atomiquement le véhicule sur les segments");
    }

    /** Libère les segments actuellement occupés par ce véhicule. */
    public synchronized void libererSegments(Vehicule vehicule) {
        for (Segment segment : segments) {
            if (segment.getOccupant() == vehicule) {
                segment.liberer(vehicule);
            }
        }
        notifyAll();
    }

    /** Gère la traversée complète d'un véhicule. À développer en binôme. */
    public void traverser(Vehicule vehicule) throws InterruptedException {
        throw new UnsupportedOperationException("TODO : implémenter la traversée de " + vehicule.getId());
    }

    /** Enregistre et gère le passage d'un bateau. À développer en binôme. */
    public void demanderPassage(Bateau bateau) throws InterruptedException {
        throw new UnsupportedOperationException("TODO : implémenter la file FIFO des bateaux");
    }
}
