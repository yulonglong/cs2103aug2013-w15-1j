package com.licensetokil.atypistcalendar.gcal;

public class DeleteSyncAction extends SyncAction {
	private String remoteTaskID;
	
	public DeleteSyncAction(String remoteTaskID) {
		setRemoteTaskID(remoteTaskID);
	}

	public String getRemoteTaskID() {
		return remoteTaskID;
	}

	public void setRemoteTaskID(String remoteTaskID) {
		this.remoteTaskID = remoteTaskID;
	}
}
