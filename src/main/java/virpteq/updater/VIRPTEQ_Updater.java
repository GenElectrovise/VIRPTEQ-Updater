package virpteq.updater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class VIRPTEQ_Updater {
	
	public static VIRPTEQ_Updater updater = new VIRPTEQ_Updater();
	static String saveLocation = updater.readSaveLoc();
	private static VIRPTEQ_Webtools webtools = new VIRPTEQ_Webtools(saveLocation);
	final static String pathProjectToSave = "../virpteq-updater/src/main/resources/save_loc/save_location.txt";
	File save_loc_file = assign();

	File assign() {
		File temp = new File("save_location.txt");
		System.out.println("save_location.txt exists? : "
				+ (temp.exists() == true ? temp.exists() + " at location = " + temp.getAbsolutePath() : temp.exists()));
		return temp;
	}

	public static String getSaveLocation() {
		return saveLocation;
	}

	public static void main(String[] args) {
		// updater.writeSaveLoc();
		//updater.testForSaveLocation();
		//updater.readSaveLoc();

		// VIRPTEQ_Updater_GUI.appendLog("Initiating update...");
		// VIRPTEQ_Updater_GUI.bounceGetSaveLocCall();
		// initUpdate();
	}

	public static void initUpdate() {
		try {
			webtools.fetchLatestVersionTxt();
			webtools.fetchFromGithub();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	

	protected void testForSaveLocation() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(save_loc_file));
			String contents = br.readLine();
			System.out.println("contents : " + contents);

			System.out.println(saveLocation = contents);

			if (contents == "NO_SAVE_LOCATION" || contents == null) {
				System.out.println("contents null or unset! : " + contents);
				// saveLocation = VIRPTEQ_Updater_GUI.requestSaveLoc();
				saveLocation = "TEST SAVE";
				writeSaveLoc();
			} else {
				System.out.println("save location found at : " + saveLocation);
			}

			br.close();
		} catch (FileNotFoundException fileNotFound) {

		} catch (IOException fileNotFound) {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void writeSaveLoc() {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(save_loc_file));
			br.write(saveLocation);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String readSaveLoc() {

		String fileContents;
		try {
			try {
				// tries to read save_location.txt // creates new InputStream of the wanted
				// resource
				// InputStream is =
				// getClass().getResourceAsStream("/save_loc/save_location.txt");
				InputStream is = new FileInputStream(new File("save_location.txt"));

				// creates new BufferedReader
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				System.out.println("Trying to read save_location.txt");

				// reads the first line of text
				VIRPTEQ_Updater_GUI.appendLog("Reading save_location.txt");
				fileContents = reader.readLine();

				// if the save location is null:
				if (fileContents == null || fileContents.equals(null)) { // request save location with requestSaveLoc()
					fileContents = VIRPTEQ_Updater_GUI.requestSaveLoc();

					// write the saveLocation to the save_location.txt file using writeSaveLoc()
					writeSaveLoc();
				} else {
					VIRPTEQ_Updater_GUI.appendLog("save_location.txt is populated");
				}

				// appends the first line of text
				VIRPTEQ_Updater_GUI.appendLog("Found save location: " + fileContents);

				// closes the reader
				reader.close();
				return fileContents;
			} catch (FileNotFoundException fileException) {
				fileException.printStackTrace();
				// if there is an exception where the file is not found (does not exist), catch
				// and append the log

				VIRPTEQ_Updater_GUI
						.appendLog("No save_location.txt found! Will create one and request location input!");
				return VIRPTEQ_Updater_GUI.requestSaveLoc();

			}

		} catch (NullPointerException n) {
			n.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;

	}
}
