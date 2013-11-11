package com.licensetokil.atypistcalendar.gcal;

public class DoCompleteSyncNode extends SyncNode {
	public DoCompleteSyncNode() {
		super();
	}

	protected int getPriority() {
		return SyncNode.PRIORITY_LOW;
	}
}
