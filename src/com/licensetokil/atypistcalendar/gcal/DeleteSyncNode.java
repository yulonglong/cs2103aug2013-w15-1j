package com.licensetokil.atypistcalendar.gcal;

public class DeleteSyncNode extends SyncNode {
	private String remoteTaskID;

	public DeleteSyncNode(String remoteTaskID) {
		super();
		setRemoteTaskID(remoteTaskID);
	}

	protected int getPriority() {
		return SyncNode.PRIORITY_MEDIUM;
	}

	public String getRemoteTaskID() {
		return remoteTaskID;
	}

	public void setRemoteTaskID(String remoteTaskID) {
		this.remoteTaskID = remoteTaskID;
	}
}
