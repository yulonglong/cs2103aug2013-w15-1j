package com.licensetokil.atypistcalendar.tasksmanager;

abstract class Task {
	protected String taskType;
	protected int uniqueID;
	protected String place;
	protected String description;

	
	public String getTaskType(){
		return taskType;
	}

	public String getDescription(){
		return description;
	}
	
	public int getUniqueID(){
		return uniqueID;
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
	
	public String toString(){
		return "";
	}
	
}
