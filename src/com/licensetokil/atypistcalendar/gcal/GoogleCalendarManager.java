package com.licensetokil.atypistcalendar.gcal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

import com.licensetokil.atypistcalendar.ATypistCalendar;
import com.licensetokil.atypistcalendar.parser.AddGoogleAction;
import com.licensetokil.atypistcalendar.parser.GoogleAction;
import com.licensetokil.atypistcalendar.parser.LoginGoogleAction;
import com.licensetokil.atypistcalendar.parser.LogoutGoogleAction;
import com.licensetokil.atypistcalendar.parser.SyncGoogleAction;
import com.licensetokil.atypistcalendar.tasksmanager.Task;


public class GoogleCalendarManager {
	protected static final boolean SYNCER_STATUS_EXECUTING = true;
	protected static final boolean SYNCER_STATUS_SLEEPING = false;

	private static final String MESSAGE_GOOGLE_LOGOUT_NOT_LOGINED = "You are currently not logged in!";
	private static final String MESSAGE_GOOGLE_LOGOUT_SUCCESSFULLY_LOGGED_OUT = "You have been logged out.";
	private static final String MESSAGE_GOOGLE_LOGIN_LOGGING_IN = "Logging in...";
	private static final String MESSAGE_GOOGLE_LOGIN_ALREADY_LOGINED = "You are already logged in!";

	private static final String MESSAGE_GOOGLE_SYNC_SYNCHORINZATION_INITIATED = "Synchorinzation with Google Calendar has been initiated. This will take place in the background.";
	private static final String MESSAGE_GOOGLE_SYNC_NOT_LOGINED = "You are currently not logged in! You will need to Login first before you can synchornise your tasks to Google Calendar. Use 'google login' to login.";

	private static final String MESSAGE_GOOGLE_ADD_NOT_LOGINED = "You are currently not logged in! You will need to Login first before you can use Google's Quick Add feature. Use 'google login' to login.";
	private static final String MESSAGE_GOOGLE_ADD_NO_USER_INPUT = "Error - you didn't include any text after the 'google add' command!";

	private static GoogleCalendarManager instance = null;

	private static Logger logger = Logger.getLogger(GoogleCalendarManager.class.getName());

	private GoogleCalendarManager() {
	}

	static public GoogleCalendarManager getInstance() {
		if (instance == null) {
			logger.info("Instance does not exist, creating new instance.");
			instance = new GoogleCalendarManager();
		}
		return instance;
	}

	public void initialize() {
		logger.info("Reading from ATC_AUTH.txt file, if possible.");
		AuthenticationManager.getInstance().readAuthenticationDetailsFromFile();

		logger.info("Checking if user is previously authenticated (from file), else authenticate through OAuth2.");
		if (!AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is not authenticated (from file), authenticating.");
			AuthenticationManager.getInstance().authenticateUser();
		}

		logger.info("Checking if user is authenticated...");
		if (AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is authenticated. Initialising remote calender, and then doing complete sync.");
			initialiseRemoteCalendar();
			doCompleteSync();
		} else {
			logger.info("User is not authenticated. Nothing to do.");
		}
	}

	public String executeCommand(GoogleAction action) {
		logger.info("Determining GoogleAction subtype.");
		if (action instanceof AddGoogleAction) {
			logger.info("AddGoogleAction sub-type detected.");
			return executeGoogleAction((AddGoogleAction) action);
		} else if (action instanceof SyncGoogleAction) {
			logger.info("SyncGoogleAction sub-type detected.");
			return executeGoogleAction((SyncGoogleAction) action);
		} else if (action instanceof LoginGoogleAction) {
			logger.info("LoginGoogleAction sub-type detected.");
			return executeGoogleAction((LoginGoogleAction) action);
		} else if (action instanceof LogoutGoogleAction) {
			logger.info("LogoutGoogleAction sub-type detected.");
			return executeGoogleAction((LogoutGoogleAction) action);
		} else {
			logger.severe("Unknown sub-class of GoogleAction received!");
			assert false;
			return "";
		}
	}

	private String executeGoogleAction(AddGoogleAction action) {
		logger.info("Checking if user is logged in.");
		if (AuthenticationManager.getInstance().isAuthenticated()) {

			logger.info("User is logged in. Checking if user input is empty.");
			if (action.getUserInput().isEmpty()) {
				logger.info("User input is empty. Returning with error message.");
				return MESSAGE_GOOGLE_ADD_NO_USER_INPUT;
			} else {
				logger.info("User input is not empty. Calling SyncManager.");
				return SyncManager.getInstance().executeAddGoogleAction(action);
			}

		} else {
			logger.info("User is not logged in. Returning with error message.");
			return MESSAGE_GOOGLE_ADD_NOT_LOGINED;
		}
	}

	private String executeGoogleAction(SyncGoogleAction action) {
		logger.info("Checking if user is logged in.");
		if (AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is logged in. Initiating complete sync.");
			doCompleteSync();
			return MESSAGE_GOOGLE_SYNC_SYNCHORINZATION_INITIATED;
		} else {
			logger.info("User is not logged in. Returning with error message.");
			return MESSAGE_GOOGLE_SYNC_NOT_LOGINED;
		}
	}

	private String executeGoogleAction(LoginGoogleAction action) {
		logger.info("Checking if user is logged in.");
		if (AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is already logged in. Returning with error message.");
			return MESSAGE_GOOGLE_LOGIN_ALREADY_LOGINED;
		} else {
			logger.info("User is not logged in. Calling AuthenticationManager to authenticate user.");
			AuthenticationManager.getInstance().authenticateUser();
			return MESSAGE_GOOGLE_LOGIN_LOGGING_IN;
		}
	}

	private String executeGoogleAction(LogoutGoogleAction action) {
		logger.info("Checking if user is logged in.");
		if (AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is logged in. Calling deleting authentication details (to logout).");
			AuthenticationManager.getInstance().deleteAuthenticationDetails();
			return MESSAGE_GOOGLE_LOGOUT_SUCCESSFULLY_LOGGED_OUT;
		} else {
			logger.info("User is not logged in. Returning with error message.");
			return MESSAGE_GOOGLE_LOGOUT_NOT_LOGINED;
		}
	}

	public void initialiseRemoteCalendar() {
		logger.info("initialiseRemoteCalendar() called.");
		SyncManager.getInstance().initialiseRemoteCalendar();
	}

	public void addRemoteTask(Task localTask) {
		logger.info("addRemoteTask(Task localTask) called - localTask ID: " + localTask.getUniqueId());
		SyncManager.getInstance().addRemoteTask(localTask);
	}

	public void updateRemoteTask(Task localTask, String remoteTask) {
		logger.info("updateRemoteTask(Task localTask, String remoteTask) called. localTask ID: " + localTask.getUniqueId() + " . remoteTask ID: " + remoteTask);
		SyncManager.getInstance().updateRemoteTask(localTask, remoteTask);
	}

	public void deleteRemoteTask(String remoteTask) {
		logger.info("deleteRemoteTask(String remoteTask) called. remoteTask ID: " + remoteTask);
		SyncManager.getInstance().deleteRemoteTask(remoteTask);
	}

	public void doCompleteSync() {
		logger.info("doCompleteSync() called.");
		SyncManager.getInstance().doCompleteSync();
	}

	protected void updateTasksManagerWithUpdatedLocalTask(Task updatedLocalTask) {
		ATypistCalendar.getInstance().updateTasksManagerWithUpdatedLocalTask(updatedLocalTask);
	}

	protected void deleteLocalTaskfromTasksManager(int localTaskId) {
		ATypistCalendar.getInstance().deleteLocalTaskfromTasksManager(localTaskId);
	}

	protected Task insertLocalTaskIntoTasksManager(String description, String location, Calendar lastModifiedDate, String remoteTaskId) {
		return ATypistCalendar.getInstance().insertLocalTaskIntoTasksManager(description, location, lastModifiedDate, remoteTaskId);
	}

	protected Task insertLocalTaskIntoTasksManager(String description, String location, Calendar lastModifiedDate, String remoteTaskId, Calendar endTime) {
		return ATypistCalendar.getInstance().insertLocalTaskIntoTasksManager(description, location, lastModifiedDate, remoteTaskId, endTime);
	}

	protected Task insertLocalTaskIntoTasksManager(String description, String location, Calendar lastModifiedDate, String remoteTaskId, Calendar endTime, Calendar startTime) {
		return ATypistCalendar.getInstance().insertLocalTaskIntoTasksManager(description, location, lastModifiedDate, remoteTaskId, endTime, startTime);
	}

	protected void updateLocalTaskWithCorrespondingTaskRemoteId(int localTaskId, String remoteTaskId) {
		ATypistCalendar.getInstance().updateLocalTaskWithCorrespondingTaskRemoteId(localTaskId, remoteTaskId);
	}

	protected ArrayList<Task> getCopyOfAllLocalTasks() {
		return ATypistCalendar.getInstance().getCopyOfAllLocalTasks();
	}

	protected void setSyncerStatus(boolean isExecuting) {
		ATypistCalendar.getInstance().setSyncerStatus(isExecuting);
	}
}