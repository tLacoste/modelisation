import java.io.File;

/**
 * Classe principal
 *
 */
public class Launcher {

	public static void main(String[] args) {
		String fileSrc = "";
		String fileDest = "";
		boolean extendImage = false;
		int nbPixelToModify = 10;
		boolean useLine = false;
		boolean useIntensity = false;
		
		int argsLength = args.length;
		if(argsLength < 2) {
			System.out.println("Arguments invalides : java -jar <nom_fichier_a_reduire>.pgm <nom_fichier_destination>.pgm <agrandir l'image ? (true/FALSE)> <nombre_de_pixel_a_modifier> <supprimer des lignes (true), des colonnes (FALSE)> <utiliser l'intensite ? (true/FALSE)>");
			System.exit(1);
		}
		if(argsLength >= 2) {
			fileSrc = args[0];
			fileDest = args[1];
		}
		if(argsLength >= 3) {
			extendImage = Boolean.parseBoolean(args[2]);
		}
		if(argsLength >= 4) {
			nbPixelToModify =  Integer.parseInt(args[3]);
		}
		if(argsLength >= 5) {
			useLine =  Boolean.parseBoolean(args[4]);
		}
		if(argsLength >= 6) {
			useIntensity =  Boolean.parseBoolean(args[5]);
		}
		SeamCarving sm = new SeamCarving(fileSrc, fileDest, extendImage, nbPixelToModify, useLine, useIntensity);
	}
	
}
