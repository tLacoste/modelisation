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
	  * reduceImageSuurballe :
        * Fonctionne de la même façon que reduceImage mais utilise l'algorithme de Suurballe
        * Par : Thierry Lacoste
	  * _reduceImageWidth :
        * Gère la suppression des colonnes d'une image lorsque le programme connait les cellules à supprimer
        * Par : Thierry Lacoste
	  * _reduceImageHeight :
        * Gère la suppression des lignes d'une image lorsque le programme connait les cellules à supprimer
        * Par : Thierry Lacoste
	  * extendImage :
        * Gère l'agrandissement d'une image
        * Par : Thierry Lacoste
	  * _extendImage :
        * Sous fonction de extendImage
        * Par : Thierry Lacoste
	  * increaseInterest :
	    * Permet de diminuer ou d'augmenter la valeur d'intérêt d'un ensemble de cases
		* Par : Thierry Lacoste
  * Modifications par rapport au sujet :
	  * possibilité de créer le graph de la première partie par intensité
	  * possibilité d'augmenter ou diminuer l'intérêt d'un carré de pixel pour les graphes avec intérêt
	  * possibilité d'augmenter la taille d'une image en colonne. ( Fonctionne pour le test.pgm, à débug)
	  * possibilité de choisir l'utilisation de l'intensité plutôt que de l'intérêt (hormis pour Suurballe)
	  * possibilité de diminuer l'image par ligne
  * Utilisation du logiciel :
      * Lancer le Launcher.java avec 2 arguments minimum:
  ```
<nom_fichier_a_reduire>.pgm <nom_fichier_destination>.pgm 
  ```
Options supplémentaires:
  ```
<nombre_de_pixel_a_modifier> <supprimer des lignes (true), des colonnes (FALSE)> <agrandir l'image ? (true/FALSE)> <utiliser l'intensite ? (true/FALSE)> <x haut gauche carré à augmenter/diminuer intérêt> <y haut gauche carré à augmenter/diminuer intérêt> <x bas droite carré à augmenter/diminuer intérêt> <y bas droite carré à augmenter/diminuer intérêt> <Augmenter/diminuer intérêt (TRUE/false)>
  ```