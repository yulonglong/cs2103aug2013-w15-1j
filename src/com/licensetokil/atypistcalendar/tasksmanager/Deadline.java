package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Deadline extends Task {
	private int uniqueID;
	private Calendar endTime;
	private String description;
	private String place;

	public Deadline(Calendar endTime, String description, String place) {
		this.endTime = endTime;
		this.description = description;
		this.place = place;
	}

	public int getUniqueID() {
		return uniqueID;
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
