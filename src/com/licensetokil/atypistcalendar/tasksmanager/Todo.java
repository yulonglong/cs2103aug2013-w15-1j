package com.licensetokil.atypistcalendar.tasksmanager;

public class Todo extends Task implements Comparable<Todo> {
	private String remoteId;
	private String taskType;
	private int uniqueId;
	private String description;
	private String place;
	private String status;

	public Todo(int uniqueId, String description, String place, String status) {
		this.uniqueId = uniqueId;
		this.taskType = "todo";
		this.description = description;
		this.place = place;
		this.status = status;
	}
	
	public Todo(Todo td){
		this.taskType = "deadline";
		this.description = td.getDescription();
		this.place = td.getPlace();
		this.uniqueId = td.getUniqueId();
		this.status = td.getStatus();
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

	public String toString() {
		if(place.equals("")){
			return "Todo@s" + uniqueId + "@s" + description + "@s" + " " + "@s" + status;
		}
		return "Todo@s" + uniqueId + "@s" + description + "@s" + place + "@s" + status;
	}
	
	public String outputStringForDisplay(){
		String output = "Event: " + this.getDescription() 
				+ "\nStatus: " + this.getStatus()
				+ "\n";
		if(!place.equals("")){
			output = output+"Place: " + this.getPlace() + "\n";
		}
		
		return output + "\n";
	}
	
	public int compareTo(Todo td){
		return description.compareTo(td.getDescription());
	}
}
