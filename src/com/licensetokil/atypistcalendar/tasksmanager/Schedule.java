package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Schedule extends Task {
	private String remoteID;
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
	
	public Schedule(Schedule s){
		this.taskType = "schedule";
		this.endTime = s.getEndTime();
		this.description = s.getDescription();
		this.place = s.getPlace();
		this.uniqueID = s.getUniqueID();
		this.startTime = s.getStartTime();
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
	
	public void setStartTime(Calendar st){
		this.startTime = st;
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

	public String toString() {
		if(place.equals("")){
			return "Schedule@s" + uniqueID + "@s" + startTime.getTime() + "@s"
					+ endTime.getTime() + "@s" + description + "@s" + " ";
		}
		return "Schedule@s" + uniqueID + "@s" + startTime.getTime() + "@s"
				+ endTime.getTime() + "@s" + description + "@s" + place ;
	}
	
	public String outputStringForDisplay(){
		SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMM d, ''yy");
		SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
		
		String output = "Event: " + this.getDescription()
				+ "\n" + "Day: " + formatDay.format(this.getStartTime().getTime())
				+ "\n" + "Starting Time: " + formatTime.format(this.getStartTime().getTime())
				+ "\n" + "Ending Time: " + formatTime.format(this.getEndTime().getTime())
				+ "\n";
		if(!place.equals("")){
			output = output+"Place: " + this.getPlace() + "\n";
		}
		
		
		return output + "\n";
	}
}
