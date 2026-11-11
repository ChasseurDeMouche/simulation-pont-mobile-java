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

## Compiler et lancer

Prérequis : JDK 17 ou plus récent.

```bash
java -jar pont-mobile.jar
```

Pour recompiler : `build.bat` (Windows) ou `sh build.sh` (Linux/macOS). La simulation dure environ 1 min 30, car elle montre le délai d'une minute entre deux ouvertures.

## Organisation du dépôt

```
src/pontmobile/
├── Main.java                  scénario de démonstration
├── Config.java                paramètres (segments, durées, délai)
├── Journal.java               affichage console synchronisé
├── modele/
│   ├── Pont.java              moniteur central (synchronized / wait / notifyAll)
│   ├── EtatPont.java          FERME, OUVERTURE, OUVERT, FERMETURE
│   ├── SensVehicule.java      G->D, D->G
│   ├── SensBateau.java        Nord->Sud, Sud->Nord
│   └── TacheAutorisationOuverture.java   TimerTask du délai d'une minute
├── vehicules/
│   ├── Vehicule.java          thread de base (entrer, avancer, sortir)
│   ├── Voiture.java           1 segment
│   ├── Camion.java            2 segments
│   ├── ConvoiExceptionnel.java 4 segments, bloque le sens opposé
│   ├── VehiculePrioritaire.java  bande d'urgence
│   ├── Ambulance.java
│   └── VehiculePompiers.java
└── bateau/
    └── Bateau.java            thread d'un bateau (file FIFO)
docs/UML.md                    schéma UML des classes
PARTIES_NON_REALISEES.txt      simplifications et parties restantes
pont-mobile.jar                exécutable
```

Les consignes de préparation du dépôt sont dans [`projet-os-github/docs/MISE_EN_PLACE_GITHUB.md`](projet-os-github/docs/MISE_EN_PLACE_GITHUB.md). Les règles de contribution sont dans [`projet-os-github/CONTRIBUTING.md`](projet-os-github/CONTRIBUTING.md).

## Fonctionnement de la synchronisation

Toute la coordination passe par un seul moniteur, la classe `Pont` : chaque thread appelle une méthode `synchronized`, attend avec `wait()` tant que sa condition n'est pas remplie, et chaque changement d'état réveille les autres avec `notifyAll()`.

- **Segments** : `voies[sens][i]` contient le véhicule qui occupe le segment, ou `null`. Un véhicule de longueur L réserve ses L premiers segments en une seule fois pour entrer, puis avance en réservant le segment suivant et en libérant le dernier.
- **Convoi** : à son arrivée il incrémente `convoisBloquants` du sens opposé (entrée bloquée), puis attend que le sens opposé soit vide et que ses 4 segments soient libres.
- **Ouverture** : le premier bateau de la file FIFO pose `ouvertureDemandee` (plus aucune entrée), attend que le pont soit vide, puis passe le pont en `OUVERTURE` puis `OUVERT`. Le dernier bateau referme le pont.
- **Délai** : à la fermeture complète, l'heure est mémorisée et un `Timer` exécute `TacheAutorisationOuverture` une minute plus tard ; elle réveille les bateaux en attente.
- **Prioritaires** : tant qu'un prioritaire traverse dans un sens, les véhicules de ce sens ne bougent plus (bande d'urgence) mais gardent leurs segments ; ils repartent à sa sortie.
- Les pauses (`sleep`) de manœuvre et de traversée sont faites **hors** du verrou pour ne pas bloquer les autres threads.

## Livrables du projet

Le dossier final doit porter le nom des deux membres du binôme, séparés par un underscore : `Nom1_Nom2`. Il doit contenir :

- les fichiers source Java commentés ;
- le schéma UML des classes créées ;
- un fichier `.jar` exécutable en ligne de commande ;
- une vidéo de démonstration de deux minutes maximum ;
- un fichier texte indiquant les parties qui n’ont pas pu être réalisées, le cas échéant.

## État d’avancement

Toutes les fonctionnalités demandées sont intégrées dans la simulation et vérifiées par le scénario de `Main` :

- [x] Circulation dans les deux sens sur N segments (voitures, camions)
- [x] Convoi exceptionnel avec blocage du sens opposé
- [x] Véhicules prioritaires et bande d'urgence
- [x] File FIFO commune des bateaux, un bateau à la fois
- [x] États FERME → OUVERTURE → OUVERT → FERMETURE, ouverture seulement pont vide
- [x] Délai d'une minute entre deux ouvertures (`Timer` / `TimerTask`)
- [x] Schéma UML ([docs/UML.md](docs/UML.md)), `.jar` exécutable, fichier des simplifications
- [ ] Vidéo de démonstration (à enregistrer par le binôme)
