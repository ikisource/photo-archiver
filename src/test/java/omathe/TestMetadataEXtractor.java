package omathe;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Test;

/**
 *
 * @author olivier MATHE
 */
public class TestMetadataEXtractor {
    
    @Test
    public void getDate() throws IOException, ImageProcessingException {
        
        File jpegFile = new File("/media/DATA/dev/photo-archiver/src/test/resources/2015-11-01_16-51-17,082.jpg");
        Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
        
        Long originalDate = null;

        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getTimeZone("Europe/Paris"));
        System.out.println("date = " + date);
        if (date != null) {
            originalDate = date.getTime();
        }
        System.out.println("originalDate = " + originalDate);
    }
    
}
