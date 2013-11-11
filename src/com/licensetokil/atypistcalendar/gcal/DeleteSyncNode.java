//@author A0097142E
package com.licensetokil.atypistcalendar.gcal;

public class DeleteSyncNode extends SyncNode {
	private String remoteTaskID;

	public DeleteSyncNode(String remoteTaskID) {
		super();
		setRemoteTaskID(remoteTaskID);
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
