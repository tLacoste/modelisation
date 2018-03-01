import java.io.File;

/**
 * Classe principal
 *
 */
public class Launcher {

	public static void main(String[] args) {
		String fileSrc = "";
		String fileDest = "";
		int nbPixelToModify = 10;
		boolean useLine = false;
		boolean useIntensity = false;
		
		int argsLength = args.length;
		if(argsLength < 2) {
			System.out.println("Arguments invalides : java -jar <nom_fichier_a_reduire>.pgm <nom_fichier_destination>.pgm <nombre_de_pixel_a_modifier> <supprimer des lignes (1), des colonnes (0)> <utiliser l'intensite (0/1)>");
			System.exit(1);
		}
		if(argsLength >= 2) {
			fileSrc = args[0];
			fileDest = args[1];
		}
		if(argsLength >= 3) {
			nbPixelToModify =  Integer.parseInt(args[2]);
		}
		if(argsLength >= 4) {
			useLine =  Boolean.parseBoolean(args[3]);
		}
		if(argsLength >= 5) {
			useIntensity =  Boolean.parseBoolean(args[4]);
		}
		
		SeamCarving sm = new SeamCarving(fileSrc, fileDest, nbPixelToModify, useLine, useIntensity);
	}
	
}
