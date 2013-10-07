package com.licensetokil.atypistcalendar.tasksmanager;
import java.util.Calendar;

public class Todo extends Task {
	private String taskType;
	private int uniqueID;
	private String description;
	private String place;
	private final Calendar startTime = null;
	private final Calendar endTime = null;

	public Todo(String description, String place) {
		this.taskType = "todo";
		this.description = description;
		this.place = place;
	}

	public int getUniqueID() {
		return uniqueID;
	}
	
	public String getTaskType(){
		return taskType;
	}
	
	public String getDescription() {
		return description;
	}

	public String getPlace() {
		return place;
	}

	public String toString() {
		return "Todo@" + uniqueID + "@" + description + "@" + place;
	}
}
