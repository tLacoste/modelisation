
import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{

	/**
	 * M�thode readpgm
	 * Permet de lire un fichier .pgm
	 * @param fn le nom du fichier
	 * @return renvoie un tableau de pixels
	 */
	public static int[][] readpgm(String fn)
	{		
		try {
			InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
			BufferedReader d = new BufferedReader(new InputStreamReader(f));
			String magic = d.readLine();
			String line = d.readLine();
			while (line.startsWith("#")) {
				line = d.readLine();
			}
			Scanner s = new Scanner(line);
			int width = s.nextInt();
			int height = s.nextInt();		   
			line = d.readLine();
			s = new Scanner(line);
			int maxVal = s.nextInt();
			int[][] im = new int[height][width];
			s = new Scanner(d);
			int count = 0;
			while (count < height*width) {
				im[count / width][count % width] = s.nextInt();
				count++;
			}
			return im;
		}

		catch(Throwable t) {
			t.printStackTrace(System.err) ;
			return null;
		}
	}

	/**
	 * M�thode writepgm
	 * Permet d'ecrire un fichier .pgm
	 * @param image tableau des pixels de l'image
	 * @param filename nom du fichier cree
	 */
	public static void writepgm(int[][] image, String filename) {
		// Déclaration des variables
		FileWriter fw = null;
		BufferedWriter bw = null;
		int imageHeight = image.length;
		int imageWidth = image[0].length;
		try {
			// Ouverture du fichier
			fw = new FileWriter(filename);
			// Ouverture du buffer
			bw = new BufferedWriter(fw);
			// Ecriture du type de fichier
			bw.write("P2\n");
			// Ecriture du moyen de création du fichier (facultatif)
			bw.write("#File written by SeamCarving.java\n");
			// Ecriture de la largeur et hauteur du fichier
			bw.write(imageWidth+" "+imageHeight+"\n");
			// Ecriture de la valeur maximum de la couleur
			bw.write("255\n");
			// Ecriture du tableau de pixels
			for(int y=0;y<imageHeight;y++) {
				for(int x=0;x<imageWidth;x++) {
					// Ecriture de la valeur du pixel
					bw.write(image[y][x]+" ");
				}
				// Retour à la ligne
				bw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}  

	/**
	 * Methode interest
	 * Permet d'avoir le facteur d'interet de chacun des pixels du tableau
	 * @param image tableau des pixels de l'image
	 * @return un tableau de valeur de facteur d'interet de chaque pixel
	 */
	public static int[][] interest(int[][] image){
		// Hauteur de l'image
		int imageHeight = image.length;
		// Largeur de l'image
		int imageWidth = image[0].length;
		// Tableau contenant le facteur d'intérêt de chaque pixel
		int[][] tabFacteurInteret = new int[imageHeight][imageWidth];
		// Parcours du tableau de l'image
		for(int y=0; y<imageHeight; y++) {
			for(int x=0; x<imageWidth; x++) {
				// Variable stockant la valeur des voisins du pixel
				int moyVoisinInteret = 0;
				if(x==0) { 
					// Si un pixel n'a pas de voisin de gauche
					moyVoisinInteret= image[y][x+1];
				}else if(x == imageWidth-1) { 
					// Si un pixel n'a pas de voisin de droite
					moyVoisinInteret = image[y][x-1];
				}else { 
					// Si un pixel a un voisin de gauche et un voisin de droite
					moyVoisinInteret = (image[y][x-1]+image[y][x+1])/2;
				}
				// Calcul du facteur d'int�r�t du pixel et ajout au tableau.
				tabFacteurInteret[y][x] = Math.abs(image[y][x] - moyVoisinInteret);
			}
		}
		return tabFacteurInteret;
	}

	/**
	 * Creer le graphe correspondant au tableau fournit en parametre
	 * @param image
	 * 				Tableau repr�sentant l'image
	 * @return g
	 * 			Graphe correspondant au tableau image
	 */
	public static Graph tograph(int[][] image) {
		int[][] itr = interest(image);
		int itrWidth = itr[0].length;
		int itrHeight = itr.length;
		// Nombre de sommets
		int nbSommets= itrWidth*itrHeight+2;
		// Instanciation du Graph
		Graph g = new Graph(nbSommets);

		// Création des arêtes du premier sommet
		for(int x=0; x<itrWidth; x++) {
			g.addEdge(new Edge(0, x+1, 0));
		}
		// Calcul du dernier sommet
		int dernierSommet = itrHeight*itrWidth +1;
		
		// Création des arêtes des sommets représentant les pixels
		for(int y=0; y<itrHeight; y++) {
			for(int x=0; x<itrWidth; x++) {
				// Facteur d'int�r�t du pixel actuel
				int facteurInteret = itr[y][x];

				int from = (y*itrWidth)+x+1;
				int to = ((y+1)*itrWidth)+x+1;

				// Si on n'est pas sur la dernière ligne
				if(y != itrHeight-1) {
					// Si le pixel a un voisin de gauche
					if(x!= 0) {
						g.addEdge(new Edge(from, to-1, facteurInteret));
					} // Si le pixel a un voisin de droite
					if(x!=itrWidth-1) {
						g.addEdge(new Edge(from, to+1, facteurInteret));
					}
				}else {
					// Sinon si on est bien à la dernière ligne
					// on change la destination
					// pour quelle corresponde au dernier sommet
					to= dernierSommet;
				}

				// Dans tous les cas on relie le pixel à celui d'en dessous
				g.addEdge(new Edge(from, to, facteurInteret));
			}
		}
		return g;
	}
	
	/**
	 * Creer le graphe correspondent au tableau fournit en paramettre
	 * @param image
	 * 				Tableau repr�sentant l'image
	 * @return g
	 * 				Graphe correspondent au tableau image
	 */
	public static Graph tographWithIntensity(int[][] image) {
		int imageWidth = image[0].length;
		int imageHeight = image.length;
		// Nombre de sommets
		int nbSommets= imageWidth*imageHeight+2;
		// Instanciation du Graph
		Graph g = new Graph(nbSommets);

		// Création des arêtes du premier sommet
		for(int x=0; x<imageWidth; x++) {
			g.addEdge(new Edge(0, x+1, 0));
		}
		// Calcul du dernier sommet
		int dernierSommet = imageHeight*imageWidth +1;
		
		// Création des arêtes des sommets représentant les pixels
		for(int y=0; y<imageHeight; y++) {
			for(int x=0; x<imageWidth; x++) {
				// Intensité du pixel actuel
				int intensity = 0;
				
				int from = (y*imageWidth)+x+1;
				int to = ((y+1)*imageWidth)+x+1;

				// Si on n'est pas sur la dernière ligne
				if(y != imageHeight-1) {
					// Si le pixel a un voisin de gauche
					if(x!= 0) {
						g.addEdge(new Edge(from, to-1, Math.abs(image[y][x-1] - image[y+1][x])));
					}
					// Si le pixel a un voisin de droite
					if(x!=imageWidth-1) {
						g.addEdge(new Edge(from, to+1, Math.abs(image[y][x+1]-image[y+1][x])));
					}
				}else {
					// Sinon si on est bien à la dernière ligne
					// on change la destination
					// pour quelle corresponde au dernier sommet
					to= dernierSommet;
				}
				
				/* Calcul de l'intensité */
				if(x== 0) {
					intensity = image[y][x+1];
				}else if(x==imageWidth-1){
					intensity = image[y][x-1];
				}else{
					intensity = image[y][x+1] - image[y][x-1];
				}

				// Dans tous les cas on relie le pixel à celui d'en dessous
				
				g.addEdge(new Edge(from, to, Math.abs(intensity)));
			}
		}
		return g;
	}

	
	public int[][] reduceImage(int[][] image, boolean useIntensity){
		// Déclaration de la valeur signifiant la suppression du pixel
		int REMOVE_CELL = -1;
		// Calcul de l'intérêt des pixels
		int imageWidth = image[0].length;
		int imageHeight = image.length;
		// Déclaration de notre nouvelle image
		int[][] imageReduced = new int[imageHeight][imageWidth-1];
		// Création du graphe
		Graph g;
		if(useIntensity) {
			g= tographWithIntensity(image);
		}else {
			g = tograph(image);
		}

		/* Récupération de la colonne à supprimer */
		int premierSommet = 0;
		int dernierSommet = imageWidth*imageHeight+1;
		ArrayList<Integer> cheminInteretMoindre = Dijsktra(g, premierSommet, dernierSommet);

		// Suppression du premier sommet qui était fictif
		cheminInteretMoindre.remove(0);
		// Suppression du dernier sommet qui était fictif
		cheminInteretMoindre.remove(cheminInteretMoindre.size()-1);

		/* Suppression des pixels de moindre interet */
		for(Integer sommet: cheminInteretMoindre) {
			// Diminution des sommets car le premier sommet fictif
			// Prenait la cellule 0
			sommet --;
			// Calcul du x et y du pixel
			int x = (sommet%imageWidth);
			int y = (sommet/imageWidth);
			image[y][x]=REMOVE_CELL;
		}

		/* Création de la nouvelle image */
		for(int y=0; y< imageHeight; y++ ) {
			for(int x=0, xImageReduced=0; x< imageWidth; x++,xImageReduced++ ) {
				// Valeur du pixel de l'ancienne image
				int val = image[y][x];

				// Si le pixel doit être retiré
				if(val==REMOVE_CELL) {
					// On n'affecte pas la valeur dans imageReduced
					// et on repositionne notre curseur
					xImageReduced--;
				}else {
					// On affecte la valeur du pixel dans notre nouvelle image
					imageReduced[y][xImageReduced] = val;
				}
			}
		}
		return imageReduced;
	}

	/**
	 * Reduit la taille de l'image en limitant les changements visuels
	 * @param fileSrc
	 * @param fileDest
	 */
	public SeamCarving(String fileSrc, String fileDest) {
		// Récupération des pixels de l'image
		int[][] image = readpgm(fileSrc);
		int nbColumnToRemove = 50;
		int nbLineToRemove = 50;
		
		/* Vérification de la taille du fichier */
		// S'il n'y a pas assez de pixels en largeur
		if(image[0].length<=nbColumnToRemove) {
			System.out.println("Largeur de l'image insuffisante");
			System.exit(1);
		}
		// S'il n'y a pas assez de pixels en hauteur
		if(image.length<=nbLineToRemove) {
			System.out.println("Hauteur de l'image insuffisante");
			System.exit(1);
		}
		// Boucle de suppression des 50 colonnes
		for(int i =0; i<nbColumnToRemove; i++) {
			image = reduceImage(image, false);
		}
		
		// Ecriture de la nouvelle image dans un nouveau fichier
		writepgm(image, fileDest);
		System.out.println("Terminé");
	}
	
	/**
	 * Algorithme de recherche de chemin le plus cour entre 2 sommets
	 * @param g
	 * 			Graphe fournit 
	 * @param s
	 * 			Sommmet de d�part
	 * @param t	
	 * 			Sommet d'arriv�
	 * @return
	 * 			Chemin rendu
	 */
	public static ArrayList<Integer> Dijsktra(Graph g, int s, int t) {
		int nbVertices = g.vertices();

		// Au départ on insère tous les noeuds dans le tas avec priorité +infini
		Heap h = new Heap(nbVertices);
		// Sauf le noeud de départ qui a une priorité de 0
		h.decreaseKey(s, 0);

		// On déclare notre chemin
		ArrayList<Integer> chemin = new ArrayList<Integer>();

		// On déclare notre tableau des sommets visités
		boolean[] visited = new boolean[nbVertices];
		// On déclare notre tableau contenant les parents de chaque sommet
		// Ceci nous permettra de retrouver le chemin de cout minimum une fois la fonction terminée
		int[] parent = new int[nbVertices];
		// Initialisation de notre tableau visited à 0
		// Initialisation de notre tableau à -1
		for(int i =0; i<nbVertices; i++) {
			visited[i] = false;
			parent[i] = -1;
		}

		int sommetPrioriteMin=-1;
		while(sommetPrioriteMin != t) {
			// On retire le noeud de priorité minimum.
			sommetPrioriteMin = h.pop();

			// On déclare que le sommet a été visité
			visited[sommetPrioriteMin] = true;

			/* On met à jour ses voisins */
			// Pour chaque successeur
			for(Edge e: g.next(sommetPrioriteMin)) {
				// Si le voisin n'a pas déjà été visité
				if(!visited[e.to]) {
					// On récupère la priorité actuelle du sommet retiré
					int prioriteSommet = h.priority(e.from);
					// On récupère la priorité actuelle du voisin
					int prioriteVoisin = h.priority(e.to);

					// Calcul de la potentielle nouvelle priorité
					int nouvellePriorite = prioriteSommet+e.cost;

					// Si la nouvelle priorité est plus faible que l'ancienne
					if(nouvellePriorite < prioriteVoisin) {
						// On met à jour la priorité de ce voisin
						h.decreaseKey(e.to, nouvellePriorite);
						// On met à jour le père de ce voisin
						parent[e.to] = e.from;
					}
				}
			}
		}

		int p=t;
		while(p!=parent[s]) {
			chemin.add(p);
			p = parent[p];
		}
		Collections.reverse(chemin);
		return chemin;
	}
}


