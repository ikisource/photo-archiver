module omathe.gui {

	requires javafx.fxml;
	requires javafx.web;
	requires javafx.base;
	requires javafx.controls;
	requires java.desktop;
	requires transitive javafx.graphics;

	// automatic modules
	requires org.controlsfx.controls;
	requires com.fasterxml.jackson.databind;
	requires metadata.extractor;
	requires java.logging;
	requires log4j;

	opens controller to javafx.fxml;
	opens model to javafx.base, com.fasterxml.jackson.databind;
	exports omathe.gui to javafx.graphics;
}