module com.salinasharudin.TimezoneManager {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.json;
	requires com.fasterxml.jackson.databind;

	opens com.salinasharudin.TimezoneManager to javafx.graphics, org.json, javafx.fxml, com.fasterxml.jackson.databind;

	exports com.salinasharudin.TimezoneManager;
}
