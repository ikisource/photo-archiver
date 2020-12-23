package exception;

public class PhotoServiceException extends Exception {

	private static final long serialVersionUID = -4014898397502738276L;

	public PhotoServiceException(String text) {
		super(text);
	}
	
	public PhotoServiceException(String text, Throwable throwable) {
		super(text);
	}
}
