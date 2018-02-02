import java.util.ArrayList;

class Test
{
   static boolean visite[];
   
   /**
    * Methode de parcour des noeuds du graphe
    * @param g
    * 			Graphe parcouru
    * @param u
    * 			Premier sommet
    */
   public static void dfs(Graph g, int u)
	 {
		visite[u] = true;
		System.out.println("Je visite " + u);
		for (Edge e: g.next(u))
		  if (!visite[e.to])
			dfs(g,e.to);
	 }

   /**
    * Methodes de test de la classe heap (file de priorit�)
    */
   public static void testHeap()
	 {
		// Crée ue file de priorité contenant les entiers de 0 à 9, tous avec priorité +infty
		Heap h = new Heap(10);
		h.decreaseKey(3,1664);
		h.decreaseKey(4,5);
		h.decreaseKey(3,8);
		h.decreaseKey(2,3);
		// A ce moment, la priorité des différents éléments est:
		// 2 -> 3
		// 3 -> 8
		// 4 -> 5
		// tout le reste -> +infini
		int x=  h.pop();
		System.out.println("On a enlevé "+x+" de la file, dont la priorité était " + h.priority(x));
		x=  h.pop();
		System.out.println("On a enlevé "+x+" de la file, dont la priorité était " + h.priority(x));
		x=  h.pop();
		System.out.println("On a enlevé "+x+" de la file, dont la priorité était " + h.priority(x));
		// La file contient maintenant uniquement les éléments 0,1,5,6,7,8,9 avec priorité +infini
	 }
   
   /**
    * Test de la cr�ation d'un graph
    */
   public static void testGraph()
	 {
		int n = 5;
		int i,j;
		Graph g = new Graph(n*n+2);
		
		for (i = 0; i < n-1; i++)
		  for (j = 0; j < n ; j++)
			g.addEdge(new Edge(n*i+j, n*(i+1)+j, 1664 - (i+j)));

		for (j = 0; j < n ; j++)		  
		  g.addEdge(new Edge(n*(n-1)+j, n*n, 666));
		
		for (j = 0; j < n ; j++)					
		  g.addEdge(new Edge(n*n+1, j, 0));
		
		g.addEdge(new Edge(13,17,1337));
		g.writeFile("test.dot");
		// dfs à partir du sommet 3
		visite = new boolean[n*n+2];
		dfs(g, 3);
	 }
   
   /**
    * Test de la m�thode Dijsktra
    */
   public static void testDijkstra()
   {
	   int n = 5;
	   int i,j;
	   Graph g = new Graph(n*n+2);
	   for (i = 0; i < n-1; i++)
			  for (j = 0; j < n ; j++)
				g.addEdge(new Edge(n*i+j, n*(i+1)+j, 1664 - (i+j)));

			for (j = 0; j < n ; j++)		  
			  g.addEdge(new Edge(n*(n-1)+j, n*n, 666));
			
			for (j = 0; j < n ; j++)					
			  g.addEdge(new Edge(n*n+1, j, 0));
			
			g.addEdge(new Edge(13,17,1337));
			
		
		ArrayList<Integer> test = new ArrayList<Integer>();
		System.out.println("Test Dijkstra !\n");
		test = SeamCarving.Dijsktra(g, 1, 8);
		System.out.println(test);
		
   }
   
   /**
    * Classe de lancement des m�thodes
    * @param args
    */
   public static void main(String[] args)
	 {
		testHeap();
		testGraph();
		testDijkstra();
	 }
}
