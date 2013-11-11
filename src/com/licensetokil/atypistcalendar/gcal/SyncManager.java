package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.licensetokil.atypistcalendar.parser.AddGoogleAction;
import com.licensetokil.atypistcalendar.tasksmanager.Task;

class SyncManager {

	protected static final Calendar REMOTE_TODO_START_END_DATE = Calendar.getInstance();
	protected static final JsonArray REMOTE_TODO_RECURRENCE_PROPERTY = new JsonArray();

	private static final String QUICK_ADD_RESOURCE_LABEL_TEXT = "text";
	private static final String QUICK_ADD_RESOURCE_LABEL_CALENDAR_ID = "calendarId";

	private static final String GOOGLE_REQUEST_URL_QUICK_ADD = "https://www.googleapis.com/calendar/v3/calendars/%s/events/quickAdd";

	private static final String ERROR_MESSAGE_QUICK_ADD_UNABLE_TO_CONNECT_TO_GOOGLE = "A Typist's Calendar is unable to connect to Google. Please try again in a little while.";

	private static final String MESSAGE_QUICK_ADD_SUCCESSFUL = "Added: \n%s";

	private static final String EVENT_RESOURCE_LABEL_UPDATED = "updated";

	private static SyncManager instance = null;

	private static Logger logger = Logger.getLogger(SyncManager.class.getName());

	private Lock goToSleepLock = new ReentrantLock();
	private Condition goToSleepCondition = goToSleepLock.newCondition();

	private PriorityBlockingQueue<SyncAction> queue;
	private Syncer syncer;
	private String remoteCalendarId;

	static {
		REMOTE_TODO_START_END_DATE.set(2013, Calendar.JANUARY, 1);
		REMOTE_TODO_RECURRENCE_PROPERTY.add(new JsonPrimitive("RRULE:FREQ=MONTHLY"));
	}

	private SyncManager() {
		queue = new PriorityBlockingQueue<SyncAction>();
		syncer = null;
		remoteCalendarId = null;
	}

	protected static SyncManager getInstance() {
		if(instance == null) {
			instance = new SyncManager();
		}
		return instance;
	}

	protected void initialiseRemoteCalendar() {
		queue.add(new InitialiseRemoteCalendarSyncAction());
		runSyncer();
	}

	protected void addRemoteTask(Task localTask) {
		queue.add(new AddSyncAction(localTask));
		runSyncer();
	}

	protected void updateRemoteTask(Task localTask, String remoteTaskID) {
		deleteRemoteTask(remoteTaskID);
		addRemoteTask(localTask);
		//TODO temporary measure, cause increasing the iCal sequence number is difficult - see Dev's guide
		//queue.add(new UpdateSyncAction(localTask, remoteTaskID));
		runSyncer();
	}

	protected void deleteRemoteTask(String remoteTaskID) {
		queue.add(new DeleteSyncAction(remoteTaskID));
		runSyncer();
	}

	protected void doCompleteSync() {
		queue.add(new DoCompleteSyncAction());
		runSyncer();
	}

	private void runSyncer() {
		if(!AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is not logged in. No use running Syncer as it will return will authentication errors. Nothing else to do, returning. (Logging in will tigger this process again.");
			return;
		}

		if(syncer == null || !syncer.isAlive()) {
			syncer = new Syncer();
			syncer.start();
		}
		else {
			//Signaling... if needed.
			goToSleepLock.lock();
			goToSleepCondition.signal();
			goToSleepLock.unlock();
		}
	}

	protected PriorityBlockingQueue<SyncAction> getQueue() {
		return queue;
	}

	protected String getRemoteCalendarId() {
		return remoteCalendarId;
	}

	protected void setRemoteCalendarId(String remoteCalendarId) {
		this.remoteCalendarId = remoteCalendarId;
	}

	protected Lock getGoToSleepLock() {
		return goToSleepLock;
	}

	protected Condition getGoToSleepCondition() {
		return goToSleepCondition;
	}

	protected String executeAddGoogleAction(AddGoogleAction action) {
		HashMap<String, String> formParameters = new HashMap<>();

		formParameters.put(QUICK_ADD_RESOURCE_LABEL_CALENDAR_ID, getRemoteCalendarId());
		formParameters.put(QUICK_ADD_RESOURCE_LABEL_TEXT, action.getUserInput());

		JsonObject serverReply;
		try {
			serverReply = Utilities.parseToJsonObject(
					Utilities.sendUrlencodedFormHttpsRequest(
							String.format(GOOGLE_REQUEST_URL_QUICK_ADD, getRemoteCalendarId()),
							Utilities.REQUEST_METHOD_POST,
							AuthenticationManager.getInstance().getAuthorizationHeader(),
							formParameters
					)
			);
		} catch (JsonParseException | IllegalStateException | IOException e) {
			logger.warning("Unable to connection to Google; exception thrown - " + e.getMessage());
			return ERROR_MESSAGE_QUICK_ADD_UNABLE_TO_CONNECT_TO_GOOGLE;
		}

		return String.format(
				MESSAGE_QUICK_ADD_SUCCESSFUL,
				insertRemoteTaskIntoTasksManager(serverReply).outputStringForDisplay()
		);
	}

	protected Task insertRemoteTaskIntoTasksManager(JsonObject remoteTask) {
		//Extracting the (common) fields from the RemoteTask object.
		String remoteTaskId = Utilities.getJsonObjectValueOrEmptyString(remoteTask, "id");
		String description = Utilities.getJsonObjectValueOrEmptyString(remoteTask, "summary");
		String location = Utilities.getJsonObjectValueOrEmptyString(remoteTask, "location");
		Calendar lastModifiedDate = getLastModifiedDateOrTimeNow(remoteTask);

		Task newLocalTask = null;
		if(remoteTask.getAsJsonObject("start").get("date") != null) {
			//Task type is a todo
			newLocalTask = GoogleCalendarManager.getInstance().insertLocalTaskIntoTasksManager(description, location, lastModifiedDate, remoteTaskId);
		}
		else {
			Calendar startTime = null;
			Calendar endTime = null;
			try {
				startTime = Utilities.parseGoogleDateTimeObject(remoteTask.getAsJsonObject("start"));
				endTime = Utilities.parseGoogleDateTimeObject(remoteTask.getAsJsonObject("end"));
			} catch (ParseException e) {
				logger.severe("Unable to parse Google DateTime object (this is unexpected as Google only returns a standardised format)");
				e.printStackTrace();
				return null;
			}

			boolean startTimeIsTheSameAsEndTime = startTime.compareTo(endTime) == 0;
			if(startTimeIsTheSameAsEndTime) {
				//Task type is a deadline
				newLocalTask = GoogleCalendarManager.getInstance().insertLocalTaskIntoTasksManager(description, location, lastModifiedDate, remoteTaskId, endTime);
			}
			else {
				//Task type is a schedule
				newLocalTask = GoogleCalendarManager.getInstance().insertLocalTaskIntoTasksManager(description, location, lastModifiedDate, remoteTaskId, endTime, startTime);
			}
		}

		//We update the remote task so it gets the local task's ID gets recorded remotely.
		SyncManager.getInstance().updateRemoteTask(newLocalTask, remoteTaskId);
		return newLocalTask;
	}

	protected static Calendar getLastModifiedDateOrTimeNow(JsonObject remoteTask) {
		try {
			return Utilities.parseGenericGoogleDateString(
					Utilities.getJsonObjectValueOrEmptyString(remoteTask, EVENT_RESOURCE_LABEL_UPDATED),
					Utilities.RFC3339_FORMAT_WITH_MILLISECONDS
			);
		} catch (ParseException e) {
			return Calendar.getInstance(); // Fail quietly
		}
	}
}
