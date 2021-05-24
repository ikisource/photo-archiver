package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import commons.AlertBuilder;
import configuration.ConfigurationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import model.Destination;
import model.Photo;
import service.AnalyseService;

/**
 * @author olivier MATHE
 */
public class MainController implements Initializable {

	private ObservableList<Destination> destinationsData;
	private final ObservableList<Photo> data = FXCollections.observableArrayList();
	private AnalyseService analyseService;

	// source

	@FXML
	private TextField filters;

	@FXML
	private CheckBox includeSubDirectories;

	@FXML
	private TextField sourceDirectory;

	@FXML
	private StackPane analyseStackPane;

	@FXML
	private Button analyseButton;

	@FXML
	private Button cancelAnalyse;

	@FXML
	private ProgressBar analyseProgress;

	@FXML
	private Label photoCounter;

	@FXML
	private TableView<Photo> photos;

	@FXML
	private TableColumn<Photo, Boolean> photoEnabledColumn;

	@FXML
	private TableColumn<Photo, String> photoNameColumn;

	@FXML
	private TableColumn<Photo, String> photoDateColumn;

	// destinations

	@FXML
	private TableView<Destination> destinations;

	@FXML
	private TableColumn<Destination, Void> destinationActionColumn;

	@FXML
	private TableColumn<Destination, String> destinationNameColumn;

	@FXML
	private TableColumn<Destination, String> destinationPathColumn;

	@FXML
	private TableColumn<Destination, Boolean> destinationRawColumn;

	@FXML
	private TableColumn<Destination, Boolean> destinationJpgColumn;

	@FXML
	private TableColumn<Destination, Boolean> destinationThumbColumn;

	@FXML
	private TableColumn<Destination, String> destinationStatusColumn;

	@Override
	public void initialize(URL location, final ResourceBundle resources) {

		// source directory
		sourceDirectory.textProperty().addListener((observable, oldValue, newValue) -> {
			File directory = new File(sourceDirectory.getText());
			if (!directory.exists() || directory.isFile()) {
				sourceDirectory.setStyle("-fx-text-fill: red;");
				analyseButton.setDisable(true);
			} else {
				sourceDirectory.setStyle("-fx-text-fill: black;");
				analyseButton.setDisable(false);
			}
		});

		analyseService = new AnalyseService();
		analyseProgress.progressProperty().bind(analyseService.progressProperty());
		analyseProgress.setStyle("-fx-accent: green;");
		fillStack(analyseButton);

		analyseService.setOnSucceeded(event -> {
			data.addAll(analyseService.getValue());
			fillStack(analyseButton);
			photoCounter.textProperty().unbind();
			analyseService.reset();
		});
		analyseService.setOnRunning(event -> {
			cancelAnalyse.setOpacity(0.5);
			fillStack(analyseProgress, cancelAnalyse);
		});
		analyseService.setOnCancelled(event -> {
			fillStack(analyseButton);
			photoCounter.textProperty().unbind();
			analyseService.reset();
		});

		destinationsData = ConfigurationManager.getConfiguration().getDestinations();
		destinations.setItems(destinationsData);
		destinations.setPlaceholder(new Label("Pas de destinations"));

		// bindings
		sourceDirectory.textProperty().bindBidirectional(ConfigurationManager.getConfiguration().sourceProperty());
		filters.textProperty().bindBidirectional(ConfigurationManager.getConfiguration().filterProperty());
		includeSubDirectories.selectedProperty().bindBidirectional(ConfigurationManager.getConfiguration().includeSubDirectoriesProperty());

		// photos
		photos.setPlaceholder(new Label("Pas de photos"));
		photos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		photos.setItems(data);

		photoEnabledColumn.setCellFactory(object -> new CheckBoxTableCell<>());

		// date
		Callback<TableColumn<Photo, String>, TableCell<Photo, String>> photoDateCellFactory = new Callback<TableColumn<Photo, String>, TableCell<Photo, String>>() {

			@Override
			public TableCell<Photo, String> call(TableColumn<Photo, String> p) {
				return new PhotoDateCellFactory();
			}
		};
		photoDateColumn.setCellValueFactory(new PropertyValueFactory<Photo, String>("date"));
		photoDateColumn.setCellFactory(photoDateCellFactory);

		// destinations
		destinationRawColumn.setCellFactory(object -> new CheckBoxTableCell<>());
		destinationJpgColumn.setCellFactory(object -> new CheckBoxTableCell<>());
		destinationThumbColumn.setCellFactory(object -> new CheckBoxTableCell<>());

		// action
		Callback<TableColumn<Destination, Void>, TableCell<Destination, Void>> destinationActionCellFactory = new Callback<TableColumn<Destination, Void>, TableCell<Destination, Void>>() {
			@Override
			public TableCell<Destination, Void> call(TableColumn<Destination, Void> p) {
				return new DestinationActionCellFactory(data);
			}
		};
		destinationActionColumn.setCellFactory(destinationActionCellFactory);
	}

	@FXML
	protected void addDestination(ActionEvent event) {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Sélection du répertoire de copie");
		File directory = directoryChooser.showDialog(null);
		if (directory != null) {
			destinationsData.add(new Destination(Boolean.TRUE, ConfigurationManager.DESTINATION_NAME, directory.toPath(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));
		}
	}

	@FXML
	protected void removeDestination(ActionEvent event) {

		Destination destination = destinations.getSelectionModel().getSelectedItem();
		if (destination != null) {

			String message = "Etes-vous sûr de vouloir supprimer la destination\n" + destination.getName() + " ?";
			ButtonType response = AlertBuilder.confirm("Supprimer une destination", message);
			if (response.equals(ButtonType.OK)) {
				destinationsData.remove(destination);
			}
		}
	}

	@FXML
	protected void selectAllPhotos(ActionEvent event) {

		data.stream().forEach(photo -> photo.setEnabled(true));
	}

	@FXML
	protected void deselectAllPhotos(ActionEvent event) {

		data.stream().forEach(photo -> photo.setEnabled(false));
	}

	@FXML
	public void browse(ActionEvent event) {

		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Sélection du répertoire de photos");
		File directory = directoryChooser.showDialog(null);
		if (directory != null) {
			sourceDirectory.setText(directory.getAbsolutePath());
		}
	}

	@FXML
	@SuppressWarnings("resource")
	protected void analyse(ActionEvent event) {

		Stream<Path> paths = null;
		try {
			File directory = new File(sourceDirectory.getText());
			Path directoryPath = Paths.get(directory.toURI());
			paths = includeSubDirectories.isSelected() ? Files.walk(directoryPath)
					.filter(Files::isRegularFile) : Files.list(directoryPath);
			List<String> extensions = getFilters();

			paths = paths.filter(p -> p.toFile().isFile())
					.filter(p -> p.toString().contains("."))
					.filter(p -> extensions.contains(p.toString().toLowerCase().substring(p.toString().lastIndexOf(".") + 1).toLowerCase())); // filter on extension
			data.clear();
			analyseService.setPaths(paths.collect(Collectors.toList()));
			analyseService.start();
			photoCounter.textProperty().bind(analyseService.messageProperty());
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private List<String> getFilters() {

		List<String> extensions = List.of();
		if (filters.getText() != null && !filters.getText().isEmpty()) {
			System.out.println("> " + filters.getText());
			String[] split = filters.getText().split(",");
			if (split != null) {
				extensions = Arrays.stream(split)
						.map(s -> s.trim().toLowerCase())
						.collect(Collectors.toList());
			}
		}
		return extensions;
	}

	@FXML
	void clearPhotos(ActionEvent event) {
		data.clear();
		photoCounter.setText("");
	}

	private void fillStack(Node... nodes) {

		analyseStackPane.getChildren().clear();
		analyseStackPane.getChildren().addAll(nodes);
	}
}
