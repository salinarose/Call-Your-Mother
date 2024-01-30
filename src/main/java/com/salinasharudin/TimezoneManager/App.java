package com.salinasharudin.TimezoneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Salina Sharudin
 * Title: TimeZone Manager
 * Description: Helps users find times that they and their contacts are mutually available.
 * Users can store a time zone and an availability schedule for each contact. The user can then add up to 
 * three contacts to a list and find the times in which all people have open availability. This availability is 
 * calculated by the program into the user's own set time zone.
 * Version: 1.0.1
 * Date Updated: 1/26/2024
 */

public class App extends Application {

    private static Scene scene;
   
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Main"), 640, 640);
        
        /*
        String cssFile = this.getClass().getResource("theme-default.css").toExternalForm();
        scene.getStylesheets().add(cssFile);
        */
        
        stage.setScene(scene);
        stage.setTitle("Time Zone Manager");
        stage.show();
        
        stage.setOnCloseRequest(event -> {
            event.consume();
            exit(stage);
        });
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
    	initialize();
    	
        launch();
    }
    
    public static void initialize() {
    	
    	// Load the contacts list from a file if it exists, or a new empty list if it does not
    	FileHelper.readContactData();
    	
    	// Load user settings
    	FileHelper.readSettingsData();
    	
    	// Load saved call data
    	FileHelper.readCallData();

    }
    
    /* Alert the user to save before exiting the program */
    public static void exit(Stage stage) {
        
        Alert alert = new Alert(Alert.AlertType.NONE, "Save changes before leaving?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("Exit");
        alert.setHeaderText("Unsaved Changes");
        alert.setContentText("Would you like to save all changes before leaving?");

        alert.showAndWait().ifPresent((btnType) -> {
			
        	// if btnType == ButtonType.CANCEL, do nothing
        	
    		  if (btnType == ButtonType.YES) {
    			  FileHelper.saveAll();
    			  stage.close();
    		  }
    		  if (btnType == ButtonType.NO) {
    			  stage.close();
    		  }
  		});
    }

}