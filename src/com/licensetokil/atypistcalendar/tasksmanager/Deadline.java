package com.licensetokil.atypistcalendar.tasksmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Deadline extends Task implements Comparable<Deadline> {
	private String remoteID;
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
	
	public Deadline(Deadline d){
		this.taskType = "deadline";
		this.endTime = d.getEndTime();
		this.description = d.getDescription();
		this.place = d.getPlace();
		this.uniqueID = d.getUniqueID();
		this.status = d.getStatus();
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
			return "Deadline@s" + uniqueID + "@s" + endTime.getTime() + "@s"
					+ description + "@s" + " " + "@s" + status;
		}
		return "Deadline@s" + uniqueID + "@s" + endTime.getTime() + "@s"
				+ description + "@s" + place + "@s" + status;
	}
	
	public String outputStringForDisplay(){
		SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMM d, ''yy");
		SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
		
		String output = "Event: " + this.getDescription()
				+ "\n" + "Day: " + formatDay.format(this.getEndTime().getTime())
				+ "\n" + "Due by: " + formatTime.format(this.getEndTime().getTime())
				+ "\n";
		if(!place.equals("")){
			output = output+"Place: " + this.getPlace() + "\n";
		}
		
		return output + "\n";
	}
	
	public int compareTo(Deadline d){
		return endTime.compareTo(d.getEndTime());
	}
}
