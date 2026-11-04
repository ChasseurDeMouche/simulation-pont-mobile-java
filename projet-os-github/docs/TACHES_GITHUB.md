# Tâches à créer dans GitHub Issues

Créer une Issue par tâche ci-dessous. Affecter chaque Issue à l’un des deux membres du binôme ; les tâches marquées **Ensemble** nécessitent une décision ou une intégration commune. Les intitulés et critères reprennent le sujet du projet.

## Étiquettes suggérées

- `conception` : modèle ou décision d’architecture ;
- `vehicules` : circulation terrestre et segments ;
- `bateaux` : file FIFO et traversée ;
- `pont` : états, ouvertures et fermetures ;
- `priorite` : ambulances et véhicules de pompiers ;
- `livrable` : documentation et fichiers de rendu.

## Issues

### 1. Définir l’architecture et les interfaces — Ensemble

**Description à coller dans l’Issue :**

Définir les classes nécessaires à la simulation et les interfaces utilisées entre elles avant le développement en parallèle. Le modèle doit représenter le pont, ses états, ses `N` segments, les véhicules, les bateaux et les threads associés.

**Critères de fin :**

- Les responsabilités des classes sont identifiées.
- Les états du pont et les sens de circulation sont nommés de façon cohérente.
- Les méthodes de coordination entre véhicule, bateau et pont sont définies.
- Les décisions sont partagées dans le binôme et reportées dans le schéma UML final.

### 2. Gérer les segments et les voitures — Responsable à désigner

**Description à coller dans l’Issue :**

Représenter les `N` segments comme des ressources partagées. Implémenter la progression d’une voiture, qui réserve un segment, avance lorsque le segment suivant est disponible, libère le segment quitté et sort complètement du pont. Autoriser les deux sens lorsque les segments requis sont disponibles, sans dépassement ni demi-tour.

**Critères de fin :**

- Une voiture ne réserve qu’un segment à la fois.
- Un segment occupé n’est pas attribué simultanément à deux véhicules.
- Une voiture libère les segments qu’elle a effectivement quittés.
- Les deux sens de circulation sont représentés.

### 3. Ajouter les camions — Responsable à désigner

**Description à coller dans l’Issue :**

Implémenter les camions, qui occupent deux segments consécutifs. Le camion doit réserver simultanément les deux segments nécessaires avant d’entrer ou d’avancer ; il ne doit jamais en occuper un en attendant l’autre.

**Critères de fin :**

- Les deux segments sont réservés atomiquement avant la progression.
- La même règle fonctionne dans les deux sens.
- Les segments quittés sont libérés après le déplacement.

### 4. Ajouter les convois exceptionnels — Responsable à désigner

**Description à coller dans l’Issue :**

Implémenter un convoi exceptionnel occupant quatre segments consécutifs. À son arrivée, bloquer l’entrée des nouveaux véhicules du sens opposé, laisser les véhicules déjà présents terminer, puis attendre leur sortie et la disponibilité des quatre segments. Les véhicules de même sens peuvent continuer si les segments le permettent.

**Critères de fin :**

- Le convoi ne démarre qu’après la sortie des véhicules du sens opposé.
- Il occupe quatre segments consécutifs.
- L’entrée opposée est débloquée après sa sortie, sauf si une autre règle de sécurité la maintient bloquée.

### 5. Gérer les états et l’ouverture du pont — Responsable à désigner

**Description à coller dans l’Issue :**

Implémenter les états `FERMÉ`, `OUVERTURE`, `OUVERT` et `FERMETURE`, dans l’ordre de transition prévu. Lorsqu’une ouverture est prise en charge, empêcher les nouveaux véhicules d’entrer, laisser les véhicules déjà engagés terminer et n’ouvrir qu’après leur sortie complète. Interdire la circulation terrestre pendant les trois autres états que `FERMÉ`.

**Critères de fin :**

- Les transitions respectent l’ordre autorisé.
- Aucun véhicule ne reste sur le pont lorsque l’ouverture commence.
- Aucun véhicule ne traverse pendant `OUVERTURE`, `OUVERT` ou `FERMETURE`.

### 6. Gérer les bateaux et la file FIFO — Responsable à désigner

**Description à coller dans l’Issue :**

Représenter les bateaux venant du nord et du sud par des threads. Ajouter une file FIFO commune, indépendante de la direction, et autoriser un seul bateau à traverser à la fois. Un bateau ne commence qu’une fois le pont complètement ouvert.

**Critères de fin :**

- Les demandes sont ajoutées à la même file FIFO.
- Les bateaux passent un par un et dans l’ordre d’arrivée.
- La direction ne modifie pas l’ordre FIFO.
- Le bateau attend l’état `OUVERT` avant de commencer.

### 7. Ajouter le délai entre les ouvertures — Responsable à désigner

**Description à coller dans l’Issue :**

À la fermeture complète, mémoriser l’heure et fixer la prochaine ouverture possible une minute plus tard pour la simulation. À l’échéance, vérifier si des bateaux attendent et déclencher une ouverture si nécessaire, à l’aide de `Timer`/`TimerTask` comme demandé dans le sujet.

**Critères de fin :**

- Le délai part de la fermeture complète du pont.
- Une ouverture ne démarre pas avant l’échéance.
- À l’échéance, une ouverture n’est déclenchée que si au moins un bateau attend.

### 8. Gérer les véhicules prioritaires — Responsable à désigner

**Description à coller dans l’Issue :**

Implémenter la priorité des ambulances et des véhicules de pompiers. À leur arrivée, les véhicules présents interrompent leur progression et se placent sur la bande d’urgence en conservant leur association à leur segment ou à leurs segments de reprise. Le véhicule prioritaire traverse sans gêner les véhicules de l’autre sens ; après sa sortie complète, les autres reprennent leur progression.

**Critères de fin :**

- Les véhicules prioritaires sont distingués des voitures, camions et convois.
- L’état de reprise de chaque véhicule interrompu est conservé.
- Les véhicules reprennent après la sortie complète du véhicule prioritaire.
- La règle ne permet pas de conflit avec les segments ni avec les véhicules de l’autre sens.

### 9. Intégrer les fonctionnalités dans une simulation — Ensemble

**Description à coller dans l’Issue :**

Intégrer les différentes parties dans une simulation commune. Vérifier les situations importantes du sujet : deux sens de circulation, segments occupés, camion, convoi exceptionnel, file de bateaux, ouverture du pont après vidage, délai d’une minute et priorité d’un véhicule d’urgence.

**Critères de fin :**

- Les fonctionnalités sont intégrées dans une même exécution.
- Les conflits d’accès aux segments et au pont sont corrigés.
- Les scénarios de démonstration sont reproductibles.
- Les fonctionnalités encore absentes sont clairement identifiées.

### 10. Préparer les livrables — Ensemble

**Description à coller dans l’Issue :**

Préparer le dossier final nommé `Nom1_Nom2`, avec les deux noms du binôme séparés par un underscore. Inclure les sources Java commentées, le schéma UML des classes, le fichier `.jar` exécutable en ligne de commande, la vidéo de démonstration de deux minutes maximum et un fichier texte décrivant les parties non réalisées, le cas échéant.

**Critères de fin :**

- Le dossier suit la convention de nommage demandée.
- Tous les éléments demandés sont présents.
- La vidéo dure au maximum deux minutes.
- Le fichier texte décrit honnêtement les éventuelles parties inachevées.
