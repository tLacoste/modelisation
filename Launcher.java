public class Launcher {

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Arguments invalides : java -jar <nom_fichier>.pgm");
			System.exit(1);
		}
		SeamCarving sm = new SeamCarving(args[0]);
	}

}
