package service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Destination;
import model.Photo;

public class CopyService extends Service<Long> {

	private static Logger logger = Logger.getLogger(CopyService.class);
	private final static int WIDTH = 1080;
	private final static int HEIGHT = 720;
	private List<Photo> photos;
	private Destination destination;
	private String author;

	public CopyService() {
		super();
	}

	public CopyService(List<Photo> photos, Destination destination, String author) {
		super();
		this.photos = photos;
		this.destination = destination;
		this.author = "";
		Comparator c;
	}

	@Override
	protected Task<Long> createTask() {

		return new Task<Long>() {

			@Override
			protected Long call() {

				long count = 0;
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
				System.out.println((count) + " photos copied");
				return count;
			}
		};
	}
}
