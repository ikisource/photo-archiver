package service;

import exception.MetadataExtractorException;

public interface MetadataExtractor {
	
	String DEFAULT_CAMERA = "UNIDENTIFIED_CAMERA";
	String DATE_TEMPLATE = "\\d{4}:\\d{2}:\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
	
	/**
	 * Retrieves the camera model from the metadata.
	 * @return A string representing the camera model
	 */
	String getCameraModel();
	
	/**
	 * Retrieves the date from the metadata.
	 * @return A string representing the date matching this format YYYY:MM:DD HH:MM:SS
	 * @throws MetadataExtractorException 
	 */
	Long getDate() throws MetadataExtractorException;
}
