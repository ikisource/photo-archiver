package configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import commons.JsonHelper;
import model.Configuration;

public class ConfigurationManager {
	
	private static Configuration configuration;

	private static final String APPLICATION_DIRECTORY = ".photoArchiver";
	public static final String CONFIGURATION_FILE_NAME = "configuration.json";
	public static final String APPLICATION_TITLE = "Photo archiver";
	public static final String DESTINATION_NAME = "dest";
	public static final String PHOTO_FILTERS = "CR2, JPG";
	public static final String MAIN_FXML = "/main.fxml";

	private static String getApplicationHome() {

		final String userHome = System.getProperty("user.home").replace("\\", "/");
		return userHome + "/" + APPLICATION_DIRECTORY;
	}

	private static File applicationHome() {

		return new File(getApplicationHome());
	}
	
	public static Configuration getConfiguration() {
		return configuration;
	}

	public static void check() {

		File applicationDirectory = applicationHome();
		// application directory
		if (!applicationDirectory.exists()) {
			// creating application home
			applicationDirectory.mkdir();
			System.out.println("Le répertoire de l'application a été créé.");
		}
		// load the configuration
		loadConfiguration();
		System.out.println(configuration);
	}

	public static void loadConfiguration() {

		File configurationFile = new File(getApplicationHome() + "/" + CONFIGURATION_FILE_NAME);
		try {
			if (configurationFile.exists()) {
				String content = Files.readString(configurationFile.toPath());
				configuration = (Configuration) JsonHelper.toObject(content, Configuration.class);
			}
			else {
				configuration = new Configuration(PHOTO_FILTERS, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveConfiguration() {
		
		File configurationFile = new File(getApplicationHome() + "/" + CONFIGURATION_FILE_NAME);
		if (!configurationFile.exists()) {
			try {
				configurationFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JsonHelper.tojson(configurationFile, configuration);
	}

}
