package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Schedule extends Task implements Comparable<Schedule>{
	private String remoteId;
	private String taskType;
	private int uniqueId;
	private Calendar startTime;
	private Calendar endTime;
	private String description;
	private String place;

	public Schedule(int uniqueId, Calendar startTime, Calendar endTime,
			String description, String place) {
		this.taskType = "schedule";
		this.uniqueId = uniqueId;
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
		this.uniqueId = s.getUniqueId();
		this.startTime = s.getStartTime();
	}
	
	public String getRemoteId(){
		return remoteId;
	}
	
	public String getTaskType() {
		return taskType;
	}
	
	public int getUniqueId() {
		return uniqueId;
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
	
	public void setRemoteId(String remoteId){
		this.remoteId = remoteId;
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
			return "Schedule@s" + uniqueId + "@s" + startTime.getTime() + "@s"
					+ endTime.getTime() + "@s" + description + "@s" + " ";
		}
		return "Schedule@s" + uniqueId + "@s" + startTime.getTime() + "@s"
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
	
	public int compareTo(Schedule s){
		return startTime.compareTo(s.getStartTime());
	}
}
