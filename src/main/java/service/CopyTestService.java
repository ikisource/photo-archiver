package service;

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

				long count = 10;
				for (int i = 0; i < count; i++) {
					if (isCancelled()) {
						System.err.println("copy cancelled");
						break;
					}
					try {
						Thread.sleep(1000);
						updateProgress(i+1, count);
						updateMessage(String.valueOf(i) + " / " + count);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return count;
			}
		};
	}
}
