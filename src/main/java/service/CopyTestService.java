package service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.log4j.Logger;

import controller.DestinationActionCellFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Destination;
import model.Photo;

public class CopyTestService extends Service<Long> {

	private static Logger logger = Logger.getLogger(CopyTestService.class);
	final static int WIDTH = 1080;
	final static int HEIGHT = 720;
	private DestinationActionCellFactory destinationActionCellFactory;
	private List<Photo> photos;
	private Destination destination;
	private String author;

	public CopyTestService() {
		super();
	}

	public CopyTestService(DestinationActionCellFactory destinationActionCellFactory, List<Photo> photos, Destination destination, String author) {
		super();
		this.destinationActionCellFactory = destinationActionCellFactory;
		this.photos = photos;
		this.destination = destination;
		this.author = "";
	}

	//	public void prepare(final Stream<Photo> photos, final Destination destination, final String author) {
	//
	//		this.photos = photos.collect(Collectors.toList());
	//		this.destination = destination;
	//		this.author = author;
	//	}

	@Override
	protected Task<Long> createTask() {

		return new Task<Long>() {

			@Override
			protected Long call() {

				long count = 0;
				long total = photos.stream().filter(p -> p.getEnabled()).count();
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
								} catch (Exception ex) {
									logger.error(ex);
								}
							}
						}
						updateProgress(count + 1, total);
						updateMessage(String.valueOf(count) + " / " + total);
					}

				}

				/*long duration = 1000;
				long t0 = Instant.now().toEpochMilli();
				long count2 = 0;
				while (count2 < 10) {
					boolean elapsed = false;
					if (isCancelled()) {
						System.err.println("copy cancelled");
						break;
					}
					do {
						long now = Instant.now().toEpochMilli();
						if (now >= t0 + duration) {
							elapsed = true;
							t0 = now;
						}
					} while (!elapsed);
					updateProgress(count2 + 1, 10);
					updateMessage(String.valueOf(count2) + " / " + 10);
					count2++;
				}*/
				//}
				System.out.println((count) + " photos copied");
				return count;
			}
		};
	}
}
