package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScheduleController implements Initializable {
	
	// Variables to control selection and de-selection
	public int selected = -1;
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
	ListView<Contact> listSelected;

	public void updateList() {
		
		// Context menu for removing items from ListView
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuItemRemove = new MenuItem("Remove");
		MenuItem menuItemRemoveAll = new MenuItem("Remove All");
		contextMenu.getItems().add(menuItemRemove);
		contextMenu.getItems().add(menuItemRemoveAll);
		listSelected.setContextMenu(contextMenu);
		
		// Context Menu events
		menuItemRemove.setOnAction(e -> {
			Object selected = listSelected.getSelectionModel().getSelectedItem();
			if (listSelected.getItems().contains(selected)) {
				listSelected.getItems().remove(selected);
			}
		});
		
		menuItemRemoveAll.setOnAction(e -> {
			clearSelection();
		});
		
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
			name.setMinWidth(80);
			Label zone = new Label(c.getTimezone());
			zone.setMinWidth(150);
			
			HBox row = new HBox();
			row.setSpacing(10);
			row.getChildren().addAll(name, zone);
			
			gridContacts.addRow(i, row);
			i++;
		}
		
		/* Events for hovering over or selecting contacts */
		for (Node node : gridContacts.getChildren()) {
		    node.setOnMouseEntered(e -> gridContacts.getChildren().forEach(r -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        if (selected != targetIndex && GridPane.getRowIndex(r) == targetIndex) {
		        	r.getStyleClass().add("highlight-selection");
		        }
		    }));
		    
		    node.setOnMouseExited(e -> gridContacts.getChildren().forEach(r -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        // TODO: Would be nice if already selected items stayed highlighted
		        /*
		        if (listSelected.getItems().contains(list.get(targetIndex))) {
		        	System.out.println("in list already!");
		        } */
		        if (selected != targetIndex && GridPane.getRowIndex(r) == targetIndex) {
		        	r.getStyleClass().remove("highlight-selection");
		        }
		    }));
		    
		    node.setOnMouseClicked(e -> gridContacts.getChildren().forEach(r -> {
			        Integer targetIndex = GridPane.getRowIndex(node);
			        
			        if (GridPane.getRowIndex(r) == targetIndex) {
			        	// De-selects if target is the currently selected item
			        	if (selected == targetIndex && GridPane.getColumnIndex(r) == 0) {
			        		r.getStyleClass().remove("selected");
			        		r.getStyleClass().remove("highlight-selection");
			        		
			        		btnAddToList.setDisable(true);
		        			selected = -1;
			        		/*
			        		// Last column
			        		if (GridPane.getColumnIndex(r) == gridContacts.getColumnCount() -1) {
			        			b = false; // reset boolean to false
			        			btnAddToList.setDisable(true);
			        			selected = -1;
			        		}
			        		
			        		else {
			        			b = true;
			        		}*/
			        	}
			        	// Item was not already selected
			        	else {
				        	// Get the item selected
				            selected = targetIndex;
				            btnAddToList.setDisable(false);
				            r.getStyleClass().remove("highlight-selection");
				            r.getStyleClass().add("selected");
			        	}
			        }
			        // Item was not the target of the click
			        else {
			        	r.getStyleClass().remove("highlight-selection");
			        	r.getStyleClass().remove("selected"); 
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
		
		// Get all the selected items and store it in a new ArrayList 
		ArrayList<Contact> others = new ArrayList<>();
		others.clear();
		others.addAll(listSelected.getItems());

		// Build the mutual schedule
		ScheduleHelper.createMutualSchedule(others);
		
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
		    		node.getStyleClass().add("selected");
		    	}
		    });
		    node.setOnMouseExited(e -> {
		    	// Only changes the background color back to default if it isn't currently selected
		    	// Right now it stays highlighted if you press the add button, then click a different time
		    	if (node != selectedTime) {
		    		node.getStyleClass().remove("selected");
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
	
	/* Ask the user to confirm the selected call time */
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

		// Load main scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnCalculate.getScene().getWindow();

			setTheme(root, stage);
			
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
    public void saveAll() {
    	FileHelper.saveAll();
    }
    
    /* close the program */
    public void close() {
    	App.exit((Stage) btnCalculate.getScene().getWindow());
    }

}
