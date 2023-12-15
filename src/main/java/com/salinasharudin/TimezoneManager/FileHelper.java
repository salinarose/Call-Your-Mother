package com.salinasharudin.TimezoneManager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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

public final class FileHelper {
	
	/* Contacts file methods */
	private static ArrayList<Contact> contacts;
	//private static Settings settings;
	public static Boolean contactFileSuccess = false;
	public static Boolean settingsFileSuccess = false;
	
	/* Read contact data from save file and load them into a new array */
	public static void readContactData() {
		
		ArrayList<Contact> contactsList = new ArrayList<>();
	    
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonArray = Files.readString(Paths.get("contacts.json"));
		
			Contact[] contactsArray = mapper.readValue(jsonArray, Contact[].class);
			
			for (Contact c : contactsArray) {
				//System.out.println(c.toString());
				contactsList.add(c);
			}
			System.out.println("Load successful.");
			contactFileSuccess = true;
			
		} catch (Exception e) {
			System.out.println("Load unsuccessful.");
			contactFileSuccess = false;
		}
		
		contacts = contactsList;
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
				System.out.println(jo);
				jo.write(writer, 1, 1);
				if (counter < last) {
					writer.append(',');
				}
				counter++;
			}

			writer.append(']');
			
			writer.flush();
			writer.close();
			//System.out.println("Save successful.");
			
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
	public static void readSettingsData() {
	    
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
		    	//System.out.println(day);
		    	for (var hour : day) {
		    		//System.out.println(hour);
		    		if (hour == null) {
		    			hour = false;
		    		}
		    		hours[dayCount][hourCount] = (Boolean) hour;
		    		hourCount++;
		    	}
		    	dayCount++;
		    }
		    settings.setSchedule(hours);
		    
		    
		    // test: print map entries
		    /*
		    for (Map.Entry<?, ?> entry : map.entrySet()) {
		        System.out.println(entry.getKey() + "=" + entry.getValue());
		    }
		    */

			System.out.println("Settings load successful.");
			settingsFileSuccess = true;
			
		} catch (Exception e) {
			System.out.println("Settings load unsuccessful.");
			settingsFileSuccess = false;
		}
	}
	
	
	/* Adds some sample people to the contacts list. */
	static void test_addContacts() {
		ArrayList<Contact> test = new ArrayList<>();
		Contact person1 = new Contact("Mom", "UTC-1");
		Contact person2 = new Contact("Dad", "UTC-3");
		Contact person3 = new Contact("Simon", "UTC-2");
		person3.setTimezone("north pole");
		test.add(person1);
		test.add(person2);
		test.add(person3);
		
		contacts = test;
		
		writeContactData();
	}
	
	/* Error alert popup that informs the user that the data file has been corrupted */
	public static void showFileAlert() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("File Error");
		alert.setHeaderText("Error reading file");
		alert.setContentText("Unable to load data. File may have been corrupted or deleted.");
		alert.showAndWait();
	}

}
