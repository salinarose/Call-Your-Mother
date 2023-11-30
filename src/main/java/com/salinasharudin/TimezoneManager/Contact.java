package com.salinasharudin.TimezoneManager;

class Contact {
	
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
	private int getuID() {
		return uID;
	}

	private String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	private String getTimezone() {
		return timezone;
	}
	
	private void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	private Boolean[][] getAvailability() {
		return availability;
	}
	
	private void setAvailability(Boolean[][] availability) {
		this.availability = availability;
	}
	
}
