package com.licensetokil.atypistcalendar.tasksmanager;

public class Todo extends Task {
	private int uniqueID;
	private String description;
	private String place;

	public Todo(String description, String place) {
		this.description = description;
		this.place = place;
	}

	public int getUniqueID() {
		return uniqueID;
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
