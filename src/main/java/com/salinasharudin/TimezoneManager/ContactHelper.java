package com.salinasharudin.TimezoneManager;

import java.util.ArrayList;

final class ContactHelper {
	
	//TODO: Add a single contact
	//TODO: Remove a contact
	
	/* Adds some sample people to the contacts list. */
	static void demo_addContacts(ArrayList<Contact> contacts) {
		Contact person1 = new Contact("Mom", "UTC-1");
		Contact person2 = new Contact("Dad", "UTC-3");
		Contact person3 = new Contact("Simon", "UTC-2");
		person3.setTimezone("north pole");
		contacts.add(person1);
		contacts.add(person2);
		contacts.add(person3);
	}
	
	/* Prints all contacts' information to the console */
	static void printContacts(ArrayList<Contact> contacts) {
		for(Contact c : contacts) {
			System.out.println(c.toString());
		}
	}
	
	/* Remove all contacts */
	static void clearContacts(ArrayList<Contact> contacts) {
		contacts.clear();
	}
	
	
}