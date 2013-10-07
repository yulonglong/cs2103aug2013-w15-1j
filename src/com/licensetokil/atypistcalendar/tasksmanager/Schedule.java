package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Schedule extends Task {
	private int uniqueID;
	private Calendar startTime;
	private Calendar endTime;
	private String description;
	private String place;

	public Schedule(Calendar startTime, Calendar endTime,
			String description, String place) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.place = place;
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
		return "Schedule@" + uniqueID + "@" + startTime.toString() + "@"
				+ endTime.toString() + "@" + description + "@" + place;
	}
}
