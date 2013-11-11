package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public abstract class Task implements Cloneable{
	protected String remoteId;
	protected TaskType taskType;
	protected int uniqueId;
	protected Calendar lastModifiedDate;
	protected String place;
	protected String description;
	protected static final String DELIMITER = "@s";
	protected static final String EMPTY_STRING = "";
	protected static final String DONE_ALIGN = "done&nbsp&nbsp";
	protected static final String DONE_NO_ALIGN = "done";
	protected static final String DISPLAY_PLACE_NOT_EMPTY = "%s at %s";
	protected static final String BLANK_SPACE = " ";

	public abstract String getRemoteId();
	public abstract TaskType getTaskType();
	public abstract String getDescription();
	public abstract int getUniqueId();
	public abstract Calendar getLastModifiedDate();
	public abstract String getPlace();
	public abstract void setPlace(String place);
	public abstract void setDescription(String description);
	public abstract void setRemoteId(String remoteId);
	public abstract void setLastModifiedDate(Calendar lastModifiedDate);
	public abstract void setUniqueId(int uniqueId);
	public abstract void setTaskType(TaskType t);
	public abstract String toString();
	public abstract String outputStringForDisplay();
	@Override
	public abstract Object clone();
}
