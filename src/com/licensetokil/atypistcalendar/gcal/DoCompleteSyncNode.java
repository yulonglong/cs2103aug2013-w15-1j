//@author A0097142E
package com.licensetokil.atypistcalendar.gcal;

public class DoCompleteSyncNode extends SyncNode {
	public DoCompleteSyncNode() {
		super();
	}

	protected int getPriority() {
		return SyncNode.PRIORITY_LOW;
	}
}
