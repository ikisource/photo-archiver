package service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import exception.MetadataExtractorException;
import java.util.Date;
import java.util.TimeZone;

public class PhotoMetadataExtractorDrew implements MetadataExtractor {

    private static Logger logger = Logger.getLogger(PhotoMetadataExtractorDrew.class);

    private Metadata metadata;

    public PhotoMetadataExtractorDrew(final InputStream inputStream) throws MetadataExtractorException {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream must be defined.");
        }
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        try {
            this.metadata = ImageMetadataReader.readMetadata(bis);
        } catch (ImageProcessingException | IOException e) {
            logger.error("Failed to initialize metadata from this file.");
            throw new MetadataExtractorException("Failed to initialize metadata from this file.");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("Failed to close the stream.");
                }
            }
        }
    }

    @Override
    public String getCameraModel() {
//		String cameraModel = MetadataExtractor.DEFAULT_CAMERA;
//		ExifIFD0Directory exifIFD0Directory = metadata.getDirectory(ExifIFD0Directory.class);
//		if (exifIFD0Directory != null && exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL) != null) {
//			cameraModel = exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL);
//		}
//		return cameraModel;
        return "toto";
    }

    @Override
    public Long getDate() {

        Long originalDate = null;

        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getTimeZone("Europe/Paris"));
        if (date != null) {
            originalDate = date.getTime();
        }
        return originalDate;
    }
//	@Override
//	public String getDate() {
//		
//		String date = null;
//                
//		ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
//		if (directory != null && directory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) != null) {
//			String dateTag = directory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
//			if (dateTag != null && !dateTag.isEmpty()) {
//				if (!Pattern.matches(DATE_TEMPLATE, dateTag)) {
//					throw new IllegalArgumentException("Date " + date + " format is not valid, YYYY:MM:DD HH:MM:SS is required.");
//				}
//				date = dateTag;
//			}
//			
//			// fraction of seconds
//			String subSecTime = directory.getString(ExifSubIFDDirectory.TAG_SUBSECOND_TIME);
//			if (subSecTime == null || subSecTime.isEmpty()) {
//				date += ",000";
//			}
//			else {
//				NumberFormat numberFormat = new DecimalFormat("000");
//				date += ","+ numberFormat.format(Short.valueOf(subSecTime));
//			}
//		}
//		return date;
//	}
}
