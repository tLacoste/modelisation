import java.awt.Point;
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
		Point topLeft = null;
		Point bottomRight = null;
		boolean increaseInterest = true;
		
		int argsLength = args.length;
		if(argsLength < 2) {
			System.out.println("Arguments invalides : java -jar <nom_fichier_a_reduire>.pgm <nom_fichier_destination>.pgm <nombre_de_pixel_a_modifier> <supprimer des lignes (true), des colonnes (FALSE)> <agrandir l'image ? (true/FALSE)> <utiliser l'intensite ? (true/FALSE)> <x haut gauche carré à augmenter/diminuer intérêt> <y haut gauche carré à augmenter/diminuer intérêt> <x bas droite carré à augmenter/diminuer intérêt> <y bas droite carré à augmenter/diminuer intérêt> <Augmenter/diminuer intérêt (TRUE/false)>");
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
			extendImage = Boolean.parseBoolean(args[4]);
			
		}
		if(argsLength >= 6) {
			useIntensity =  Boolean.parseBoolean(args[5]);
		}
		if(argsLength >= 10) {
			topLeft = new Point(Integer.parseInt(args[6]), Integer.parseInt(args[7]));
			bottomRight = new Point(Integer.parseInt(args[8]), Integer.parseInt(args[9]));
		}
		if(argsLength >= 11) {
			increaseInterest = Boolean.parseBoolean(args[10]);
		}
		
		SeamCarving sm = new SeamCarving(fileSrc, fileDest, extendImage, nbPixelToModify, useLine, useIntensity, topLeft, bottomRight, increaseInterest);
	}
	
}
