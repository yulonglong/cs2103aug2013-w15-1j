//@author A0097142E
package com.licensetokil.atypistcalendar.gcal;

import com.licensetokil.atypistcalendar.tasksmanager.Task;

public class AddSyncNode extends SyncNode {
	private Task localTask;

	public AddSyncNode(Task localTask) {
		super();
		setLocalTask(localTask);
	}

	public Task getLocalTask() {
		return localTask;
	}

	public void setLocalTask(Task localTask) {
		this.localTask = localTask;
	}

	protected int getPriority() {
		return SyncNode.PRIORITY_MEDIUM;
	}
}
