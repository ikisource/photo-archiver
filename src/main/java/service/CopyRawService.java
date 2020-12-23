package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Destination;
import model.Photo;
import org.apache.log4j.Logger;

/**
 * This task and copy raw photos into the destination.
 *
 * @author Olivier MATHE
 * @since 1.0
 */
public class CopyRawService extends Service<Long> {

    private static Logger logger = Logger.getLogger(CopyRawService.class);

    private Set<Photo> photos;
    private Destination destination;
    private String author;

    public void prepare(final Stream<Photo> photos, final Destination destination, final String author) {

        this.photos = photos.collect(Collectors.toSet());
        this.destination = destination;
        this.author = author;
    }

    @Override
    protected Task<Long> createTask() {

        return new Task<Long>() {

            @Override
            protected Long call() {

                long count = photos.size();
                int i = 0;
                for (Photo photo : photos) {
                    i++;
                    logger.debug(photo);
                    String photoPath = photo.buildDirectoryName();
                    Path directoryPath = Paths.get(destination.getPath(), author, "raw", photoPath);
                    
                    Path dest = Paths.get(destination.getPath(), "", photo.getName());
                    try {
                        Files.createDirectories(directoryPath);
                    
                        Path photoFile = Paths.get(directoryPath.toString(), photo.getName());
                        System.out.println("photoFile = " + photoFile);
                        
                        Files.copy(photo.getPath(), photoFile, StandardCopyOption.REPLACE_EXISTING);
                        updateProgress(i, count);
                        updateMessage(String.valueOf(i) + " / " + count);
                    } catch (IOException ex) {
                        logger.error(ex);
                    }
                }
                return count;
            }
        };
    }
}
