package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController implements Initializable {

	@FXML 
	TextField tfUsername;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tfUsername.setText(Settings.getInstance().getUsername());
		System.out.println("username: " + Settings.getInstance().getUsername());
		System.out.println("zone: " + Settings.getInstance().getZone());
		
	}
	
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
	
	@FXML
	Button btnBack;
	
	/* Returns to main scene */
	public void goToMainScene() {
		// Load main scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnBack.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Time Zone Manager");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
