package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Deadline extends Task {
	private String taskType;
	private int uniqueID;
	private final Calendar startTime = null;
	private Calendar endTime;
	private String description;
	private String place;
	private String status;

	public Deadline(int uniqueID, Calendar endTime, String description, String place, String status) {
		this.taskType = "deadline";
		this.endTime = endTime;
		this.description = description;
		this.place = place;
		this.uniqueID = uniqueID;
		this.status = status;
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
	
	public String getStatus(){
		return status;
	}

	public String toString() {
		return "Deadline@" + uniqueID + "@" + endTime.toString() + "@"
				+ description + "@" + place;
	}
}
