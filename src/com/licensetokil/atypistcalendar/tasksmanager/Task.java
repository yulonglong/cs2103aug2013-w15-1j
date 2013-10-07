package com.licensetokil.atypistcalendar.tasksmanager;
import java.util.Calendar;

abstract class Task {
	protected String taskType;
	protected int uniqueID;
	protected String description;
	protected Calendar startTime;
	protected Calendar endTime;
	
	public String getTaskType(){
		return taskType;
	}

	public String getDescription(){
		return description;
	}
	
	public Calendar getStartTime(){
		return startTime;
	}
	
	public Calendar getEndTime(){
		return endTime;
	}
}
