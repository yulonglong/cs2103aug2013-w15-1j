package com.licensetokil.atypistcalendar.tasksmanager;

abstract class Task {
	protected String taskType;
	protected int uniqueID;
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
	
	public String toString(){
		return "";
	}
	
}
