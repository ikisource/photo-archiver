module fr.omathe.photoarchiver {

	requires javafx.fxml;
	requires javafx.web;
	
	// automatic modules
	requires javafx.base;
	requires transitive javafx.graphics;
	requires javafx.controls;
	requires org.controlsfx.controls;
	requires metadata.extractor;
	requires java.logging;
	requires log4j;
	requires java.desktop;

	opens controller to javafx.fxml;
	opens model to javafx.base;
	exports omathe.gui to javafx.graphics;
}