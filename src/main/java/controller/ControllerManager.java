package controller;

import java.io.IOException;
import java.io.InputStream;

import configuration.ConfigurationManager;
import javafx.fxml.FXMLLoader;

public class ControllerManager {

	private static MainController mainController;

	public static MainController getMainController() {

		if (mainController == null) {
			try (InputStream is = ControllerManager.class.getResource(ConfigurationManager.MAIN_FXML).openStream()) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.load(is);
				mainController = (MainController) fxmlLoader.getController();
			} catch (IOException e) {
				//				Logger.error(e);
				//				Notifier.notifyError(e.getMessage());
			}
		}
		return mainController;
	}

}
