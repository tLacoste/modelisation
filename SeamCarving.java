import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{

	/**
	 * Méthode readpgm
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
	 * Méthode writepgm
	 * Permet d'écrire un fichier .pgm
	 * @param image tableau des pixels de l'image
	 * @param filename nom du fichier créé
	 */
	public void writepgm(int[][] image, String filename) {
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
					fw.write(image[y][x]+" ");
				}
				// Retour à la ligne
				fw.write("\n");
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
	 * Méthode interest
	 * Permet d'avoir le facteur d'intérêt de chacun des pixels du tableau
	 * @param image tableau des pixels de l'image
	 * @return un tableau de valeur de facteur d'intérêt de chaque pixel
	 */
	public int[][] interest(int[][] image){
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
				// Calcul du facteur d'intérêt du pixel et ajout au tableau.
				tabFacteurInteret[y][x] = Math.abs(image[y][x] - moyVoisinInteret);
			}
		}
		return tabFacteurInteret;
	}

	public Graph tograph(int[][] itr) {
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
		// Création des arêtes des sommets représentant les pixels
		for(int y=0; y<itrHeight; y++) {
			for(int x=0; x<itrWidth; x++) {
				// Facteur d'intérêt du pixel actuel
				int facteurInteret = itr[y][x];

				int from = (y*itrWidth)+x+1;
				int to = ((y+1)*itrWidth)+x+1;

				// Si on n'est pas sur la dernière ligne
				if(y != itrHeight-1) {
					// Si le pixel a un voisin de gauche
					if(x!= 0) {
						g.addEdge(new Edge(from, to+1, facteurInteret));
					} // Si le pixel a un voisin de droite
					if(x!=itrWidth-1) {
						g.addEdge(new Edge(from, to-1, facteurInteret));
					}
				}else {
					// Sinon si on est bien à la dernière ligne
					// on change la destination
					// pour quelle corresponde au dernier sommet
					to= itrWidth*itrHeight+1;
				}

				// Dans tous les cas on relie le pixel à celui d'en dessous
				g.addEdge(new Edge(from, to, facteurInteret));
			}
		}
		// Création des arêtes au dernier sommet
		for(int x=0; x<itrWidth; x++) {
			g.addEdge(new Edge(0, x+1, 0));
		}
		return g;
	}
	
	public int[] Dijsktra(Graph g, int s, int t) {
		return null;
		
	}
}
