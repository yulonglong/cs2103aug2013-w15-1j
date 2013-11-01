package com.licensetokil.atypistcalendar.tasksmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Deadline extends Task implements Comparable<Deadline> {
	private String remoteId;
	private String taskType;
	private int uniqueId;
	private Calendar endTime;
	private String description;
	private String place;
	private String status;

	public Deadline(int uniqueId, Calendar endTime, String description, String place, String status) {
		this.taskType = "deadline";
		this.endTime = endTime;
		this.description = description;
		this.place = place;
		this.uniqueId = uniqueId;
		this.status = status;
	}
	
	public Deadline(Deadline d){
		this.taskType = "deadline";
		this.endTime = d.getEndTime();
		this.description = d.getDescription();
		this.place = d.getPlace();
		this.uniqueId = d.getUniqueId();
		this.status = d.getStatus();
	}
	
	public String getRemoteId(){
		return remoteId;
	}

	public int getUniqueId() {
		return uniqueId;
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
	
	public void setRemoteId(String remoteId){
		this.remoteId = remoteId;
	}

	public String toString() {
		if(place.equals("")){
			return "Deadline@s" + uniqueId + "@s" + endTime.getTime() + "@s"
					+ description + "@s" + " " + "@s" + status;
		}
		return "Deadline@s" + uniqueId + "@s" + endTime.getTime() + "@s"
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
