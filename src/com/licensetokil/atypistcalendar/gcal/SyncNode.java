package com.licensetokil.atypistcalendar.gcal;

public abstract class SyncNode implements Comparable<SyncNode> {
	public static final int PRIORITY_HIGH = 1;
	public static final int PRIORITY_MEDIUM = 2;
	public static final int PRIORITY_LOW = 3;

	private static int creationIdCounter = 0;

	protected int creationId = 0;

	public SyncNode() {
		creationId = creationIdCounter++;
	}

	protected abstract int getPriority();

	protected int getCreationId() {
		return creationId;
	}

	@Override
	public int compareTo(SyncNode obj) {
		if(getPriority() == obj.getPriority()) {
			return getCreationId() - obj.getCreationId();
		}
		return getPriority() - obj.getPriority();
	}
}
