import java.util.ArrayList;
import java.io.*;

/**
 * Classe repr�senant un graph
 * @author Marvin
 *
 */
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
   
   /**
    * Ajout d'une arr�te
    * @param e
    * 			Arr�te
    */
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
   
   /**
    * Renvoit la liste des arr�tes
    * @return list
    * 				Liste des arr�tes
    */			
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
   
   /**
    * Fonction d'ecriture de fichier
    * @param s
    * 			Chaine a ecrire
    */
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
   
}
