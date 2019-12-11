package virpteq.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class VIRPTEQ_Webtools {
	private static VIRPTEQ_Webtools webtools = new VIRPTEQ_Webtools(System.getProperty("user.dir"));
	
	public static void main(String[] args) {
		webtools.fetch(EnumFetchType.LATEST_JAR);
	}
	
	
	private String saveLocation;
	private EnumFetchType fetchType;
	private String version;

	public VIRPTEQ_Webtools(String saveLocation) {
		this.saveLocation = saveLocation;
		fetch(EnumFetchType.VERSION);
	}

	private static boolean isRedirected(Map<String, List<String>> header) {
		for (String hv : header.get(null)) {
			if (hv.contains(" 301 ") || hv.contains(" 302 "))
				return true;
		}
		return false;
	}

	public void fetch(EnumFetchType fetchType) {
		switch (fetchType) {
		case LATEST_JAR:
			String[] JAR_messages = {"Will commence fetching jar", "This is a jar test messsage"};
			doFetching(EnumFetchType.LATEST_JAR, JAR_messages, getJarLink());
			break;
		case VERSION:
			String[] VERSION_messages = {"Will commence fetching version", "This is a version test messsage"};
			doFetching(EnumFetchType.VERSION, VERSION_messages, getVersionLink());
			break;
		}
	}

	private String getJarLink(){
		String baseLink = "https://github.com/GenElectrovise/VIRPTEQ-Core/releases/download/";
		String version = this.version;
		String fileName = "VIRPTEQ_Calculator_" + version + ".jar";
		return new String(baseLink + version + fileName);
	}
	
	private String getVersionLink(){
		return "https://raw.githubusercontent.com/GenElectrovise/VIRPTEQ-Core/master/latestversion.txt";
	}

	private void doFetching(EnumFetchType fetchType, String[] logMessages, String link) {
		try {
			VIRPTEQ_Updater_GUI.appendLog(logMessages[0]);
			VIRPTEQ_Updater_GUI.appendLog(logMessages[1]);

			// Opens http
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			VIRPTEQ_Updater_GUI.appendLog("Connection established");

			// Be redirected
			Map<String, List<String>> header = http.getHeaderFields();
			while (isRedirected(header)) {
				VIRPTEQ_Updater_GUI.appendLog("Being redirected by GitHub");
				link = header.get("Location").get(0);
				url = new URL(link);
				http = (HttpURLConnection) url.openConnection();
				header = http.getHeaderFields();
			}

			// Get the file to write to
			File fileOut;
			switch (fetchType) {
			case LATEST_JAR:
				fileOut = new File(saveLocation + "/" + "VIRPTEQ_Calculator_" + ".jar"); // TODO name it correctly
				writeFile(fileOut, http);
				break;
			case VERSION:
				fileOut = new File(saveLocation + "/latestversion.txt");
				writeFile(fileOut, http);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void writeFile(File writeTo, HttpURLConnection http) {
		try {
			// Get an InputStream from the http
			InputStream input = http.getInputStream();
			byte[] buffer = new byte[4096];
			int n = -1;

			// Open OutputStream
			OutputStream output = new FileOutputStream(writeTo);

			// Write the file
			while ((n = input.read(buffer)) != -1) {
				output.write(buffer, 0, n);
			}

			VIRPTEQ_Updater_GUI.appendLog("Written. Closing writer."); // https://github.com/GenElectrovise/VIRPTEQ-Calc
			output.close();
		} catch (Exception e) {

		}
	}

	public void fetchFromGithub() throws Throwable {
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

	public String fetchLatestVersionTxt() {
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
}
