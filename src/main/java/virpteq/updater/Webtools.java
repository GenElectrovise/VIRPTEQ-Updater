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

/**
 * Webtools for the VIRPTEQ Calculator. Provides all the necessary methods for
 * interfacing with GitHub.
 * 
 * @author GenElectrovise
 *
 */
public class Webtools {
	private String saveLocation;
	private String version;

	public static void main(String[] args) {
		Registry.webtools.fetch(EnumFetchType.VERSION);
		Registry.webtools.fetch(EnumFetchType.LATEST_JAR);
	}

	/**
	 * @param saveLocation the save location for the virpteq calculator defined by
	 *                     the user
	 */
	public Webtools(String saveLocation) {
		this.saveLocation = saveLocation;
		fetch(EnumFetchType.VERSION);
	}

	/**
	 * @param header The header of a HTTP response
	 * @return Returns whether the HTTP request is being redirected
	 */
	private static boolean isRedirected(Map<String, List<String>> header) {
		for (String hv : header.get(null)) {
			if (hv.contains(" 301 ") || hv.contains(" 302 "))
				return true;
		}
		return false;
	}

	/**
	 * Initiates fetching the latest version of the inputed EnumFetchType
	 * 
	 * @param fetchType
	 */
	public void fetch(EnumFetchType fetchType) {
		switch (fetchType) {
		case LATEST_JAR:
			String[] JAR_messages = { "Will commence fetching jar", "This is a jar test messsage" };
			doFetching(EnumFetchType.LATEST_JAR, JAR_messages, getJarLink());
			break;
		case VERSION:
			String[] VERSION_messages = { "Will commence fetching version", "This is a version test messsage" };
			doFetching(EnumFetchType.VERSION, VERSION_messages, getVersionLink());
			break;
		}
	}

	/**
	 * 
	 * @return A concatenated string formed of all of the parts of the link to the
	 *         latest VIRPTEQ_Calc.jar \n Formed of: the base link to the github
	 *         repo, the fetched version, and the file name to fetch
	 */
	private String getJarLink() {
		String baseLink = "https://github.com/GenElectrovise/VIRPTEQ-Core/releases/download/";
		String version = this.version;
		String fileName = "VIRPTEQ_Calculator_" + version + ".jar";
		return new String(baseLink + version + "/" + fileName);
	}

	/**
	 * 
	 * @return A link to the latestversion.txt file on github
	 */
	private String getVersionLink() {
		return "https://raw.githubusercontent.com/GenElectrovise/VIRPTEQ-Core/master/latestversion.txt";
	}

	/**
	 * Fetches the file defined by the link and fetchtype
	 * 
	 * @param fetchType   Whether you want to fetch, Eg. the
	 *                    <b>latestversion.txt</b> file or the latest
	 *                    <b>VIRPTEQ_Calc.jar</b>
	 * @param logMessages A <b>String[]</b> of messages to be appended to the log.
	 *                    Makes the application versatile and multi-purpose
	 * @param link        The link to the file to fetch
	 */
	private void doFetching(EnumFetchType fetchType, String[] logMessages, String link) {
		try {
			Registry.gui.appendLog(logMessages[0]);
			Registry.gui.appendLog(logMessages[1]);

			// Opens http
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			Registry.gui.appendLog("Connection established");

			// Be redirected
			Map<String, List<String>> header = http.getHeaderFields();
			while (isRedirected(header)) {
				Registry.gui.appendLog("Being redirected by GitHub");
				link = header.get("Location").get(0);
				url = new URL(link);
				http = (HttpURLConnection) url.openConnection();
				header = http.getHeaderFields();
			}

			// Get the file to write to
			File fileOut;
			switch (fetchType) {
			case LATEST_JAR:
				System.out.println("Got file");
				fileOut = new File(saveLocation + "/" + "VIRPTEQ_Calculator_" + version + ".jar"); // TODO name it
																											// correctly
				Registry.filetools.writeFile(fileOut, http);
				break;
			case VERSION:
				fileOut = new File(saveLocation + "/latestversion.txt");
				// fileOut = new File(System.getProperty("user.dir" +
				// "/virpteq_configs/latestversion.txt"));
				Registry.filetools.writeFile(fileOut, http);
				BufferedReader br = new BufferedReader(new FileReader(fileOut));
				version = br.readLine();
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	public void fetchFromGithub() throws Throwable {
		Registry.gui.appendLog(">>>>> BEGIN FETCHING .JAR <<<<<");
		String version = fetchLatestVersionTxt();
		String fileName = "VIRPTEQ_Calculator_" + version + ".jar";
		Registry.gui.appendLog("Will search for file: " + fileName);
		String link = "https://github.com/GenElectrovise/VIRPTEQ-Core/releases/download/" + version + "/" + fileName; // https://github.com/GenElectrovise/VIRPTEQ-Calc/releases/download/1.2/VIRPTEQ_Calculator_1.2.jar
		Registry.gui.appendLog("Will search for file: " + fileName + " :On site: " + link);
		URL url = new URL(link);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		Registry.gui.appendLog("Connection established");
		Map<String, List<String>> header = http.getHeaderFields();
		while (isRedirected(header)) {
			Registry.gui.appendLog("Being redirected by GitHub");
			link = header.get("Location").get(0);
			url = new URL(link);
			http = (HttpURLConnection) url.openConnection();
			header = http.getHeaderFields();
		}
		Registry.gui.appendLog("Opening InputStream!");
		InputStream input = http.getInputStream();
		byte[] buffer = new byte[4096];
		Registry.gui.appendLog("Byte buffer set");
		int n = -1;
		File fileOut = new File(saveLocation + "/" + fileName);
		Registry.gui.appendLog("Got file");
		Registry.gui.appendLog("Writing file to: " + fileOut.getAbsolutePath());
		OutputStream output = new FileOutputStream(fileOut);
		Registry.gui.appendLog("Opening OutputStream. Writing!");
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
		Registry.gui.appendLog("Written. Closing writer."); // https://github.com/GenElectrovise/VIRPTEQ-Calc
		output.close();
	}

	@Deprecated
	public String fetchLatestVersionTxt() {
		try {
			Registry.gui.appendLog(">>>>> BEGIN FETCHING VERSION <<<<<");

			String fileName = "latestversion.txt";

			Registry.gui.appendLog("Will search for file: " + fileName);
			String link = "https://raw.githubusercontent.com/GenElectrovise/VIRPTEQ-Core/master/" + fileName; // https://github.com/GenElectrovise/VIRPTEQ-Calc/releases/download/1.2/VIRPTEQ_Calculator_1.2.jar
			Registry.gui.appendLog("Will search for file: " + fileName + " :On site: " + link);
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			Registry.gui.appendLog("Connection established");
			Map<String, List<String>> header = http.getHeaderFields();
			while (isRedirected(header)) {
				Registry.gui.appendLog("Being redirected by GitHub");
				link = header.get("Location").get(0);
				url = new URL(link);
				http = (HttpURLConnection) url.openConnection();
				header = http.getHeaderFields();
			}
			Registry.gui.appendLog("Opening InputStream!");
			InputStream input = http.getInputStream();
			byte[] buffer = new byte[4096];
			Registry.gui.appendLog("Byte buffer set");
			int n = -1;
			File fileOut = new File(saveLocation + "/" + fileName);
			Registry.gui.appendLog("Got file");
			Registry.gui.appendLog("Writing file to: " + fileOut.getAbsolutePath());
			OutputStream output = new FileOutputStream(fileOut);
			Registry.gui.appendLog("Opening OutputStream. Writing!");
			while ((n = input.read(buffer)) != -1) {
				output.write(buffer, 0, n);
			}
			Registry.gui.appendLog("Written. Closing writer."); // https://github.com/GenElectrovise/VIRPTEQ-Calc
																// //https://github.com/GenElectrovise/VIRPTEQ-Core/releases/download/1.2.1/VIRPTEQ_Calculator_1.2.1.jar
			output.close();

			BufferedReader br = new BufferedReader(new FileReader(fileOut.getAbsolutePath()));
			String versionOut = br.readLine();
			Registry.gui.appendLog("Found version: " + versionOut);
			br.close();
			return versionOut;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "1.2.1";
	}
}
