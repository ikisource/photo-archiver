package model;

import java.nio.file.Path;

/**
 *
 * @author Olivier MATHE
 */
public interface PhotoMetadataExtractor {
    
    PhotoMetadata extract(Path path);
    
}
