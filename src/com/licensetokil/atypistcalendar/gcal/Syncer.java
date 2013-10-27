package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.licensetokil.atypistcalendar.gcal.util.GoogleCalendarUtilities;
import com.licensetokil.atypistcalendar.gcal.util.HttpsConnectionHelper;
import com.licensetokil.atypistcalendar.tasksmanager.Deadline;
import com.licensetokil.atypistcalendar.tasksmanager.Schedule;
import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.tasksmanager.Todo;

class Syncer extends Thread {
	public Syncer() {
	}
	
	public void run() {
		SyncAction currentSyncAction = SyncManager.getInstance().getQueue().peek();
		
		while(currentSyncAction != null) {
			try {
				if(currentSyncAction instanceof AddSyncAction) {
					executeSyncAction((AddSyncAction)currentSyncAction);
				}
				else if(currentSyncAction instanceof UpdateSyncAction) {
					executeSyncAction((UpdateSyncAction)currentSyncAction);
				}
				else if(currentSyncAction instanceof DeleteSyncAction) {				
					executeSyncAction((DeleteSyncAction)currentSyncAction);
				}
				else if(currentSyncAction instanceof InitialiseRemoteCalendarSyncAction) {
					executeSyncAction((InitialiseRemoteCalendarSyncAction)currentSyncAction);
				}
				else if(currentSyncAction instanceof DoCompleteSyncAction) {
					executeSyncAction((DoCompleteSyncAction)currentSyncAction);
				}
				else {
					assert false;
				}
			}
			catch(IllegalStateException | JsonParseException | IOException e) {
				//TODO what to do?
				e.printStackTrace();
			}
			
			SyncManager.getInstance().getQueue().poll(); //drop the item that has been sync'ed
			currentSyncAction = SyncManager.getInstance().getQueue().peek();
		}
	}

	private void executeSyncAction(DoCompleteSyncAction currentSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		syncAllTasks(TasksManager.getAllTasks(), "");
	}

	private void executeSyncAction(InitialiseRemoteCalendarSyncAction currentSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		if(!remoteCalendarExists()) {
			createRemoteCalendar();
		}
	}

	private void executeSyncAction(UpdateSyncAction updateSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		JsonObject requestBody = createRemoteTaskRequestBody(updateSyncAction.getLocalTask());
		
		HttpsConnectionHelper.sendJsonRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" +
						SyncManager.getInstance().getRemoteCalendarId() + 
						"/events" + 
						updateSyncAction.getRemoteTaskID(),
				HttpsConnectionHelper.REQUEST_METHOD_PUT,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				requestBody);
	}

	private void executeSyncAction(DeleteSyncAction deleteSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		HttpsConnectionHelper.sendJsonRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" + 
						SyncManager.getInstance().getRemoteCalendarId() + 
						"/events/" + 
						deleteSyncAction.getRemoteTaskID(),
				HttpsConnectionHelper.REQUEST_METHOD_DELETE,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				null);
	}

	private void executeSyncAction(AddSyncAction addSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		JsonObject requestBody = createRemoteTaskRequestBody(addSyncAction.getLocalTask());
		
		HttpsConnectionHelper.sendJsonRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" +
						SyncManager.getInstance().getRemoteCalendarId() + 
						"/events",
				HttpsConnectionHelper.REQUEST_METHOD_POST,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				requestBody);
	}

	private JsonObject createRemoteTaskRequestBody(Task localTask) {
		JsonObject requestBody = new JsonObject();
		
		if(localTask instanceof Schedule) {
			Schedule localSchedule = (Schedule)localTask;
			
			requestBody.addProperty("summary", localSchedule.getDescription());
			requestBody.addProperty("description", localSchedule.getDescription());
			requestBody.addProperty("location", localSchedule.getPlace());
			requestBody.add("start", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getStartTime()));
			requestBody.add("end", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getEndTime()));
			requestBody.add("extendedProperties", GoogleCalendarUtilities.createExtendedPropertiesObject(localSchedule.getUniqueID()));
		}
		else if(localTask instanceof Deadline) {
			//TODO handle done/undone
			
			Deadline localSchedule = (Deadline)localTask;
			
			requestBody.addProperty("summary", "Deadline:" + localSchedule.getDescription());
			requestBody.addProperty("description", localSchedule.getDescription());
			requestBody.addProperty("location", localSchedule.getPlace());
			requestBody.add("start", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getEndTime()));
			requestBody.add("end", GoogleCalendarUtilities.createDateTimeObject(localSchedule.getEndTime()));
			requestBody.add("extendedProperties", GoogleCalendarUtilities.createExtendedPropertiesObject(localSchedule.getUniqueID()));
		}
		else if(localTask instanceof Todo) {
			//TODO handle done/undone
			Todo localTodo = (Todo)localTask;
			
			Calendar firstOfCurrentMonth = Calendar.getInstance();
			firstOfCurrentMonth.set(Calendar.DATE, 1);
			
			JsonArray recurrenceArray = new JsonArray();
			recurrenceArray.add(new JsonPrimitive("RRULE:FREQ=MONTHLY"));
			
			requestBody.addProperty("summary", "Todo: " + localTodo.getDescription());
			requestBody.addProperty("description", localTodo.getDescription());
			requestBody.addProperty("location", localTodo.getPlace());
			requestBody.add("start", GoogleCalendarUtilities.createDateObject(firstOfCurrentMonth));
			requestBody.add("end", GoogleCalendarUtilities.createDateObject(firstOfCurrentMonth));
			requestBody.add("extendedProperties", GoogleCalendarUtilities.createExtendedPropertiesObject(localTodo.getUniqueID()));
			requestBody.add("recurrence", recurrenceArray);
		}
		else {
			assert false;
		}
		
		return requestBody;
	}
	
	private boolean remoteCalendarExists()
			throws IOException, JsonParseException, IllegalStateException {
		JsonObject serverReply = GoogleCalendarUtilities.parseToJsonObject(
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
				SyncManager.getInstance().setRemoteCalendarId(calendarListElement.get("id").getAsString());
				return true;
			}
		}

		return false;
	}
	
	private void createRemoteCalendar()
			throws IOException, JsonParseException, IllegalStateException {
		//TODO assert remoteCalendarExists() == false??
		
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("summary", "A Typist's Calendar");
		
		JsonObject serverReply = GoogleCalendarUtilities.parseToJsonObject(
				HttpsConnectionHelper.sendJsonRequest(
						"https://www.googleapis.com/calendar/v3/calendars",
						HttpsConnectionHelper.REQUEST_METHOD_POST,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						requestBody)
				);
		
		SyncManager.getInstance().setRemoteCalendarId(serverReply.get("id").getAsString());
	}
	
	private void syncAllTasks(ArrayList<Task> tasks, String pageToken)
			throws IOException, JsonParseException, IllegalStateException {
		HashMap<String, String> formParameters = new HashMap<String, String>();
		formParameters.put("pageToken", pageToken);
		
		JsonObject serverReply = GoogleCalendarUtilities.parseToJsonObject(
				HttpsConnectionHelper.sendUrlencodedFormRequest(
						"https://www.googleapis.com/calendar/v3/calendars/" +
								SyncManager.getInstance().getRemoteCalendarId() + 
								"/events",
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
}
