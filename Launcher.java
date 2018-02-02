import java.io.File;

public class Launcher {

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Arguments invalides : java -jar <nom_fichier>.pgm");
			System.exit(1);
		}
		String file = args[0];
		File f = new File(file);
		System.out.println(f.getAbsolutePath());
		if(f.exists() && !f.isDirectory()) { 
		    System.out.println("Exist");
		}
		SeamCarving sm = new SeamCarving(file);
	}
	
}
