package com.licensetokil.atypistcalendar.gcal;

public class GoogleCalendarManager {
	private static GoogleCalendarManager instance = null;
	
	private GoogleCalendarManager() {
	}
	
	static public GoogleCalendarManager getInstance() {
		if(instance == null) {
			instance = new GoogleCalendarManager();
		}
		return instance;
	}
	
	public void authenticateUser() {
		AuthenticationManager.getInstance().authenticateUser();
	}
}