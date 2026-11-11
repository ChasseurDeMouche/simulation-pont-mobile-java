# Schéma UML des classes

Le diagramme ci-dessous est affiché automatiquement par GitHub (syntaxe Mermaid).

```mermaid
classDiagram
    direction LR

    class Thread {
        <<Java>>
        +run()
    }
    class TimerTask {
        <<Java>>
        +run()
    }

    class Main {
        -threads : List~Thread~
        +main(args)$
        -lancer(attenteMs, t)$
    }
    class Config {
        <<utilitaire>>
        +NB_SEGMENTS$ : int
        +DUREE_SEGMENT$ : long
        +DUREE_MANOEUVRE$ : long
        +DUREE_TRAVERSEE_BATEAU$ : long
        +DELAI_ENTRE_OUVERTURES$ : long
    }
    class Journal {
        <<utilitaire>>
        +log(message)$
        +brut(texte)$
    }

    class EtatPont {
        <<enumeration>>
        FERME
        OUVERTURE
        OUVERT
        FERMETURE
    }
    class SensVehicule {
        <<enumeration>>
        GAUCHE_DROITE
        DROITE_GAUCHE
        +oppose() SensVehicule
    }
    class SensBateau {
        <<enumeration>>
        NORD_SUD
        SUD_NORD
    }

    class Pont {
        -nbSegments : int
        -etat : EtatPont
        -voies : Vehicule[][]
        -nbVehicules : int[]
        -convoisBloquants : int[]
        -convoiSurPont : boolean[]
        -prioritaires : List~Vehicule~
        -fileBateaux : Queue~Bateau~
        -ouvertureDemandee : boolean
        -ouvertureAutorisee : boolean
        -prochaineOuverture : LocalTime
        -timer : Timer
        +entrer(v)
        +avancer(v) boolean
        +entrerPrioritaire(v)
        +avancerPrioritaire(v)
        +sortirPrioritaire(v)
        +arriveeBateau(b)
        +attendrePassage(b)
        +finPassage(b)
        +autoriserOuverture()
    }
    class TacheAutorisationOuverture {
        -pont : Pont
        +run()
    }

    class Vehicule {
        <<abstract>>
        -identifiant : String
        #pont : Pont
        -sens : SensVehicule
        -positionTete : int
        +getLongueur()* int
        +bloqueSensOppose() boolean
        +run()
    }
    class Voiture {
        +getLongueur() int
    }
    class Camion {
        +getLongueur() int
    }
    class ConvoiExceptionnel {
        +getLongueur() int
        +bloqueSensOppose() boolean
    }
    class VehiculePrioritaire {
        <<abstract>>
        +getLongueur() int
        +run()
    }
    class Ambulance
    class VehiculePompiers

    class Bateau {
        -identifiant : String
        -sens : SensBateau
        -pont : Pont
        +run()
    }

    Thread <|-- Vehicule
    Thread <|-- Bateau
    TimerTask <|-- TacheAutorisationOuverture
    Vehicule <|-- Voiture
    Vehicule <|-- Camion
    Vehicule <|-- ConvoiExceptionnel
    Vehicule <|-- VehiculePrioritaire
    VehiculePrioritaire <|-- Ambulance
    VehiculePrioritaire <|-- VehiculePompiers

    Pont --> EtatPont
    Pont o-- Vehicule : occupe les segments
    Pont o-- Bateau : file FIFO
    Pont ..> TacheAutorisationOuverture : programme
    TacheAutorisationOuverture --> Pont
    Vehicule --> Pont
    Vehicule --> SensVehicule
    Bateau --> Pont
    Bateau --> SensBateau
    Main ..> Pont : crée
    Main ..> Vehicule : lance
    Main ..> Bateau : lance
```

## Rôle des classes

| Classe | Rôle |
|---|---|
| `Main` | Crée le pont et lance le scénario de démonstration (threads décalés dans le temps). |
| `Config` | Regroupe les paramètres (nombre de segments, durées, délai d'une minute). |
| `Journal` | Affichage synchronisé des messages dans la console. |
| `Pont` | **Moniteur** central : états du pont, segments, convois, prioritaires, file FIFO des bateaux, ouvertures/fermetures. Toutes les attentes se font avec `synchronized` / `wait()` / `notifyAll()`. |
| `TacheAutorisationOuverture` | `TimerTask` lancée une minute après chaque fermeture complète ; autorise l'ouverture suivante et la déclenche si des bateaux attendent. |
| `Vehicule` | Thread de base d'un véhicule : entrer, avancer segment par segment, sortir. |
| `Voiture` / `Camion` / `ConvoiExceptionnel` | Occupent 1 / 2 / 4 segments. Le convoi bloque le sens opposé. |
| `VehiculePrioritaire` | Thread d'un véhicule prioritaire : les véhicules de son sens s'arrêtent sur la bande d'urgence. |
| `Ambulance` / `VehiculePompiers` | Les deux types de véhicules prioritaires. |
| `Bateau` | Thread d'un bateau : file FIFO, attente de l'ouverture complète, traversée. |
