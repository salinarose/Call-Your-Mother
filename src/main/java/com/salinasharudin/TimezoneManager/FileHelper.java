package com.salinasharudin.TimezoneManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class FileHelper {
	
	private static Map<String, String> calls = new HashMap<>();
	
	/* Contacts file methods */
	private static ArrayList<Contact> contacts;
	public static Boolean contactFileSuccess = false;
	public static Boolean settingsFileSuccess = false;
	
	/* Read contact data from save file and load them into a new array */
	public static Boolean readContactData() {
		
		ArrayList<Contact> contactsList = new ArrayList<>();
		Boolean success;
	    
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonArray = Files.readString(Paths.get("contacts.json"));
		
			Contact[] contactsArray = mapper.readValue(jsonArray, Contact[].class);
			
			for (Contact c : contactsArray) {
				//System.out.println(c.toString());
				contactsList.add(c);
			}
			//System.out.println("Load successful.");
			contactFileSuccess = true;
			success = true;
			
		} catch (Exception e) {
			System.out.println("Load unsuccessful.");
			contactFileSuccess = false;
			showFileAlert("contacts list");
			success = false;
		}
		
		contacts = contactsList;
		return success;
	}
	
	public static ArrayList<Contact> getContacts() {
		return contacts;
	}
	
	/* Write contact data to a file */
	public static void writeContactData() {

		try {
			FileWriter writer = new FileWriter("contacts.json", false);
			
			writer.append('[');
			
			// Counter and last variables needed for formatting
			int counter = 0;
			int last = contacts.size() - 1;
			for (Contact c: contacts) {
				JSONObject jo = new JSONObject(c);
				//System.out.println(jo);
				jo.write(writer, 1, 1);
				if (counter < last) {
					writer.append(',');
				}
				counter++;
			}
			
			writer.append(']');
			
			writer.flush();
			writer.close();
			
		} catch (IOException ioe) {
			System.out.println("Error with save file.");
			ioe.printStackTrace();
		}
	}
	
	/* Settings file methods */
	public static void writeSettingsData() {
		try {
			FileWriter writer = new FileWriter("settings.json");
			JSONObject jo = new JSONObject(Settings.getInstance());
			jo.write(writer, 1, 1);
			writer.flush();
			writer.close();
			System.out.println("Settings save succcessful.");
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Save failed.");
		}
	}
	
	/* Read settings data from save file */
	public static Boolean readSettingsData() {
		//Boolean failed = false;
	    
		try {
			
			// convert JSON file to map
			ObjectMapper mapper = new ObjectMapper();
		    Map<?, ?> map = mapper.readValue(Paths.get("settings.json").toFile(), Map.class);

		    // Load each field in settings from map 
		    Settings settings = Settings.getInstance();
		    
		    settings.setZone(ZoneId.of((String) map.get("zone")));
		    settings.setUsername(map.get("username").toString());
		    settings.setTheme((String) map.get("theme"));
		    
		    // Load schedule
		    Boolean[][] hours = new Boolean[7][24];
		    ArrayList<ArrayList> v = (ArrayList<ArrayList>) map.get("schedule");
		    int dayCount = 0;
		    for (ArrayList day : v) {
		    	int hourCount = 0;
		    	for (var hour : day) {
		    		if (hour == null) {
		    			hour = false;
		    		}
		    		hours[dayCount][hourCount] = (Boolean) hour;
		    		hourCount++;
		    	}
		    	dayCount++;
		    }
		    settings.setSchedule(hours);

			//System.out.println("Settings load successful.");
			settingsFileSuccess = true;
			return true;
			//failed = false;
			
		} catch (Exception e) {
			System.out.println("Settings load unsuccessful.");
			settingsFileSuccess = false;
			return false;
			//failed = true;
			//showFileAlert("settings");
			
		}
		
		//if (failed) showFileAlert("settings");
	}
	
	/* Methods for handling scheduled call data */
	/* Read call file */
	public static Boolean readCallData() {
		// Try with resources ensures the reader always gets closed
		try (BufferedReader reader = new BufferedReader(new FileReader("calls.txt"))) {
			
			String line;
			while ((line = reader.readLine()) != null) {
				String[] entry = line.split("\t");
				String time = entry[0];
				String people = entry[1];
				
				calls.put(time, people);
			}
			
			return true;
			
		} catch (IOException e) {
			System.out.println("call data not found");
			//e.printStackTrace();
			return false;
		}
	}
	
	/* Write to call file */
	public static void writeCallData() {
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("calls.txt"));
			
			// Write each <"Day: Time", "Contacts">
			for (Map.Entry<String, String> call : calls.entrySet()) {
				// Key and value separated by a tab
				writer.write(call.getKey() + "\t" + call.getValue());
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing to call file");
		} 
		
	}
	
	/* Get calls */
	public static Map<String, String> getCalls() {
		return calls;
	}
	
	/* Error alert pop-up that informs the user that the data file has been corrupted */
	public static void showFileAlert(String fileType) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("File Error");
		alert.setHeaderText("Error reading " + fileType + " file");
		alert.setContentText("Unable to load data. File may have been corrupted or deleted.");
		alert.showAndWait();
	}

    /* save all files */
    public static void saveAll() {
    	// save contacts data
    	writeContactData();
    	
    	// save settings data
    	writeSettingsData();
    	
    	// save calls data
    	writeCallData();
    }
}
