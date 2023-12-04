package com.salinasharudin.TimezoneManager;

import java.util.ArrayList;

final class ContactHelper {
	
	//TODO: Add a single contact
	
	/* Remove a specified contact */
	static void remove(ArrayList<Contact> contacts, Contact contact) {
		contacts.remove(contact);
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