package com.licensetokil.atypistcalendar.tasksmanager;
import java.util.Calendar;

public class Todo extends Task {
	private String taskType;
	private int uniqueID;
	private String description;
	private String place;
	private String status;

	public Todo(int uniqueID, String description, String place, String status) {
		this.uniqueID = uniqueID;
		this.taskType = "todo";
		this.description = description;
		this.place = place;
		this.status = status;
	}

	public int getUniqueID() {
		return uniqueID;
	}
	
	public String getTaskType(){
		return taskType;
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

	public String toString() {
		return "Todo@" + uniqueID + "@" + description + "@" + place;
	}
}
