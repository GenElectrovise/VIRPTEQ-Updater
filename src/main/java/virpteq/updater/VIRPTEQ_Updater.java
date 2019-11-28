package virpteq.updater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

class VIRPTEQ_Updater {

	static String saveLocation = null;
	final static String pathProjectToSave = "../virpteq-updater/src/main/resources/save_loc/save_location.txt";

	public static String getSaveLocation() {
		return saveLocation;
	}

	public static void main(String[] args) {
		VIRPTEQ_Updater_GUI.appendLog("Initiating update...");
		VIRPTEQ_Updater_GUI.bounceGetSaveLocCall();
		initUpdate();
	}

	public static void initUpdate() {
		try {
			fetchLatestVersionTxt();
			fetchFromGithub();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static boolean isRedirected(Map<String, List<String>> header) {
		for (String hv : header.get(null)) {
			if (hv.contains(" 301 ") || hv.contains(" 302 "))
				return true;
		}
		return false;
	}

	public static void fetchFromGithub() throws Throwable {
		VIRPTEQ_Updater_GUI.appendLog(">>>>> BEGIN FETCHING .JAR <<<<<");
		String version = fetchLatestVersionTxt();
		String fileName = "VIRPTEQ_Calculator_" + version + ".jar";
		VIRPTEQ_Updater_GUI.appendLog("Will search for file: " + fileName);
		String link = "https://github.com/GenElectrovise/VIRPTEQ-Core/releases/download/" + version + "/" + fileName; // https://github.com/GenElectrovise/VIRPTEQ-Calc/releases/download/1.2/VIRPTEQ_Calculator_1.2.jar
		VIRPTEQ_Updater_GUI.appendLog("Will search for file: " + fileName + " :On site: " + link);
		URL url = new URL(link);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		VIRPTEQ_Updater_GUI.appendLog("Connection established");
		Map<String, List<String>> header = http.getHeaderFields();
		while (isRedirected(header)) {
			VIRPTEQ_Updater_GUI.appendLog("Being redirected by GitHub");
			link = header.get("Location").get(0);
			url = new URL(link);
			http = (HttpURLConnection) url.openConnection();
			header = http.getHeaderFields();
		}
		VIRPTEQ_Updater_GUI.appendLog("Opening InputStream!");
		InputStream input = http.getInputStream();
		byte[] buffer = new byte[4096];
		VIRPTEQ_Updater_GUI.appendLog("Byte buffer set");
		int n = -1;
		File fileOut = new File(saveLocation + "/" + fileName);
		VIRPTEQ_Updater_GUI.appendLog("Got file");
		VIRPTEQ_Updater_GUI.appendLog("Writing file to: " + fileOut.getAbsolutePath());
		OutputStream output = new FileOutputStream(fileOut);
		VIRPTEQ_Updater_GUI.appendLog("Opening OutputStream. Writing!");
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		VIRPTEQ_Updater_GUI.appendLog("Written. Closing writer."); // https://github.com/GenElectrovise/VIRPTEQ-Calc
		output.close();
	}

	public static String fetchLatestVersionTxt() {
		try {
			VIRPTEQ_Updater_GUI.appendLog(">>>>> BEGIN FETCHING VERSION <<<<<");

			String fileName = "latestversion.txt";

			VIRPTEQ_Updater_GUI.appendLog("Will search for file: " + fileName);
			String link = "https://raw.githubusercontent.com/GenElectrovise/VIRPTEQ-Core/master/" + fileName; // https://github.com/GenElectrovise/VIRPTEQ-Calc/releases/download/1.2/VIRPTEQ_Calculator_1.2.jar
			VIRPTEQ_Updater_GUI.appendLog("Will search for file: " + fileName + " :On site: " + link);
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			VIRPTEQ_Updater_GUI.appendLog("Connection established");
			Map<String, List<String>> header = http.getHeaderFields();
			while (isRedirected(header)) {
				VIRPTEQ_Updater_GUI.appendLog("Being redirected by GitHub");
				link = header.get("Location").get(0);
				url = new URL(link);
				http = (HttpURLConnection) url.openConnection();
				header = http.getHeaderFields();
			}
			VIRPTEQ_Updater_GUI.appendLog("Opening InputStream!");
			InputStream input = http.getInputStream();
			byte[] buffer = new byte[4096];
			VIRPTEQ_Updater_GUI.appendLog("Byte buffer set");
			int n = -1;
			File fileOut = new File(saveLocation + "/" + fileName);
			VIRPTEQ_Updater_GUI.appendLog("Got file");
			VIRPTEQ_Updater_GUI.appendLog("Writing file to: " + fileOut.getAbsolutePath());
			OutputStream output = new FileOutputStream(fileOut);
			VIRPTEQ_Updater_GUI.appendLog("Opening OutputStream. Writing!");
			while ((n = input.read(buffer)) != -1) {
				output.write(buffer, 0, n);
			}
			VIRPTEQ_Updater_GUI.appendLog("Written. Closing writer."); // https://github.com/GenElectrovise/VIRPTEQ-Calc
																		// //https://github.com/GenElectrovise/VIRPTEQ-Core/releases/download/1.2.1/VIRPTEQ_Calculator_1.2.1.jar
			output.close();

			BufferedReader br = new BufferedReader(new FileReader(fileOut.getAbsolutePath()));
			String versionOut = br.readLine();
			VIRPTEQ_Updater_GUI.appendLog("Found version: " + versionOut);
			br.close();
			return versionOut;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "1.2.1";
	}

	protected void testForSaveLocation() {
		try {
			try {
				// tries to read save_location.txt
				// creates new InputStream of the wanted resource
				InputStream is = getClass().getResourceAsStream("/save_loc/save_location.txt");

				// creates new BufferedReader
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				System.out.println("Trying to read save_location.txt");

				// reads the first line of text
				VIRPTEQ_Updater_GUI.appendLog("Reading save_location.txt");
				saveLocation = reader.readLine();

				// if the save location is null:
				if (saveLocation == null || saveLocation.equals(null)) {
					// request save location with requestSaveLoc()
					saveLocation = VIRPTEQ_Updater_GUI.requestSaveLoc();

					// write the saveLocation to the save_location.txt file using writeSaveLoc()
					writeSaveLoc();
				} else {
					VIRPTEQ_Updater_GUI.appendLog("save_location.txt is populated");
				}

				// prints the first line of text
				System.out.println(saveLocation);

				// closes the reader
				reader.close();
			} catch (FileNotFoundException fileException) {
				fileException.printStackTrace();
				// if there is an exception where the file is not found (does not exist), catch
				// and append the log
				VIRPTEQ_Updater_GUI
						.appendLog("No save_location.txt found! Will create one and request location input!");

			}

			// append the log with found location
			VIRPTEQ_Updater_GUI.appendLog("Found save location: " + saveLocation);

		} catch (NullPointerException n) {
			n.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void writeSaveLoc() {
		try {
			Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(pathProjectToSave), "utf-8"));
			writer.write(saveLocation);
			writer.close();
		} catch (Exception e) {
		}
	}
}
