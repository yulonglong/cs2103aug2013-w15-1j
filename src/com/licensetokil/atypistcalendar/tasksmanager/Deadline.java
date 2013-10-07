package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Deadline extends Task {
	private String taskType;
	private int uniqueID;
	private final Calendar startTime = null;
	private Calendar endTime;
	private String description;
	private String place;

	public Deadline(Calendar endTime, String description, String place) {
		this.taskType = "deadline";
		this.endTime = endTime;
		this.description = description;
		this.place = place;
	}

	public int getUniqueID() {
		return uniqueID;
	}
	
	public String getTaskType(){
		return taskType;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public String getDescription() {
		return description;
	}

	public String getPlace() {
		return place;
	}

	public String toString() {
		return "Deadline@" + uniqueID + "@" + endTime.toString() + "@"
				+ description + "@" + place;
	}
}
