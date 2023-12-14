package com.salinasharudin.TimezoneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Author: Salina Sharudin
 * App: TimeZone Manager
 * Version: 0.0.3
 * Date Updated: 12/10/2023
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
    	
        launch();
    }
    
    public static void initialize() {
    	// Load the contacts list from a file if it exists, or a new empty list if it does not
    	FileHelper.readContactData();
    	ArrayList<Contact> contactList = FileHelper.getContacts();
    	
    	FileHelper.writeSettingsData();
    	
    	//TODO: create user settings and load them here
    }

}