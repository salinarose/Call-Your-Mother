package com.salinasharudin.TimezoneManager;

public class Contact {
	
	//Fields
	private int uID;
	private static int iterator = 0;
	private String name;
	private String timezone; //may change if I decide to use offset instead
	private Boolean[][] availability;
	
	//Constructor
	Contact(String name, String timezone) {
		this.name = name;
		this.timezone = timezone;
		this.availability = new Boolean[7][24];
		this.uID = iterator + 1;
		iterator += 1;
	}
	
	//Methods
	int getuID() {
		return uID;
	}

	String getName() {
		return name;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	String getTimezone() {
		return timezone;
	}
	
	void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	Boolean[][] getAvailability() {
		return availability;
	}
	
	void setAvailability(Boolean[][] availability) {
		this.availability = availability;
	}
	
	public String toString() {
		return "Name: " + this.name
				+ " ID: " + this.uID 
				+ " timezone: " + this.timezone;
	}
	
}
