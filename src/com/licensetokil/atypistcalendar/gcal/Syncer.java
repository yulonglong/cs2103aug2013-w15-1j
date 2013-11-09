package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.licensetokil.atypistcalendar.ATypistCalendar;
import com.licensetokil.atypistcalendar.tasksmanager.Deadline;
import com.licensetokil.atypistcalendar.tasksmanager.Schedule;
import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.TaskNotFoundException;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.tasksmanager.Todo;

class Syncer extends Thread {
	private static Logger logger = Logger.getLogger(Syncer.class.getName());

	public Syncer() {
		logger.fine("Constructor of Syncer called.");
	}

	public void run() {
		logger.fine("run() called.");


		while(true) {
			SyncAction currentSyncAction = SyncManager.getInstance().getQueue().poll();

			while(currentSyncAction != null) {
				logger.info("Working on next SyncAction. Figuring out the sub-type.");
				try {
					if(currentSyncAction instanceof InitialiseRemoteCalendarSyncAction) {
						logger.info("InitialiseRemoteCalendarSyncAction sub-type detected.");
						executeSyncAction((InitialiseRemoteCalendarSyncAction)currentSyncAction);
					}
					else if(currentSyncAction instanceof AddSyncAction) {
						logger.info("AddSyncAction sub-type detected.");
						executeSyncAction((AddSyncAction)currentSyncAction);
					}
					else if(currentSyncAction instanceof UpdateSyncAction) {
						logger.info("UpdateSyncAction sub-type detected.");
						executeSyncAction((UpdateSyncAction)currentSyncAction);
					}
					else if(currentSyncAction instanceof DeleteSyncAction) {
						logger.info("DeleteSyncAction sub-type detected.");
						executeSyncAction((DeleteSyncAction)currentSyncAction);
					}
					else if(currentSyncAction instanceof DoCompleteSyncAction) {
						logger.info("DoCompleteSyncAction sub-type detected.");

						logger.info("Checking if following SyncActions in queue are DoCompleteSyncAction. Dropping them if they are, for efficency.");
						SyncAction nextSyncAction = SyncManager.getInstance().getQueue().peek();
						while(nextSyncAction instanceof DoCompleteSyncAction) {
							logger.info("Dropping next DoCompleteSyncAction");
							SyncManager.getInstance().getQueue().poll();
							nextSyncAction = SyncManager.getInstance().getQueue().peek();
						}
						logger.info("Check done.");

						executeSyncAction((DoCompleteSyncAction)currentSyncAction);
					}
					else {
						logger.severe("Unexcepted (subclass of) SyncAction enqueued.");
						assert false;
					}

					logger.info("Dequeuing SyncAction item that has been completed (removing head of queue).");
					currentSyncAction = SyncManager.getInstance().getQueue().poll();
				}
				catch(IllegalStateException | JsonParseException | IOException e) {
					logger.warning("Exception thrown: " + e.getMessage());
					logger.warning("Waiting 1 minute before retrying...");

					sleepForOneMinute();
				}
			}

			logger.info("Queue is empty, sleeping for 1 minute before next sync...");
			sleepForOneMinute();
			logger.info("Thread woke up. Checking if queue is empty.");
			if(SyncManager.getInstance().getQueue().isEmpty()) {
				logger.info("Queue is empty. Doing a complete sync.");
				SyncManager.getInstance().doCompleteSync();
			}
			else {
				logger.info("Queue is not empty. Continuing with enqueued SyncAction.");
			}
		}
	}

	private void sleepForOneMinute() {
		try {
			SyncManager.getInstance().getGoToSleepLock().lock();
			SyncManager.getInstance().getGoToSleepCondition().await(15, TimeUnit.MINUTES);
			SyncManager.getInstance().getGoToSleepLock().unlock();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void executeSyncAction(InitialiseRemoteCalendarSyncAction currentSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(InitialiseRemoteCalendarSyncAction currentSyncAction) called.");

		logger.info("Checking if remote calendar exists.");
		if(!remoteCalendarExists()) {
			logger.info("Remote calendar does not exist, creating remote calendar.");
			createRemoteCalendar();
		}
		else {
			logger.info("Remote calendar exists. Nothing to do.");
		}
	}

	private void executeSyncAction(AddSyncAction addSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(AddSyncAction currentSyncAction) called.");

		logger.info("Adding: " + addSyncAction.getLocalTask().toString());

		JsonObject requestBody = createRemoteTaskRequestBody(addSyncAction.getLocalTask());

		JsonObject serverReply = Util.parseToJsonObject(
				Util.sendJsonHttpsRequest(
						"https://www.googleapis.com/calendar/v3/calendars/" +
								SyncManager.getInstance().getRemoteCalendarId() +
								"/events",
						Util.REQUEST_METHOD_POST,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						requestBody
				)
		);

		try {
			TasksManager.getInstance().updateCorrespondingTaskRemoteId(
					addSyncAction.getLocalTask().getUniqueId(),
					Util.getJsonObjectValueOrEmptyString(serverReply, "id")
			);
		} catch (TaskNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void executeSyncAction(UpdateSyncAction updateSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(UpdateSyncAction currentSyncAction) called.");

		JsonObject requestBody = createRemoteTaskRequestBody(updateSyncAction.getLocalTask());

		Util.sendJsonHttpsRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" +
						SyncManager.getInstance().getRemoteCalendarId() +
						"/events/" +
						updateSyncAction.getRemoteTaskID(),
				Util.REQUEST_METHOD_PUT,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				requestBody);
	}

	private void executeSyncAction(DeleteSyncAction deleteSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(DeleteSyncAction currentSyncAction) called.");

		logger.info("Deleting: " + deleteSyncAction.getRemoteTaskID());

		Util.sendJsonHttpsRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" +
						SyncManager.getInstance().getRemoteCalendarId() +
						"/events/" +
						deleteSyncAction.getRemoteTaskID(),
				Util.REQUEST_METHOD_DELETE,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				null);
	}

	private void executeSyncAction(DoCompleteSyncAction currentSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(DoCompleteSyncAction currentSyncAction) called.");

		logger.info("Getting a copy of all local tasks from TasksManager. Tasks will be deleted from this local copy as its corresponding remote task is found. All remaining local tasks without the corresponding remote task will be uploaded accordingly.");
		syncAllTasks(ATypistCalendar.getInstance().getCopyOfAllLocalTasks(), null);
	}

	private boolean remoteCalendarExists()
			throws IOException, JsonParseException, IllegalStateException {
		logger.fine("remoteCalendarExists() called.");

		JsonObject serverReply = Util.parseToJsonObject(
				Util.sendJsonHttpsRequest(
						"https://www.googleapis.com/calendar/v3/users/me/calendarList",
						Util.REQUEST_METHOD_GET,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						null)
				);

		JsonArray calendarList = serverReply.getAsJsonArray("items");
		Iterator<JsonElement> calendarListIterator = calendarList.iterator();
		while(calendarListIterator.hasNext()) {
			JsonObject calendarListElement = (JsonObject)calendarListIterator.next();

			if(Util.getJsonObjectValueOrEmptyString(calendarListElement, "summary").equals("A Typist's Calendar")) {
				SyncManager.getInstance().setRemoteCalendarId(calendarListElement.get("id").getAsString());
				return true;
			}
		}

		return false;
	}

	private void createRemoteCalendar()
			throws IOException, JsonParseException, IllegalStateException {
		logger.fine("createRemoteCalendar() called.");

		//TODO assert remoteCalendarExists() == false??

		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("summary", "A Typist's Calendar");
		requestBody.addProperty("timeZone", Calendar.getInstance().getTimeZone().getID());
		requestBody.addProperty("location", Calendar.getInstance().getTimeZone().getID());

		JsonObject serverReply = Util.parseToJsonObject(
				Util.sendJsonHttpsRequest(
						"https://www.googleapis.com/calendar/v3/calendars",
						Util.REQUEST_METHOD_POST,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						requestBody)
				);

		SyncManager.getInstance().setRemoteCalendarId(serverReply.get("id").getAsString());
	}

	private JsonObject createRemoteTaskRequestBody(Task localTask) {
		logger.fine("createRemoteTaskRequestBody(Task localTask) called.");

		JsonObject requestBody = new JsonObject();

		requestBody.addProperty("location", localTask.getPlace());
		requestBody.add("extendedProperties", createExtendedPropertiesObject(localTask.getUniqueId()));

		if(localTask instanceof Schedule) {
			Schedule localSchedule = (Schedule)localTask;

			requestBody.addProperty("summary", localSchedule.getDescription());
			requestBody.add("start", Util.createGoogleDateTimeObject(localSchedule.getStartTime()));
			requestBody.add("end", Util.createGoogleDateTimeObject(localSchedule.getEndTime()));
		}
		else if(localTask instanceof Deadline) {
			//TODO handle done/undone

			Deadline localDeadline = (Deadline)localTask;

			requestBody.addProperty("summary", "DL: " + localDeadline.getDescription());
			requestBody.add("start", Util.createGoogleDateTimeObject(localDeadline.getEndTime()));
			requestBody.add("end", Util.createGoogleDateTimeObject(localDeadline.getEndTime()));
		}
		else if(localTask instanceof Todo) {
			//TODO handle done/undone
			Todo localTodo = (Todo)localTask;

			requestBody.addProperty("summary", "Todo: " + localTodo.getDescription());
			requestBody.add("start", Util.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));
			requestBody.add("end", Util.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));
			requestBody.add("recurrence", SyncManager.REMOTE_TODO_RECURRENCE_PROPERTY);
		}
		else {
			assert false;
		}

		return requestBody;
	}

	private void syncAllTasks(ArrayList<Task> localTasks, String pageToken)
			throws IOException, JsonParseException, IllegalStateException {
		logger.fine("syncAllTasks(ArrayList<Task> localTasks, String pageToken) called.");

		logger.info("Getting remote task list with pageToken: " + pageToken);
		JsonObject serverReply = getRemoteTaskList(pageToken);

		logger.info("Iterating through the remote task list (for current page).");
		JsonArray remoteTasksList = serverReply.getAsJsonArray("items");
		Iterator<JsonElement> remoteTasksListIterator = remoteTasksList.iterator();
		while(remoteTasksListIterator.hasNext()) {
			logger.info("Examining next remote task.");
			examineRemoteTask(localTasks, remoteTasksListIterator.next().getAsJsonObject());
		}
		logger.info("Completed iterating through remote task list (for current page).");

		if(serverReply.get("nextPageToken") != null) {
			logger.info("Next page for remote task list is avaliable, continuing on to next page.");
			syncAllTasks(localTasks, serverReply.get("nextPageToken").getAsString());
		}
		else {
			logger.info("Already on last page for remote task list. Enqueuing all remaining local tasks (count = " + localTasks.size() + ") for (1) uploading to Google Calendar, or (2) deletion from local list.");
			for(Task currentLocalTask : localTasks) {
				if(currentLocalTask.getRemoteId() == null) {
					SyncManager.getInstance().addRemoteTask(currentLocalTask);
				}
				else {
					try {
						TasksManager.getInstance().deleteGoogleTask(currentLocalTask.getUniqueId());
					} catch (TaskNotFoundException e) {
						//Do nothing.
					}
				}
			}
		}
	}

	private void examineRemoteTask(ArrayList<Task> localTasks, JsonObject remoteTask) {
		logger.fine("examineRemoteTask(ArrayList<Task> localTasks, JsonObject remoteTask) called.");

		Integer correspondingLocalTaskId = getCorrespondingLocalTaskId(remoteTask);
		logger.info("Checking if remote task was not created by ATC (i.e. by user, or other programs)");
		if(correspondingLocalTaskId == null) {
			logger.info("Remote task was not created by ATC. Inserting into local TasksManger.");
			insertRemoteTaskIntoTasksManager(remoteTask);
		}
		else {
			logger.info("Remote task was created by ATC. Searching for corresponding local task.");
			Task currentLocalTask = searchForCorrespondingLocalTask(localTasks, correspondingLocalTaskId);
			if(currentLocalTask != null) {
				logger.info("Corresponding local task found, checking for discrepancies between local and remote copies.");
				if(!isIdentical(remoteTask, currentLocalTask)) {
					logger.info("Discrepancies between local and remote copies found. Synchornising the two copies.");
					synchorniseLocalAndRemoteTask(remoteTask, currentLocalTask);
				}
				else {
					logger.info("No discrepancies between local and remote copies found. Nothing to do as remote copy is sync'ed.");
				}

				logger.info("Removing corresponding local task from the local tasks list.");
				localTasks.remove(currentLocalTask);
			}
			else {
				logger.info("Corresponding local task not found, enqueuing to delete remote copy.");
				SyncManager.getInstance().deleteRemoteTask(remoteTask.get("id").getAsString());
			}
		}
	}

	private void insertRemoteTaskIntoTasksManager(JsonObject remoteTask) {
		String remoteTaskId = Util.getJsonObjectValueOrEmptyString(remoteTask, "id");
		String description = Util.getJsonObjectValueOrEmptyString(remoteTask, "summary");
		String location = Util.getJsonObjectValueOrEmptyString(remoteTask, "location");
		Calendar lastModifiedDate = null;
		try {
			lastModifiedDate = Util.parseGenericGoogleDateString(remoteTask.get("updated").getAsString(), Util.getRfc3339FormatWithMilliseconds());
		} catch (ParseException e) {
			//TODO Fail quietly
			lastModifiedDate = Calendar.getInstance();
		}

		Task localTask = null;

		if(remoteTask.getAsJsonObject("start").get("date") != null) {
			//Task type is a todo
			localTask = TasksManager.getInstance().addTodoFromGoogle(description, location, lastModifiedDate, remoteTaskId);
		}
		else {
			Calendar startTime = null;
			Calendar endTime = null;
			try {
				startTime = Util.parseGoogleDateTimeObject(remoteTask.getAsJsonObject("start"));
				endTime = Util.parseGoogleDateTimeObject(remoteTask.getAsJsonObject("end"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Breaking out... Help!");
				return;
			}

			if(startTime.compareTo(endTime) == 0) {
				//Task type is a deadline
				localTask = TasksManager.getInstance().addDeadlineFromGoogle(endTime, description, location, lastModifiedDate, remoteTaskId);
			}
			else {
				//Task type is a schedule
				localTask = TasksManager.getInstance().addScheduleFromGoogle(startTime, endTime, description, location, lastModifiedDate, remoteTaskId);
			}
		}

		SyncManager.getInstance().updateRemoteTask(localTask, remoteTaskId);
	}

	private void synchorniseLocalAndRemoteTask(JsonObject remoteTask, Task currentLocalTask) {
		Calendar remoteTaskLastModifiedTime = null;
		try {
			remoteTaskLastModifiedTime = Util.parseGenericGoogleDateString(remoteTask.get("updated").getAsString(), Util.getRfc3339FormatWithMilliseconds());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar localTaskLastModifiedTime = currentLocalTask.getLastModifiedDate();

		//We take the one modified the latest.
		if(localTaskLastModifiedTime.compareTo(remoteTaskLastModifiedTime) >= 0) { // >= instead of > implies that the local copy get priority
			SyncManager.getInstance().updateRemoteTask(currentLocalTask, remoteTask.get("id").getAsString());
		}
		else {
			updateLocalTaskfromRemoteTask(remoteTask, currentLocalTask);
		}
	}

	private void updateLocalTaskfromRemoteTask(JsonObject remoteTask, Task currentLocalTask) {
		logger.fine("updateLocalTaskfromRemoteTask called.");

		String remoteTaskId = Util.getJsonObjectValueOrEmptyString(remoteTask, "id");
		String description = Util.getJsonObjectValueOrEmptyString(remoteTask, "summary");
		String location = Util.getJsonObjectValueOrEmptyString(remoteTask, "location");
		Calendar lastModifiedDate = null;
		try {
			lastModifiedDate = Util.parseGenericGoogleDateString(remoteTask.get("updated").getAsString(), Util.getRfc3339FormatWithMilliseconds());
		} catch (ParseException e) {
			//TODO Fail quietly
			lastModifiedDate = Calendar.getInstance();
		}

		if(currentLocalTask instanceof Todo) {
			logger.fine("currentLocalTask is of type Todo.");
			Todo currentLocalTodo = (Todo)currentLocalTask;
			currentLocalTodo.setDescription(removePrefix(description, "Todo: "));
			currentLocalTodo.setPlace(location);
			currentLocalTodo.setLastModifiedDate(lastModifiedDate);
			currentLocalTodo.setRemoteId(remoteTaskId);

			try {
				TasksManager.getInstance().updateGoogleTask(currentLocalTask);
			} catch (TaskNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			Calendar startTime = null;
			Calendar endTime = null;
			try {
				startTime = Util.parseGoogleDateTimeObject(remoteTask.getAsJsonObject("start"));
				endTime = Util.parseGoogleDateTimeObject(remoteTask.getAsJsonObject("end"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Breaking out... Help!");
				return;
			}

			if(currentLocalTask instanceof Deadline) {
				logger.fine("currentLocalTask is of type Deadline.");
				Deadline currentLocalDeadline = (Deadline)currentLocalTask;
				currentLocalDeadline.setDescription(removePrefix(description, "DL: "));
				currentLocalDeadline.setPlace(location);
				currentLocalDeadline.setLastModifiedDate(lastModifiedDate);
				currentLocalDeadline.setEndTime(endTime);
				currentLocalDeadline.setRemoteId(remoteTaskId);

				try {
					TasksManager.getInstance().updateGoogleTask(currentLocalDeadline);
				} catch (TaskNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				logger.fine("currentLocalTask is of type Schedule.");
				Schedule currentLocalSchedule = (Schedule)currentLocalTask;
				currentLocalSchedule.setDescription(description);
				currentLocalSchedule.setPlace(location);
				currentLocalSchedule.setLastModifiedDate(lastModifiedDate);
				currentLocalSchedule.setStartTime(startTime);
				currentLocalSchedule.setEndTime(endTime);
				currentLocalSchedule.setRemoteId(remoteTaskId);

				try {
					TasksManager.getInstance().updateGoogleTask(currentLocalSchedule);
				} catch (TaskNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private JsonObject getRemoteTaskList(String pageToken)
			throws IOException, JsonParseException, IllegalStateException {
		logger.fine("getRemoteTaskList(String pageToken) called.");

		HashMap<String, String> formParameters = null;

		if(pageToken != null) {
			formParameters = new HashMap<String, String>();
			formParameters.put("pageToken", pageToken);
		}

		JsonObject serverReply = Util.parseToJsonObject(
				Util.sendUrlencodedFormHttpsRequest(
						"https://www.googleapis.com/calendar/v3/calendars/" +
								SyncManager.getInstance().getRemoteCalendarId() +
								"/events",
						Util.REQUEST_METHOD_GET,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						formParameters)
				);
		return serverReply;
	}

	private Task searchForCorrespondingLocalTask(ArrayList<Task> localTasks, Integer extendedPropertiesLocalTaskID) {
		logger.fine("searchForCorrespondingLocalTask(ArrayList<Task> localTasks, String extendedPropertiesLocalTaskID) called.");

		for(Task currentLocalTask : localTasks) {
			if(extendedPropertiesLocalTaskID == currentLocalTask.getUniqueId()) {
				return currentLocalTask;
			}
		}
		return null;
	}

	private Integer getCorrespondingLocalTaskId(JsonObject remoteTask) {
		if(privateExtendedPropertiesExist(remoteTask)) {
			return remoteTask.getAsJsonObject("extendedProperties").getAsJsonObject("private").get("atc_localTaskId").getAsInt();
		}
		return null;
	}

	private boolean privateExtendedPropertiesExist(JsonObject remoteTask) {
		if(remoteTask.getAsJsonObject("extendedProperties") == null) {
			return false;
		}
		if(remoteTask.getAsJsonObject("extendedProperties").getAsJsonObject("private") == null) {
			return false;
		}
		if(remoteTask.getAsJsonObject("extendedProperties").getAsJsonObject("private").get("atc_localTaskId") == null) {
			return false;
		}
		return true;
	}

	private boolean isIdentical(JsonObject remoteTask, Task localTask) {
		logger.fine("isIdentical(JsonObject remoteTask, Task localTask) called.");

		boolean locationIsIdentical = Util.getJsonObjectValueOrEmptyString(remoteTask, "location").equals(localTask.getPlace());
		boolean summaryIsIdentical = false;
		boolean startTimeIsIdentical = false;
		boolean endTimeIsIdentical = false;
		boolean recurrenceIsIdentical = false;

		if(localTask instanceof Schedule) {
			Schedule localSchedule = (Schedule)localTask;

			summaryIsIdentical = Util.getJsonObjectValueOrEmptyString(remoteTask, "summary").equals(localSchedule.getDescription());
			startTimeIsIdentical = remoteTask.getAsJsonObject("start").equals(Util.createGoogleDateTimeObject(localSchedule.getStartTime()));
			endTimeIsIdentical = remoteTask.getAsJsonObject("end").equals(Util.createGoogleDateTimeObject(localSchedule.getEndTime()));
			recurrenceIsIdentical = true; // we don't bother about recurrence for Schedules, so we just set it to true
		}
		else if(localTask instanceof Deadline) {
			//TODO handle done/undone
			Deadline localDeadline = (Deadline)localTask;

			summaryIsIdentical = Util.getJsonObjectValueOrEmptyString(remoteTask, "summary").equals("DL: " + localDeadline.getDescription());
			startTimeIsIdentical = remoteTask.getAsJsonObject("start").equals(Util.createGoogleDateTimeObject(localDeadline.getEndTime()));
			endTimeIsIdentical = remoteTask.getAsJsonObject("end").equals(Util.createGoogleDateTimeObject(localDeadline.getEndTime()));
			recurrenceIsIdentical = true; // we don't bother about recurrence for Deadlines, so we just set it to true
		}
		else if(localTask instanceof Todo) {
			//TODO handle done/undone
			Todo localTodo = (Todo)localTask;

			summaryIsIdentical = Util.getJsonObjectValueOrEmptyString(remoteTask, "summary").equals("Todo: " + localTodo.getDescription());
			startTimeIsIdentical = remoteTask.getAsJsonObject("start").equals(Util.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));
			endTimeIsIdentical = remoteTask.getAsJsonObject("end").equals(Util.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));

			if(remoteTask.get("recurrence") != null) {
				recurrenceIsIdentical = remoteTask.get("recurrence").getAsJsonArray().equals(SyncManager.REMOTE_TODO_RECURRENCE_PROPERTY);
			}
		}
		else {
			assert false;
			return false;
		}

		return summaryIsIdentical &&
				locationIsIdentical &&
				startTimeIsIdentical &&
				endTimeIsIdentical &&
				recurrenceIsIdentical;
	}

	private JsonObject createExtendedPropertiesObject(int localTaskID) {
		JsonObject privateExtendedProperties = new JsonObject();
		privateExtendedProperties.addProperty("atc_localTaskId", Integer.toString(localTaskID));

		JsonObject extendedProperties = new JsonObject();
		extendedProperties.add("private", privateExtendedProperties);

		return extendedProperties;
	}

	private String removePrefix(String originalString, String prefix) {
		String originalStringInLowerCase = originalString.toLowerCase();
		String prefixInLowerCase = prefix.toLowerCase();
		if(originalStringInLowerCase.startsWith(prefixInLowerCase)) {
			return originalString.substring(prefix.length());
		}
		else {
			return originalString;
		}
	}
}
