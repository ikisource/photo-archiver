package model;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Configuration {

	private StringProperty source;
	private StringProperty filter;
	private BooleanProperty includeSubDirectories;
	private ObservableList<Destination> destinations;

	public Configuration() {
		this.source = new SimpleStringProperty();
		this.filter = new SimpleStringProperty();
		this.includeSubDirectories = new SimpleBooleanProperty(true);
		this.destinations = FXCollections.observableArrayList();
	}

	public Configuration(String filter, Boolean includeSubDirectories) {
		super();
		this.source = new SimpleStringProperty();
		this.filter = new SimpleStringProperty(filter);
		this.includeSubDirectories = new SimpleBooleanProperty(includeSubDirectories);
		this.destinations = FXCollections.observableArrayList();
	}

	public String getSource() {
		return source.get();
	}

	public void setSource(String source) {
		this.source.set(source);
	}
	
	public StringProperty sourceProperty() {
		return source;
	}

	public String getFilter() {
		return filter.get();
	}

	public void setFilter(String filter) {
		this.filter.set(filter);
	}
	
	public StringProperty filterProperty() {
		return filter;
	}

	public Boolean getIncludeSubDirectories() {
		return includeSubDirectories.get();
	}

	public void setIncludeSubDirectories(Boolean includeSubDirectories) {
		this.includeSubDirectories.set(includeSubDirectories);
	}
	
	public BooleanProperty includeSubDirectoriesProperty() {
		return includeSubDirectories;
	}

	public ObservableList<Destination> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<Destination> destinations) {
		if (destinations != null) {
			this.destinations = FXCollections.observableArrayList(destinations);
		}
	}

	@Override
	public String toString() {
		return "Configuration [source=" + source.get() + ", filter=" + filter.get() + ", includeSubDirectories=" + includeSubDirectories.get() + ", destinations=" + destinations + "]";
	}

}
