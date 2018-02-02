import java.io.File;

public class Launcher {

	/**
	 * Classe principal
	 * @param args
	 * 				Liste des arguments
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Arguments invalides : java -jar <nom_fichier_a_reduire>.pgm <nom_fichier_destination>.pgm");
			System.exit(1);
		}
		String fileSrc = args[0];
		String fileDest = args[1];
		SeamCarving sm = new SeamCarving(fileSrc, fileDest);
	}
	
}
