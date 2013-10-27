package com.licensetokil.atypistcalendar.gcal;

import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class GoogleCalendarManager {
	private static GoogleCalendarManager instance = null;

	private GoogleCalendarManager() {
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
	
	public boolean userIsAuthenticated() {
		return AuthenticationManager.getInstance().isAuthenticated();
	}
	
	public void initialiseRemoteCalendar() {
		SyncManager.getInstance().initialiseRemoteCalendar();
	}
	
	public void addRemoteTask(Task localTask) {
		SyncManager.getInstance().addRemoteTask(localTask);
	}
	
	public void updateRemoteTask(Task localTask, String remoteTask) {
		SyncManager.getInstance().updateRemoteTask(localTask, remoteTask);
	}
	
	public void deleteRemoteTask(String remoteTask) {
		SyncManager.getInstance().deleteRemoteTask(remoteTask);
	}
	
	public void doCompleteSync() {
		SyncManager.getInstance().doCompleteSync();
	}
}