package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import commons.AlertBuilder;
import configuration.ConfigurationManager;
import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import model.Destination;
import model.DrewPhotoMetadataExtractor;
import model.Photo;
import model.PhotoMetadata;
import model.PhotoMetadataExtractor;
import service.AnalyseService;

/**
 *
 * @author olivier MATHE
 */
public class MainController implements Initializable {

	private ObservableList<Destination> destinationsData;
	private PhotoMetadataExtractor metadataExtractor;
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

		Platform.runLater(() -> {
			analyseButton.requestFocus();
		});

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

		// bindings
		this.metadataExtractor = new DrewPhotoMetadataExtractor();
	}

	//    @FXML
	//    public void browseDestination(MouseEvent mouseEvent) {
	//        System.out.println("mouse clicked");
	//        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
	//            if (mouseEvent.getClickCount() == 2) {
	//                //System.out.println("Double clicked");
	//                DirectoryChooser directoryChooser = new DirectoryChooser();
	//                directoryChooser.setTitle("Sélection du répertoire de copie");
	//                File directory = directoryChooser.showDialog(null);
	//                if (directory != null) {
	//                    sourceDirectory.setValue(directory.getAbsolutePath());
	//                }
	//            }
	//
	//        } else {
	//            System.out.println("simple clicked");
	//        }
	//    }

	public void extract(Path path) {

		PhotoMetadata photoMetadata = new PhotoMetadata();
		try {

			//File file = new File("/media/DATA/dev/photo-archiver/src/test/resources/2015-11-01_16-51-17,082.jpg");
			Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
			ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			if (directory != null) {
				// date
				Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				if (date != null) {
					photoMetadata.setDate(date.getTime());
					System.out.println("date: " + date);
				}
				// camera
			}
			for (Directory dir : metadata.getDirectories()) {
				for (Tag tag : dir.getTags()) {
					System.out.println(tag.toString());
					//System.out.println(tag.getDirectoryName() + "," + tag.getTagType() + ", " + tag.getTagName() + ", " + tag.getDescription());
				}
			}

		} catch (ImageProcessingException | IOException ex) {
			Logger.getLogger(DrewPhotoMetadataExtractor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	protected void addDestination(ActionEvent event) {

		System.out.println("controller.MainController.addDestination()");
		DirectoryChooser directoryChooser = new DirectoryChooser();
		//directoryChooser.setInitialDirectory(new File("/media/DATA/copy"));
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
	protected void selectDestination(MouseEvent event) {

		Destination destination = destinations.getSelectionModel().getSelectedItem();
		if (destination != null) {
			System.out.println(destination);
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
		displayPhotoCounter(null);
	}

	private void displayPhotoCounter(List<Path> invalidPhotos) {

		String text = "";
		if (data.isEmpty()) {
			photoCounter.setText("");
		} else if (data.size() == 1) {
			text = data.size() + " photo analysée";
		} else {
			text = data.size() + " photos analysées";
		}
		// photos without date
		long noDateCounter = data.stream().filter(photo -> photo.getDate() == null).count();
		if (noDateCounter > 0) {
			if (noDateCounter == 1) {
				text += ", " + noDateCounter + " photo sans date";
			} else {
				text += ", " + noDateCounter + " photos sans date";
			}
		}
		// invalid photos
		if (invalidPhotos != null && !invalidPhotos.isEmpty()) {
			if (invalidPhotos.size() == 1) {
				text += ", " + invalidPhotos.size() + " photo invalide";
			} else {
				text += ", " + invalidPhotos.size() + " photos invalides";
			}
		}
		photoCounter.setText(text);
	}

	private void fillStack(Node... nodes) {

		analyseStackPane.getChildren().clear();
		analyseStackPane.getChildren().addAll(nodes);
	}
}
