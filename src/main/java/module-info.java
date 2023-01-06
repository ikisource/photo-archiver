module omathe.gui {

	requires javafx.fxml;
	requires javafx.web;
	requires javafx.base;
	requires transitive javafx.graphics;
	requires javafx.controls;
	
	// automatic modules
	requires org.controlsfx.controls;
	requires com.fasterxml.jackson.databind;
	requires metadata.extractor;
	requires java.logging;
	requires log4j;
	requires java.desktop;

	opens controller to javafx.fxml;
	opens model to javafx.base, com.fasterxml.jackson.databind;
	exports omathe.gui to javafx.graphics;
}