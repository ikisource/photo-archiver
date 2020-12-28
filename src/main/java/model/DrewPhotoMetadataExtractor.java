/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

/**
 * @author olivier
 */
public class DrewPhotoMetadataExtractor implements PhotoMetadataExtractor {

    @Override
    public PhotoMetadata extract(Path path) {

        PhotoMetadata photoMetadata = new PhotoMetadata();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            if (directory != null) {
                // date
                Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (date != null) {
                    photoMetadata.setDate(date.getTime());
                }
                // camera
                String camera = directory.getString(ExifSubIFDDirectory.TAG_MODEL);
                if (camera == null) {
                    photoMetadata.setCamera(getTagValue(metadata, "Model"));
                }
                // subSec
                String subSec = directory.getString(ExifSubIFDDirectory.TAG_SUBSECOND_TIME);
                photoMetadata.setSubSeconds(subSec == null ? "00" : subSec);
            }
            return photoMetadata;
        } catch (ImageProcessingException | IOException ex) {
            //Logger.getLogger(DrewPhotoMetadataExtractor.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("La photo " + path + " n'est pas exploitable");
        }
        return photoMetadata;
    }

    private String getTagValue(Metadata metadata, String tagKey) {

        String value = "unknown";

        search:
        for (Directory dir : metadata.getDirectories()) {
            for (Tag tag : dir.getTags()) {
                if (tag.getTagName().equals(tagKey)) {
                    value = tag.getDescription();
                    break search;
                }
            }
        }
        return value;
    }

}
