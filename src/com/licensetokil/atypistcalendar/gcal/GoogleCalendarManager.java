package com.licensetokil.atypistcalendar.gcal;

import java.util.logging.Logger;

import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class GoogleCalendarManager {
	private static GoogleCalendarManager instance = null;

	private static Logger logger = Logger.getLogger("com.atypistscalendar.gcal.googlecalendarmanager");

	private GoogleCalendarManager() {
		logger.fine("Constructor of GoogleCalendarManager called");
	}

	static public GoogleCalendarManager getInstance() {
		logger.fine("getInstance() called.");
		if(instance == null) {
			logger.info("Instance does not exist, creating new instance.");
			instance = new GoogleCalendarManager();
		}
		return instance;
	}

	public void initialise() {
		logger.fine("initialised() called.");

		logger.info("Reading from ATC_AUTH.txt file, if possible.");
		AuthenticationManager.getInstance().readAuthenticationDetailsFromFile();

		logger.info("Checking if user is previously authenticated (from file), else authenticate through OAuth2.");
		if(!userIsAuthenticated()) {
			logger.info("User is not authenticated (from file), authenticating.");
			authenticateUser();
		}

		logger.info("Checking if user is authenticated...");
		if(userIsAuthenticated()) {
			logger.info("User is authenticated. Initialising remote calender, and then doing complete sync.");
			initialiseRemoteCalendar();
			doCompleteSync();
		}
		else {
			logger.info("User is not authenticated. Nothing to do.");
		}
	}

	//TODO Do we still need this?
	public void authenticateUser() {
		logger.fine("authenticateUser() called.");
		AuthenticationManager.getInstance().authenticateUser();
	}

	//TODO Do we still need this?
	public boolean userIsAuthenticated() {
		logger.fine("userIsAuthenticated() called.");
		return AuthenticationManager.getInstance().isAuthenticated();
	}

	public void initialiseRemoteCalendar() {
		logger.fine("initialiseRemoteCalendar() called.");
		SyncManager.getInstance().initialiseRemoteCalendar();
	}

	public void addRemoteTask(Task localTask) {
		logger.fine("addRemoteTask(Task localTask) called.");
		SyncManager.getInstance().addRemoteTask(localTask);
	}

	public void updateRemoteTask(Task localTask, String remoteTask) {
		logger.fine("updateRemoteTask(Task localTask, String remoteTask) called.");
		SyncManager.getInstance().updateRemoteTask(localTask, remoteTask);
	}

	public void deleteRemoteTask(String remoteTask) {
		logger.fine("deleteRemoteTask(String remoteTask) called.");
		SyncManager.getInstance().deleteRemoteTask(remoteTask);
	}

	public void doCompleteSync() {
		logger.fine("doCompleteSync() called.");
		SyncManager.getInstance().doCompleteSync();
	}
}