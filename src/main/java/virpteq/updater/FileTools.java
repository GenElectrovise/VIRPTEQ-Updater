package virpteq.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileTools {
	static File save_location_file;
	static FileTools filetools = new FileTools();
	static String save_location;

	public static void main(String[] args) {
		// filetools.makeSaveFile();
		filetools.doStuffWithSaves();
	}

	public String doStuffWithSaves() {
		try {
			if (!new File(System.getProperty("user.dir") + "/virpteq_configs/save_location.txt").exists()) {
				if (!new File(System.getProperty("user.dir") + "/virpteq_configs").mkdirs()) {
					PrintWriter pw = new PrintWriter(
							new File(System.getProperty("user.dir") + "/virpteq_configs/save_location.txt"));
					pw.write("NO_SAVE_LOCATION_131219");

					pw.close();
				}
			}
			BufferedReader br = new BufferedReader(
					new FileReader(new File(System.getProperty("user.dir") + "/virpteq_configs/save_location.txt")));
			String contents = br.readLine();
			if (contents.contentEquals("NO_SAVE_LOCATION_131219")) {
				save_location = Registry.gui.requestSaveLoc();
				// save_location =
				// "C:\\Users\\adam_\\OneDrive\\Desktop\\Java\\Virpteq_test_dir";
				PrintWriter pw = new PrintWriter(
						new File(System.getProperty("user.dir") + "/virpteq_configs/save_location.txt"));
				pw.write(save_location);
				pw.close();
			} else
				save_location = contents;
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (new File(System.getProperty("user.dir") + "/virpteq_configs").exists())
				filetools.doStuffWithSaves();
		}

		System.out.println("Found save location : " + save_location);
		return save_location;
	}

	/**
	 * Writes a file to the given location
	 * 
	 * @param writeTo The File object to write to
	 * @param http    The <b>HttpURLConnection</b> providing the InputStream from
	 *                which to write
	 */
	public void writeFile(File writeTo, HttpURLConnection http) {
		System.out.println("writeFile FileTools");
		System.out.println("Will write to : " + writeTo.getAbsolutePath());
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

			Registry.gui.appendLog("Written. Closing writer."); // https://github.com/GenElectrovise/VIRPTEQ-Calc
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the first line of the given file.
	 * 
	 * @param file The file
	 * @return The first line of text of the file
	 */
	public String readFile(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String contents = br.readLine();
			br.close();
			return contents;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String readSaveLoc() {
		return readFile(save_location_file);
	}

}
