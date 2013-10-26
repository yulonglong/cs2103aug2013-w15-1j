package com.licensetokil.atypistcalendar.gcal;

import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.Schedule;
import com.licensetokil.atypistcalendar.tasksmanager.Deadline;
import com.licensetokil.atypistcalendar.tasksmanager.Todo;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class GoogleCalendarManager {
	private static GoogleCalendarManager instance = null;
	
	private JsonParser jsonParser;
	private String remoteCalendarId;

	private GoogleCalendarManager() {
		jsonParser = new JsonParser();
		setRemoteCalendarId(null);
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

	public boolean remoteCalendarExists()
			throws IOException, JsonParseException, IllegalStateException {
		JsonObject serverReply = parseStringIntoJsonObject(
				HttpsConnectionHelper.sendJsonRequest(
						"https://www.googleapis.com/calendar/v3/users/me/calendarList",
						HttpsConnectionHelper.REQUEST_METHOD_GET,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						null)
				);

		JsonArray calendarList = (JsonArray)serverReply.get("items");
		Iterator<JsonElement> calendarListIterator = calendarList.iterator();
		while(calendarListIterator.hasNext()) {
			JsonObject calendarListElement = (JsonObject)calendarListIterator.next();
			if(calendarListElement.get("summary").getAsString().equals("A Typist's Calendar")) {
				return true;
			}
		}

		return false;
	}
	
	public void createRemoteCalendar()
			throws IOException, JsonParseException, IllegalStateException {
		//TODO assert remoteCalendarExists() == false??
		
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("summary", "A Typist's Calendar");
		
		JsonObject serverReply = parseStringIntoJsonObject(
				HttpsConnectionHelper.sendJsonRequest(
						"https://www.googleapis.com/calendar/v3/calendars",
						HttpsConnectionHelper.REQUEST_METHOD_POST,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						requestBody)
				);
		
		setRemoteCalendarId(serverReply.get("id").getAsString());
	}
	
	public void syncAllTasks(List<Task> tasks) {
		
	}
	
	public void createRemoteTask(Task task) {
		
	}
	
	public void deleteRemoteTask(Task task) {
		
	}
	
	public JsonObject parseStringIntoJsonObject(String stringToParse)
			throws JsonParseException, IllegalStateException {
		return (JsonObject)jsonParser.parse(stringToParse);
	}

	public String getRemoteCalendarId() {
		return remoteCalendarId;
	}

	public void setRemoteCalendarId(String remoteCalendarId) {
		this.remoteCalendarId = remoteCalendarId;
	}
}