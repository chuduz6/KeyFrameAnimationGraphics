/**
 * Main command-line entry point.
 * Parses commands, load scenes, renders them and saves results to image files.
 * @author fabio
 */
public class Main {	
	/**
	 * Main entry point.
	 */
	public static void main(String args[]) {
		if(args.length != 1) {
			System.out.println("usage: Main sceneFilename");
			return;
		}

		MainFrame frame = new MainFrame(args[0]);
		frame.setVisible(true);
	}
}
