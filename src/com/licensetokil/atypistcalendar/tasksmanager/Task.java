package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public abstract class Task implements Cloneable {
	protected String remoteId;
	protected String taskType;
	protected int uniqueId;
	protected Calendar lastModifiedDate;
	protected String place;
	protected String description;

	public String getRemoteId(){
		return remoteId;
	}

	public String getTaskType(){
		return taskType;
	}

	public String getDescription(){
		return description;
	}

	public int getUniqueId(){
		return uniqueId;
	}

	public Calendar getLastModifiedDate(){
		return lastModifiedDate;
	}

	public String getPlace(){
		return place;
	}

	public void setPlace(String place){
		this.place = place;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public void setRemoteId(String remoteId){
		this.remoteId = remoteId;
	}

	public void setLastModifiedDate(Calendar lastModifiedDate){
		this.lastModifiedDate = lastModifiedDate;
	}

	public String toString(){
		return "";
	}

	public String outputStringForDisplay(){
		return "";
	}

	@Override
	public abstract Object clone();
}
