package com.salinasharudin.TimezoneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Salina Sharudin
 * App: TimeZone Manager
 * Version: 0.0.1
 * Date Updated: 11/16/2023
 */
public class App extends Application {

    private static Scene scene;
   
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main"), 640, 480);
        stage.setScene(scene);
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
    	/* Testing ContactHelper class*/
    	ArrayList<Contact> contacts = new ArrayList<>();
    	ContactHelper.demo_addContacts(contacts);
    	contacts.add(new Contact("Rosie", "5"));
    	//ContactHelper.printContacts(contacts);
    	
    	
    	//TODO: make an initialize method that will load contacts from a file
    	FileHelper.writeContactData(contacts);
    	contacts = FileHelper.readContactData();
    	
        //launch();
    }

}