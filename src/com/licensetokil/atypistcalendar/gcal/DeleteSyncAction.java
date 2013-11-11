package com.licensetokil.atypistcalendar.gcal;

public class DeleteSyncAction extends SyncAction {
	private String remoteTaskID;

	public DeleteSyncAction(String remoteTaskID) {
		super();
		setRemoteTaskID(remoteTaskID);
	}

	protected int getPriority() {
		return SyncAction.PRIORITY_MEDIUM;
	}

	public String getRemoteTaskID() {
		return remoteTaskID;
	}

	public void setRemoteTaskID(String remoteTaskID) {
		this.remoteTaskID = remoteTaskID;
	}
}
