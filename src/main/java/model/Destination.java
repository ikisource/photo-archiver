package model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Olivier MATHE
 */
public class Destination {

	private StringProperty name;
	private StringProperty path;
	private BooleanProperty raw;
	private BooleanProperty jpg;
	private BooleanProperty thumb;
	private StringProperty status;

	public Destination(Boolean enabled, String name, Path path, Boolean raw, Boolean jpg, Boolean thumb) {
		this.name = new SimpleStringProperty(name);
		this.path = new SimpleStringProperty(path.toString());
		this.raw = new SimpleBooleanProperty(raw);
		this.jpg = new SimpleBooleanProperty(jpg);
		this.thumb = new SimpleBooleanProperty(thumb);
		this.status = new SimpleStringProperty();
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public String getPath() {
		return path.get();
	}

	public void setPath(String path) {
		this.path.set(path);
	}

	public StringProperty pathProperty() {
		return path;
	}

	public Boolean getRaw() {
		return raw.get();
	}

	public void setRaw(Boolean raw) {
		this.raw.set(raw);
	}

	public BooleanProperty rawProperty() {
		return raw;
	}

	public Boolean getJpg() {
		return jpg.get();
	}

	public void setJpg(Boolean jpg) {
		this.jpg.set(jpg);
	}

	public BooleanProperty jpgProperty() {
		return jpg;
	}

	public Boolean getThumb() {
		return thumb.get();
	}

	public void setThumb(Boolean thumb) {
		this.thumb.set(thumb);
	}

	public BooleanProperty thumbProperty() {
		return thumb;
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public StringProperty statusProperty() {
		return status;
	}

	public boolean exists() {
		Path p = Paths.get(path.get());
		return p.toFile().exists();
	}

	public void create() {
		if (!exists()) {
			Path p = Paths.get(path.get());
			p.toFile().mkdir();
		}
	}

	public void updateStatus(int rawCount, int jpgCount, int thumbCount, long rawTotal, long jpgTotal) {
		String statusRaw = "";
		String statusJpg = "";
		String statusThumb = "";
		if (raw.get()) {
			statusRaw += "RAW: " + rawCount + "/" + rawTotal;
		}
		if (jpg.get()) {
			statusJpg = "JPG: " + jpgCount + "/" + jpgTotal;
		}
		if (thumb.get()) {
			statusThumb = "Vignettes: " + thumbCount + "/" + jpgTotal;
		}
		setStatus(Stream.of(statusRaw, statusJpg, statusThumb)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.joining(", ")));
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + Objects.hashCode(this.path);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Destination other = (Destination) obj;
		if (!Objects.equals(this.path, other.path)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Destination{" + "name=" + name.get() + ", path=" + path.get() + ", raw=" + raw.get() + ", jpg=" + jpg.get() + ", thumb=" + thumb.get() + '}';
	}

}
