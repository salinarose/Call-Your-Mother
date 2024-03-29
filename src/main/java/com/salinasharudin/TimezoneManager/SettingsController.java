package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

	@FXML
	TextField tfUsername;
	private Boolean[][] hours; // temporary array that holds schedule until save confirmed

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfUsername.setText(Settings.getInstance().getUsername());

		initZoneChoices();
		cbZone.setValue(Settings.getInstance().getZone().toString());

		initThemeChoices();
		cbTheme.setValue(Settings.getInstance().getTheme());

		initGrid();
	}

	/* Initialize zone choice box */
	@FXML
	ChoiceBox<String> cbZone;

	private void initZoneChoices() {
		List<String> options = ZoneId.getAvailableZoneIds().stream().map(zone -> zone.toString()).sorted()
				.collect(Collectors.toList());

		cbZone.getItems().addAll(options);
		cbZone.setOnAction(this::getZone);
	}

	@FXML
	Label lblTime = new Label();

	/* Update time and location labels */
	public void updateLabels() {
		if (cbZone.getValue() != null) {
			try {
				ZoneId zone = ZoneId.of(cbZone.getValue());
				ZonedDateTime time = ZonedDateTime.now(zone);
				lblTime.setText("Current time: " + time.format(DateTimeFormatter.ofPattern("hh:mm a")).toString());
			} catch (DateTimeException e) {
				System.out.println("date time exception");
			}
		} else {
			lblTime.setText("Time: ");
		}
	}

	/* Gets the selected value of the zone */
	public void getZone(ActionEvent event) {
		updateLabels();
	}

	/* Initialize theme choice box */
	@FXML
	ChoiceBox<String> cbTheme;

	private void initThemeChoices() {
		String[] options = { "theme-default.css", "theme-dark.css" };
		cbTheme.getItems().addAll(options);
		cbTheme.setOnAction(this::getTheme);
	}

	/* Gets the selected theme */
	public String getTheme(ActionEvent event) {
		String theme = cbTheme.getValue();
		if (theme == "light") {
			return "theme-default.css";
		} else if (theme == "dark") {
			return "theme-dark.css";
		} else
			return "theme-default.css";
	}

	/* Clears all fields */
	public void clearAll() {
		tfUsername.setText(null);
		cbZone.setValue(null);
		cbTheme.setValue("default");
		noneAvailable();
	}

	/* Reset all fields */
	public void resetAll() {
		tfUsername.setText(Settings.getInstance().getUsername());
		cbZone.setValue(Settings.getInstance().getZone().toString());
		cbTheme.setValue(Settings.getInstance().getTheme());
		this.resetGrid();
	}

	/* Sets up the schedule grid */
	@FXML
	GridPane grid;

	public void initGrid() {
		this.hours = new Boolean[7][24];

		Boolean[][] oldHours = Settings.getInstance().getSchedule();
		for (int i = 0; i < 7; i++) {
			hours[i] = oldHours[i].clone();
		}

		makeGrid();
	}

	public void makeGrid() {
		grid.getChildren().clear();

		// Day of the week labels
		grid.add(new Label("S"), 1, 0);
		grid.add(new Label("M"), 2, 0);
		grid.add(new Label("T"), 3, 0);
		grid.add(new Label("W"), 4, 0);
		grid.add(new Label("T"), 5, 0);
		grid.add(new Label("F"), 6, 0);
		grid.add(new Label("S"), 7, 0);

		// Hours column
		for (int r = 1; r < 25; r++) {
			// Hours column
			grid.add(new Label((r - 1) + ":00"), 0, r);

			// CheckBoxes corresponding to hours array
			for (int c = 1; c < 8; c++) {
				CheckBox check = new CheckBox();
				int day = c - 1;
				int hr = r - 1;

				if (hours[day][hr] == null || hours[day][hr] == false) {
					check.setSelected(false);
				} else {
					check.setSelected(true);
				}

				grid.add(check, c, r);
				check.setOnMouseClicked(e -> {
					hours[day][hr] = check.isSelected();
				});
			}
		}
	}

	/* Button actions for changing availability grid */
	/* Reset to default */
	public void resetGrid() {
		initGrid();
	}

	/* Change all to available */
	public void allAvailable() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 24; j++) {
				hours[i][j] = true;
			}
		}
		makeGrid();
	}

	/* Change all to none available */
	public void noneAvailable() {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 24; j++) {
				hours[i][j] = false;
			}
		}
		makeGrid();
	}

	/* Save all changes */
	public void saveAll() {
		if (tfUsername.getText() != null && cbZone.getValue() != null && cbTheme.getValue() != null) {
			Settings s = Settings.getInstance();
			s.setUsername(tfUsername.getText());
			s.setTheme(cbTheme.getValue());
			s.setZone(ZoneId.of(cbZone.getValue()));
			s.setSchedule(hours);

			FileHelper.writeSettingsData();
			goToSettingsScene();

		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setHeaderText("Settings not saved.");
			alert.setContentText("Username, zone, and theme must not be blank.");
			alert.show();
		}
	}

	@FXML
	Button btnBack;

	public void goBackClicked() {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirm");
		confirmAlert.setContentText("Are you sure you want to leave? Any changes you've made will not be saved.");
		confirmAlert.showAndWait().ifPresent((btnType) -> {

			if (btnType == ButtonType.OK) {
				goToMainScene();
			}
		});
	}

	/* Returns to main scene */
	public void goToMainScene() {
		// Load main scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnBack.getScene().getWindow();

			setTheme(root, stage);

			stage.setTitle("Time Zone Manager");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Goes to the user settings scene */
	public void goToSettingsScene() {
		// TODO: add confirmation alert / save changes first
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsScene.fxml"));
		Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnBack.getScene().getWindow();

			setTheme(root, stage);

			stage.setTitle("Settings");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Set the theme */
	private void setTheme(Parent root, Stage stage) {
		String theme = Settings.getInstance().getTheme();

		Scene scene = new Scene(root);
		String currentTheme = this.getClass().getResource(theme).toExternalForm();
		scene.getStylesheets().add(currentTheme);

		stage.setScene(scene);
	}

	/* save all files */
	public void saveAllFiles() {
		saveAll();
		FileHelper.saveAll();
	}

	/* close the program */
	public void close() {
		App.exit((Stage) btnBack.getScene().getWindow());
	}

}
