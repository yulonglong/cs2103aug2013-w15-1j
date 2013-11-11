package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.licensetokil.atypistcalendar.tasksmanager.Deadline;
import com.licensetokil.atypistcalendar.tasksmanager.Schedule;
import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.Todo;

class Syncer extends Thread {
	private static final String TASK_DESCRIPTION_PREFIX_TODO = "Todo: ";
	private static final String TASK_DESCRIPTION_PREFIX_DEADLINE = "DL: ";

	private static final String CALENDARS_LIST_RESOURCE_LABEL_ITEMS_ARRAY = "items";

	private static final String CALENDAR_RESOURCE_LABEL_SUMMARY = "summary";
	private static final String CALENDAR_RESOURCE_LABEL_TIMEZONE = "timeZone";
	private static final String CALENDAR_RESOURCE_LABEL_LOCATION = "location";
	private static final String CALENDAR_RESOURCE_LABEL_ID = "id";

	private static final String EVENTS_LIST_RESOURCE_PAGE_TOKEN = "pageToken";
	private static final String EVENTS_LIST_RESOURCE_LABEL_ITEMS_ARRAY = "items";

	private static final String EVENT_RESOURCE_LABEL_LOCATION = "location";
	private static final String EVENT_RESOURCE_LABEL_SUMMARY = "summary";
	private static final String EVENT_RESOURCE_LABEL_END = "end";
	private static final String EVENT_RESOURCE_LABEL_START = "start";
	private static final String EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES = "extendedProperties";
	private static final String EVENT_RESOURCE_LABEL_RECURRENCE = "recurrence";
	private static final String EVENT_RESOURCE_LABEL_ID = "id";
	private static final String EVENT_RESOURCE_LABEL_UPDATED = "updated";

	private static final String EVENT_EXTENDED_PROPERTIES_RESOURCE_LABEL_PRIVATE = "private";

	private static final String EVENT_EXTENDED_PROPERTIES_PRIVATE_RESOURCE_LABEL_ATC_LOCAL_TASK_ID = "atc_localTaskId";

	private static final String GOOGLE_REQUEST_URL_LIST_CALENDARS = "https://www.googleapis.com/calendar/v3/users/me/calendarList";
	private static final String GOOGLE_REQUEST_URL_ADD_CALENDAR = "https://www.googleapis.com/calendar/v3/calendars";
	private static final String GOOGLE_REQUEST_URL_LIST_EVENTS = "https://www.googleapis.com/calendar/v3/calendars/%s/events";
	private static final String GOOGLE_REQUEST_URL_ADD_EVENT = "https://www.googleapis.com/calendar/v3/calendars/%s/events";
	private static final String GOOGLE_REQUEST_URL_UPDATE_EVENT = "https://www.googleapis.com/calendar/v3/calendars/%s/events/%s";
	private static final String GOOGLE_REQUEST_URL_DELETE_EVENT = "https://www.googleapis.com/calendar/v3/calendars/%s/events/%s";

	private static final int PERIOD_TO_SLEEP_IN_SECONDS = 15000;

	private static final String EMPTY_PAGE_TOKEN = "";
	private static final Task NULL_CORRESPONDING_LOCAL_TASK = null;
	private static final int NULL_CORRESPONDING_LOCAL_TASK_ID = -1;

	private static Logger logger = Logger.getLogger(Syncer.class.getName());

	public Syncer() {
		setPriority(Thread.MIN_PRIORITY);
	}

	@Override
	public void run() {
		logger.info("Syncer is running.");

		GoogleCalendarManager.getInstance().setSyncerStatus(GoogleCalendarManager.SYNCER_STATUS_EXECUTING);

		while (true) {
			SyncNode currentSyncNode = SyncManager.getInstance().getSyncQueue().poll();

			while (currentSyncNode != null) {
				try {
					logger.info("Working on next SyncNode. Figuring out the sub-type.");
					determineSyncNodeSubtypeAndExecute(currentSyncNode);

					logger.info("Dequeuing SyncNode item that has been completed (removing head of queue).");
					currentSyncNode = SyncManager.getInstance().getSyncQueue().poll();
				} catch (IllegalStateException | JsonParseException | IOException e) {
					logger.warning("Exception thrown: " + e.getMessage());
					logger.warning("Sleeping before retrying...");

					sleep();

					logger.info("Thread woke up. Retrying...");
				}
			}

			logger.info("Queue is empty, sleeping before next sync...");
			sleep();
			logger.info("Thread woke up. Checking if queue is empty.");
			if (SyncManager.getInstance().getSyncQueue().isEmpty()) {
				logger.info("Queue is empty. Doing a complete sync.");
				SyncManager.getInstance().doCompleteSync();
			} else {
				logger.info("Queue is not empty. Continuing with enqueued SyncNode.");
			}
		}
	}

	private void determineSyncNodeSubtypeAndExecute(SyncNode currentSyncNode)
			throws IllegalStateException, JsonParseException, IOException {
		if (currentSyncNode instanceof InitialiseRemoteCalendarSyncNode) {
			logger.info("InitialiseRemoteCalendarSyncNode sub-type detected.");
			executeSyncNode((InitialiseRemoteCalendarSyncNode) currentSyncNode);
		} else if (currentSyncNode instanceof AddSyncNode) {
			logger.info("AddSyncNode sub-type detected.");
			executeSyncNode((AddSyncNode) currentSyncNode);
		} else if (currentSyncNode instanceof UpdateSyncNode) {
			logger.info("UpdateSyncNode sub-type detected.");
			executeSyncNode((UpdateSyncNode) currentSyncNode);
		} else if (currentSyncNode instanceof DeleteSyncNode) {
			logger.info("DeleteSyncNode sub-type detected.");
			executeSyncNode((DeleteSyncNode) currentSyncNode);
		} else if (currentSyncNode instanceof DoCompleteSyncNode) {
			logger.info("DoCompleteSyncNode sub-type detected.");

			logger.info("Checking if following SyncNodes in queue are DoCompleteSyncNode. Dropping them if they are, for efficency.");
			SyncNode nextSyncNode = SyncManager.getInstance().getSyncQueue().peek();
			while (nextSyncNode instanceof DoCompleteSyncNode) {
				logger.info("Dropping next DoCompleteSyncNode");
				SyncManager.getInstance().getSyncQueue().poll();
				nextSyncNode = SyncManager.getInstance().getSyncQueue().peek();
			}
			logger.info("Check done.");

			executeSyncNode((DoCompleteSyncNode) currentSyncNode);
		} else {
			logger.severe("Unexcepted (subclass of) SyncNode enqueued.");
			assert false;
		}
	}

	private void executeSyncNode(InitialiseRemoteCalendarSyncNode currentSyncNode)
			throws JsonParseException, IllegalStateException, IOException {

		logger.info("Checking if remote calendar exists.");
		if (!remoteCalendarExists()) {
			logger.info("Remote calendar does not exist, creating remote calendar.");
			createRemoteCalendar();
		} else {
			logger.info("Remote calendar exists. Nothing to do.");
		}
	}

	private void executeSyncNode(AddSyncNode addSyncNode)
			throws JsonParseException, IllegalStateException, IOException {
		logger.info("Adding: " + addSyncNode.getLocalTask().toString());

		JsonObject requestBody = createRemoteTaskRequestBody(addSyncNode.getLocalTask());

		JsonObject serverReply = Utilities.parseToJsonObject(
				Utilities.sendJsonHttpsRequest(
						String.format(GOOGLE_REQUEST_URL_ADD_EVENT, SyncManager.getInstance().getRemoteCalendarId()),
						Utilities.REQUEST_METHOD_POST,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						requestBody
				)
		);

		GoogleCalendarManager.getInstance().updateLocalTaskWithCorrespondingTaskRemoteId(
				addSyncNode.getLocalTask().getUniqueId(),
				Utilities.getJsonObjectValueOrEmptyString(serverReply, EVENT_RESOURCE_LABEL_ID)
		);
	}

	private void executeSyncNode(UpdateSyncNode updateSyncNode)
			throws JsonParseException, IllegalStateException, IOException {
		logger.info("Updating remote copy: " + updateSyncNode.getRemoteTaskID() + " to match " + updateSyncNode.getLocalTask().toString());

		JsonObject requestBody = createRemoteTaskRequestBody(updateSyncNode.getLocalTask());

		Utilities.sendJsonHttpsRequest(
				String.format(
						GOOGLE_REQUEST_URL_UPDATE_EVENT,
						SyncManager.getInstance().getRemoteCalendarId(),
						updateSyncNode.getRemoteTaskID()
				),
				Utilities.REQUEST_METHOD_PUT,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				requestBody
		);
	}

	private void executeSyncNode(DeleteSyncNode deleteSyncNode)
			throws JsonParseException, IllegalStateException, IOException {
		logger.info("Deleting: " + deleteSyncNode.getRemoteTaskID());

		Utilities.sendJsonHttpsRequest(
				String.format(
						GOOGLE_REQUEST_URL_DELETE_EVENT,
						SyncManager.getInstance().getRemoteCalendarId(),
						deleteSyncNode.getRemoteTaskID()
				),
				Utilities.REQUEST_METHOD_DELETE,
				AuthenticationManager.getInstance().getAuthorizationHeader(),
				Utilities.EMPTY_REQUEST_BODY
		);
	}

	private void executeSyncNode(DoCompleteSyncNode currentSyncNode)
			throws JsonParseException, IllegalStateException, IOException {
		logger.fine("executeSyncNode(DoCompleteSyncNode currentSyncNode) called.");

		logger.info("Getting a copy of all local tasks from TasksManager. Tasks will be deleted from this local copy as its corresponding remote task is found. All remaining local tasks without the corresponding remote task will be uploaded accordingly.");
		syncAllTasks(GoogleCalendarManager.getInstance().getCopyOfAllLocalTasks(), EMPTY_PAGE_TOKEN);
	}

	private boolean remoteCalendarExists()
			throws IOException, JsonParseException, IllegalStateException {

		JsonObject serverReply = Utilities.parseToJsonObject(
				Utilities.sendJsonHttpsRequest(
						GOOGLE_REQUEST_URL_LIST_CALENDARS,
						Utilities.REQUEST_METHOD_GET,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						Utilities.EMPTY_REQUEST_BODY
				)
		);

		JsonArray calendarList = serverReply.getAsJsonArray(CALENDARS_LIST_RESOURCE_LABEL_ITEMS_ARRAY);
		for(JsonElement i : calendarList) {
			JsonObject currentCalendar = (JsonObject) i;

			boolean currentCalendarNameEqualsATypistsCalendar = Utilities
					.getJsonObjectValueOrEmptyString(currentCalendar, CALENDAR_RESOURCE_LABEL_SUMMARY)
					.equals("A Typist's Calendar");
			if (currentCalendarNameEqualsATypistsCalendar) {
				SyncManager.getInstance().setRemoteCalendarId(
						Utilities.getJsonObjectValueOrEmptyString(currentCalendar, CALENDAR_RESOURCE_LABEL_ID)
				);
				return true;
			}
		}

		return false;
	}

	private void createRemoteCalendar()
			throws IOException, JsonParseException, IllegalStateException {

		// Assumes remoteCalendarExists() == true.
		// We don't assert because it's quite an expensive operation
		// (needs to connect to Google and wait for reply).

		JsonObject requestBody = new JsonObject();
		requestBody.addProperty(CALENDAR_RESOURCE_LABEL_SUMMARY, "A Typist's Calendar");
		requestBody.addProperty(CALENDAR_RESOURCE_LABEL_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
		requestBody.addProperty(CALENDAR_RESOURCE_LABEL_LOCATION, Calendar.getInstance().getTimeZone().getID());

		JsonObject serverReply = Utilities.parseToJsonObject(
				Utilities.sendJsonHttpsRequest(
						GOOGLE_REQUEST_URL_ADD_CALENDAR,
						Utilities.REQUEST_METHOD_POST,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						requestBody
				)
		);

		SyncManager.getInstance().setRemoteCalendarId(
				Utilities.getJsonObjectValueOrEmptyString(serverReply, CALENDAR_RESOURCE_LABEL_ID)
		);
	}

	private void syncAllTasks(ArrayList<Task> localTasks, String pageToken)
			throws IOException, JsonParseException, IllegalStateException {
		logger.info("Getting remote task list with pageToken: " + pageToken);
		JsonObject serverReply = getRemoteTaskList(pageToken);

		logger.info("Iterating through the remote task list (for current page).");
		JsonArray remoteTasksList = serverReply.getAsJsonArray(EVENTS_LIST_RESOURCE_LABEL_ITEMS_ARRAY);
		for (JsonElement i : remoteTasksList) {
			logger.info("Examining next remote task.");
			examineRemoteTask(localTasks, i.getAsJsonObject());
		}
		logger.info("Completed iterating through remote task list (for current page).");

		boolean nextPageTokenExists = Utilities.getJsonObjectValueOrEmptyString(serverReply, "nextPageToken") != EMPTY_PAGE_TOKEN;
		if (nextPageTokenExists) {

			logger.info("Next page for remote task list is avaliable, continuing on to next page.");
			syncAllTasks(localTasks, Utilities.getJsonObjectValueOrEmptyString(serverReply, "nextPageToken"));

		} else {

			logger.info("Already on last page for remote task list. Enqueuing all remaining local tasks (count = " + localTasks.size() + ") for either uploading to Google Calendar or deletion from local list.");
			for (Task currentLocalTask : localTasks) {

				boolean currentLocalTaskHasNoRemoteId = currentLocalTask.getRemoteId() == null;
				if (currentLocalTaskHasNoRemoteId) {
					logger.info("Adding local task, uniqueId = " + currentLocalTask.getUniqueId());
					SyncManager.getInstance().addRemoteTask(currentLocalTask);
				} else {
					logger.info("Deleting local task, uniqueId = " + currentLocalTask.getUniqueId());
					GoogleCalendarManager.getInstance().deleteLocalTaskfromTasksManager(currentLocalTask.getUniqueId());
				}

			}

		}
	}

	private void examineRemoteTask(ArrayList<Task> localTasks, JsonObject remoteTask) {

		int correspondingLocalTaskId = getCorrespondingLocalTaskId(remoteTask);

		logger.info("Checking if remote task was not created by ATC (i.e. by user, or other programs)");
		if (correspondingLocalTaskId == NULL_CORRESPONDING_LOCAL_TASK_ID) {

			logger.info("Remote task was not created by ATC. Inserting into local TasksManger.");
			SyncManager.getInstance().insertRemoteTaskIntoTasksManager(remoteTask);

		} else {

			logger.info("Remote task was created by ATC. Searching for corresponding local task.");
			Task currentLocalTask = searchForCorrespondingLocalTask(localTasks, correspondingLocalTaskId);
			if (currentLocalTask == NULL_CORRESPONDING_LOCAL_TASK) {

				logger.info("Corresponding local task not found, enqueuing to delete remote copy.");
				SyncManager.getInstance().deleteRemoteTask(
						Utilities.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_ID)
				);

			} else {

				logger.info("Corresponding local task found, checking for discrepancies between local and remote copies.");
				if (isIdentical(remoteTask, currentLocalTask)) {
					logger.info("No discrepancies between local and remote copies found. Nothing to do as remote copy is sync'ed.");
				} else {
					logger.info("Discrepancies between local and remote copies found. Synchornising the two copies.");
					synchorniseLocalAndRemoteTask(remoteTask, currentLocalTask);
				}

				logger.info("Removing corresponding local task from the local tasks list.");
				localTasks.remove(currentLocalTask);

			}

		}
	}

	private Task searchForCorrespondingLocalTask(ArrayList<Task> localTasks, int extendedPropertiesLocalTaskID) {

		for (Task currentLocalTask : localTasks) {
			if (extendedPropertiesLocalTaskID == currentLocalTask.getUniqueId()) {
				return currentLocalTask;
			}
		}
		return NULL_CORRESPONDING_LOCAL_TASK;
	}

	private void synchorniseLocalAndRemoteTask(JsonObject remoteTask, Task currentLocalTask) {
		Calendar remoteTaskLastModifiedTime = null;
		try {
			remoteTaskLastModifiedTime = Utilities.parseGenericGoogleDateString(
					Utilities.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_UPDATED),
					Utilities.RFC3339_FORMAT_WITH_MILLISECONDS
			);
		} catch (ParseException e) {
			logger.severe("Unable to parse Google DateTime object. Unexpected as Google returns a standard format. Failing quietly.");
			e.printStackTrace();
			return;
		}

		Calendar localTaskLastModifiedTime = currentLocalTask.getLastModifiedDate();

		boolean localTaskModifiedLaterThanRemoteTask = localTaskLastModifiedTime.compareTo(remoteTaskLastModifiedTime) >= 0;
		if (localTaskModifiedLaterThanRemoteTask) {

			SyncManager.getInstance().updateRemoteTask(
					currentLocalTask,
					Utilities.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_ID)
			);

		} else {
			updateLocalTaskFromRemoteTask(remoteTask, currentLocalTask);
		}
	}

	private JsonObject getRemoteTaskList(String pageToken)
			throws IOException, JsonParseException, IllegalStateException {

		HashMap<String, String> formParameters = Utilities.EMPTY_FORM_PARAMETERS;

		if (pageToken != EMPTY_PAGE_TOKEN) {
			formParameters = new HashMap<String, String>();
			formParameters.put(EVENTS_LIST_RESOURCE_PAGE_TOKEN, pageToken);
		}

		JsonObject serverReply = Utilities.parseToJsonObject(
				Utilities.sendUrlencodedFormHttpsRequest(
						String.format(
								GOOGLE_REQUEST_URL_LIST_EVENTS,
								SyncManager.getInstance().getRemoteCalendarId()
						),
						Utilities.REQUEST_METHOD_GET,
						AuthenticationManager.getInstance().getAuthorizationHeader(),
						formParameters
				)
		);

		return serverReply;
	}

	private int getCorrespondingLocalTaskId(JsonObject remoteTask) {
		if (privateExtendedPropertiesExist(remoteTask)) {
			return remoteTask
					.getAsJsonObject(EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES)
					.getAsJsonObject(EVENT_EXTENDED_PROPERTIES_RESOURCE_LABEL_PRIVATE)
					.get(EVENT_EXTENDED_PROPERTIES_PRIVATE_RESOURCE_LABEL_ATC_LOCAL_TASK_ID)
					.getAsInt();
		}
		return NULL_CORRESPONDING_LOCAL_TASK_ID;
	}

	private boolean privateExtendedPropertiesExist(JsonObject remoteTask) {
		if (remoteTask.getAsJsonObject(EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES) == null) {
			return false;
		}
		if (remoteTask.getAsJsonObject(EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES)
				.getAsJsonObject(EVENT_EXTENDED_PROPERTIES_RESOURCE_LABEL_PRIVATE) == null) {
			return false;
		}
		if (remoteTask.getAsJsonObject(EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES)
				.getAsJsonObject(EVENT_EXTENDED_PROPERTIES_RESOURCE_LABEL_PRIVATE)
				.get(EVENT_EXTENDED_PROPERTIES_PRIVATE_RESOURCE_LABEL_ATC_LOCAL_TASK_ID) == null) {
			return false;
		}
		return true;
	}

	private String removePrefix(String originalString, String prefix) {
		String originalStringInLowerCase = originalString.toLowerCase();
		String prefixInLowerCase = prefix.toLowerCase();

		if (originalStringInLowerCase.startsWith(prefixInLowerCase)) {
			return originalString.substring(prefix.length());
		} else {
			return originalString;
		}
	}

	private JsonObject createExtendedPropertiesObject(int localTaskId) {
		JsonObject privateExtendedProperties = new JsonObject();
		privateExtendedProperties.addProperty(
				EVENT_EXTENDED_PROPERTIES_PRIVATE_RESOURCE_LABEL_ATC_LOCAL_TASK_ID,
				Integer.toString(localTaskId)
		);

		JsonObject extendedProperties = new JsonObject();
		extendedProperties.add(
				EVENT_EXTENDED_PROPERTIES_RESOURCE_LABEL_PRIVATE,
				privateExtendedProperties
		);

		return extendedProperties;
	}

	private void sleep() {
		logger.info("Sleeping for " + PERIOD_TO_SLEEP_IN_SECONDS + " seconds.");

		GoogleCalendarManager.getInstance().setSyncerStatus(GoogleCalendarManager.SYNCER_STATUS_SLEEPING);

		try {
			SyncManager.getInstance().getGoToSleepLock().lock();
			SyncManager.getInstance().getGoToSleepCondition()
					.await(PERIOD_TO_SLEEP_IN_SECONDS, TimeUnit.SECONDS);
			SyncManager.getInstance().getGoToSleepLock().unlock();
		} catch (InterruptedException e) {
			logger.severe("Thread was interrupted.");
			e.printStackTrace();
		}

		GoogleCalendarManager.getInstance().setSyncerStatus(GoogleCalendarManager.SYNCER_STATUS_EXECUTING);
	}

	private JsonObject createRemoteTaskRequestBody(Task localTask) {
		if (localTask instanceof Schedule) {
			return createRemoteTaskRequestBodyFromSchedule((Schedule)localTask);
		} else if (localTask instanceof Deadline) {
			return createRemoteTaskRequestBodyFromDeadline((Deadline)localTask);
		} else if (localTask instanceof Todo) {
			return createRemoteTaskRequestBodyFromTodo((Todo)localTask);
		} else {
			logger.severe("Unexpected sub-type of Task detected.");
			assert false;
			return null;
		}
	}

	private JsonObject createRemoteTaskRequestBodyFromSchedule(Schedule localSchedule) {
		JsonObject requestBody = new JsonObject();

		requestBody.addProperty(EVENT_RESOURCE_LABEL_LOCATION, localSchedule.getPlace());
		requestBody.addProperty(EVENT_RESOURCE_LABEL_SUMMARY, localSchedule.getDescription());
		requestBody.add(EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES, createExtendedPropertiesObject(localSchedule.getUniqueId()));
		requestBody.add(EVENT_RESOURCE_LABEL_START, Utilities.createGoogleDateTimeObject(localSchedule.getStartTime()));
		requestBody.add(EVENT_RESOURCE_LABEL_END, Utilities.createGoogleDateTimeObject(localSchedule.getEndTime()));

		return requestBody;
	}

	private JsonObject createRemoteTaskRequestBodyFromDeadline(Deadline localDeadline) {
		// TODO handle done/undone
		JsonObject requestBody = new JsonObject();

		requestBody.addProperty(EVENT_RESOURCE_LABEL_LOCATION, localDeadline.getPlace());
		requestBody.addProperty(EVENT_RESOURCE_LABEL_SUMMARY, TASK_DESCRIPTION_PREFIX_DEADLINE + localDeadline.getDescription());
		requestBody.add(EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES, createExtendedPropertiesObject(localDeadline.getUniqueId()));
		requestBody.add(EVENT_RESOURCE_LABEL_START, Utilities.createGoogleDateTimeObject(localDeadline.getEndTime()));
		requestBody.add(EVENT_RESOURCE_LABEL_END, Utilities.createGoogleDateTimeObject(localDeadline.getEndTime()));

		return requestBody;
	}

	private JsonObject createRemoteTaskRequestBodyFromTodo(Todo localTodo) {
		// TODO handle done/undone
		JsonObject requestBody = new JsonObject();

		requestBody.addProperty(EVENT_RESOURCE_LABEL_LOCATION, localTodo.getPlace());
		requestBody.addProperty(EVENT_RESOURCE_LABEL_SUMMARY, TASK_DESCRIPTION_PREFIX_TODO + localTodo.getDescription());
		requestBody.add(EVENT_RESOURCE_LABEL_EXTENDED_PROPERTIES, createExtendedPropertiesObject(localTodo.getUniqueId()));
		requestBody.add(EVENT_RESOURCE_LABEL_START, Utilities.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));
		requestBody.add(EVENT_RESOURCE_LABEL_END, Utilities.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));
		requestBody.add(EVENT_RESOURCE_LABEL_RECURRENCE, SyncManager.REMOTE_TODO_RECURRENCE_PROPERTY);

		return requestBody;
	}

	private void updateLocalTaskFromRemoteTask(JsonObject remoteTask, Task currentLocalTask) {
		// Extracting the (common) fields from the RemoteTask object.
		String remoteTaskId = Utilities.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_ID);
		String description = Utilities.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_SUMMARY);
		String location = Utilities.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_LOCATION);
		Calendar lastModifiedDate = SyncManager.getLastModifiedDateOrTimeNow(remoteTask);

		if (currentLocalTask instanceof Todo) {

			logger.info("currentLocalTask is of type Todo.");
			updateLocalTodo((Todo) currentLocalTask, description, location, remoteTaskId, lastModifiedDate);

		} else {

			// Extract the start and end time of the RemoteTask object.
			Calendar startTime = null;
			Calendar endTime = null;
			try {
				startTime = Utilities.parseGoogleDateTimeObject(remoteTask.getAsJsonObject(EVENT_RESOURCE_LABEL_START));
				endTime = Utilities.parseGoogleDateTimeObject(remoteTask.getAsJsonObject(EVENT_RESOURCE_LABEL_END));
			} catch (ParseException e) {
				logger.severe("Unable to parse Google DateTime object (this is unexpected as Google only returns a standardised format). Failing quietly.");
				e.printStackTrace();
				assert false;
				return;
			}

			if (currentLocalTask instanceof Deadline) {
				logger.info("currentLocalTask is of type Deadline.");
				updateLocalDeadline((Deadline) currentLocalTask, remoteTaskId, description, location, lastModifiedDate, endTime);
			} else if (currentLocalTask instanceof Schedule) {
				logger.info("currentLocalTask is of type Schedule.");
				updateLocalSchedule((Schedule) currentLocalTask, remoteTaskId, description, location, lastModifiedDate, startTime, endTime);
			} else {
				logger.severe("Unknown Task sub-type detected.");
				assert false;
			}

		}
	}

	private void updateLocalTodo(Todo localTodo, String description,
			String location, String remoteTaskId, Calendar lastModifiedDate) {
		localTodo.setDescription(removePrefix(description, TASK_DESCRIPTION_PREFIX_TODO));
		localTodo.setPlace(location);
		localTodo.setLastModifiedDate(lastModifiedDate);
		localTodo.setRemoteId(remoteTaskId);

		GoogleCalendarManager.getInstance().updateTasksManagerWithUpdatedLocalTask(localTodo);
	}

	private void updateLocalDeadline(Deadline localDeadline,
			String remoteTaskId, String description, String location,
			Calendar lastModifiedDate, Calendar endTime) {
		localDeadline.setDescription(removePrefix(description, TASK_DESCRIPTION_PREFIX_DEADLINE));
		localDeadline.setPlace(location);
		localDeadline.setLastModifiedDate(lastModifiedDate);
		localDeadline.setEndTime(endTime);
		localDeadline.setRemoteId(remoteTaskId);

		GoogleCalendarManager.getInstance().updateTasksManagerWithUpdatedLocalTask(localDeadline);
	}

	private void updateLocalSchedule(Schedule localSchedule,
			String remoteTaskId, String description, String location,
			Calendar lastModifiedDate, Calendar startTime, Calendar endTime) {
		localSchedule.setDescription(description);
		localSchedule.setPlace(location);
		localSchedule.setLastModifiedDate(lastModifiedDate);
		localSchedule.setStartTime(startTime);
		localSchedule.setEndTime(endTime);
		localSchedule.setRemoteId(remoteTaskId);

		GoogleCalendarManager.getInstance().updateTasksManagerWithUpdatedLocalTask(localSchedule);
	}

	private boolean isIdentical(JsonObject remoteTask, Task localTask) {
		if (localTask instanceof Schedule) {
			return isIdenticalSchedule(remoteTask, (Schedule) localTask);
		} else if (localTask instanceof Deadline) {
			return isIdenticalDeadline(remoteTask, (Deadline) localTask);
		} else if (localTask instanceof Todo) {
			return isIdenticalTodo(remoteTask, (Todo) localTask);
		} else {
			logger.severe("Unknown Task sub-type detected.");
			assert false;
			return false;
		}
	}

	private boolean isIdenticalSchedule(JsonObject remoteTask, Schedule localSchedule) {
		boolean locationIsIdentical = Utilities
				.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_LOCATION)
				.equals(localSchedule.getPlace());
		boolean summaryIsIdentical = Utilities
				.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_SUMMARY)
				.equals(localSchedule.getDescription());
		boolean startTimeIsIdentical = remoteTask
				.getAsJsonObject(EVENT_RESOURCE_LABEL_START)
				.equals(Utilities.createGoogleDateTimeObject(localSchedule.getStartTime()));
		boolean endTimeIsIdentical = remoteTask
				.getAsJsonObject(EVENT_RESOURCE_LABEL_END)
				.equals(Utilities.createGoogleDateTimeObject(localSchedule.getEndTime()));

		return locationIsIdentical &&
				summaryIsIdentical &&
				startTimeIsIdentical &&
				endTimeIsIdentical;
	}

	private boolean isIdenticalDeadline(JsonObject remoteTask, Deadline localDeadline) {
		// TODO handle done/undone
		boolean locationIsIdentical = Utilities
				.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_LOCATION)
				.equals(localDeadline.getPlace());
		boolean summaryIsIdentical = Utilities
				.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_SUMMARY)
				.equals(TASK_DESCRIPTION_PREFIX_DEADLINE + localDeadline.getDescription());
		boolean startTimeIsIdentical = remoteTask
				.getAsJsonObject(EVENT_RESOURCE_LABEL_START)
				.equals(Utilities.createGoogleDateTimeObject(localDeadline.getEndTime()));
		boolean endTimeIsIdentical = remoteTask
				.getAsJsonObject(EVENT_RESOURCE_LABEL_END)
				.equals(Utilities.createGoogleDateTimeObject(localDeadline.getEndTime()));

		return locationIsIdentical &&
				summaryIsIdentical &&
				startTimeIsIdentical &&
				endTimeIsIdentical;
	}

	private boolean isIdenticalTodo(JsonObject remoteTask, Todo localTodo) {
		// TODO handle done/undone
		boolean locationIsIdentical = Utilities
				.getJsonObjectValueOrEmptyString(remoteTask, CALENDAR_RESOURCE_LABEL_LOCATION)
				.equals(localTodo.getPlace());
		boolean summaryIsIdentical = Utilities
				.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_SUMMARY)
				.equals(TASK_DESCRIPTION_PREFIX_TODO + localTodo.getDescription());
		boolean startTimeIsIdentical = remoteTask
				.getAsJsonObject(EVENT_RESOURCE_LABEL_START)
				.equals(Utilities.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));
		boolean endTimeIsIdentical = remoteTask
				.getAsJsonObject(EVENT_RESOURCE_LABEL_END)
				.equals(Utilities.createGoogleDateObject(SyncManager.REMOTE_TODO_START_END_DATE));

		boolean recurrenceIsIdentical = false;
		if (remoteTask.get(EVENT_RESOURCE_LABEL_RECURRENCE) != null) {
			recurrenceIsIdentical = remoteTask
					.getAsJsonArray(EVENT_RESOURCE_LABEL_RECURRENCE)
					.equals(SyncManager.REMOTE_TODO_RECURRENCE_PROPERTY);
		}

		return locationIsIdentical &&
				summaryIsIdentical &&
				startTimeIsIdentical &&
				endTimeIsIdentical &&
				recurrenceIsIdentical;
	}
}
