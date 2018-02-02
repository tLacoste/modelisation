# Modélisation

## Auteurs:

  * **Thierry Lacoste** - *alias Zhord* [github](https://github.com/tLacoste)
  * **Marvin Fornito** - [github](https://github.com/fornito2u)

## Première partie:
  
  * Travail effectué:
    * writepgm : Thierry Lacoste
    * interest : Thierry Lacoste
    * tograph :  Thierry Lacoste
    * reduceImage : Thierry Lacoste
    * Dijkstra : 
      * Réalisation d'un Dijkstra sans heap *disponible sur git*: Marvin Fornito
      * Réalisation du Dijkstra avec heap: Thierry Lacoste
    * Commentaires : Marvin Fornito et Thierry Lacoste
    * Test dijsktra : Marvin Fornito
  * Spécificités :
      * tographWithIntensity :
        * Créer un graph comme tograph mais n'utilise plus l'intérêt des pixel mais leur intensité
        * Par : Thierry Lacoste
      * tographLineWithIntensity :
        * Créer un graphe comme tographWithIntensity mais où chaque niveau de noeud n'est plus une ligne mais une colonne 
        * Par : Thierry Lacoste
      * tographLine :
        * Créer un graphe où chaque niveau de noeud n'est plus une ligne mais une colonne
        * Par : Thierry Lacoste
      * interestLine :
        * Fonctionne de la même façon que interest mais permet de calculer l'intérêt des pixel pour une suppression de ligne
        * Par : Thierry Lacoste
      * reduceImageLine :
        * Fonctionne de la même façon que reduceImage mais pour gérer la suppression d'une ligne
        * Par : Thierry Lacoste
  * Modifications par rapport au sujet :
        * Le logiciel se lance avec 4 arguments au lieu d'un seul
        * La méthode tograph n'a pas comme argument le tableau d'intérêt de chaque pixel mais le tableau des pixels de l'image
  * Utilisation du logiciel :
        * Lancer le Launcher.java avec 4 arguments:
  ```
<nom_fichier_a_reduire>.pgm <nom_fichier_destination>.pgm <nombre_ligne_suppr> <nombre_colonnes_suppr>
  ```
