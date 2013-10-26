package com.licensetokil.atypistcalendar.gcal;

import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class UpdateSyncAction extends SyncAction {
	private Task localTask;
	private String remoteTaskID;
	
	public UpdateSyncAction(Task localTask, String remoteTaskID) {
		setLocalTask(localTask);
		setRemoteTaskID(remoteTaskID);
	}

	public Task getLocalTask() {
		return localTask;
	}

	public void setLocalTask(Task localTask) {
		this.localTask = localTask;
	}

	public String getRemoteTaskID() {
		return remoteTaskID;
	}

	public void setRemoteTaskID(String remoteTaskID) {
		this.remoteTaskID = remoteTaskID;
	}
}
