package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.licensetokil.atypistcalendar.ATypistCalendar;
import com.licensetokil.atypistcalendar.gcal.util.GoogleCalendarUtilities;
import com.licensetokil.atypistcalendar.gcal.util.HttpsConnectionHelper;
import com.licensetokil.atypistcalendar.tasksmanager.Deadline;
import com.licensetokil.atypistcalendar.tasksmanager.Schedule;
import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.Todo;

class Syncer extends Thread {
	private static Logger logger = Logger.getLogger("com.atypistscalendar.gcal.syncmanger.syncer");

	public Syncer() {
		logger.fine("Constructor of Syncer called.");
	}

	public void run() {
		logger.fine("run() called.");
		SyncAction currentSyncAction = SyncManager.getInstance().getQueue().peek();

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
					executeSyncAction((DoCompleteSyncAction)currentSyncAction);
				}
				else {
					logger.severe("Unexcepted (subclass of) SyncAction enqueued.");
					assert false;
				}

				logger.info("Dropping SyncAction item that has been done (removing head of queue).");
				SyncManager.getInstance().getQueue().poll();
				currentSyncAction = SyncManager.getInstance().getQueue().peek();
			}
			catch(IllegalStateException | JsonParseException | IOException e) {
				logger.warning("Exception thrown: " + e.getMessage());
				e.printStackTrace();
			}
		}
		logger.info("Queue is empty, terminating...");
	}

	private void executeSyncAction(InitialiseRemoteCalendarSyncAction currentSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(InitialiseRemoteCalendarSyncAction currentSyncAction) called.");

		logger.info("Checking if remote calendar exists.");
		if(!remoteCalendarExists()) {
			logger.info("Remote calendar does not exist, creating remote calendar.");
			createRemoteCalendar();
		}
	}

	private void executeSyncAction(AddSyncAction addSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(AddSyncAction currentSyncAction) called.");

		JsonObject requestBody = createRemoteTaskRequestBody(addSyncAction.getLocalTask());

		HttpsConnectionHelper.sendJsonRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" +
						SyncManager.getInstance().getRemoteCalendarId() +
						"/events",
				HttpsConnectionHelper.REQUEST_METHOD_POST,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				requestBody);
	}

	private void executeSyncAction(UpdateSyncAction updateSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(UpdateSyncAction currentSyncAction) called.");

		JsonObject requestBody = createRemoteTaskRequestBody(updateSyncAction.getLocalTask());

		HttpsConnectionHelper.sendJsonRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" +
						SyncManager.getInstance().getRemoteCalendarId() +
						"/events/" +
						updateSyncAction.getRemoteTaskID(),
				HttpsConnectionHelper.REQUEST_METHOD_PUT,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				requestBody);
	}

	private void executeSyncAction(DeleteSyncAction deleteSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(DeleteSyncAction currentSyncAction) called.");

		HttpsConnectionHelper.sendJsonRequest(
				"https://www.googleapis.com/calendar/v3/calendars/" +
						SyncManager.getInstance().getRemoteCalendarId() +
						"/events/" +
						deleteSyncAction.getRemoteTaskID(),
				HttpsConnectionHelper.REQUEST_METHOD_DELETE,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				null);
	}

	private void executeSyncAction(DoCompleteSyncAction currentSyncAction)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncAction(DoCompleteSyncAction currentSyncAction) called.");

		logger.info("Getting a copy of all local tasks from TasksManager. Tasks will be deleted from this local copy as its corresponding remote task is found. All remaining local tasks without the corresponding remote task will be uploaded accordingly.");
		syncAllTasks(ATypistCalendar.getInstance().getCopyOfAllLocalTasks(), null);
	}

	private JsonObject createRemoteTaskRequestBody(Task localTask) {
		logger.fine("createRemoteTaskRequestBody(Task localTask) called.");

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

			Deadline localDeadline = (Deadline)localTask;

			requestBody.addProperty("summary", "Deadline:" + localDeadline.getDescription());
			requestBody.addProperty("description", localDeadline.getDescription());
			requestBody.addProperty("location", localDeadline.getPlace());
			requestBody.add("start", GoogleCalendarUtilities.createDateTimeObject(localDeadline.getEndTime()));
			requestBody.add("end", GoogleCalendarUtilities.createDateTimeObject(localDeadline.getEndTime()));
			requestBody.add("extendedProperties", GoogleCalendarUtilities.createExtendedPropertiesObject(localDeadline.getUniqueID()));
		}
		else if(localTask instanceof Todo) {
			//TODO handle done/undone
			Todo localTodo = (Todo)localTask;

			requestBody.addProperty("summary", "Todo: " + localTodo.getDescription());
			requestBody.addProperty("description", localTodo.getDescription());
			requestBody.addProperty("location", localTodo.getPlace());
			requestBody.add("start", GoogleCalendarUtilities.createDateObject(SyncManager.getInstance().getRemoteTodoStartEndDate()));
			requestBody.add("end", GoogleCalendarUtilities.createDateObject(SyncManager.getInstance().getRemoteTodoStartEndDate()));
			requestBody.add("extendedProperties", GoogleCalendarUtilities.createExtendedPropertiesObject(localTodo.getUniqueID()));
			requestBody.add("recurrence", SyncManager.getInstance().getRemoteTodoRecurrenceProperty());
		}
		else {
			assert false;
		}

		return requestBody;
	}

	private boolean remoteCalendarExists()
			throws IOException, JsonParseException, IllegalStateException {
		logger.fine("remoteCalendarExists() called.");

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
		logger.fine("createRemoteCalendar() called.");

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

	private void syncAllTasks(ArrayList<Task> localTasks, String pageToken)
			throws IOException, JsonParseException, IllegalStateException {
		logger.fine("syncAllTasks(ArrayList<Task> localTasks, String pageToken) called.");

		//TODO perhaps make this iterative

		logger.info("Getting remote task list with pageToken: " + pageToken);
		JsonObject serverReply = getRemoteTaskList(pageToken);

		logger.info("Iterating through the remote task list (for current page).");
		JsonArray remoteTasksList = serverReply.getAsJsonArray("items");
		Iterator<JsonElement> remoteTasksListIterator = remoteTasksList.iterator();
		while(remoteTasksListIterator.hasNext()) {
			logger.info("Examining next remote task.");
			JsonObject remoteTask = remoteTasksListIterator.next().getAsJsonObject();

			String extendedPropertiesLocalTaskID = remoteTask
					.getAsJsonObject("extendedProperties")
					.getAsJsonObject("private")
					.get("atc_localTaskID")
					.getAsString();
			logger.info("Checking if remote task was not created by ATC (i.e. by user, or other programs)");
			if(extendedPropertiesLocalTaskID == null) {
				logger.info("Remote task was not created by ATC. Inserting into local TasksManger.");
				//TODO insert into tasksManager
			}
			else {
				logger.info("Remote task was created by ATC. Searching for corresponding local task.");
				Task currentLocalTask = searchForCorrespondingLocalTask(localTasks, extendedPropertiesLocalTaskID);
				if(currentLocalTask != null) {
					logger.info("Corresponding local task found, checking for discrepancies between local and remote copies.");
					if(!isIdentical(remoteTask, currentLocalTask)) {
						logger.info("Discrepancies between local and remote copies found. Enqueuing to update remote copy to match local one.");
						GoogleCalendarManager.getInstance().updateRemoteTask(currentLocalTask, remoteTask.get("id").getAsString());
					}
					else {
						logger.info("No discrepancies between local and remote copies found. Nothing to do as remote copy is sync'ed.");
					}

					logger.info("Removing corresponding local task from the local tasks list.");
					localTasks.remove(currentLocalTask);
					break;
				}
				else {
					logger.info("Corresponding local task not found, enqueuing to delete remote copy.");
					GoogleCalendarManager.getInstance().deleteRemoteTask(remoteTask.get("id").getAsString());
				}
			}
		}
		logger.info("Completed iterating through remote task list (for current page).");

		if(serverReply.get("nextPageToken") != null) {
			logger.info("Next page for remote task list is avaliable, continuing on to next page.");
			syncAllTasks(localTasks, serverReply.get("nextPageToken").getAsString());
		}
		else {
			logger.info("Already on last page for remote task list. Enqueuing all remaining local tasks (count = " + localTasks.size() + ") for uploading to Google Calendar.");
			Iterator<Task> localTasksIterator = localTasks.iterator();
			while(localTasksIterator.hasNext()) {
				GoogleCalendarManager.getInstance().addRemoteTask(localTasksIterator.next());
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

		JsonObject serverReply = GoogleCalendarUtilities.parseToJsonObject(
				HttpsConnectionHelper.sendUrlencodedFormRequest(
						"https://www.googleapis.com/calendar/v3/calendars/" +
								SyncManager.getInstance().getRemoteCalendarId() +
								"/events",
						HttpsConnectionHelper.REQUEST_METHOD_GET,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						formParameters)
				);
		return serverReply;
	}

	private Task searchForCorrespondingLocalTask(ArrayList<Task> localTasks, String extendedPropertiesLocalTaskID) {
		logger.fine("searchForCorrespondingLocalTask(ArrayList<Task> localTasks, String extendedPropertiesLocalTaskID) called.");

		int extendedPropertiesLocalTaskIDAsInt = Integer.parseInt(extendedPropertiesLocalTaskID);

		Iterator<Task> localTasksIterator = localTasks.iterator();
		while(localTasksIterator.hasNext()) {
			Task currentLocalTask = localTasksIterator.next();

			if(extendedPropertiesLocalTaskIDAsInt == currentLocalTask.getUniqueID()) {
				return currentLocalTask;
			}
		}
		return null;
	}

	private boolean isIdentical(JsonObject remoteTask, Task localTask) {
		logger.fine("isIdentical(JsonObject remoteTask, Task localTask) called.");

		if(localTask instanceof Schedule) {
			Schedule localSchedule = (Schedule)localTask;

			final boolean summaryIsIdentical = remoteTask.get("summary").getAsString().equals(localSchedule.getDescription());
			final boolean descriptionIsIdentical = remoteTask.get("description").getAsString().equals(localSchedule.getDescription());

			final boolean locationIsIdentical;
			if(remoteTask.get("location") != null) {
				locationIsIdentical = remoteTask.get("location").getAsString().equals(localSchedule.getPlace());
			}
			else {
				locationIsIdentical = localSchedule.getPlace().equals("");
			}

			final boolean startTimeIsIdentical = remoteTask.getAsJsonObject("start").equals(GoogleCalendarUtilities.createDateTimeObject(localSchedule.getStartTime()));
			final boolean endTimeIsIdentical = remoteTask.getAsJsonObject("end").equals(GoogleCalendarUtilities.createDateTimeObject(localSchedule.getEndTime()));

			return summaryIsIdentical &&
					descriptionIsIdentical &&
					locationIsIdentical &&
					startTimeIsIdentical &&
					endTimeIsIdentical;
		}
		else if(localTask instanceof Deadline) {
			//TODO handle done/undone
			Deadline localDeadline = (Deadline)localTask;

			final boolean summaryIsIdentical = remoteTask.get("summary").getAsString().equals("Deadline: " + localDeadline.getDescription());
			final boolean descriptionIsIdentical = remoteTask.get("description").getAsString().equals(localDeadline.getDescription());

			final boolean locationIsIdentical;
			if(remoteTask.get("location") != null) {
				locationIsIdentical = remoteTask.get("location").getAsString().equals(localDeadline.getPlace());
			}
			else {
				locationIsIdentical = localDeadline.getPlace().equals("");
			}

			final boolean startTimeIsIdentical = remoteTask.getAsJsonObject("start").equals(GoogleCalendarUtilities.createDateTimeObject(localDeadline.getEndTime()));
			final boolean endTimeIsIdentical = remoteTask.getAsJsonObject("end").equals(GoogleCalendarUtilities.createDateTimeObject(localDeadline.getEndTime()));

			return summaryIsIdentical &&
					descriptionIsIdentical &&
					locationIsIdentical &&
					startTimeIsIdentical &&
					endTimeIsIdentical;
		}
		else if(localTask instanceof Todo) {
			//TODO handle done/undone
			Todo localTodo = (Todo)localTask;

			final boolean summaryIsIdentical = remoteTask.get("summary").getAsString().equals("Todo: " + localTodo.getDescription());
			final boolean descriptionIsIdentical = remoteTask.get("description").getAsString().equals(localTodo.getDescription());

			final boolean locationIsIdentical;
			if(remoteTask.get("location") != null) {
				locationIsIdentical = remoteTask.get("location").getAsString().equals(localTodo.getPlace());
			}
			else {
				locationIsIdentical = localTodo.getPlace().equals("");
			}

			final boolean startTimeIsIdentical = remoteTask.getAsJsonObject("start").equals(GoogleCalendarUtilities.createDateObject(SyncManager.getInstance().getRemoteTodoStartEndDate()));
			final boolean endTimeIsIdentical = remoteTask.getAsJsonObject("end").equals(GoogleCalendarUtilities.createDateObject(SyncManager.getInstance().getRemoteTodoStartEndDate()));
			final boolean recurrenceIsIdentical = remoteTask.get("recurrence").getAsJsonArray().equals(SyncManager.getInstance().getRemoteTodoRecurrenceProperty());

			return summaryIsIdentical &&
					descriptionIsIdentical &&
					locationIsIdentical &&
					startTimeIsIdentical &&
					endTimeIsIdentical &&
					recurrenceIsIdentical;
		}
		else {
			assert false;
			return false;
		}
	}
}
