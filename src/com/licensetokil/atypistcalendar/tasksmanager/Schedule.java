package com.licensetokil.atypistcalendar.tasksmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Schedule extends Task implements Comparable<Schedule>, Cloneable {
	private String remoteId;
	private String taskType;
	private int uniqueId;
	private Calendar lastModifiedDate;
	private Calendar startTime;
	private Calendar endTime;
	private String description;
	private String place;

	public Schedule(int uniqueId, Calendar startTime, Calendar endTime,
			String description, String place, Calendar lastModifiedDate) {
		this.taskType = "schedule";
		this.uniqueId = uniqueId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.place = place;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Schedule(Schedule s){
		this.taskType = "schedule";
		this.endTime = s.getEndTime();
		this.description = s.getDescription();
		this.place = s.getPlace();
		this.uniqueId = s.getUniqueId();
		this.startTime = s.getStartTime();
	}

	public Schedule() {
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

	public Calendar getLastModifiedDate(){
		return lastModifiedDate;
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

	public void setLastModifiedDate(Calendar lastModifiedDate){
		this.lastModifiedDate = lastModifiedDate;
	}

	public String toString() {
		if(place.equals("")){
			return "Schedule@s" + uniqueId + "@s" + startTime.getTime() + "@s"
					+ endTime.getTime() + "@s" + description + "@s" + " " + "@s" + lastModifiedDate.getTime() + "@s" + remoteId;
		}
		return "Schedule@s" + uniqueId + "@s" + startTime.getTime() + "@s"
				+ endTime.getTime() + "@s" + description + "@s" + place + "@s" + lastModifiedDate.getTime() + "@s" + remoteId;
	}

	public String outputStringForDisplay(){
		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMM dd, ''yy");

		String output = "[" + formatDay.format(startTime.getTime()) + "] [" + formatTime.format(startTime.getTime()) + " - " + formatTime.format(endTime.getTime()) + "] " +
							description;
		if(!place.equals("")){
			output = output+" at " + this.getPlace();
		}

		return output;
	}

	public int compareTo(Schedule s){
		return startTime.compareTo(s.getStartTime());
	}

	@Override
	public Object clone() {
		Schedule clonedObject = new Schedule();
		clonedObject.remoteId = this.remoteId;
		clonedObject.taskType = this.taskType;
		clonedObject.uniqueId = this.uniqueId;
		clonedObject.description = this.description;
		clonedObject.place = this.place;
		clonedObject.startTime = (Calendar)this.startTime.clone();
		clonedObject.endTime = (Calendar)this.endTime.clone();
		clonedObject.lastModifiedDate = (Calendar)this.lastModifiedDate.clone();

		return clonedObject;
	}
}
