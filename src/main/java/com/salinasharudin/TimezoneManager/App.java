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
 * App: TimeZone Manager
 * Version: 0.8
 * Date Updated: 1/03/2023
 */
public class App extends Application {

    private static Scene scene;
    //private static ArrayList<Contact> contacts;
   
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Main"), 640, 640);
        stage.setScene(scene);
        stage.setTitle("Time Zone Manager");
        stage.show();
        
        stage.setOnCloseRequest(event -> {
            event.consume();
            exit(stage);
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
    	initialize();
    	
    	//testScheduleHelper();
    	
        launch();
    }
    
    public static void initialize() {
    	/* Test data - only run if current contacts file deleted or corrupted
    	FileHelper.test_addContacts();
    	ArrayList<Contact> contactList = FileHelper.getContacts();
    	for (Contact c : contactList) {
    		System.out.println(c.toString());
    	}
    	*/
    	
    	// Load the contacts list from a file if it exists, or a new empty list if it does not
    	FileHelper.readContactData();
    	
    	// Load user settings
    	FileHelper.readSettingsData();
    	
    	FileHelper.readCallData();
    }
    
    public static void testScheduleHelper() {
    	ArrayList<Contact> others = new ArrayList<>();
    	//others.add(FileHelper.getContacts().get(0));
    	others.add(FileHelper.getContacts().get(2));
    	
    	ScheduleHelper.buildSchedule(others);
    	
    }
    
    public void exit(Stage stage) {
        
        Alert alert = new Alert(Alert.AlertType.NONE, "Save changes before leaving?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle("Exit");
        alert.setHeaderText("Unsaved Changes");
        alert.setContentText("Would you like to save all changes before leaving?");

        alert.showAndWait().ifPresent((btnType) -> {
			
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