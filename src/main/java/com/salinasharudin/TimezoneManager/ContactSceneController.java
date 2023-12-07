package com.salinasharudin.TimezoneManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ContactSceneController implements Initializable {
	
	/* Initializes scene */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		setupGrid();
	}
	
	public void getSelection(int selected) {
		System.out.println("received: " + selected);
	}
	
	@FXML
	GridPane gridAvailability;
	
	/* Sets up the availability grid */
	public void setupGrid() {
		
		// Hours column
		for (int i = 1; i < 25; i++) {
			//gridAvailability.addRow(i, new Label((i) + ":00"));
			gridAvailability.add(new Label((i) + ":00"), 0, i);
			for (int j = 1; j < 8; j++) {
				gridAvailability.add(new CheckBox(), j, i);
			}
		}
	}
	
	/* Returns to main scene */
	public void goToMainScene() throws IOException {
		App.setRoot("Main");
		
		// Need to debug this
		// This just opens new windows
		/*
		System.out.println("here");
		
        //Load main scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
 
        //Show scene 2 in new window
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Time Zone Manager");
        stage.show();
        */
	}
	
}
