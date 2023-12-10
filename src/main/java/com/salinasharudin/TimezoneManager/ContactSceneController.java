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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ContactSceneController implements Initializable {
	
	private int selected;
	private Boolean[][] hours;
	
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
		makeGrid();
	}
	
	public void makeGrid() {
		gridAvailability.getChildren().clear();
		
		// Day of the week labels
		gridAvailability.add(new Label("S"), 1, 0);
		gridAvailability.add(new Label("M"), 2, 0);
		gridAvailability.add(new Label("T"), 3, 0);
		gridAvailability.add(new Label("W"), 4, 0);
		gridAvailability.add(new Label("T"), 5, 0);
		gridAvailability.add(new Label("F"), 6, 0);
		gridAvailability.add(new Label("S"), 7, 0);
		
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
    
    /* Clears all fields */
    public void clearAll() {
    	tfName.setText(null);
    	cbZone.setValue(null);
    	noneAvailable();
    	
    	// other fields
    	//image
    	//scheduled calls
    }
    
    public void resetAll() {
    	Contact c = FileHelper.getContacts().get(selected);
    	tfName.setText(c.getName());
    	cbZone.setValue(c.getTimezone());
    	this.resetGrid();
    	
    	// any other fields
    }
    
    /* Saves contact data */
    public void saveContact(ActionEvent event){
    	// TODO: Currently still allows if the name is a whitespace character
    	if (tfName.getText() == null || cbZone.getValue() == null ||
    			tfName.getText() == "") {
    		
    		// Alert informs user that the contact can not be saved if necessary fields are empty
    		Alert saveAlert = new Alert(Alert.AlertType.INFORMATION);
    		saveAlert.setTitle("Save Error");
    		saveAlert.setHeaderText("Name and zone can not be empty.");
    		saveAlert.showAndWait();
    	}
    	else {
    		// System alert asks user for confirmation to save changes
    		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
    		confirmAlert.setTitle("Confirm Changes");
    		confirmAlert.setHeaderText("Contact info may have changed. Are you sure you want to save these changes?");
    		confirmAlert.showAndWait().ifPresent((btnType) -> {
    			
    		  if (btnType == ButtonType.OK) {
    			// Saves as new contact
    		    if (selected == -1) {
    		    	// Save as a new contact and add to the arraylist
    		    	String name = tfName.getText();
    		    	String zone = cbZone.getValue();
    		    	Contact c = new Contact(name, zone);
    		    	c.setAvailability(hours);
    		    	FileHelper.getContacts().add(c);
    		    	
    		    	// other fields
    		    }
    		    // Saves changes to existing contact
    		    else {
    		    	// Make changes to existing contact and no need to update the arraylist
    		    	Contact c = FileHelper.getContacts().get(selected);
    		    	c.setName(tfName.getText());
    		    	c.setTimezone(cbZone.getValue());
    		    	c.setAvailability(hours);
    		    	
    		    	// other fields
    		    }
    		    System.out.println("Changes saved.");
    		    /* TODO: I want to change this so that it doesn't go back to the main scene. However, for new contacts I will
    		     * need to change the selected value so that subsequent changes do not lead to multiple new new contacts. I 
    		     * also would like the alert to have a slightly different message.
    		     */
    		    // return to main scene
				goToMainScene();
    		  } else if (btnType == ButtonType.CANCEL) {
    			  System.out.println("Changes not saved.");
    		  }
    		});
    	}
    }
    
    /* Removes the contact from the list and sends the user back to the main scene */
    public void deleteContact() {
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmAlert.setTitle("Confirm Delete");
		confirmAlert.setHeaderText("Are you sure you want to delete this contact?");
		confirmAlert.setContentText("Note: If you confirm, you will not be able to undo this change.");
		confirmAlert.showAndWait().ifPresent((btnType) -> {
			
		  if (btnType == ButtonType.OK) {
			  if (selected != -1) {
				  FileHelper.getContacts().remove(selected);
			  }
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
			Stage stage = (Stage) gridAvailability.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Time Zone Manager");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
