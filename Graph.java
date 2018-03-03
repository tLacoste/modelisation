import java.util.ArrayList;
import java.awt.Point;
import java.io.*;

public class Graph
{
   private ArrayList<Edge>[] adj;
   private final int V;
   int E;
@SuppressWarnings("unchecked")
   public Graph(int N)
	 {
		this.V = N;
		this.E = 0;
		 adj = (ArrayList<Edge>[]) new ArrayList[N];
		for (int v= 0; v < N; v++)
		  adj[v] = new ArrayList<Edge>();
		
	 }

   public int vertices()
	 {
		return V;
	 }
   
   public void addEdge(Edge e)
	 {
		int v = e.from;
		int w = e.to;
		adj[v].add(e);
		adj[w].add(e);
	 }
   
   public Iterable<Edge> adj(int v)
	 {
		return adj[v];
	 }     

   public Iterable<Edge> next(int v)
	 {
		ArrayList<Edge> n = new ArrayList<Edge>();
		for (Edge e: adj(v))
		  if (e.to != v)
			n.add(e);
		return n;
	 }      
   
   public Iterable<Edge> edges()
	 {
		ArrayList<Edge> list = new ArrayList<Edge>();
        for (int v = 0; v < V; v++)
            for (Edge e : adj(v)) {
                if (e.to != v)
                    list.add(e);
            }
        return list;
    }
   
   
   public void writeFile(String s)
	 {
		try
		  {			 
			 PrintWriter writer = new PrintWriter(s, "UTF-8");
			 writer.println("digraph G{");
			 for (Edge e: edges())
			   writer.println(e.from + "->" + e.to + "[label=\"" + e.cost + "\"];");
			 writer.println("}");
			 writer.close();
		  }
		catch (IOException e)
		  {
		  }						
	 }
   
   /**
    * Créé le graph à la façon Suurballe
    * @param image tableau de pixels
    * @return le graph correspondant
    */
   public static Graph tographSuurballe(int[][] image) {
		int[][] itr = interest(image);
		// Hauteur de l'image
		int imageHeight = image.length;
		// Largeur de l'image
		int imageWidth = image[0].length;
		// Nombre de sommets
		// Toutes les lignes de pixels ont deux sommets sauf la première et la dernière, 
		// - on fait l'addition du double de toutes les lignes du milieu plus les deux extrémitées
		// - on multiplie cela par la largeur
		// - et on ajoute les deux sommets fictifs
		// int nbSommets= ((imageHeight - 2) * 2 + 2) * imageWidth + 2;
		int nbSommets = ((imageHeight-2)*2 +2)*imageWidth +2;
		// Instanciation du Graph
		Graph g = new Graph(nbSommets);

		// Création des arêtes du premier sommet
		for(int x=0; x<imageWidth; x++) {
			g.addEdge(new Edge(0, x+1, 0));
		}
		// Calcul du dernier sommet
		int dernierSommet = nbSommets-1;
		
		// Création des arêtes des sommets représentant les pixels
		for(int y=0; y<imageHeight; y++) {
			for(int x=0; x<imageWidth; x++) {
				// Facteur d'int�r�t du pixel actuel
				int facteurInteret = itr[y][x];

				int from = ((y*2-1)*imageWidth)+x+1;
				int to = from+imageWidth;

				if(y == 0 ) {
					from = x+1;
					to = from+imageWidth;
				}else {
					if(y < imageHeight -1) {
						g.addEdge(new Edge(from, to, 0));
						//System.out.println(from+"->"+to+"[label=0]");
						from = to;
						to = to+imageWidth;
					}
				}

				// Si on n'est pas sur la dernière ligne
				if(y < imageHeight-1) {
					// Si le pixel a un voisin de gauche
					if(x!= 0) {
						g.addEdge(new Edge(from, to-1, facteurInteret));
						//System.out.println(from+"->"+(to-1)+"[label="+facteurInteret+"]");
					} // Si le pixel a un voisin de droite
					if(x<imageWidth-1) {
						g.addEdge(new Edge(from, to+1, facteurInteret));
						//System.out.println(from+"->"+(to+1)+"[label="+facteurInteret+"]");
					}
				}else {
					// Sinon si on est bien à la dernière ligne
					// on change la destination
					// pour quelle corresponde au dernier sommet
					to= dernierSommet;
				}

				// Dans tous les cas on relie le pixel à celui d'en dessous
				g.addEdge(new Edge(from, to, facteurInteret));
				//System.out.println(from+"->"+to+"[label="+facteurInteret+"]");
			}
		}
		return g;
	}
   
   /**
	 * Transforme une image sous forme de tableau en un graph
	 * @param image tableau de pixels
	 * @param topLeft point du carré en haut à gauche à garder, supprimer ou inexistant
	 * @param bottomLeft point du carré en bas à droite à garder, supprimer ou inexistant
	 * @return le graph correspondant
	 */
	public static Graph tograph(int[][] image, Point topLeft, Point bottomRight, boolean increaseInteret) {
		int[][] itr = interest(image);
		itr = increaseInterest(itr, topLeft, bottomRight, increaseInteret);
		// Hauteur de l'image
		int imageHeight = image.length;
		// Largeur de l'image
		int imageWidth = image[0].length;
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
				// Facteur d'int�r�t du pixel actuel
				int facteurInteret = itr[y][x];

				int from = (y*imageWidth)+x+1;
				int to = ((y+1)*imageWidth)+x+1;

				// Si on n'est pas sur la dernière ligne
				if(y != imageHeight-1) {
					// Si le pixel a un voisin de gauche
					if(x!= 0) {
						g.addEdge(new Edge(from, to-1, facteurInteret));
					} // Si le pixel a un voisin de droite
					if(x!=imageWidth-1) {
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
    * Créé le graph par lignes
    * @param image tableau de pixels
    * @param topLeft point du carré en haut à gauche à garder, supprimer ou inexistant
    * @param bottomLeft point du carré en bas à droite à garder, supprimer ou inexistant
    * @return le graph correspondant
    */
	public static Graph tographLine(int[][] image, Point topLeft, Point bottomRight, boolean increaseInteret) {
		int[][] itr = interestLine(image);
		itr = increaseInterest(itr, topLeft, bottomRight, increaseInteret);
		// Hauteur de l'image
		int imageHeight = image.length;
		// Largeur de l'image
		int imageWidth = image[0].length;
		// Nombre de sommets
		int nbSommets= imageWidth*imageHeight+2;
		// Instanciation du Graph
		Graph g = new Graph(nbSommets);

		// Création des arêtes du premier sommet
		for(int y=0; y<imageHeight; y++) {
			g.addEdge(new Edge(0, y+1, 0));
		}
		// Calcul du dernier sommet
		int dernierSommet = imageHeight*imageWidth +1;
		
		// Création des arêtes des sommets représentant les pixels
		for(int x=0; x<imageWidth; x++) {
			for(int y=0; y<imageHeight; y++) {
				// Facteur d'int�r�t du pixel actuel
				int facteurInteret = itr[y][x];

				int from = (x*imageHeight)+y+1;
				int to = ((x+1)*imageHeight)+y+1;

				// Si on n'est pas sur la dernière ligne
				if(x != imageWidth-1) {
					// Si le pixel a un voisin de haut
					if(y!= 0) {
						g.addEdge(new Edge(from, to-1, facteurInteret));
					} // Si le pixel a un voisin de bas
					if(y!=imageHeight-1) {
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
	 * Passage d'une image au graphe avec intensité
	 * @param image
	 * 				Tableau
	 * @return g
	 * 				Graphe
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
	
	/**
    * Créé le graph par lignes et avec l'intensité
    * @param image tableau de pixels
    * @return le graph correspondant
    */
	public static Graph tographLineWithIntensity(int[][] image) {
		int imageWidth = image[0].length;
		int imageHeight = image.length;
		// Nombre de sommets
		int nbSommets= imageWidth*imageHeight+2;
		// Instanciation du Graph
		Graph g = new Graph(nbSommets);

		// Création des arêtes du premier sommet
		for(int y=0; y<imageHeight; y++) {
			g.addEdge(new Edge(0, y+1, 0));
		}
		// Calcul du dernier sommet
		int dernierSommet = imageHeight*imageWidth +1;
		
		// Création des arêtes des sommets représentant les pixels
		for(int x=0; x<imageWidth; x++) {
			for(int y=0; y<imageHeight; y++) {
				// Intensité du pixel actuel
				int intensity = 0;
				
				int from = (x*imageHeight)+y+1;
				int to = ((x+1)*imageHeight)+y+1;

				// Si on n'est pas sur la dernière ligne
				if(x != imageWidth-1) {
					// Si le pixel a un voisin de gauche
					if(y!= 0) {
						g.addEdge(new Edge(from, to-1, Math.abs(image[y-1][x] - image[y][x+1])));
					}
					// Si le pixel a un voisin de droite
					if(y!=imageHeight-1) {
						g.addEdge(new Edge(from, to+1, Math.abs(image[y+1][x]-image[y][x+1])));
					}
				}else {
					// Sinon si on est bien à la dernière ligne
					// on change la destination
					// pour quelle corresponde au dernier sommet
					to= dernierSommet;
				}
				
				/* Calcul de l'intensité */
				if(y== 0) {
					intensity = image[y+1][x];
				}else if(y==imageHeight-1){
					intensity = image[y-1][x];
				}else{
					intensity = image[y+1][x] - image[y-1][x];
				}

				// Dans tous les cas on relie le pixel à celui d'en dessous
				g.addEdge(new Edge(from, to, Math.abs(intensity)));
			}
		}
		return g;
	}
	
	public static int[][] increaseInterest(int[][] itr, Point topLeft, Point bottomRight, boolean increaseInteret) {
		if(topLeft!=null && bottomRight != null) {
			for(int y = topLeft.y; y<=bottomRight.y; y++) {
				for(int x = topLeft.x; x<=bottomRight.x; x++) {
					itr[y][x] = increaseInteret?Integer.MAX_VALUE:0;
				}
			}
		}
		return itr;
	}
	
	/**
	 * Methode interest
	 * Permet d'avoir le facteur d'interet de chacun des pixels du tableau
	 * @param image tableau des pixels de l'image
	 * @return un tableau de valeur de facteur d'int�r�t de chaque pixel
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
	 * Methode interestLine
	 * Permet d'avoir le facteur d'interet de chacun des pixels du tableau
	 * en regardant par ligne
	 * @param image tableau des pixels de l'image
	 * @return un tableau de valeur de facteur d'int�r�t de chaque pixel
	 */
	public static int[][] interestLine(int[][] image){
		// Hauteur de l'image
		int imageHeight = image.length;
		// Largeur de l'image
		int imageWidth = image[0].length;
		// Tableau contenant le facteur d'intérêt de chaque pixel
		int[][] tabFacteurInteret = new int[imageHeight][imageWidth];
		// Parcours du tableau de l'image
		for(int x=0; x<imageWidth;x++) {
			for(int y=0; y<imageHeight; y++) {
				// Variable stockant la valeur des voisins du pixel
				int moyVoisinInteret = 0;
				if(y==0) { 
					// Si un pixel n'a pas de voisin de haut
					moyVoisinInteret= image[y+1][x];
				}else if(y == imageHeight-1) { 
					// Si un pixel n'a pas de voisin de bas
					moyVoisinInteret = image[y-1][x];
				}else { 
					// Si un pixel a un voisin de haut et un voisin de bas
					moyVoisinInteret = (image[y-1][x]+image[y+1][x])/2;
				}
				// Calcul du facteur d'int�r�t du pixel et ajout au tableau.
				tabFacteurInteret[y][x] = Math.abs(image[y][x] - moyVoisinInteret);
			}
		}
		return tabFacteurInteret;
	}
}
