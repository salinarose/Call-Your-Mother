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
	public	Map<Integer, ArrayList<Integer>> map = new HashMap<>();
	
	// TODO: get members 
	public void getMembers(ArrayList<Contact> others) {
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
		
		for (Contact c : others) {
			Boolean[][] other = c.getAvailability();
			// Get the user's offset as an int
			int otherOffsetInt = getOffsetInt(OffsetDateTime.now(ZoneId.of(c.getTimezone())).getOffset());
			if (otherOffsetInt == -100) {
				map.clear();
				showErrorAlert();
				return;
			}
			
			int dif = getDif(userOffsetInt, otherOffsetInt);
			
			
			//TODO: stopped here!!
			//checkSchedule(dif);
			
			//map = getMutual(map, other, dif);
		}
	}
	

	
	// TODO: get offset
	public int getOffsetInt(ZoneOffset offset) {
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
	public int getDif(int user, int other) {
		return (0 - user) - (0 - other);
	}
	
	// TODO: check time 

	// TODO: get mutual schedule
	public void getMutual(Map<Integer, ArrayList<Integer>> map, Boolean[][] other) {

	}
	
	/* Alert that shows if there is an error converting offset to int */
	private void showErrorAlert() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText("There was an error creating the schedule.");
		alert.show();
	}
}
