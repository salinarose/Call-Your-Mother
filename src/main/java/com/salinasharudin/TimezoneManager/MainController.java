package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MainController implements Initializable {

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
	
	/* Displays the user's list of contacts */
	public void displayContacts() {
		// Get the contact list
		ArrayList<Contact> list = FileHelper.getContacts();
		
		// Clear the grid pane
		gridPaneContacts.getChildren().clear();
		
		// Populate the grid pane with the new contacts
		int i = 0;
		for (Contact c : list) {
			gridPaneContacts.addRow(i, new Label(c.getName()), new Label(c.getTimezone()));
			i++;
		}
	}
	
	/* When add new button clicked, go to contact edit scene */
	public void goToContactScene() throws IOException {
		App.setRoot("ContactScene");
	}

    
}


