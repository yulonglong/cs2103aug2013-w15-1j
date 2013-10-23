package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

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

	public boolean remoteCalendarExists() {
		JsonObject serverReply;
		try {
			String serverReplyAsString = HttpsConnectionHelper.sendJsonRequest(
					"https://www.googleapis.com/calendar/v3/users/me/calendarList",
					HttpsConnectionHelper.REQUEST_METHOD_GET,
					AuthenticationManager.getInstance().getAuthorizationHeader(),
					null);
			serverReply = (JsonObject)new JsonParser().parse(serverReplyAsString);
		} catch (IllegalStateException | JsonParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		JsonArray calendarList = (JsonArray)serverReply.get("items");
		Iterator<JsonElement> calendarListIterator = calendarList.iterator();
		while(calendarListIterator.hasNext()) {
			JsonObject calendarListElement = (JsonObject)calendarListIterator.next();
			if(calendarListElement.get("summary").equals("A Typist's Calendar")) {
				return true;
			}
		}

		return false;
	}
	
	public void createRemoteCalendar() {
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("summary", "A Typist's Calendar");
		
		try {
			HttpsConnectionHelper.sendJsonRequest(
					"https://www.googleapis.com/calendar/v3/calendars",
					HttpsConnectionHelper.REQUEST_METHOD_POST,
					AuthenticationManager.getInstance().getAuthorizationHeader(),
					requestBody);
		} catch (IllegalStateException | JsonParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}