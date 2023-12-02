package com.salinasharudin.TimezoneManager;

public class Contact {
	
	//Fields
	private String name;
	private String timezone; //may change if I decide to use offset instead
	private Boolean[][] availability;
	
	//Constructors
	Contact() {}
	
	Contact(String name, String timezone) {
		this.name = name;
		this.timezone = timezone;
		this.availability = new Boolean[7][24];
	}
	
	//Methods
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	public Boolean[][] getAvailability() {
		return availability;
	}
	
	public void setAvailability(Boolean[][] availability) {
		this.availability = availability;
	}
	
	public String toString() {
		return "Name: " + this.name
				+ " timezone: " + this.timezone;
	}
	
	public void setJO() {
		return;
	}
	
}
