package virpteq.updater;

import java.io.File;

class Updater {
	Runtime run = Runtime.getRuntime();

	public static void main(String[] args) {
		Registry.updater.launchCalculator(true);
	}

	public static Updater updater = new Updater();
	File save_loc_file = assign();

	File assign() {
		File temp = new File("save_location.txt");
		System.out.println("save_location.txt exists? : "
				+ (temp.exists() == true ? temp.exists() + " at location = " + temp.getAbsolutePath() : temp.exists()));
		return temp;
	}

	public void initUpdate() {
		try {
			Registry.webtools.fetch(EnumFetchType.VERSION);
			Registry.webtools.fetch(EnumFetchType.LATEST_JAR);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void launchCalculator(boolean closeThis) {
		try {

			System.out.println("Launching calculator");
			String[] array = { "cd " + FileTools.save_location,
					"java -jar VIRPTEQ_Calculator_" + Registry.webtools.versionPublic + ".jar" };

			System.out.println("\"C:\\Program Files\\Java\\jre1.8.0_201\\bin\\java.exe\" -jar "
					+ FileTools.save_location + "\\VIRPTEQ_Calculator_" + Registry.webtools.versionPublic + ".jar");

			run.exec("\"C:\\Program Files\\Java\\jre1.8.0_201\\bin\\java.exe\" -jar " + FileTools.save_location
					+ "\\VIRPTEQ_Calculator_" + Registry.webtools.versionPublic + ".jar");
			
			if(closeThis) System.exit(0);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
