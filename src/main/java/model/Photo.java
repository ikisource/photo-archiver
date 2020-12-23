package model;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Olivier MATHE
 */
public class Photo {
    
    private static final String DATE_FORMAT = "yyyy:MM:dd kk:mm:ss";
    private static final String DESTINATION_DIRECTORY_PATH = "yyyy/MM";

    private BooleanProperty enabled;
    private StringProperty originName;
    private StringProperty name;
    private Path path;
    private StringProperty date;
    private StringProperty camera;
    private PhotoMetadata metadata;

    public Photo(Boolean enabled, String name, Path path, PhotoMetadata metadata) {
        this.enabled = new SimpleBooleanProperty(enabled);
        this.name = new SimpleStringProperty(name);
        this.originName = new SimpleStringProperty(path.getFileName().toString());
        this.path = path;
        this.metadata = metadata;
        this.date = new SimpleStringProperty(new SimpleDateFormat(DATE_FORMAT).format(metadata.getDate()));
        this.camera = new SimpleStringProperty(metadata.getCamera());
    }

    public Boolean getEnabled() {
        return enabled.get();
    }

    public void setEnabled(Boolean enabled) {
        this.enabled.set(enabled);
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public String getOriginName() {
        return originName.get();
    }

    public void setOriginName(String originName) {
        this.originName.set(originName);
    }

    public StringProperty originNameProperty() {
        return originName;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty cameraProperty() {
        return camera;
    }

    public Path getPath() {
        return path;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public String getExtension() {

        String[] split = originName.get().split("\\.");
        return split[1] == null ? "" : split[1];
    }

    public void formatName() {
        String[] split = path.getFileName().toString().split("\\.");
        String extension = split[1].toLowerCase();
        this.name.set(date.get().replaceAll(":", "-").replace(" ", "_") + "," + metadata.getSubSeconds() + "." + extension);
    }

    public String buildDirectoryName() {
        Instant instant = Instant.ofEpochMilli(metadata.getDate());
        //int m = instant.get(ChronoField.MONTH_OF_YEAR);

        
        // 2015:11
        String[] split = date.get().split(":");
        
        System.out.println("directory : " + split[0] + File.separator + split[1]);
        return split[0] + File.separator + split[1];
    }

    @Override
    public String toString() {
        return "Photo{" + "enabled=" + enabled + ", originName=" + originName + ", name=" + name + ", path=" + path + ", date=" + date + ", camera=" + camera + ", metadata=" + metadata + '}';
    }
    
}
