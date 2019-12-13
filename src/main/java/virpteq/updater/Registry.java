package virpteq.updater;

public class Registry {
	static Updater updater = new Updater();
	static Updater_GUI gui = new Updater_GUI();
	static FileTools filetools = new FileTools();
	//static Webtools webtools = new Webtools(new String(System.getProperty("user.dir") + "/virpteq_configs"));
	static Webtools webtools = new Webtools(new String(filetools.doStuffWithSaves()));
}
