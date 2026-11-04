package fr.iutgon.r305.pontmobile.modele;

import fr.iutgon.r305.pontmobile.vehicules.Vehicule;

/** Ressource partagée représentant un segment numéroté du pont. */
public class Segment {

    private final int numero;
    private Vehicule occupant;

    /** Crée un segment dont le numéro commence à 1. */
    public Segment(int numero) {
        if (numero < 1) {
            throw new IllegalArgumentException("Le numéro du segment doit être positif.");
        }
        this.numero = numero;
    }

    /** Retourne le numéro du segment. */
    public int getNumero() {
        return numero;
    }

    /** Indique si aucun véhicule n'occupe le segment. */
    public synchronized boolean estLibre() {
        return occupant == null;
    }

    /** Retourne le véhicule occupant le segment, ou null si le segment est libre. */
    public synchronized Vehicule getOccupant() {
        return occupant;
    }

    /** Réserve ce segment si celui-ci est libre. */
    public synchronized boolean reserver(Vehicule vehicule) {
        if (vehicule == null) {
            throw new IllegalArgumentException("Le véhicule ne peut pas être null.");
        }
        if (occupant != null) {
            return false;
        }
        occupant = vehicule;
        return true;
    }

    /** Libère le segment s'il appartient bien au véhicule fourni. */
    public synchronized void liberer(Vehicule vehicule) {
        if (occupant != vehicule) {
            throw new IllegalStateException("Ce véhicule n'occupe pas le segment " + numero + ".");
        }
        occupant = null;
    }
}
