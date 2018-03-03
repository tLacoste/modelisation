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
	 * Permet d'�crire un fichier .pgm
	 * @param image tableau des pixels de l'image
	 * @param filename nom du fichier cr��
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
	 * Trouve les deux chemins les plus court
	 * @param g graph 
	 * @param s premier sommet
	 * @param t dernier sommet
	 * @return une liste de sommets à supprimer
	 */
	public LinkedHashSet<Integer> twopath(Graph g, int s, int t) {
		// Recherche du premier chemin
		ArrayList<Integer> cheminInteretMoindre = new ArrayList<Integer>();

		int nbVertices = g.vertices();

		/* Début bellmanford */
		int[] d = new int[nbVertices];
		int[] parent = new int[nbVertices];
		for(int i =0; i<nbVertices; i++) {
			d[i] = Integer.MAX_VALUE;
			parent[i] = -1;
		}

		d[0] = 0;

		for(int u = 0; u<nbVertices; u++) {
			for(Edge e: g.next(u)) {
				if(d[u] + e.cost < d[e.to]) {
					d[e.to] = d[u] + e.cost;
					parent[e.to] = u;
				}
			}
		}
		/* Fin bellmanford */
		// Création du chemin grace au bellmanford
		int i = t;
		while(i != -1) {
			cheminInteretMoindre.add(i);
			i = parent[i];
		}

		// Modification du cout des arêtes
		for(Edge e: g.edges()) {
			e.cost -= d[e.to] -d[e.from];
			if(e.cost < 0)
				e.cost = 0;
			// Inversion des arêtes
			if(cheminInteretMoindre.contains(e.to) && parent[e.to] == e.from) {
				int tmp = e.from;
				e.from = e.to;
				e.to = tmp;
			}
		}

		// Recherche du deuxième chemin
		ArrayList<Integer> deuxiemeCheminInteretMoindre = Dijsktra(g, s, t);
		// Déclaration de la liste des sommets à retirer
		LinkedHashSet<Integer> v = new LinkedHashSet<Integer>();
		for(Integer sommet: cheminInteretMoindre) {
			v.add(sommet);
		}
		for(Integer sommet: deuxiemeCheminInteretMoindre) {
			v.add(sommet);
		}
		return v;
	}
	// Déclaration de la valeur signifiant la suppression du pixel
	private final int REMOVE_CELL = -1;

	/**
	 * Permet à partir d'un tableau contenant les valeurs des pixels
	 * et -1 si le pixel est à enlever, d'enlever les pixels et de créer un nouveau tableau
	 * @param image tableau de pixels contenant -1 si le pixel doit être supprimé
	 * @param amount nombre de colonnes à supprimer
	 * @return le nouveau tableau de pixels
	 */
	private int[][] _reduceImageWidth(int[][] image, int amount){
		int imageWidth = image[0].length;
		int imageHeight = image.length;

		// Création de la nouvelle image
		int[][] imageReduced = new int[imageHeight][imageWidth-amount];

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
	 * Permet à partir d'un tableau contenant les valeurs des pixels
	 * et -1 si le pixel est à enlever, d'enlever les pixels et de créer un nouveau tableau
	 * @param image tableau de pixels contenant -1 si le pixel doit être supprimé
	 * @param amount nombre de lignes à supprimer
	 * @return le nouveau tableau de pixels
	 */
	private int[][] _reduceImageHeight(int[][] image, int amount){
		int imageWidth = image[0].length;
		int imageHeight = image.length;

		// Création de la nouvelle image
		int[][] imageReduced = new int[imageHeight-amount][imageWidth];

		for(int x=0; x< imageWidth; x++ ) {
			for(int y=0, yImageReduced=0; y< imageHeight; y++,yImageReduced++ ) {
				// Valeur du pixel de l'ancienne image
				int val = image[y][x];

				// Si le pixel doit être retiré
				if(val==REMOVE_CELL) {
					// On n'affecte pas la valeur dans imageReduced
					// et on repositionne notre curseur
					yImageReduced--;
				}else {
					// On affecte la valeur du pixel dans notre nouvelle image
					imageReduced[yImageReduced][x] = val;
				}
			}
		}
		return imageReduced;
	}

	/**
	 * Réduit l'image
	 * @param image tableau de pixels à réduire de 2
	 * @param useIntensity utiliser l'intensité ? TODO: Pas encore mis en place
	 * @return l'image réduite
	 */
	public int[][] reduceImageSuurballe(int[][] image, boolean useIntensity){
		// Calcul de l'intérêt des pixels
		int imageWidth = image[0].length;
		int imageHeight = image.length;

		Graph g;
		g = Graph.tographSuurballe(image);

		/* Récupération des extrémités des colonnes à supprimer */
		int premierSommet = 0;
		int dernierSommet =((imageHeight-2)*2 +2)*imageWidth +1;

		// Déclaration de notre nouvelle image
		LinkedHashSet<Integer> twopath = twopath(g, premierSommet, dernierSommet);

		// Suppression du premier sommet qui était fictif
		twopath.remove(premierSommet);
		// Suppression du dernier sommet qui était fictif
		twopath.remove(dernierSommet);



		/* Suppression des pixels de moindre interet */
		for(Integer sommet: twopath) {
			// Diminution des sommets car le premier sommet fictif
			// Prenait la cellule 0
			sommet --;

			// Calcul du x et y du pixel
			int x = (sommet%imageWidth);
			int y = (sommet/imageWidth);
			// Si c'est un sommet qui n'a pas été dupliqué
			if(y==0 || y%2 == 1) {
				// Calcul de la vrai valeur du sommet ( retire les sommets dupliqués)
				sommet -=(int) (Math.floor(Math.abs((sommet/imageWidth-1)/2)))*imageWidth;
				// Calcul de y
				y = (sommet/imageWidth);

				image[y][x]=REMOVE_CELL;
			}

		}

		/* Création de la nouvelle image */
		int[][] imageReduced = _reduceImageWidth(image, 2);

		return imageReduced;
	}

	public int[][] extendImage(int nbPixelToModify, int[][] image, boolean useIntensity){
		int imageWidth = image[0].length;
		int imageHeight = image.length;
		ArrayList<ArrayList<Integer>> save = new ArrayList<>();
		for(int y = 0; y < imageHeight; y++) {
			ArrayList<Integer> a = new ArrayList<>();
			save.add(a);
			for(int x = 0; x<imageWidth; x++) {
				a.add(x);
			}
		}
		int[][] initialImage = new int[imageHeight][imageWidth];
		

		for(int y = 0; y<initialImage.length;y++) {
			for(int x = 0; x<initialImage[0].length; x++) {
				initialImage[y][x]= image[y][x];
			}
		}
		
		
		for(int i =0; i<nbPixelToModify; i++) {
			image = _extendImage(image, save, useIntensity);
		}
		image = new int[imageHeight][imageWidth+nbPixelToModify];
		
		for(int y = 0; y< imageHeight; y++) {
			ArrayList<Integer> a = save.get(y);
			int[] initialLine  = initialImage[y];
			int[] line = image[y];
			for(int x = 0, realX = 0; x< imageWidth; x++, realX ++) {
				int val = initialLine[x];
				line[realX]=val;
				if(a.contains(x)) {
					realX++;
					line[realX]=val;
				}
			}
		}
			
		return image;
	}
	public int[][] _extendImage(int[][] image, ArrayList<ArrayList<Integer>> save, boolean useIntensity){
		// Calcul de l'intérêt des pixels
		int imageWidth = image[0].length;
		int imageHeight = image.length;
		// Création du graphe
		Graph g;
		if(useIntensity) {
			g= Graph.tographWithIntensity(image);
		}else {
			g = Graph.tograph(image);
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

		// Création de la nouvelle image
		int[][] imageReduced = new int[imageHeight][imageWidth-1];

		for(int y=0; y< imageHeight; y++ ) {
			for(int x=0, xImageReduced=0; x< imageWidth; x++,xImageReduced++ ) {
				// Valeur du pixel de l'ancienne image
				int val = image[y][x];

				// Si le pixel doit être retiré
				if(val==REMOVE_CELL) {
					save.get(y).remove(x);
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
	 * R�duit la taille d'une image 
	 * @param image
	 * 				Image
	 * @param useIntensity
	 * 				Intensite
	 * @return imageReduced
	 * 				Image r�duite
	 */
	public int[][] reduceImage(int[][] image, boolean useIntensity){
		// Calcul de l'intérêt des pixels
		int imageWidth = image[0].length;
		int imageHeight = image.length;
		// Création du graphe
		Graph g;
		if(useIntensity) {
			g= Graph.tographWithIntensity(image);
		}else {
			g = Graph.tograph(image);
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
		int[][] imageReduced = _reduceImageWidth(image, 1);
		return imageReduced;
	}

	/**
	 * Réduit une image par ligne
	 * @param image
	 * @param useIntensity
	 * @return une image réduite
	 */
	public int[][] reduceImageLine(int[][] image, boolean useIntensity){
		// Calcul de l'intérêt des pixels
		int imageWidth = image[0].length;
		int imageHeight = image.length;
		// Création du graphe
		Graph g;
		if(useIntensity) {
			g= Graph.tographLineWithIntensity(image);
		}else {
			g = Graph.tographLine(image);
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
			int x = (sommet/imageHeight);
			int y = (sommet%imageHeight);
			image[y][x]=REMOVE_CELL;
		}

		/* Création de la nouvelle image */
		int[][] imageReduced = _reduceImageHeight(image, 1);

		return imageReduced;
	}

	/**
	 * Constructeur
	 * @param fileSrc Fichier source qui sera modifié
	 * @param fileDest Fichier de destination, résultat des modifications du fichier source
	 * @param extendImage Augmenter la taille de l'image ?
	 * @param nbPixelToModify Nombre de pixel à modifier
	 * @param useLine Modifier des lignes et non plus des colonnes
	 * @param useIntensity Utiliser l'intensité pour la création du graphe
	 */
	public SeamCarving(String fileSrc, String fileDest, boolean extendImage, int nbPixelToModify, boolean useLine, boolean useIntensity) {
		// Récupération des pixels de l'image
		int[][] image = readpgm(fileSrc);

		if(extendImage) {
			image = extendImage(2, image, false);
		}
		else if(!useLine) {
			// S'il n'y a pas assez de pixels en largeur
			if(image[0].length<=nbPixelToModify) {
				System.out.println("Largeur de l'image insuffisante");
				System.exit(1);
			}
			// Boucle de suppression des colonnes
			for(int i =0, l = (int) Math.floor(nbPixelToModify/2); i<l; i++) {
				image = reduceImageSuurballe(image, useIntensity);
			}
			for(int i =0; i<nbPixelToModify%2; i++) {
				image = reduceImage(image, useIntensity);
			}
		}else {
			// S'il n'y a pas assez de pixels en hauteur
			if(image.length<=nbPixelToModify) {
				System.out.println("Hauteur de l'image insuffisante");
				System.exit(1);
			}


			// Boucle de suppression des lignes
			for(int i =0; i<nbPixelToModify; i++) {
				image = reduceImageLine(image, useIntensity);
			}
		}

		// Ecriture de la nouvelle image dans un nouveau fichier
		writepgm(image, fileDest);
		System.out.println("Terminé");


	}

	/**
	 * Algorithme de Dijsktra
	 * @param g graphe
	 * @param s premier sommet
	 * @param t dernier sommet
	 * @return le chemin le plus court
	 */
	public static ArrayList<Integer> Dijsktra(Graph g, int s, int t) {
		int nbVertices = g.vertices();

		// Au dÃ©part on insÃ¨re tous les noeuds dans le tas avec prioritÃ© +infini
		Heap h = new Heap(nbVertices);
		// Sauf le noeud de dÃ©part qui a une prioritÃ© de 0
		h.decreaseKey(s, 0);

		// On dÃ©clare notre chemin
		ArrayList<Integer> chemin = new ArrayList<Integer>();

		// On dÃ©clare notre tableau des sommets visitÃ©s
		boolean[] visited = new boolean[nbVertices];
		// On dÃ©clare notre tableau contenant les parents de chaque sommet
		// Ceci nous permettra de retrouver le chemin de cout minimum une fois la fonction terminÃ©e
		int[] parent = new int[nbVertices];
		// Initialisation de notre tableau visited Ã  0
		// Initialisation de notre tableau Ã  -1
		for(int i =0; i<nbVertices; i++) {
			visited[i] = false;
			parent[i] = -1;
		}

		int sommetPrioriteMin=-1;
		while(sommetPrioriteMin != t) {
			// On retire le noeud de prioritÃ© minimum.
			sommetPrioriteMin = h.pop();

			// On dÃ©clare que le sommet a Ã©tÃ© visitÃ©
			visited[sommetPrioriteMin] = true;

			/* On met Ã  jour ses voisins */
			// Pour chaque successeur
			for(Edge e: g.next(sommetPrioriteMin)) {
				// Si le voisin n'a pas dÃ©jÃ  Ã©tÃ© visitÃ©
				if(!visited[e.to]) {
					// On rÃ©cupÃ¨re la prioritÃ© actuelle du sommet retirÃ©
					int prioriteSommet = h.priority(e.from);
					// On rÃ©cupÃ¨re la prioritÃ© actuelle du voisin
					int prioriteVoisin = h.priority(e.to);

					// Calcul de la potentielle nouvelle prioritÃ©
					int nouvellePriorite = prioriteSommet+e.cost;

					// Si la nouvelle prioritÃ© est plus faible que l'ancienne
					if(nouvellePriorite < prioriteVoisin) {
						// On met Ã  jour la prioritÃ© de ce voisin
						h.decreaseKey(e.to, nouvellePriorite);
						// On met Ã  jour le pÃ¨re de ce voisin
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