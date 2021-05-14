package controller;

import java.util.List;

import javafx.concurrent.Worker.State;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import model.Destination;
import model.Photo;
import service.CopyService;

public class DestinationProgressCellFactory extends TableCell<Destination, Void> {

	private List<Photo> photos;

	private HBox hBox;
	private Button copy;
	private Button cancel;
//	private TextField textField;
//	private StackPane stackPane;
	private ProgressBar progressBar;
	private ProgressIndicator progressIndicator;
	CopyService service;

	public DestinationProgressCellFactory(List<Photo> photos) {
		super();

		this.photos = photos;

		copy = new Button("Copier");
		cancel = new Button("Annuler");
		//cancel.setVisible(false);
		progressBar = new ProgressBar(0);
		//progressBar.setProgress(0);
		progressIndicator = new ProgressIndicator(0);
        //progressIndicator.setProgress(0);
		hBox = new HBox(copy);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setSpacing(5);
		
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

			//service.prepare(photos.stream().filter(photo -> photo.getExtension().equalsIgnoreCase("jpg")), destination, "author");
			service.start();
			progressBar.progressProperty().bind(service.progressProperty());
			progressIndicator.progressProperty().bind(service.progressProperty());
			//cancel.setDisable(true);
			
			service.setOnRunning(event -> {
//				copy.setDisable(true);
//				copy.setVisible(false);
//				cancel.setDisable(false);
				fillHBox(cancel, progressBar, progressIndicator);
			});
			service.setOnSucceeded(event -> {
//				copy.setDisable(false);
//				cancel.setDisable(true);
//				cancel.setVisible(false);
				fillHBox(copy);
				service.reset();
			});
			service.setOnCancelled(event -> {
//				copy.setDisable(false);
//				cancel.setDisable(true);
//				cancel.setVisible(false);
				fillHBox(copy);
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
			//service = new CopyService(this, photos, destination, "");
		}
	}
	
	private void fillHBox(Node ... nodes) {
		
		hBox.getChildren().clear();
		hBox.getChildren().addAll(nodes);
		
//		Button copy;
//		private Button cancel;
//		private TextField textField;
//		private StackPane stackPane;
//		private ProgressBar progressBar;
	}

}
