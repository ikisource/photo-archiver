package controller;

import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.Destination;
import model.Photo;
import service.CopyService;

public class DestinationActionCellFactory extends TableCell<Destination, Void> {

	private List<Photo> photos;

	private HBox hBox;
	private Button copy;
	private Button cancel;
	private StackPane stackPane;
	private ProgressBar progressBar;
	CopyService service;

	public DestinationActionCellFactory(ObservableList<Photo> photos) {
		super();

		this.photos = photos;

		copy = new Button("Copier");
		BooleanBinding bb = Bindings.createBooleanBinding(() -> photos.size() == 0, photos);
		copy.disableProperty().bind(bb);
		cancel = new Button("Annuler");
		progressBar = new ProgressBar(0);
		hBox = new HBox(copy);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setSpacing(5);
		
		progressBar.setPrefWidth(70);
		copy.setPrefWidth(70);
		progressBar.setPrefHeight(25);

		stackPane = new StackPane(copy);
		setGraphic(stackPane);

		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		copy.setOnAction(e -> {
			System.out.println("copier vers " + getTableRow().getItem().getName());
			service.start();
			progressBar.progressProperty().bind(service.progressProperty());

			service.setOnRunning(event -> {
				cancel.setOpacity(0.5);
				fillStack(progressBar, cancel);
			});
			service.setOnSucceeded(event -> {
				fillStack(copy);
				service.reset();
			});
			service.setOnCancelled(event -> {
				fillStack(copy);
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

		if (empty) {
			setGraphic(null);
		} else {
			setGraphic(stackPane);
			if (getTableRow() != null) {
				Destination destination = getTableRow().getItem();
				service = new CopyService(photos, destination, "");
			}
		}
	}

	private void fillStack(Node... nodes) {

		stackPane.getChildren().clear();
		stackPane.getChildren().addAll(nodes);
	}

}
