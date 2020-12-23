package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Destination;
import model.Photo;

/**
 * This task creates a thumb copy of the photos and copy them into the
 * destination.
 *
 * @author Olivier MATHE
 * @since 1.0
 */
public class CopyThumbService extends Service<Long> {
    
    final static int WIDTH = 1080;
    final static int HEIGHT = 720;
    
    private static Logger logger = Logger.getLogger(CopyThumbService.class);
    
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
                    if (isCancelled()) {
                        logger.info("copy cancelled");
                        break;
                    }
                    i++;
                    logger.debug(photo);
                    String photoPath = photo.buildDirectoryName();
                    Path directoryPath = Paths.get(destination.getPath(), author, "thumb", photoPath);

                    try {
                        Files.createDirectories(directoryPath);
                        Path dest = Paths.get(directoryPath.toString(), photo.getName());
                        
                        ThumbService.resizeImage(photo.getPath().toFile(), dest.toFile(), WIDTH, HEIGHT);
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
