package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class UpdateAction extends LocalAction{
	private String description;
	private Calendar startTime;
	private Calendar endTime;
	private String place;
	private String updatedDescription;
	private Calendar updatedStartTime;
	private Calendar updatedEndTime;
	private String updatedPlace;
	
	public UpdateAction(){
		type = LocalActionType.UPDATE;
		startTime = null;
		endTime = null;
		description = new String();
		place = new String();
		updatedStartTime = null;
		updatedEndTime = null;
		updatedDescription = new String();
		updatedPlace = new String();
	}
	
	public String toString(){
		return ("Type        : " + type + "\n" +
			    "Start Time  : " + startTime.getTime() + "\n" +
		        "End Time    : " + endTime.getTime() + "\n" +
		        "Description : " + description + "\n" +
		        "Place       : " + place + "\n" +
		        "Updated Start Time  : " + updatedStartTime.getTime() + "\n" +
		        "Updated End Time    : " + updatedEndTime.getTime() + "\n" +
		        "Updated Description : " + updatedDescription + "\n" +
		        "Updated Place       : " + updatedPlace + "\n");
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
	
	public Calendar getUpdatedStartTime(){
		return updatedStartTime;
	}

	public Calendar getUpdatedEndTime(){
		return updatedEndTime;
	}

	public String getUpdatedDescription(){
		return updatedDescription;
	}

	public String getUpdatedPlace(){
		return updatedPlace;
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
	public void setUpdatedStartTime(Calendar newUpdatedStartTime){
		updatedStartTime = newUpdatedStartTime;
	}

	public void setUpdatedEndTime(Calendar newUpdatedEndTime){
		updatedEndTime = newUpdatedEndTime;
	}

	public void setUpdatedDescription(String newUpdatedDescription){
		updatedDescription = newUpdatedDescription;
	}

	public void setUpdatedPlace(String newUpdatedPlace){
		updatedPlace = newUpdatedPlace;
	}
	
}
