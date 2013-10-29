package com.licensetokil.atypistcalendar.tasksmanager;

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
	
	public Todo(Todo td){
		this.taskType = "deadline";
		this.description = td.getDescription();
		this.place = td.getPlace();
		this.uniqueID = td.getUniqueID();
		this.status = td.getStatus();
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
		if(place.equals("")){
			return "@Todo@" + uniqueID + "@" + description + "@" + " " + "@" + status;
		}
		return "@Todo@" + uniqueID + "@" + description + "@" + place + "@" + status;
	}
	
	public String outputStringForDisplay(){
		String output = "Event: " + this.getDescription() 
				+ "\nStatus: " + this.getStatus()
				+ "\n";
		if(!place.equals("")){
			output = output+"Place: " + this.getPlace() + "\n";
		}
		
		return output;
	}
}
