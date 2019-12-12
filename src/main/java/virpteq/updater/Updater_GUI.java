package virpteq.updater;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

/**
 * The GUI for the VIRPTEQ Calculator
 * 
 * @author GenElectrovise
 *
 */
class Updater_GUI {

	private JFrame frame;
	JTextPane txtpnInstructions = new JTextPane();
	JButton btnUpdate = new JButton("Update");
	JPanel bgpanel = new JPanel();
	static JTextArea txtpnLog = new JTextArea();
	boolean updating;
	static Updater virpteq_updater = new Updater();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					virpteq_updater.testForSaveLocation();
					Updater_GUI window = new Updater_GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Updater_GUI() {
		initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("VIRPTEQ-Updater");
		frame.setBounds(100, 100, 660, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		bgpanel.setBorder(new TitledBorder(null, "VIRPTEQ Calculator Updater", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		bgpanel.setBounds(10, 11, 624, 239);
		frame.getContentPane().add(bgpanel);
		bgpanel.setLayout(null);

		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnUpdate.setEnabled(false);
				appendLog("Starting update", false);
				appendLog("Searching for updates...");
				Updater.initUpdate();
			}
		});
		btnUpdate.setBounds(525, 161, 89, 23);
		btnUpdate.setEnabled(true);
		bgpanel.add(btnUpdate);

		txtpnInstructions.setEditable(false);
		txtpnInstructions.setFont(new Font("Tahoma", Font.BOLD, 9));
		txtpnInstructions.setText(
				"This is the VIRPTEQ Calculator Updater!\r\nTo test for and apply updates, click the update button below.\r\nIt will download the latest version \r\nof VIRPTEQ Calculator from our Git repository on: \"github.com/GenElectrovise/VIRPTEQ-Core\"");
		txtpnInstructions.setBounds(10, 161, 604, 67);
		bgpanel.add(txtpnInstructions);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 604, 129);
		bgpanel.add(scrollPane);
		scrollPane.setViewportView(txtpnLog);
		txtpnLog.setWrapStyleWord(true);

		txtpnLog.setToolTipText("Log of actions");
		txtpnLog.setFont(new Font("Tahoma", Font.BOLD, 7));
		txtpnLog.setEditable(false);
		txtpnLog.setWrapStyleWord(true);
		txtpnLog.setColumns(20);

	}

	/**
	 * Appends the log on the GUI. Calls <b>appendLog(String toAppend, boolean
	 * newLine)</b> with args: <b>(toAppend, true)</b>
	 * 
	 * @param toAppend Text to append
	 */
	protected static void appendLog(String toAppend) {
		appendLog(toAppend, true);
	}

	/**
	 * Appends the log on the GUI, with option to append to same line
	 * 
	 * @param toAppend Text to append
	 * @param newLine  Whether the text should be logged on a new line
	 */
	protected static void appendLog(String toAppend, boolean newLine) {
		if (newLine)
			txtpnLog.setText(txtpnLog.getText() + "\n" + toAppend);
		else
			txtpnLog.setText(txtpnLog.getText() + toAppend);
		System.out.println(toAppend);
	}

	/**
	 * Requests a save location from the user. Has to be in this class to properly encapsulate the JComponents in the GUI properly
	 * @return A file path from the user
	 */
	public static String requestSaveLoc() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Select a home directory for VIRPTEQ");
		fileChooser.showSaveDialog(Updater_GUI.txtpnLog);
		while (!new File(fileChooser.getSelectedFile().getAbsolutePath()).exists()) {
			System.out.println(new File(fileChooser.getSelectedFile().getAbsolutePath()));
			fileChooser.setSelectedFile(null);
			requestSaveLoc();
		}
		System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
		return fileChooser.getSelectedFile().getAbsolutePath();
	}

	public static void bounceGetSaveLocCall() {
		virpteq_updater.testForSaveLocation();
	}
}
