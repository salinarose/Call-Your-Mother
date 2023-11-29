package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainController implements Initializable {

	//Create local clock
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
	
	public void displayLocalDateTime() {
		lblLocalLocation.setText(clockNow.getZone().toString());
		lblLocalTimeZone.setText(clockNow.format(DateTimeFormatter.ofPattern("z")));
		lblLocalTime.setText(clockNow.format(formatTime));
		lblLocalDay.setText(clockNow.getDayOfWeek().toString());
		lblLocalDate.setText(clockNow.format(formatDate));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		displayLocalDateTime();
	}
    
}


