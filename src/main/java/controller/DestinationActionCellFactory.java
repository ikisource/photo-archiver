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
//		copy.disableProperty().bind(new SimpleBooleanProperty(photos.size() == 0));
		BooleanBinding bb = Bindings.createBooleanBinding(() ->
				photos.size( )== 0, photos);
		copy.disableProperty().bind(bb);
		
//		allSelected = Bindings.createBooleanBinding(() -> 
//        // compute value of binding:
//        Stream.of(packages).allMatch(CheckBox::isSelected), 
//        // array of thing to observe to recompute binding - this gives the array
//        // of all the check boxes' selectedProperty()s.
//        Stream.of(packages).map(CheckBox::selectedProperty).toArray(Observable[]::new));
		
		
		
		cancel = new Button("Annuler");
		//cancel.setVisible(false);
		progressBar = new ProgressBar(0);
//		progressBar.setProgress(0);
//        progressIndicator.setProgress(0);
		hBox = new HBox(copy);
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setSpacing(5);
		
		/*progressBar.prefHeightProperty().bind(copy.prefHeightProperty());
		progressBar.minHeightProperty().bind(copy.minHeightProperty());
		progressBar.maxHeightProperty().bind(copy.maxHeightProperty());
		
		progressBar.prefWidthProperty().bind(copy.prefWidthProperty());
		progressBar.minWidthProperty().bind(copy.minWidthProperty());
		progressBar.maxWidthProperty().bind(copy.maxWidthProperty());*/
		progressBar.setPrefWidth(70);
		copy.setPrefWidth(70);
		progressBar.setPrefHeight(25);
		
		stackPane = new StackPane(copy);
		setGraphic(stackPane);
		
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		copy.setOnAction(e -> {
			System.out.println("copier vers " + getTableRow().getItem().getName());

			//service.prepare(photos.stream().filter(photo -> photo.getExtension().equalsIgnoreCase("jpg")), destination, "author");
			service.start();
			progressBar.progressProperty().bind(service.progressProperty());
			//cancel.setDisable(true);
			
			service.setOnRunning(event -> {
//				copy.setDisable(true);
//				copy.setVisible(false);
//				cancel.setDisable(false);
				//fillHBox(cancel, progressBar, progressIndicator);
				cancel.setOpacity(0.5);
				fillStack(progressBar, cancel);
			});
			service.setOnSucceeded(event -> {
//				copy.setDisable(false);
//				cancel.setDisable(true);
//				cancel.setVisible(false);
				//fillHBox(copy);
				fillStack(copy);
				service.reset();
			});
			service.setOnCancelled(event -> {
//				copy.setDisable(false);
//				cancel.setDisable(true);
//				cancel.setVisible(false);
//				fillHBox(copy);
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
	
	private void fillStack(Node ... nodes) {
		
		stackPane.getChildren().clear();
		stackPane.getChildren().addAll(nodes);
	}

}
