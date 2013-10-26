package com.licensetokil.atypistcalendar.gcal;

import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.Schedule;
import com.licensetokil.atypistcalendar.tasksmanager.Deadline;
import com.licensetokil.atypistcalendar.tasksmanager.Todo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

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

		JsonArray calendarList = serverReply.getAsJsonArray("items");
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
		
		this.setRemoteCalendarId(serverReply.get("id").getAsString());
	}
	
	public void syncAllTasks(ArrayList<Task> tasks)
			throws IOException, JsonParseException, IllegalStateException {
		syncAllTasks(tasks, "");
	}
	
	private void syncAllTasks(ArrayList<Task> tasks, String pageToken)
			throws IOException, JsonParseException, IllegalStateException {
		HashMap<String, String> formParameters = new HashMap<String, String>();
		formParameters.put("pageToken", pageToken);
		
		JsonObject serverReply = parseStringIntoJsonObject(
				HttpsConnectionHelper.sendUrlencodedFormRequest(
						"https://www.googleapis.com/calendar/v3/calendars/" + getRemoteCalendarId() + "/events",
						HttpsConnectionHelper.REQUEST_METHOD_GET,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						formParameters)
				);
		
		JsonArray remoteTasksList = serverReply.getAsJsonArray("items");
		Iterator<JsonElement> remoteTasksListIterator = remoteTasksList.iterator();
		while(remoteTasksListIterator.hasNext()) {
			JsonObject remoteTask = (JsonObject)remoteTasksListIterator.next();
			
			JsonObject remoteTaskExtendedProperties = remoteTask.getAsJsonObject("extendedProperties").getAsJsonObject("private").getAsJsonObject("atypistscalendar");
			if(remoteTaskExtendedProperties == null) {
				//TODO push into insert queue
			}
			else {
				//int correspondingLocalTaskUniqueID = remoteTaskExtendedProperties.get("uniqueID").getAsInt();
				//Iterator<Task>  
			}
		}
		
		if(serverReply.get("nextPageToken") != null) {
			syncAllTasks(tasks, serverReply.get("nextPageToken").getAsString());
		}
	}
	
	public void createRemoteTask(Task task) {
		
	}
	
	public void updateRemoteTask(Task task) {
		
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