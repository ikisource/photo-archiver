package omathe.gui;

import configuration.ConfigurationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main class of the application
 * @author Olivier MATHE
 */
public class Main extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {

		System.out.println("Initialisation de l'application");

		ConfigurationManager.check();
	}
	
	@SuppressWarnings("resource")
	@Override
	public void start(final Stage stage) throws Exception {

		final BorderPane root = FXMLLoader.load(getClass().getResource(ConfigurationManager.MAIN_FXML));
		stage.setTitle("Photo archiver");
		stage.setScene(new Scene(root));
		stage.getIcons().add(new Image(Main.class.getResource(ConfigurationManager.APPLICATION_ICON).toString()));
		
		stage.setOnCloseRequest(e -> {
			closeApplication();
		});
		
		stage.show();
	}
	
	private void closeApplication() {

		ConfigurationManager.saveConfiguration();
	}
}
