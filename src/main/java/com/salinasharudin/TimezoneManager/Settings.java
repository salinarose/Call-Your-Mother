package com.salinasharudin.TimezoneManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Settings {
	
	// Singleton variable
	//private static Settings single_instance = null; // lazy initialization
	private static final Settings single_instance = new Settings(); // eager initialization
	
	private String username = "User";
	private ZoneId zone;
	private Boolean[][] schedule = new Boolean[7][24];
	private String theme = "default";
	
	// Private constructor to ensure it is a singleton class
	private Settings() {}
 
    // Static method to create instance of Singleton class
	// Eager initialization
	public static Settings getInstance() {
		return single_instance;
	}
	/*// Lazy initialization 
    public static Settings Settings()
    {
        // To ensure only one instance is created
        if (single_instance == null) {
            single_instance = new Settings();
        }
        return single_instance;
    }
    */

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// If the zone is null, set it to the default (system)
	public ZoneId getZone() {
		if (zone == null) {
			zone = ZonedDateTime.now().getZone();
		}
		return zone;
	}

	public void setZone(ZoneId zone) {
		this.zone = zone;
	}

	public LocalDateTime getLocalClock() {
		return LocalDateTime.now(getZone());
	}
	
	public Boolean[][] getSchedule() {
		return schedule;
	}
	
	public void setSchedule(Boolean[][] array) {
		this.schedule = array;
	}
	
	private String getTheme() {
		return theme;
	}
	
	private void setTheme(String filePath) {
		this.theme = filePath;
	}
	
}
