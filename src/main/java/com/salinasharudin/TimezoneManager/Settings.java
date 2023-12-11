package com.salinasharudin.TimezoneManager;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Settings {
	
	private static Settings single_instance = null;
	
	private String username;
	private static ZoneId zone;
	private static LocalDateTime localClock;
	// Boolean[][] schedule;
	// String themeFile
	
	private Settings(ZoneId zone)
    {
        this.zone = zone;
        this.localClock = LocalDateTime.now(zone);
    }
 
    // Method
    // Static method to create instance of Singleton class
    public static Settings Settings(ZoneId zone)
    {
        // To ensure only one instance is created
        if (single_instance == null) {
            single_instance = new Settings(zone);
        }
        return single_instance;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ZoneId getZone() {
		return zone;
	}

	public void setZone(ZoneId zone) {
		this.zone = zone;
		this.localClock = LocalDateTime.now(zone);
	}

	public LocalDateTime getLocalClock() {
		return localClock;
	}
	
	
}
