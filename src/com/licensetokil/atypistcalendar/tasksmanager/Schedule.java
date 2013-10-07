package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Schedule extends Task {
	private String taskType;
	private int uniqueID;
	private Calendar startTime;
	private Calendar endTime;
	private String description;
	private String place;

	public Schedule(int uniqueID, Calendar startTime, Calendar endTime,
			String description, String place) {
		this.taskType = "schedule";
		this.uniqueID = uniqueID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.place = place;
	}
	
	public String getTaskType() {
		return taskType;
	}
	
	public int getUniqueID() {
		return uniqueID;
	}

	public Calendar getStartTime() {
		return startTime;
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
		return "Schedule@" + uniqueID + "@" + startTime.getTime() + "@"
				+ endTime.getTime() + "@" + description + "@" + place;
	}
}
