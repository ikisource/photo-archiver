package service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Destination;
import model.Photo;

public class CopyTestService extends Service<Long> {

	final static int WIDTH = 1080;
	final static int HEIGHT = 720;

	private static Logger logger = Logger.getLogger(CopyTestService.class);

	private Set<Photo> photos;
	private Destination destination;
	private String author;

	public void prepare(final Stream<Photo> photos, final Destination destination, final String author) {

		this.photos = photos.collect(Collectors.toSet());
		this.destination = destination;
		this.author = author;
	}

	@Override
	protected Task<Long> createTask() {

		return new Task<Long>() {

			@Override
			protected Long call() {

				long duration = 1000;
				long t0 = Instant.now().toEpochMilli();
				long count = 0;
				while (count < 10) {
					boolean elapsed = false;
					if (isCancelled()) {
						System.err.println("copy cancelled");
						break;
					}
					do {
						long now = Instant.now().toEpochMilli();
						if (now >= t0 + duration) {
							elapsed = true;
							t0 = now;
						}
					} while (!elapsed);
					updateProgress(count + 1, 10);
					updateMessage(String.valueOf(count) + " / " + 10);
					count++;
				}
				//}
				System.out.println("service terminé");
				return count;
			}
		};
	}
}
