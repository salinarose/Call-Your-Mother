package com.salinasharudin.TimezoneManager;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Alert;

public final class ScheduleHelper {
	// Map will store all the hours that all members have mutually available for each day
	public static Map<Integer, ArrayList<Integer>> map = new HashMap<>();
	
	// TODO: get members 
	public static void buildSchedule(ArrayList<Contact> others) {
		Boolean[][] user = Settings.getInstance().getSchedule();
		
		// Get the user offset
		ZoneOffset userOffset = OffsetDateTime.now(Settings.getInstance().getZone()).getOffset();
		
		// Get the offset represented as an int
		int userOffsetInt = getOffsetInt(userOffset);
		
		// Terminate if there was an error obtaining the int representation of the offset
		if (userOffsetInt == -100) {
			map.clear();
			showErrorAlert();
			return;
		}
		
		// get the differences for each other contact
		int[] difs = new int[others.size()];
		
		for (int i = 0; i < others.size(); i++) {
			//Boolean[][] other = c.getAvailability();
			// Get the user's offset as an int
			Contact c = others.get(i);
			int otherOffsetInt = getOffsetInt(OffsetDateTime.now(ZoneId.of(c.getTimezone())).getOffset());
			if (otherOffsetInt == -100) {
				map.clear();
				showErrorAlert();
				return;
			}
			
			difs[i] = getDif(userOffsetInt, otherOffsetInt);
		}
		
		/* Build the schedule */
		for (int day = 0; day < 7; day++) {
			for (int hr = 0; hr < 24; hr++) {
				// Check if the user has that time available
				if (user[day][hr] == true) {
					
					Boolean same = false;
					// Check each contact's schedule
					for (int i = 0; i < others.size(); i++) {
						int dif = difs[i];
						if (dif == 0) {
							// same zone - no conversion needed
							same = checkTime(others.get(i), day, hr);
						} else {
							// conversion needed
							int new_day = (day + ((hr + dif) / 24)) % 7;
							int new_hr = (hr + dif) % 24;
							if (new_hr < 0) {
								new_hr += 24;
								if (new_day - 1 < 0) {
									same = checkTime(others.get(i), 6, new_hr);
								} else {
								same = checkTime(others.get(i), new_day - 1, new_hr);
								}
							} else {
								same = checkTime(others.get(i), new_day, new_hr);
							}
						}
						// If this contact is compatible, no need to check the others
						if (same != true) {
							break;
						}
					}
					
					// If same is true, all of the contacts were available at that time
					//map.put(day, map.get(day).add(hr));
					if (same == true) {
						//System.out.println(day + ", " + hr);
						
				        if (!map.containsKey(day)) {
				            map.put(day, new ArrayList<>());
				        }
				        map.get(day).add(hr);
					}
					
				} else continue;
			}
		}
		
	}
	
	public static Boolean checkTime(Contact other, int day, int hr) {
		if (other.getAvailability()[day][hr] != null &&
				other.getAvailability()[day][hr] == true) return true;
		else return false;
	}
	
	// TODO: get offset
	public static int getOffsetInt(ZoneOffset offset) {
		String o = offset.toString();
		o = o.substring(0, 3);
		try {
			int i = Integer.parseInt(o);
			return i;
		} catch (Exception e) {
			System.out.println("error converting offset to int");
			//System.exit(1);
			return -100;
		}
	}
	
	// Gets the difference in offset between the user and the other contact. For example, -05 (user) and +08 (other) have a +13 difference.
	public static int getDif(int user, int other) {
		return (0 - user) - (0 - other);
	}


	// TODO: get mutual schedule
	public Map<Integer, ArrayList<Integer>> getMutual() {
		return this.map;
	}
	
	/* Alert that shows if there is an error converting offset to int */
	private static void showErrorAlert() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText("There was an error creating the schedule.");
		alert.show();
	}
}
