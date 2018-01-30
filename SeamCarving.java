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
	
	/**
	 * Renvoit le chemin le plus cour d'un graphe g entre les sommets s et t
	 * @param g
	 * 			Graphe
	 * @param s	
	 * 			Sommet de depart
	 * @param t
	 * 			Sommet d'arrive
	 * @return
	 * 		   Chemin le plus court sous la forme d'un tableau (suite de sommet)
	 */
	public int[] Dijsktra(Graph g, int s, int t) 
	{
		//							Partie 1
		//-------------------------------------------------------------------
		// Tableau inverse a celui a retourner a la fin de la methode
		int[] cheminPlusCour = null;
		// Parcour de tous les sommets possible depuis celui de depart
		Test.dfs(g, s); 
		// Verification qu'un chemin existe entre les deux sommets
		if(s > t || Test.visite[t] != true)
		{
			System.out.println("Erreur : Il n'existe aucun chemin entre les deux sommets"); 
		}
		// Si le noeud de depart et d'arrive sont identique
		if(s == t)
		{
			cheminPlusCour[0] = s;
			return cheminPlusCour;
		}
		
		//						    Partie 2
		//---------------------------------------------------------------------
		// Tableau a 3 colonnes : Numero du noeud (Colonne 0), Poids du noeud = Interet du pixel (Colonne 1), Boolean "Deja Parcouru ?" (Colonne 2)
		String[][] tableauPoids = null; 
		// Tableau a 2 colonnes : Numero du noeud (Colonne 0), Numero de l'antecedent (Colonne 1)
		String[][] tableauAntecedents = null;
		int i; // Represente la colonnes actuel du tableau : 0, 1 ou 2
		int j; // Représente le noeud du graphe pour chaque colonne du tableau
		int arg = 0;  // Variable permettant de passer facilement d'un entier a un string
		// Parcour des 3 colonnes (seulement 2 pour le tableau des antecedents)
		for(i = 0; i < 3; ++i)
		{
			// Initialisation du tableau des poids et du tableau des antecedents
			for(j = 0; j < g.getAdj().length-1; ++j) // j doit correspondre au numero du noeud dans le tableau adj de la classe Graph
			{
				// Creation de la colonne 0 pour les deux tableaux
				if(i == 0)
				{
					arg = j + 1;
					// Tableau des poids colonne 0
					tableauPoids[i][j] = Integer.toString(arg);
					// Tableau des antecedents colonne 0
					tableauAntecedents[i][j] = Integer.toString(arg);
				}
				// Creation de la colonne 1 pour les deux tableaux
				if (i == 1)
				{
					// Tableau des poids colonne 1
					if(j != s)
					{
						arg = -1; // Cas ou la case de la colonne 1 n'est pas la case de depart => Poid negatif
					}
					else
					{
						arg = 0; // Cas ou la case de la colonne 1 correspond au noeud de depart => Poids de 0
					}
					tableauPoids[i][j] = Integer.toString(arg);
					// Tableau des antecedents colonne 1
					tableauAntecedents[i][j] = "Aucun";
				}
				// Creation de la colonne 2 pour le tableau des poids
				if(i == 2)
				{
					tableauPoids[i][j] = "Non";
				}
			}
		}
		
		//								Partie 3
		//------------------------------------------------------------------
		// On recherche le noeud non parcouru ayant le poid le plus faible, puis on le parcour
		int indiceNoeudActuel = s; // Initialisation de l'indice du noeud actuel au noeud de depart
		ArrayList<Edge> listeEdgeNoeudActuel = null; // Initialisation. Correspond a la liste des Edges liee au noeudActuel
		int[] filsNoeudActuel = null; // Liste des fils du noeud actuel, utile pour la partie 4		
		int[] costEdgePereFils = null; // List des couts des noeuds entre le noeud actuel et son fils. Va en paralelle avec le tableau precedent.
		int pos = 0; // Position dans le tableau fulsNoeudActuel
		i = 1; // On va parcourir la colonne 1 du tableau des poids
		int poidPlusFaible = 0; // Variable de stockage du poid le plus faible
		int indiceNoeud = -1; // Variable de stockage de l'indice du noeud de poid le plus faible = Numero du noeud dans la colonne 0
		boolean finit = false; // Termine la boucle while quand le noeud de poids le plus faible est le noeud d'arrive
		
		while(finit == false)
		{
			////////BOUCLE TANT QUE/////////
			for (j = 0; j < g.getAdj().length-1; ++j)
			{
				if (Integer.parseInt(tableauPoids[i][j]) <= poidPlusFaible  // Trouver le poid le plus faible, poid de 0 = noeud de depart
					&& Integer.parseInt(tableauPoids[i][j]) != -1 // Poid different de -1 => pour eviter qu'un poids negatif soit considerer comme le poids le plus faible  
					&& tableauPoids[2][j] == "Non") // Uniquement les noeuds non parcouru
				{
					poidPlusFaible = Integer.parseInt(tableauPoids[i][j]);
					indiceNoeud = j + 1 ; // j + 1 car j commence a 0 et on veut que j corresponde au numero du noeud 
				}
			}
			tableauPoids[2][indiceNoeud] = "Oui"; // On parcour le noeud non parcouru de poids le plus faible
			// On s'arrete si le noeud d'arrive est parcouru
			if(indiceNoeud == t)
			{
				finit = true;
				break;
			}
			//								Partie 4
			//------------------------------------------------------------------------
			// Recherche et changement du poid des fils du noeud actuel
			indiceNoeudActuel = indiceNoeud; // Mise a jour de l'indice du noeud actuel
			listeEdgeNoeudActuel = g.adjBis(indiceNoeudActuel); // noeudActuel prend la valeur de la liste des Edges lie a ce noeud
			// On doit trouver tous les fils du noeudActuel, on regarde chaque edge lies au noeud actuel
			for(Edge e : listeEdgeNoeudActuel)
			{
				// Si le noeud actuel a un edge d'on le point de depart ("From") est lui-meme = si le noeud actuel a un fils
				if(e.getFrom() == indiceNoeudActuel)  
				{
					filsNoeudActuel[pos] = e.getTo(); // On stock l'indice du fils du noeudActuel
					costEdgePereFils[pos] = e.getCost(); // On stock le poid du Edge entre le pere et le fils
					pos += 1; // Incremente la position pour le prochaine ajout
				}
			}
			
			// Pour chaque fils du noeud actuel
			for(int k = 0; k < filsNoeudActuel.length; ++k)
			{
				int poidsNoeudPere = Integer.parseInt(tableauPoids[1][indiceNoeudActuel]); // On stock le poids du noeud pere/actuel pour y acceder plus facilement
				int poidsLiaisonPereFils = costEdgePereFils[k]; // On stock le poid du Edge entre le pere et le fils
				// Condition :
				// Si le noeud fils na pas encore etait parcouru
				// ET Si 
				//  	.Soit la distance pour aller du depart au noeud fils en passant par le noeud pere 
				// 		 est plus petite que la distance pour aller du depart au noeud fils en passant par 
				//		 un autre chemin
				//      . Soit le poid du noeud fils est = a -1
				if(tableauPoids[2][filsNoeudActuel[k]] != "Oui")
				{
					if(((poidsNoeudPere + poidsLiaisonPereFils) < Integer.parseInt(tableauPoids[1][filsNoeudActuel[k]])) 
					     || (tableauPoids[1][filsNoeudActuel[k]] == Integer.toString(-1)) )
					{
						// Le poids du fils prend la valeur du chemin du debut au fils en passant par le pere
						tableauPoids[1][filsNoeudActuel[k]] = Integer.toString(poidsNoeudPere + poidsLiaisonPereFils);
						// On indique que l'antecedent du noeud fils est le noeud pere
						tableauAntecedents[1][filsNoeudActuel[k]] = Integer.toString(indiceNoeudActuel);
					}			
				}
			}
		}
				
		//								Partie 5
		//------------------------------------------------------------------------
		// Construction du tableau du chemin  le plus cour via le tableau des antecedents
		boolean stop = false;
		String antecedentActuel = tableauAntecedents[1][t]; // Indice antecedent actuel a ranger dans le tableau
		int compteurAntecedent = 0; // Permet de designer l'indice du tableau a return et de compter les antecedents (-1)
		cheminPlusCour[compteurAntecedent] = Integer.parseInt(antecedentActuel);
		// Temps que le tableau n'est pas remplis
		while(stop == false)
		{
			// Si l'antecedent actuel est le noeud de depart
			if(Integer.parseInt(antecedentActuel) == s)
			{
				stop = true;
				break;
			}
			antecedentActuel = tableauAntecedents[1][Integer.parseInt(antecedentActuel)];
			compteurAntecedent += 1;
			cheminPlusCour[compteurAntecedent] = Integer.parseInt(antecedentActuel);
		}
		
		// On doit envoyer le tableau inverse a celui construit car on la construit 
		// en partant du noeud d'arrive via les antecedents
		int[] cheminPlusCourFinal = null; // Tableau d'entier a rendre
		int compteurInverse = 0; // Permet de parcourir le tableau a rendre
		for(int m = cheminPlusCour.length - 1; m >= 0; --m) // Parcour inverse du tableau construit
		{
			cheminPlusCourFinal[compteurInverse] = cheminPlusCour[m];
			++compteurInverse;
		}
		return cheminPlusCourFinal;
	}
	
	
}
