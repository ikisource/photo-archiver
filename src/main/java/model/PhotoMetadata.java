package model;

/**
 *
 * @author Olivier MATHE
 */
public class PhotoMetadata {

	private Boolean valid;
	private String camera;
	private Long date;
	private String subSeconds;

	public PhotoMetadata() {
		super();
		this.valid = true;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

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
		return "PhotoMetadata [valid=" + valid + ", camera=" + camera + ", date=" + date + ", subSeconds=" + subSeconds + "]";
	}

}
