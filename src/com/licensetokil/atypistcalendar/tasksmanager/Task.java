package com.licensetokil.atypistcalendar.tasksmanager;

public abstract class Task{
	protected String remoteId;
	protected String taskType;
	protected int uniqueId;
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
	
	public String toString(){
		return "";
	}
	
	public String outputStringForDisplay(){
		return "";
	}

}
