package com.licensetokil.atypistcalendar.gcal;

public class InitialiseRemoteCalendarSyncAction extends SyncAction {
	public InitialiseRemoteCalendarSyncAction() {
		super();
	}

	@Override
	protected int getPriority() {
		return SyncAction.PRIORITY_HIGH;
	}
}
