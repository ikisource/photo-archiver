package controller;

import javafx.concurrent.Worker.State;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.Destination;
import service.CopyTestService;

public class DestinationActionCellFactory extends TableCell<Destination, Void> {

	private HBox hBox;
	private Button copy;
	private Button cancel;
	private TextField textField;
	private StackPane stackPane;
    private ProgressBar progressBar;
    CopyTestService service;

	public DestinationActionCellFactory() {
		super();
		copy = new Button("Copier");
		cancel = new Button("Annuler");
		progressBar = new ProgressBar();
		hBox = new HBox(copy, cancel, progressBar);
		setGraphic(hBox);
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		CopyTestService service = new CopyTestService();
		progressBar.progressProperty().bind(service.progressProperty());
		
		copy.setOnAction(e -> {
			System.out.println("copier vers " + getTableRow().getItem().getName());
			
			//thumbService.prepare(data.stream().filter(photo -> photo.getExtension().equalsIgnoreCase("jpg")), destination, "author");
			service.start();
		});
		
		cancel.setOnAction(e -> {
			if (service.getState().equals(State.RUNNING)) {
				service.cancel();
				service.reset();
			}
			System.out.println("Annuler copie vers " + getTableRow().getItem().getName());
			//thumbService.prepare(data.stream().filter(photo -> photo.getExtension().equalsIgnoreCase("jpg")), destination, "author");
		});
	}

	@Override
	protected void updateItem(Void arg0, boolean empty) {
		super.updateItem(arg0, empty);
		
		if (empty) {
			setGraphic(null);
		}
	}

}
