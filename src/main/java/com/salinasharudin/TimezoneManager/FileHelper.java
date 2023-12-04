package com.salinasharudin.TimezoneManager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

public final class FileHelper {
	
	private static ArrayList<Contact> contacts;
	
	// Read contact data from save file and load them into a new array
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
			
		} catch (IOException e) {
			System.out.println("Load unsuccessful.");
			e.printStackTrace();
		}
		
		contacts = contactsList;
		//return contactsList;
	}
	
	public static ArrayList<Contact> getContacts() {
		return contacts;
	}
	
	// Write contact data to a file
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
				jo.write(writer, 0, 0);
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
	
	/* Adds some sample people to the contacts list. */
	static void test_addContacts() {
		ArrayList<Contact> contacts = new ArrayList<>();
		Contact person1 = new Contact("Mom", "UTC-1");
		Contact person2 = new Contact("Dad", "UTC-3");
		Contact person3 = new Contact("Simon", "UTC-2");
		person3.setTimezone("north pole");
		contacts.add(person1);
		contacts.add(person2);
		contacts.add(person3);
	}

}