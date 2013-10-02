package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class LocalAction extends Action{
	private ActionType type;
	private Calendar startTime;
	private Calendar endTime;
	private String description;
	private String place;

	public LocalAction(){
		type = null;
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();
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

	public ActionType getType(){
		return type;
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

	public void setType(ActionType newActionType){
		type = newActionType;
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