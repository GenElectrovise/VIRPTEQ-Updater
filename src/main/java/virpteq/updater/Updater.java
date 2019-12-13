package virpteq.updater;

import java.io.File;

class Updater {

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
}

