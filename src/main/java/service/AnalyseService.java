package service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.DrewPhotoMetadataExtractor;
import model.Photo;
import model.PhotoMetadata;
import model.PhotoMetadataExtractor;

public class AnalyseService extends Service<List<Photo>> {

	private static Logger logger = Logger.getLogger(AnalyseService.class);
	private File directory;
	private Stream<Path> files;
	private boolean includeSubDirectories;
	private List<String> extensions;
	private PhotoMetadataExtractor metadataExtractor;

	public AnalyseService() {
		super();
		this.metadataExtractor = new DrewPhotoMetadataExtractor();;
	}
	
	public void setFiles(Stream<Path> files) {
		this.files = files;
	}

	@Override
	protected Task<List<Photo>> createTask() {

		return new Task<List<Photo>>() {

			@Override
			protected List<Photo> call() {
				
				List<Photo> photos = new ArrayList<>();
				List<Path> invalidPhotos = new ArrayList<>();
				try {
					files.forEach(path -> {
								PhotoMetadata metadata = metadataExtractor.extract(path);
								if (metadata.getValid()) {
									Photo photo = new Photo(true, path.getFileName().toString(), path, metadata);
									photo.formatName();
									photos.add(photo);
								} else {
									invalidPhotos.add(path);
								}
							});
					//Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					if (files != null) {
						files.close();
						//displayPhotoCounter(invalidPhotos);
					}
				}

				/*long count = 0;
				int rawCount = 0;
				int jpgCount = 0;
				int thumbCount = 0;

				long total = photos.stream().filter(p -> p.getEnabled()).count();
				long rawTotal = photos.stream().filter(p -> p.getExtension().equalsIgnoreCase("cr2"))
						.filter(p -> p.getEnabled()).count();
				long jpgTotal = photos.stream().filter(p -> p.getExtension().equalsIgnoreCase("jpg"))
						.filter(p -> p.getEnabled()).count();

				for (Photo photo : photos) {
					if (photo.getEnabled()) {
						if (isCancelled()) {
							break;
						}
						String extension = photo.getExtension().toLowerCase();
						String photoPath = photo.buildDirectoryName();
						System.out.println("photoPath = " + photoPath);

						if (extension.equalsIgnoreCase("jpg")) {
							boolean copied = false;
							if (destination.getJpg()) {
								try {
									Path directoryPath = Paths.get(destination.getPath(), author, "jpg", photoPath);
									Files.createDirectories(directoryPath);
									Path photoFile = Paths.get(directoryPath.toString(), photo.getName());
									Files.copy(photo.getPath(), photoFile, StandardCopyOption.REPLACE_EXISTING);
									System.out.println("photo " + photo.getOriginName() + " copied to " + directoryPath);
									copied = true;
									jpgCount++;
								} catch (Exception ex) {
									logger.error(ex);
								}
							}
							if (destination.getThumb()) {
								Path directoryPath = Paths.get(destination.getPath(), author, "thumb", photoPath);
								try {
									Files.createDirectories(directoryPath);
									Path dest = Paths.get(directoryPath.toString(), photo.getName());
									ThumbService.resizeImage(photo.getPath().toFile(), dest.toFile(), WIDTH, HEIGHT);
									System.out.println("photo " + photo.getOriginName() + " copied to " + directoryPath);
									copied = true;
									thumbCount++;
								} catch (Exception ex) {
									logger.error(ex);
								}
							}
							if (copied) {
								count++;
							}
						} else if (extension.equalsIgnoreCase("cr2")) {
							if (destination.getRaw()) {
								try {
									Path directoryPath = Paths.get(destination.getPath(), author, "raw", photoPath);
									Files.createDirectories(directoryPath);
									Path photoFile = Paths.get(directoryPath.toString(), photo.getName());
									System.out.println("photo " + photo.getOriginName() + " copied to " + directoryPath);
									Files.copy(photo.getPath(), photoFile, StandardCopyOption.REPLACE_EXISTING);
									count++;
									rawCount++;
								} catch (Exception ex) {
									logger.error(ex);
								}
							}
						}
						updateProgress(count + 1, total);
						updateMessage(String.valueOf(count) + " / " + total);
						destination.updateStatus(rawCount, jpgCount, thumbCount, rawTotal, jpgTotal);
					}
				}
				System.out.println((count) + " photos copied");*/
				return photos;
			}
		};
	}
}
