package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gson.JsonParseException;
import com.licensetokil.atypistcalendar.parser.AddGoogleAction;
import com.licensetokil.atypistcalendar.parser.GoogleAction;
import com.licensetokil.atypistcalendar.parser.LoginGoogleAction;
import com.licensetokil.atypistcalendar.parser.LogoutGoogleAction;
import com.licensetokil.atypistcalendar.parser.SyncGoogleAction;
import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class GoogleCalendarManager {
	private static GoogleCalendarManager instance = null;

	private static Logger logger = Logger.getLogger(GoogleCalendarManager.class.getName());

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
		if(!AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is not authenticated (from file), authenticating.");
			AuthenticationManager.getInstance().authenticateUser();
		}

		logger.info("Checking if user is authenticated...");
		if(AuthenticationManager.getInstance().isAuthenticated()) {
			logger.info("User is authenticated. Initialising remote calender, and then doing complete sync.");
			initialiseRemoteCalendar();
			doCompleteSync();
		}
		else {
			logger.info("User is not authenticated. Nothing to do.");
		}
	}

	public String executeCommand(GoogleAction action) {
		try {
			if(action instanceof AddGoogleAction) {
				return executeGoogleAction((AddGoogleAction) action);
			}
			else if(action instanceof SyncGoogleAction) {
				return executeGoogleAction((SyncGoogleAction) action);
			}
			else if(action instanceof LoginGoogleAction) {
				return executeGoogleAction((LoginGoogleAction) action);
			}
			else if(action instanceof LogoutGoogleAction) {
				return executeGoogleAction((LogoutGoogleAction) action);
			}
			else {
				logger.severe("Unknown sub-class of GoogleAction received!");
				assert false;
				return "";
			}
		}
		catch(IOException | JsonParseException | IllegalStateException e) {
			//TODO
			e.printStackTrace();
			return "Error";
		}
	}

	private String executeGoogleAction(AddGoogleAction action)
			throws IllegalStateException, JsonParseException, IOException {
		if(AuthenticationManager.getInstance().isAuthenticated()) {
			HashMap<String, String> formParameters = new HashMap<>();

			formParameters.put("calendarId", SyncManager.getInstance().getRemoteCalendarId());
			formParameters.put("text", action.getUserInput());

//			JsonObject serverReply = Util.parseToJsonObject(
					String serverReply = Util.sendUrlencodedFormHttpsRequest(
							"https://www.googleapis.com/calendar/v3/calendars/" +
									SyncManager.getInstance().getRemoteCalendarId() +
									"/events/quickAdd",
							Util.REQUEST_METHOD_POST,
							AuthenticationManager.getInstance().getAuthorizationHeader(),
							formParameters
//					)
			);

			System.out.println("lol okay");
			System.out.println(serverReply);

			return "lol okay";
		}
		else {
			return "You are currently not logged in! You will need to Login first before you can use Google's Quick Add feature.";
		}
	}

	private String executeGoogleAction(SyncGoogleAction action) {
		if(AuthenticationManager.getInstance().isAuthenticated()) {
			doCompleteSync();
			return "Synchorinzation with Google Calendar has been initiated. This will take place in the background.";
		}
		else {
			return "You are currently not logged in! You will need to Login first before you can synchornise your tasks to Google Calendar.";
		}
	}

	private String executeGoogleAction(LoginGoogleAction action) {
		if(AuthenticationManager.getInstance().isAuthenticated()) {
			return "You are already logged in!";
		}
		else {
			AuthenticationManager.getInstance().authenticateUser();
			return "Logging in...";
		}
	}

	private String executeGoogleAction(LogoutGoogleAction action) {
		if(AuthenticationManager.getInstance().isAuthenticated()) {
			AuthenticationManager.getInstance().forgetAuthenticationDetails();
			return "You have been logged out.";
		}
		else {
			return "You are currently not logged in!";
		}
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