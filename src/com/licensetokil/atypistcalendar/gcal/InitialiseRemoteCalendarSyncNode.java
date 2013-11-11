package com.licensetokil.atypistcalendar.gcal;

public class InitialiseRemoteCalendarSyncNode extends SyncNode {
	public InitialiseRemoteCalendarSyncNode() {
		super();
	}

	protected int getPriority() {
		return SyncNode.PRIORITY_HIGH;
	}
}
