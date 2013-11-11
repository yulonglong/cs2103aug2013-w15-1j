//@author A0097142E
package com.licensetokil.atypistcalendar.gcal;

import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class UpdateSyncNode extends SyncNode {
	private Task localTask;
	private String remoteTaskID;

	public UpdateSyncNode(Task localTask, String remoteTaskID) {
		super();
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

	protected int getPriority() {
		return SyncNode.PRIORITY_MEDIUM;
	}
}
