package controller;

import java.util.List;

import javafx.concurrent.Worker.State;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.Destination;
import model.Photo;
import service.CopyTestService;

public class DestinationActionCellFactory extends TableCell<Destination, Void> {

	private List<Photo> photos;

	private HBox hBox;
	private Button copy;
	private Button cancel;
	private TextField textField;
	private StackPane stackPane;
	private ProgressBar progressBar;
	CopyTestService service;

	public DestinationActionCellFactory(List<Photo> photos) {
		super();

		this.photos = photos;

		copy = new Button("Copier");
		cancel = new Button("Annuler");
		progressBar = new ProgressBar();
		hBox = new HBox(copy, cancel, progressBar);
		setGraphic(hBox);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		//		System.out.println("getTableRow() = " + getTableRow());
		//		if (getTableRow() != null) {
		//			Destination destination = getTableRow().getItem();
		//			System.out.println(destination);
		//			service = new CopyTestService(this, photos, destination, "");
		//		}

		copy.setOnAction(e -> {
			System.out.println("copier vers " + getTableRow().getItem().getName());

			progressBar.progressProperty().bind(service.progressProperty());
			//service.prepare(photos.stream().filter(photo -> photo.getExtension().equalsIgnoreCase("jpg")), destination, "author");
			service.start();
			cancel.setDisable(true);
			service.setOnRunning(event -> {
				copy.setDisable(true);
				cancel.setDisable(false);
			});
			service.setOnSucceeded(event -> {
				copy.setDisable(false);
				cancel.setDisable(true);
				service.reset();
			});
			service.setOnCancelled(event -> {
				copy.setDisable(false);
				cancel.setDisable(true);
				service.reset();
				System.out.println("Annuler copie vers " + getTableRow().getItem().getName());
			});
		});

		cancel.setOnAction(e -> {
			if (service.getState().equals(State.RUNNING)) {
				service.cancel();
			}
		});

	}

	@Override
	protected void updateItem(Void arg0, boolean empty) {
		super.updateItem(arg0, empty);

		System.out.println("updateItem " + empty);
		if (empty) {
			setGraphic(null);
		} else {
			Destination destination = getTableRow().getItem();
			System.out.println(destination);
			service = new CopyTestService(this, photos, destination, "");
		}
	}

}
