# Mise en place du projet sur GitHub

Ce guide prépare l’espace de travail commun du binôme. Il ne crée pas le dépôt à votre place : les deux membres doivent se connecter à leur compte GitHub pour le créer et s’ajouter.

## 1. Créer le dépôt

1. Sur GitHub, créer un nouveau dépôt nommé par exemple `pont-mobile-java`.
2. Ajouter une courte description : `Simulation Java concurrente d’un pont mobile, de véhicules et de bateaux`.
3. Choisir la visibilité demandée par l’enseignant ou le binôme.
4. Créer le dépôt vide, sans README ni fichiers générés automatiquement, afin d’y déposer les fichiers de ce kit.
5. Dans les paramètres du dépôt, ajouter le collègue comme collaborateur avec les droits nécessaires pour contribuer au code.

Le nom `pont-mobile-java` est une suggestion pour le dépôt GitHub. Il ne remplace pas la consigne de nommage du dossier final : `Nom1_Nom2`.

## 2. Ajouter les fichiers de départ

Copier à la racine du dépôt :

- `README.md`
- `.gitignore`
- `CONTRIBUTING.md`
- le dossier `docs/`
- le dossier `.github/`

Puis, dans un terminal ouvert à la racine du dossier :

```bash
git init
git add README.md .gitignore CONTRIBUTING.md docs .github
git commit -m "chore: initialiser le dépôt du projet"
git branch -M main
git remote add origin URL_DU_DEPOT_GITHUB
git push -u origin main
```

Remplacer `URL_DU_DEPOT_GITHUB` par l’adresse affichée par GitHub pour votre dépôt.

## 3. Créer le suivi des tâches

1. Ouvrir l’onglet **Issues** et activer son utilisation si nécessaire.
2. Créer les Issues listées dans [`TACHES_GITHUB.md`](TACHES_GITHUB.md). Copier le titre et la description de chaque tâche ; attribuer un responsable parmi les deux membres.
3. Créer les étiquettes proposées dans ce même document, puis les appliquer aux Issues.
4. Dans **Projects**, créer un tableau lié au dépôt avec les colonnes `À faire`, `En cours`, `À relire` et `Terminé`.
5. Ajouter les Issues au tableau et les déplacer au fur et à mesure. Une tâche ne passe en `Terminé` qu’après intégration dans `main`.

Si vous n’utilisez pas GitHub Projects, les Issues et leurs états suffisent pour suivre le travail.

## 4. Règle de travail à deux

- Garder `main` pour le code intégré.
- Créer une branche par tâche, par exemple `feature/etats-pont`, `feature/vehicules-segments` ou `feature/file-bateaux`.
- Ouvrir une pull request vers `main` quand une tâche est prête ; le collègue la relit avant fusion.
- Se mettre d’accord ensemble sur les interfaces partagées avant que les deux parties du programme soient développées en parallèle : notamment l’état du pont, les segments, le sens de circulation et les points d’entrée des threads.
- Mettre à jour les Issues et le tableau après chaque séance de travail.

## 5. Contrôle de fin de préparation

- [ ] Le dépôt GitHub existe et les deux membres y ont accès.
- [ ] `README.md`, `.gitignore`, `CONTRIBUTING.md` et `docs/` sont présents.
- [ ] Les tâches du projet sont suivies par des Issues.
- [ ] Le tableau de suivi est prêt, si le binôme choisit d’utiliser GitHub Projects.
- [ ] Chaque membre sait créer une branche, pousser ses commits et ouvrir une pull request.
