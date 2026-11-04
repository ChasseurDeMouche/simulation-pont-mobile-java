# Contribuer au projet

Ce dépôt est travaillé par le binôme du projet. Les deux membres contribuent au code, à sa documentation et à son intégration.

## Organisation Git

- `main` contient une version intégrée du projet.
- Chaque tâche est développée dans une branche dédiée, créée depuis `main` et nommée avec un préfixe explicite, par exemple `feature/segments-vehicules` ou `fix/file-bateaux`.
- Une tâche importante est suivie par une Issue GitHub. L’Issue indique son responsable et ses critères de fin.
- Avant de fusionner, l’auteur pousse sa branche et ouvre une *pull request* vers `main`. Le binôme relit les changements et vérifie que la fonctionnalité s’intègre au sujet.
- Après fusion, la branche de travail peut être supprimée. Les deux membres récupèrent ensuite la version à jour de `main`.

## Commits

Faire des commits courts qui décrivent une modification précise. Exemples :

- `feat: ajouter la file FIFO des bateaux`
- `fix: libérer les segments après la traversée`
- `docs: préciser les règles du convoi exceptionnel`

Ne pas ajouter les fichiers compilés `.class`, les fichiers temporaires ou les paramètres personnels d’un IDE. Le fichier `.jar` de livraison reste conservable dans le dossier final.

## Avant une pull request

- Relire les règles du sujet concernées par la modification.
- Vérifier que les commentaires des classes et des méthodes modifiées sont présents et compréhensibles.
- Décrire dans la pull request ce qui a changé et les situations vérifiées.
- Signaler toute règle encore incomplète au lieu de la marquer comme terminée.
