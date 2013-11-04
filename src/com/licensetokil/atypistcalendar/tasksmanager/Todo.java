package com.licensetokil.atypistcalendar.tasksmanager;

import java.util.Calendar;

public class Todo extends Task implements Comparable<Todo>, Cloneable {
	private String remoteId;
	private String taskType;
	private int uniqueId;
	private Calendar lastModifiedDate;
	private String description;
	private String place;
	private String status;

	public Todo(int uniqueId, String description, String place, String status, Calendar lastModifiedDate) {
		this.uniqueId = uniqueId;
		this.taskType = "todo";
		this.description = description;
		this.place = place;
		this.status = status;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Todo(Todo td) {
		this.taskType = "todo";
		this.description = td.getDescription();
		this.place = td.getPlace();
		this.uniqueId = td.getUniqueId();
		this.status = td.getStatus();
	}

	public Todo() {
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

	public String getDescription() {
		return description;
	}

	public String getPlace() {
		return place;
	}

	public String getStatus(){
		return status;
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
			return "Todo@s" + uniqueId + "@s" + description + "@s" + " " + "@s" + status + "@s" + lastModifiedDate.getTime();
		}
		return "Todo@s" + uniqueId + "@s" + description + "@s" + place + "@s" + status + "@s" + lastModifiedDate.getTime();
	}

	public String outputStringForDisplay(){
		String output = description;
		if(!place.equals("")){
			output = output+" at " + this.getPlace();
		}

		output = output + " [Status: " + status + "]";

		return output;
	}

	public int compareTo(Todo td){
		return description.compareTo(td.getDescription());
	}

	@Override
	public Object clone() {
		Todo clonedObject = new Todo();
		clonedObject.remoteId = this.remoteId;
		clonedObject.taskType = this.taskType;
		clonedObject.uniqueId = this.uniqueId;
		clonedObject.description = this.description;
		clonedObject.place = this.place;
		clonedObject.status = this.status;
		clonedObject.lastModifiedDate = (Calendar)this.lastModifiedDate.clone();

		return clonedObject;
	}
}
