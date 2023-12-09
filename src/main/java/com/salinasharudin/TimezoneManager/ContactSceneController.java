package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ContactSceneController implements Initializable {
	
	private int selected;
	
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
			//System.out.println(c.toString());
			loadData(c);
		}
	}
	
	/* Initializes scene */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Set up availability grid
		//initGrid();
		
		// Set up choicebox
		initZoneChoices();
		
	}
	
	@FXML
	TextField tfName; 
	
	/* Loads fields for existing contacts */
	private void loadData(Contact c) {
		tfName.setText(c.getName());
		cbZone.setValue(c.getTimezone());
		initGrid();
	}

	/* Sets up the availability grid */
	@FXML
	GridPane gridAvailability;
	
	public void initGrid() {
		System.out.println("grid: " + selected);
		Boolean[][] hours;
		
		if (selected == -1) {
			hours = new Boolean[7][24];
		}
		else {
			hours = FileHelper.getContacts().get(selected).getAvailability();
		}
		
		// Hours column
		for (int r = 1; r < 25; r++) {
			// Hours column
			gridAvailability.add(new Label((r - 1) + ":00"), 0, r);
			
			// CheckBoxes corresponding to hours array
			for (int c = 1; c < 8; c++) {
				CheckBox check = new CheckBox();
				
				if (hours[c - 1][r - 1] == null || hours[c - 1][r - 1] == false) {
					check.setSelected(false);
				}
				else {
					check.setSelected(true);
				}
				
				gridAvailability.add(check, c, r);
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
    		FileHelper.getContacts().add(c);
    		
    		// other fields
    	}
    	else {
    		// Make changes to existing contact and no need to update the arraylist
    		Contact c = FileHelper.getContacts().get(selected);
    		c.setName(tfName.getText());
    		c.setTimezone(cbZone.getValue());
    		
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
