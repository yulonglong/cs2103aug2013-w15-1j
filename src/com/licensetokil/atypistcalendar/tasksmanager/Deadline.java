package com.licensetokil.atypistcalendar.tasksmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Deadline extends Task implements Comparable<Deadline>, Cloneable {
	private String remoteId;
	private String taskType;
	private int uniqueId;
	private Calendar lastModifiedDate;
	private Calendar endTime;
	private String description;
	private String place;
	private String status;

	public Deadline(int uniqueId, Calendar endTime, String description, String place, String status, Calendar lastModifiedDate) {
		this.taskType = "deadline";
		this.endTime = endTime;
		this.description = description;
		this.place = place;
		this.uniqueId = uniqueId;
		this.status = status;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Deadline(Deadline d){
		this.taskType = "deadline";
		this.endTime = d.getEndTime();
		this.description = d.getDescription();
		this.place = d.getPlace();
		this.uniqueId = d.getUniqueId();
		this.status = d.getStatus();
	}

	public Deadline() {
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

	public Calendar getLastModifiedDate(){
		return lastModifiedDate;
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

	public void setLastModifiedDate(Calendar lastModifiedDate){
		this.lastModifiedDate = lastModifiedDate;
	}

	public String toString() {
		if(place.equals("")){
			return "Deadline@s" + uniqueId + "@s" + endTime.getTime() + "@s"
					+ description + "@s" + " " + "@s" + status + "@s" + lastModifiedDate.getTime() + "@s" + remoteId;
		}
		return "Deadline@s" + uniqueId + "@s" + endTime.getTime() + "@s"
				+ description + "@s" + place + "@s" + status + "@s" + lastModifiedDate.getTime() + "@s" + remoteId;
	}

	public String outputStringForDisplay(){
		SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
		SimpleDateFormat formatDay = new SimpleDateFormat("EEE, MMM d, ''yy");

		String output = "[" + formatDay.format(endTime.getTime()) + "] [by " + formatTime.format(endTime.getTime()) + "] "  + "[Status: " + status + "] " + description;
		if(!place.equals("")){
			output = output+" at " + this.getPlace();
		}

		return output;
	}

	public int compareTo(Deadline d){
		return endTime.compareTo(d.getEndTime());
	}

	@Override
	public Object clone() {
		Deadline clonedObject = new Deadline();
		clonedObject.remoteId = this.remoteId;
		clonedObject.taskType = this.taskType;
		clonedObject.uniqueId = this.uniqueId;
		clonedObject.description = this.description;
		clonedObject.place = this.place;
		clonedObject.endTime = (Calendar)this.endTime.clone();
		clonedObject.lastModifiedDate = (Calendar)this.lastModifiedDate.clone();

		return clonedObject;
	}
}
