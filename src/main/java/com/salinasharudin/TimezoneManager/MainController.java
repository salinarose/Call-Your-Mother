package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainController implements Initializable {
	
	// variables that control selection and de-selection
	public int selected = -1;
	Boolean b = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/* Calls display methods */
		displayLocalDateTime();
		displayContacts();
		
		/* Shows an alert to the user if there is an error loading the contact file */
		if (FileHelper.contactFileSuccess == false) {
			FileHelper.showFileAlert();
			// Change variable so that the alert only shows once after attempting to load the file
			FileHelper.contactFileSuccess = true;
		}
	}

	// Creates a local clock using the time zone specified in user's settings
	private ZonedDateTime clockNow = ZonedDateTime.now(Settings.getInstance().getZone());
	private DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm a");
	private DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd MMM yyyy");
	
	@FXML
	Label lblLocalLocation;
	@FXML
	Label lblLocalTimeZone;
	@FXML
	Label lblLocalTime;
	@FXML
	Label lblLocalDay;
	@FXML
	Label lblLocalDate;
	
	/* Displays a local clock, date, location, and time zone */
	public void displayLocalDateTime() {
		lblLocalLocation.setText(clockNow.getZone().toString());
		lblLocalTimeZone.setText(clockNow.format(DateTimeFormatter.ofPattern("z")));
		lblLocalTime.setText(clockNow.format(formatTime));
		lblLocalDay.setText(clockNow.getDayOfWeek().toString());
		lblLocalDate.setText(clockNow.format(formatDate));
		
		// Obtain offset from clock
		//String offset = clockNow.getOffset().toString();
	
	}
	
	@FXML
	GridPane gridPaneContacts;
	@FXML
	Button btnEdit;
	
	@FXML
	/* Displays the user's list of contacts */
	public void displayContacts() {
		// Get the contact list
		ArrayList<Contact> list = FileHelper.getContacts();
		
		// Clear the grid pane
		gridPaneContacts.getChildren().clear();
		
		// Populate the grid pane with the new contacts
		int i = 0;
		for (Contact c : list) {
			Label name = new Label(c.getName());
			name.setPrefWidth(60);
			Label zone = new Label(c.getTimezone());
			zone.setPrefWidth(150);
			gridPaneContacts.addRow(i, name, zone);
			i++;
		}
		
		/* Events for hovering over or selecting contacts */
		for (Node node : gridPaneContacts.getChildren()) {
		    node.setOnMouseEntered(e -> gridPaneContacts.getChildren().forEach(r -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        if (selected != targetIndex && GridPane.getRowIndex(r) == targetIndex) {
		            r.setStyle("-fx-background-color:#FFFFFF;");
		        }
		    }));
		    node.setOnMouseExited(e -> gridPaneContacts.getChildren().forEach(r -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        if (selected != targetIndex && GridPane.getRowIndex(r) == targetIndex) {
		            r.setStyle("-fx-background-color:#F3F3F3;");
		        }
		    }));
		    node.setOnMouseClicked(e -> gridPaneContacts.getChildren().forEach(r -> {
			        Integer targetIndex = GridPane.getRowIndex(node);
			        
			        if (GridPane.getRowIndex(r) == targetIndex) {
			        	// De-selects if target is the currently selected item
			        	if (b == true || selected == targetIndex && GridPane.getColumnIndex(r) == 0) {
			        		r.setStyle("-fx-background-color:#F3F3F3;");
			        		
			        		// Last column
			        		if (GridPane.getColumnIndex(r) == gridPaneContacts.getColumnCount() -1) {
			        			b = false; // reset boolean to false
			        			btnEdit.setDisable(true);
			        			selected = -1;
			        		}
			        		else {
			        			b = true;
			        		}
			        	}
			        	// Item was not already selected
			        	else {
				        	// Get the item selected
				            selected = targetIndex;
				            btnEdit.setDisable(false);
				            r.setStyle("-fx-background-color:#cccccc;");
				            b = false;
			        	}
			        }
			        // Item was not the target of the click
			        else {
			        	r.setStyle("-fx-background-color:#F3F3F3;");   
			        }
			}));
		}
		
	}
	
	public void btnAddClicked() throws IOException {
		selected = -1;
		goToContactScene();
	}
	
	/* When add new or edit button clicked, switches to the contact scene */
	public void goToContactScene() throws IOException {
        // Load contact editing scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ContactScene.fxml"));
        Parent root = loader.load();
 
        // Get controller of scene2
        ContactSceneController scene2Controller = loader.getController();
        
        // Pass selection index to the contact edit scene
        scene2Controller.getSelection(selected);
 
        
        // Load scene2 in same window
        // I could've used any node in this scene below, I chose btnEdit this time
        Stage stage = (Stage) btnEdit.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Edit Contact");
        stage.show();
    }
	
	/* Goes to the user settings scene */
	public void goToSettingsScene() {
		// TODO: add confirmation alert / save changes first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsScene.fxml"));
        Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnEdit.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Settings");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Go to schedule scene */
	public void goToScheduleScene() {
		try {
			App.setRoot("ScheduleScene");
		} catch (IOException e) {
			System.out.println("Error loading schedule scene from main scene.");
		}
	}
    
}


