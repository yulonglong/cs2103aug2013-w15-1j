package com.licensetokil.atypistcalendar.gcal;

public class DoCompleteSyncAction extends SyncAction {
	public DoCompleteSyncAction() {
		super();
	}

	protected int getPriority() {
		return SyncAction.PRIORITY_LOW;
	}
}
