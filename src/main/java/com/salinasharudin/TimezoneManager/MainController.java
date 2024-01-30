package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable {
	
	// variables that control selection and de-selection
	public int selected = -1;
	Boolean b = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/* Calls display methods */
		displayLocalDateTime();
		displayContacts();
		displayCalls();
	}
	
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
		// Creates a local clock using the time zone specified in user's settings
		ZonedDateTime location = ZonedDateTime.now(Settings.getInstance().getZone());
		DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm:ss a");
		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd MMM yyyy");
		
		// Time and location do not need to be updated in real time
		lblLocalTimeZone.setText(location.format(DateTimeFormatter.ofPattern("z")));
		lblLocalLocation.setText(location.getZone().toString());
		
		final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),
				new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				LocalDateTime clockNow = LocalDateTime.now();
				lblLocalTime.setText(clockNow.format(formatTime));
				lblLocalDay.setText(clockNow.getDayOfWeek().toString());
				lblLocalDate.setText(clockNow.format(formatDate));
			}
		}));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
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
			name.setMinWidth(60);
			name.setAlignment(Pos.BASELINE_LEFT);
			
			Label zone = new Label(c.getTimezone());
			zone.setMinWidth(150);
			zone.setAlignment(Pos.BASELINE_LEFT);
			
			HBox row = new HBox();
			row.setSpacing(10);
			
			row.getChildren().addAll(name, zone);
			gridPaneContacts.addRow(i, row);
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
	
	@FXML
	ListView<String> listCalls;
	
	/* Populate table with calls data */
	public void displayCalls() {
		
		listCalls.setPlaceholder(new Label("No Scheduled calls found."));
		
		Map<String, String> calls = FileHelper.getCalls();
		
		for (String key : calls.keySet()) {
			listCalls.getItems().add(key + calls.get(key));
		}
		
		// Context menu for removing items from ListView
			ContextMenu contextMenu = new ContextMenu();
			MenuItem menuItemRemove = new MenuItem("Remove");
			MenuItem menuItemRemoveAll = new MenuItem("Remove All");
			contextMenu.getItems().add(menuItemRemove);
			contextMenu.getItems().add(menuItemRemoveAll);
			listCalls.setContextMenu(contextMenu);
				
			// Context Menu events
			menuItemRemove.setOnAction(e -> {
				Object selected = listCalls.getSelectionModel().getSelectedItem();
				
				// Obtain just the key. It should always end in the space after 'm', as in 'am' or 'pm'
				int i = selected.toString().indexOf('m');
				String key = selected.toString().substring(0, i + 2);
				
				if (FileHelper.getCalls().containsKey(key)) {
					FileHelper.getCalls().remove(key);
					listCalls.getItems().remove(selected);
				} else {
					System.out.println("Error: key not found in calls.");
				}
			});
			
			// Remove all the calls
			menuItemRemoveAll.setOnAction(e -> {
				FileHelper.getCalls().clear();
				listCalls.getItems().clear();
			});
	}
	
	/* Methods for changing scenes */	
	public void btnAddClicked() throws IOException {
		selected = -1;
		goToContactScene();
	}
	
	/* When add new or edit button clicked, switches to the contact scene */
	public void goToContactScene() throws IOException {
        // Load contact editing scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ContactScene.fxml"));
        Parent root;
        try {
	        root = loader.load();
	 
	        // Get controller of scene2
	        ContactSceneController scene2Controller = loader.getController();
	        
	        // Pass selection index to the contact edit scene
	        scene2Controller.getSelection(selected);

	        // Load next scene in same window
	        Stage stage = (Stage) btnEdit.getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.setTitle("Edit Contact");
	        stage.show();
        } catch (IOException e) {
        	e.printStackTrace();
        	System.out.println("error loading contact edit scene");
        }
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
	
	@FXML
	Button btnPlan;
	
	/* Go to schedule scene */
	public void goToScheduleScene() {
		
		//TODO: ask to save changes first
		
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScheduleScene.fxml"));
        Parent root;
		try {
			root = loader.load();
			Stage stage = (Stage) btnPlan.getScene().getWindow();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error loading schedule scene.");
		}
	}
    
    /* save all files */
    public void saveAll() {
    	FileHelper.saveAll();
    }
    
    /* close the program */
    public void close() {
    	App.exit((Stage) btnPlan.getScene().getWindow());
    }
    
}


