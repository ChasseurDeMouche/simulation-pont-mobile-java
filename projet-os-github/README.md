# Simulation d’un pont mobile bidirectionnel

Projet de programmation système en Java réalisé en binôme dans le cadre de la ressource R3.05.

## Objectif

Simuler la circulation de véhicules sur un pont mobile et le passage de bateaux sous ce pont. Chaque véhicule et chaque bateau est représenté par un thread Java. Le programme coordonne leurs accès au pont et à ses segments afin de respecter les règles de sécurité et de circulation du sujet.

## Fonctionnalités demandées

- Quatre flux : véhicules de gauche à droite et de droite à gauche ; bateaux du nord vers le sud et du sud vers le nord.
- États du pont : `FERMÉ`, `OUVERTURE`, `OUVERT` et `FERMETURE`, avec les transitions dans cet ordre. Le sujet autorise éventuellement une simplification à deux états ; la version de référence du projet vise les quatre états.
- Ouverture uniquement après la sortie complète des véhicules déjà engagés. Aucun véhicule ne circule pendant l’ouverture, lorsque le pont est ouvert, ni pendant la fermeture.
- Pont divisé en `N` segments partagés. Les véhicules peuvent avancer dans les deux sens si les segments requis sont disponibles, sans dépassement ni demi-tour.
- Une voiture réserve un segment à la fois ; un camion réserve simultanément deux segments consécutifs ; un convoi exceptionnel en occupe quatre et interdit la présence de véhicules dans le sens opposé pendant son passage.
- Les bateaux partagent une file FIFO, passent un par un et commencent leur traversée seulement lorsque le pont est complètement ouvert.
- Une nouvelle ouverture n’est autorisée qu’une minute après la fermeture complète précédente dans la simulation. Le sujet demande l’emploi de `Timer`/`TimerTask` pour vérifier l’échéance et la présence de bateaux en attente.
- Ambulances et véhicules de pompiers prioritaires : les véhicules présents s’arrêtent sur la bande d’urgence en conservant leur association aux segments de reprise, puis reprennent leur progression après le passage prioritaire.

## Organisation du dépôt

Les consignes de préparation du dépôt sont dans [`docs/MISE_EN_PLACE_GITHUB.md`](docs/MISE_EN_PLACE_GITHUB.md). La liste des tâches à créer dans GitHub est dans [`docs/TACHES_GITHUB.md`](docs/TACHES_GITHUB.md). Les règles de contribution sont dans [`CONTRIBUTING.md`](CONTRIBUTING.md).

## Livrables du projet

Le dossier final doit porter le nom des deux membres du binôme, séparés par un underscore : `Nom1_Nom2`. Il doit contenir :

- les fichiers source Java commentés ;
- le schéma UML des classes créées ;
- un fichier `.jar` exécutable en ligne de commande ;
- une vidéo de démonstration de deux minutes maximum ;
- un fichier texte indiquant les parties qui n’ont pas pu être réalisées, le cas échéant.

## État d’avancement

À compléter par le binôme au fil du projet : fonctionnalités terminées, décisions de conception utiles et éventuelles difficultés. Ne pas présenter une fonctionnalité comme terminée avant de l’avoir intégrée à la simulation.
