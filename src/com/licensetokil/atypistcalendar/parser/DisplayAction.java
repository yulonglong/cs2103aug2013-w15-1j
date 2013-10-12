package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class DisplayAction extends LocalAction {
	private String description;
	private String taskType;
	private Calendar startTime;
	private Calendar endTime;
	//private String place;
	private String status;
	
	public DisplayAction(){
		type = LocalActionType.DISPLAY;
		taskType = new String();
		startTime = null;
		endTime = null;
		description = new String();
		//place = new String();
		status = new String();
	}
	
	public String toString(){
		String stringStartTime;
		if (startTime==null){
			stringStartTime = new String("null");
		}
		else{
			stringStartTime= startTime.getTime().toString();
		}
		String stringEndTime;
		if (endTime==null){
			stringEndTime = new String("null");
		}
		else{
			stringEndTime= endTime.getTime().toString();
		}
		return ("Type	: " + type + "\n" +
				"Task Type	: " + taskType + "\n" +
				"Status	: " + status + "\n" +
			    "Start Time	: " + stringStartTime + "\n" +
		        "End Time	: " + stringEndTime + "\n" +
		        "Description	: " + description + "\n");// +
		        //"Place	: " + place + "\n");
	}
	

	public Calendar getStartTime(){
		return startTime;
	}

	public Calendar getEndTime(){
		return endTime;
	}

	public String getDescription(){
		return description;
	}

//	public String getPlace(){
//		return place;
//	}
	
	public LocalActionType getType(){
		return type;
	}
	
	public String getStatus(){
		return status;
	}
	
	public String getTaskType(){
		return taskType;
	}

	public void setStartTime(Calendar newStartTime){
		startTime = newStartTime;
	}

	public void setEndTime(Calendar newEndTime){
		endTime = newEndTime;
	}

	public void setDescription(String newDescription){
		description = newDescription;
	}

//	public void setPlace(String newPlace){
//		place = newPlace;
//	}
	
	public void setStatus(String newStatus){
		status = newStatus;
	}
	
	public void setTaskType(String newTaskType){
		taskType = newTaskType;
	}
}
