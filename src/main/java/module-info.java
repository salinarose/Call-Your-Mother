module com.salinasharudin.TimezoneManager {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.salinasharudin.TimezoneManager to javafx.fxml;
    exports com.salinasharudin.TimezoneManager;
}
