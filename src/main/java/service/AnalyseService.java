package service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import configuration.ConfigurationManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.DrewPhotoMetadataExtractor;
import model.Photo;
import model.PhotoMetadata;
import model.PhotoMetadataExtractor;

public class AnalyseService extends Service<List<Photo>> {

	private List<Path> paths;
	private PhotoMetadataExtractor metadataExtractor;

	public AnalyseService() {
		super();
		this.metadataExtractor = new DrewPhotoMetadataExtractor();
		;
	}

	public void setPaths(List<Path> paths) {
		this.paths = paths;
	}

	@Override
	protected Task<List<Photo>> createTask() {

		return new Task<List<Photo>>() {

			@Override
			protected List<Photo> call() {

				long total = paths.size();
				List<Photo> photos = new ArrayList<>();
				Map<String, Integer> counters = new HashMap<>();
				paths.forEach(path -> {
					PhotoMetadata metadata = metadataExtractor.extract(path);
					Photo photo = new Photo(true, path.getFileName().toString(), path, metadata);
					if (metadata.getValid()) {
						photo.formatName();
						String extension = photo.getExtension().toUpperCase();
						if (photo.getDate() == null) {
							counters.put(ConfigurationManager.ANALYSE_NO_DATE, counters.getOrDefault(ConfigurationManager.ANALYSE_NO_DATE, 0) + 1);
						} else {
							counters.put(extension, counters.getOrDefault(extension, 0) + 1);
						}
					} else {
						counters.put(ConfigurationManager.ANALYSE_INVALID, counters.getOrDefault(ConfigurationManager.ANALYSE_INVALID, 0) + 1);
					}
					photos.add(photo);
					long count = counters.values().stream().reduce(0, Integer::sum);
					updateProgress(count, total);
					updateMessage(displayCounters(total, counters));
				});
				long count = counters.values().stream().reduce(0, Integer::sum);
				System.out.println(count + " photos analysées");
				return photos;
			}
		};
	}

	private String displayCounters(long total, Map<String, Integer> counters) {

		String text = String.valueOf(total);
		text += total == 1 ? " photo analysée ( " : " photos analysées (";
		text += counters.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", "));
		text += ")";
		return text;
	}
}
