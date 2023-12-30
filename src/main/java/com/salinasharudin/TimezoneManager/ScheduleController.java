package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ScheduleController implements Initializable {
	
	// Variables to control selection and deselection
	public int selected = -1;
	Boolean b = false;
	private Node selectedTime;

	@FXML
	Button btnCalculate;
	@FXML
	Button btnClear;
	@FXML
	Label lblMax;
	@FXML
	Label lblNoResults;
	@FXML
	Button btnAddToSchedule;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize button and label visibility
		btnCalculate.setDisable(true);
		btnClear.setDisable(true);
		btnAddToSchedule.setDisable(true);
		lblMax.setVisible(false);
		lblNoResults.setVisible(false);
		
		// Initialize contacts list
		loadContacts();
	}
	
	@FXML
	GridPane gridContacts;
	@FXML
	Button btnAddToList;
	@FXML
	ListView listSelected;
	
	public void updateList() {
		// Check that the number selected is valid
		if (selected != -1 && selected < FileHelper.getContacts().size()) {
			Contact c = FileHelper.getContacts().get(selected);
			// Add the selected contact to the list view only if it has not yet been added
			if (!listSelected.getItems().contains(c)) {
				listSelected.getItems().add(c);			
			}
		}
		
		// Check the number of items in the list and enable the calculate button if it has between 1-3 items
		int items = listSelected.getItems().size();
		if (items > 0) {
			btnClear.setDisable(false);
		}
		
		if (items > 0 && items <= 3) {
			btnCalculate.setDisable(false);
		} else {
			btnCalculate.setDisable(true);
			if (items > 3) {
				lblMax.setVisible(true);
			} else {
				lblMax.setVisible(false);
			}
		}
	}
	
	/* Displays contacts on the left-hand side */
	public void loadContacts() {
		// Get the contact list
		ArrayList<Contact> list = FileHelper.getContacts();
		
		// Clear the grid pane
		gridContacts.getChildren().clear();
		btnAddToList.setDisable(true);
		
		// Populate the grid pane with the new contacts
		int i = 0;
		for (Contact c : list) {
			Label name = new Label(c.getName());
			name.setPrefWidth(60);
			Label zone = new Label(c.getTimezone());
			//zone.setPrefWidth(150);
			gridContacts.addRow(i, name, zone);
			i++;
		}
		
		/* Events for hovering over or selecting contacts */
		for (Node node : gridContacts.getChildren()) {
		    node.setOnMouseEntered(e -> gridContacts.getChildren().forEach(r -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        if (selected != targetIndex && GridPane.getRowIndex(r) == targetIndex) {
		            r.setStyle("-fx-background-color:#FFFFFF;");
		        }
		    }));
		    node.setOnMouseExited(e -> gridContacts.getChildren().forEach(r -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        if (selected != targetIndex && GridPane.getRowIndex(r) == targetIndex) {
		            r.setStyle("-fx-background-color:#F3F3F3;");
		        }
		    }));
		    node.setOnMouseClicked(e -> gridContacts.getChildren().forEach(r -> {
			        Integer targetIndex = GridPane.getRowIndex(node);
			        
			        if (GridPane.getRowIndex(r) == targetIndex) {
			        	// De-selects if target is the currently selected item
			        	if (b == true || selected == targetIndex && GridPane.getColumnIndex(r) == 0) {
			        		r.setStyle("-fx-background-color:#F3F3F3;");
			        		
			        		// Last column
			        		if (GridPane.getColumnIndex(r) == gridContacts.getColumnCount() -1) {
			        			b = false; // reset boolean to false
			        			btnAddToList.setDisable(true);
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
				            btnAddToList.setDisable(false);
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
	
	/* Calculate mutually available times for all contacts in the list */
	public void calculate() {
		
		// Clear old results if they exist
		ScheduleHelper.map.clear();
		
		// In case it is already visible
		lblNoResults.setVisible(false);
		
		// Get all the selected items and store it in a new arraylist 
		ArrayList<Contact> others = new ArrayList<>();
		others.clear();
		others.addAll(listSelected.getItems());

		// Build the mutual schedule
		ScheduleHelper.buildSchedule(others);
		
		// Display the results
		gridResults.getChildren().clear();
		showResults();

	}
	
	@FXML
	GridPane gridResults;
	
	/* Displays the results of calculate */
	private void showResults() {
		Map<Integer, ArrayList<Integer>> results = ScheduleHelper.map;
		
		//gridResults.getChildren().clear();
		
		if (results.isEmpty()) {
			lblNoResults.setVisible(true);
		} else {
			// Print results to the gridpane
			for (int day = 0; day < 7; day++) {
				
				if (results.containsKey(day)) {
					int row = gridResults.getRowCount();
					Label label = new Label();
					
					if (day == 0) {
						label.setText("SUNDAY: ");
						gridResults.add(label, 2, row - 1, 4, 1);
					} else {
						label.setText(DayOfWeek.of(day) + ": ");
						gridResults.add(label, 2, row, 4, 1);
					}
					
					int col = 0;
					
					for (int h: results.get(day)) {
						String hour;
						if (h == 0 ) {
							hour = "12:00 am ";
						} 
						else if (h == 12) {
							hour = "12:00 pm ";
						}
						else if (h < 12) {
							hour = h + ":00 am ";
						} else {
							hour = h % 12 + ":00 pm ";
						}
						if (col > 4) {
							row++;
							col = 0;
						}
						gridResults.add(new Label(hour), col, row + 1);	
						col++;
					}
					
					gridResults.add(new Label(""), 0, gridResults.getRowCount() + 1);
				}
			}			
		}
		
		selectTime();
		
	}
	
	/* Handle mouse events for selecting the time */
	public void selectTime() {
		
		/* Events for hovering over or selecting times */
		for (Node node : gridResults.getChildren()) {
			// Gets the text value of the selected label
		    String value = ((Labeled) node).getText().toString();
		    
		    node.setOnMouseEntered(e -> {
		    	// Only highlight if a time (not a day label)
		    	if (!value.contains("DAY")) {
		    		node.setStyle("-fx-background-color:#FFFFFF;");
		    	}
		    });
		    node.setOnMouseExited(e -> {
		    	// Only changes the background color back to default if it isn't currently selected
		    	// Right now it stays highlighted if you press the add button, then click a different time
		    	if (node != selectedTime) {
		            node.setStyle("-fx-background-color:#F3F3F3;");
		    	}
		    });
		    node.setOnMouseClicked(e -> {
			    // Make sure the selected node is a time, not a day label
		    	if (!value.contains("DAY")) {
		    		btnAddToSchedule.setDisable(false);
		    		this.selectedTime = node;
		    	} else {
		    		btnAddToSchedule.setDisable(true);
		    	}
		    });
		}
	}
	
	/* Clear selected items in the list box */
	public void clearSelection() {
		listSelected.getItems().clear();
		btnCalculate.setDisable(true);
		btnClear.setDisable(true);
		btnAddToSchedule.setDisable(true);
		gridResults.getChildren().clear();
		selectedTime = null;
	}
	
	/* Add selected time to the schedule */
	public void addToSchedule() {
		// Get the selected node
		String time = ((Labeled) selectedTime).getText().toString();
		
		// Find the corresponding day
		String day = "";
		int row = GridPane.getRowIndex(selectedTime);
		
		// This feels very inefficient, but I haven't figured out a better way yet. 
		// Loop through all the results, looking for one that contains a day in the label until we have reached the row of the selected node
		for (Node node : gridResults.getChildren()) {
			if (GridPane.getRowIndex(node) >= row) break;
			String value = ((Labeled) node).getText().toString();
			if (value.contains("DAY")) {
				day = value;
			}
		}
		
		StringBuilder people = new StringBuilder();
		Object[] others = listSelected.getItems().toArray();
		for (int i = 0; i < others.length; i++) {
			people.append(((Contact) others[i]).getName());
			if (i != others.length - 1) {
				people.append(", ");
			}
		}
		
		System.out.println(day + time + " " + people.toString());

		// Ask for confirmation
		// If key already exists, ask "A call on DAY at TIME with CONTACTS already exists. Would you like to overwrite with PEOPLE? "
		String message;
		if (FileHelper.getCalls().containsKey(day + time)) {
			message = "A call on " + day + time + " with " 
					+ FileHelper.getCalls().get(day + time) 
					+ " already exists. Would you like to overwrite with" 
					+ people.toString() + "?";
		} else {
			message = "Add new call on " + day + time + "with " + people.toString() + "?";
		}
		confirmCall(message, day + time, people.toString());
	}
	
	public void confirmCall(String message, String time, String people) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm call");
		alert.setContentText(message);
		alert.showAndWait().ifPresent((btnType) -> {
			
  		  if (btnType == ButtonType.OK) {
  			  FileHelper.getCalls().put(time, people);  			  
  		  }
		});
	}

	/* Methods for leaving the current scene */
	/* Go to main scene */
	public void goToMainScene() {
		
		FileHelper.writeCallData();
		
		/*
		try {
			App.setRoot("Main");
		} catch (IOException e) {
			System.out.println("Error loading main scene from schedule scene.");
		}
		*/
		// Load main scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnCalculate.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Time Zone Manager");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Goes to the user settings scene */
	public void goToSettingsScene() {
		
		FileHelper.writeCallData();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsScene.fxml"));
        Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnCalculate.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.setTitle("Settings");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		try {
			App.setRoot("SettingsScene");
		} catch (IOException e) {
			System.out.println("Error loading settings from schedule scene.");
		}*/
	}

}
