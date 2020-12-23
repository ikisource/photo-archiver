package exception;

public class MediaException extends Exception {

	private static final long serialVersionUID = -4014898397502738276L;

	public MediaException(String text) {
		super(text);
	}
	
	public MediaException(String text, Throwable throwable) {
		super(text);
	}
}
