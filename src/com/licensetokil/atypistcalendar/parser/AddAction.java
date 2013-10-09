package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class AddAction extends LocalAction {
	private String description;
	private Calendar startTime;
	private Calendar endTime;
	private String place;
	
	public AddAction(){
		type = LocalActionType.ADD;
		startTime = null;
		endTime = null;
		description = new String();
		place = new String();
	}
	
	public String toString(){
		return ("Type        : " + type + "\n" +
			    "Start Time  : " + startTime.getTime() + "\n" +
		        "End Time    : " + endTime.getTime() + "\n" +
		        "Description : " + description + "\n" +
		        "Place       : " + place + "\n");
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

	public String getPlace(){
		return place;
	}
	
	public LocalActionType getType(){
		return type;
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

	public void setPlace(String newPlace){
		place = newPlace;
	}
	
}
