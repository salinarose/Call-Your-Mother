package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ContactSceneController implements Initializable {
	
	private int selected;
	Boolean[][] hours;
	
	/* Get the index of the selected contact. If it is -1, it is a new contact that has yet to be added */
	public void getSelection(int selected) {
		this.selected = selected;
		
		//System.out.println("Selected: " + selected);
		if (selected == -1) {
			// Adding new contact. All fields should be blank 
			System.out.println("new contact");
		}
		else {
			System.out.println("existing contact");
			Contact c = FileHelper.getContacts().get(selected);
			loadData(c);
		}
		// Initialize the availability grid
		initGrid();
	}
	
	/* Initializes scene */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set up choicebox
		initZoneChoices();
		
	}
	
	@FXML
	TextField tfName; 
	
	/* Loads fields for existing contacts */
	private void loadData(Contact c) {
		tfName.setText(c.getName());
		cbZone.setValue(c.getTimezone());
	}

	/* Sets up the availability grid */
	@FXML
	GridPane gridAvailability;
	
	public void initGrid() {
		this.hours = new Boolean[7][24];
		
		if (selected != -1) {
			Boolean[][] oldHours = FileHelper.getContacts().get(selected).getAvailability();
			for (int i = 0; i < 7; i++) {
				hours[i] = oldHours[i].clone();
			}
		}
		
		// Hours column
		for (int r = 1; r < 25; r++) {
			// Hours column
			gridAvailability.add(new Label((r - 1) + ":00"), 0, r);
			
			// CheckBoxes corresponding to hours array
			for (int c = 1; c < 8; c++) {
				CheckBox check = new CheckBox();
				int day = c - 1;
				int hr = r - 1;
				
				if (hours[day][hr] == null || hours[day][hr] == false) {
					check.setSelected(false);
				}
				else {
					check.setSelected(true);
				}
				
				gridAvailability.add(check, c, r);
				check.setOnMouseClicked(e -> {
					hours[day][hr] = check.isSelected();
				});
			}
		}
	}
	
	/* Initialize zone choice box */
	@FXML
	ChoiceBox<String> cbZone;
	String[] choices = {"zone 1", "zone 2", "zone 3", "zone 4", "zone 5"};
	private void initZoneChoices() {
		cbZone.getItems().addAll(choices);
		cbZone.setOnAction(this::getZone);
		// TODO: make it uneditable
	}
	
	/* Gets the selected value of the zone */
    public void getZone(ActionEvent event) {
        String zone = cbZone.getValue();
    }
    
    /* Saves contact data */
    public void saveContact(ActionEvent event) throws IOException {
    	if (selected == -1) {
    		// Save as a new contact and add to the arraylist
    		String name = tfName.getText();
    		String zone = cbZone.getValue();
    		Contact c = new Contact(name, zone);
    		c.setAvailability(hours);
    		FileHelper.getContacts().add(c);
    		
    		// other fields
    	}
    	else {
    		// Make changes to existing contact and no need to update the arraylist
    		Contact c = FileHelper.getContacts().get(selected);
    		c.setName(tfName.getText());
    		c.setTimezone(cbZone.getValue());
    		c.setAvailability(hours);
    		
    		// other fields
    	}
    	// return to main scene
    	goToMainScene();
    }
	
	/* Returns to main scene */
	public void goToMainScene() throws IOException {
		// Load main scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
 
        Stage stage = (Stage) gridAvailability.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Time Zone Manager");
        stage.show();
	}
}
