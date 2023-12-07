package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class MainController implements Initializable {
	
	int selected = -1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/* Calls display methods */
		displayLocalDateTime();
		displayContacts();
	}

	// Creates a local clock
	private ZonedDateTime clockNow = ZonedDateTime.now();
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
			name.setPrefWidth(75);
			Label zone = new Label(c.getTimezone());
			zone.setPrefWidth(75);
			gridPaneContacts.addRow(i, name, zone);
			i++;
		}
		
		/* Events for hovering over or selecting contacts */
		for (Node node : gridPaneContacts.getChildren()) {
		    node.setOnMouseEntered(e -> gridPaneContacts.getChildren().forEach(c -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        if (GridPane.getRowIndex(c) == targetIndex) {
		            c.setStyle("-fx-background-color:#FFFFFF;");
		        }
		    }));
		    node.setOnMouseExited(e -> gridPaneContacts.getChildren().forEach(c -> {
		        Integer targetIndex = GridPane.getRowIndex(node);
		        if (GridPane.getRowIndex(c) == targetIndex) {
		            c.setStyle("-fx-background-color:#F3F3F3;");
		        }
		    }));
		    node.setOnMouseClicked(e -> gridPaneContacts.getChildren().forEach(r -> {
			        Integer targetIndex = GridPane.getRowIndex(node);
			        if (GridPane.getRowIndex(r) == targetIndex) {
			        	// Get the item selected
			            selected = targetIndex;
			            btnEdit.setDisable(false);
			            
			            // TODO: (wishlist) disable edit button if clicked away from gridpane
			        }
			}));
		    
		}
		
	}
	
	public void editContact() throws IOException {
		System.out.println(selected);
		App.setRoot("ContactScene");
	}

	@FXML
	/* When add new button clicked, go to contact edit scene */
	public void goToContactScene() throws IOException {
		App.setRoot("ContactScene");
	}
	
	/* Get selected contact 
	public void test() {
		gridPaneContacts.getrow
	}
	*/



    
}


