package com.salinasharudin.TimezoneManager;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScheduleController {

	/* Methods for leaving the current scene */
	
	/* Go to main scene */
	public void goToMainScene() {
		try {
			App.setRoot("Main");
		} catch (IOException e) {
			System.out.println("Error loading main scene from schedule scene.");
		}
	}
	
	/* Goes to the user settings scene */
	public void goToSettingsScene() {
		try {
			App.setRoot("SettingsScene");
		} catch (IOException e) {
			System.out.println("Error loading settings from schedule scene.");
		}
	}
}
