package com.licensetokil.atypistcalendar.gcal;

import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class AddSyncAction extends SyncAction {
	private Task localTask;
	
	public AddSyncAction(Task localTask) {
		setLocalTask(localTask);
	}

	public Task getLocalTask() {
		return localTask;
	}

	public void setLocalTask(Task localTask) {
		this.localTask = localTask;
	}
}
