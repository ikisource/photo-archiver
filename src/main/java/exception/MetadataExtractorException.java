package exception;

public class MetadataExtractorException extends Exception {

	private static final long serialVersionUID = -4014898397502738276L;

	public MetadataExtractorException(String text) {
		super(text);
	}
	
	public MetadataExtractorException(String text, Throwable throwable) {
		super(text);
	}
}
