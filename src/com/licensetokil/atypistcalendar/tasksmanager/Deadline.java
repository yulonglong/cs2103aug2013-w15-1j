package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Deadline extends Task {
	private String taskType;
	private int uniqueID;
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
	
	public void setEndTime(Calendar st){
		this.endTime = st;
	}
	
	public void setDescription(String d){
		this.description = d;
	}
	
	public void setPlace(String p){
		this.place = p;
	}
	
	public void setStatus(String s){
		this.status = s;
	}

	public String toString() {
		if(place.equals("")){
			this.place = " ";
		}
		return "@Deadline@" + uniqueID + "@" + endTime.getTime() + "@"
				+ description + "@" + place + "@" + status;
	}
}
