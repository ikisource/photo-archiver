package model;

/**
 *
 * @author Olivier MATHE
 */
public class PhotoMetadata {

    private String camera;
    private Long date;
    private String subSeconds;

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getSubSeconds() {
        return subSeconds;
    }

    public void setSubSeconds(String subSeconds) {
        this.subSeconds = subSeconds;
    }

    @Override
    public String toString() {
        return "PhotoMetadata{" + "camera=" + camera + ", date=" + date + ", subSeconds=" + subSeconds + '}';
    }

}
