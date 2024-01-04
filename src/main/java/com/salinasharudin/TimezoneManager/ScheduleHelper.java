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
	
	// Get members 
	public static void createMutualSchedule(ArrayList<Contact> others) {
		
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
		
		buildSchedule(difs, others);
	}
	
	public static void buildSchedule(int[] difs, ArrayList<Contact> others) {

		Boolean[][] user = Settings.getInstance().getSchedule();
		
		/* Build the schedule */
		for (int day = 0; day < 7; day++) {
			for (int hr = 0; hr < 24; hr++) {
				// Check if the user has that time available
				if (user[day][hr] == true) {
					
					Boolean same = false;
					
					// Check each contact's schedule
					for (int i = 0; i < others.size(); i++) {
						int dif = difs[i];
						
						// TODO: move to separate method
						if (dif == 0) {
							// same zone - no conversion needed
							same = checkTime(others.get(i), day, hr);
						} else {
							// conversion needed
							int[] converted = new int[2]; // converted time as [new_day, new_hr]
							converted = convertTime(dif, day, hr);
							
							same = checkTime(others.get(i), converted[0], converted[1]);
						}
						
						// If this contact is not compatible, no need to check the others
						if (same != true) {
							break;
						}
					}
					
					// If same is true, all of the contacts were available at that time
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
	
	private static int[] convertTime(int dif, int day, int hr) {
		// Convert the day and time using the dif
		int new_day = (day + ((hr + dif) / 24)) % 7;
		int new_hr = (hr + dif) % 24;
		
		// Dif is negative and large enough to set it back to the previous day
		if (new_hr < 0) {
			new_hr += 24;
			
			// If previous day was already the first day of the week, set it to the last day of the week
			if (new_day - 1 < 0) {
				new_day = 6;
			} else {
				// Else just set it back one day
				new_day -= 1;
			}
		} 
		return new int[]{new_day, new_hr};
	}

	public static Boolean checkTime(Contact other, int day, int hr) {
		if (other.getAvailability()[day][hr] != null &&
				other.getAvailability()[day][hr] == true) return true;
		else return false;
	}
	
	// Get Offset
	public static int getOffsetInt(ZoneOffset offset) {
		String o = offset.toString();
		if (o == "Z") {
			return 0;
		}
		
		try {
			o = o.substring(0, 3);
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


	// Get mutual schedule
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
