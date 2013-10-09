package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class UpdateAction extends LocalAction{
	private String query;
	private Calendar startTime;
	private Calendar endTime;
	private String locationQuery;
	private String updatedQuery;
	private Calendar updatedStartTime;
	private Calendar updatedEndTime;
	private String updatedLocationQuery;
	
	public UpdateAction(){
		type = LocalActionType.UPDATE;
		startTime = null;
		endTime = null;
		query = new String();
		locationQuery = new String();
		updatedStartTime = null;
		updatedEndTime = null;
		updatedQuery = new String();
		updatedLocationQuery = new String();
	}
	
	public String toString(){
		return ("Type        : " + type + "\n" +
			    "Start Time  : " + startTime.getTime() + "\n" +
		        "End Time    : " + endTime.getTime() + "\n" +
		        "Description : " + query + "\n" +
		        "Place       : " + locationQuery + "\n" +
		        "Updated Start Time  : " + updatedStartTime.getTime() + "\n" +
		        "Updated End Time    : " + updatedEndTime.getTime() + "\n" +
		        "Updated Query       : " + updatedQuery + "\n" +
		        "Updated Place       : " + updatedLocationQuery + "\n");
	}
	

	public Calendar getStartTime(){
		return startTime;
	}

	public Calendar getEndTime(){
		return endTime;
	}

	public String getQuery(){
		return query;
	}

	public String getLocationQuery(){
		return locationQuery;
	}
	
	public Calendar getUpdatedStartTime(){
		return updatedStartTime;
	}

	public Calendar getUpdatedEndTime(){
		return updatedEndTime;
	}

	public String getUpdatedDescription(){
		return updatedQuery;
	}

	public String getUpdatedLocationQuery(){
		return updatedLocationQuery;
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

	public void setQuery(String newQuery){
		query = newQuery;
	}

	public void setLocationQuery(String newLocationQuery){
		locationQuery = newLocationQuery;
	}
	public void setUpdatedStartTime(Calendar newUpdatedStartTime){
		updatedStartTime = newUpdatedStartTime;
	}

	public void setUpdatedEndTime(Calendar newUpdatedEndTime){
		updatedEndTime = newUpdatedEndTime;
	}

	public void setUpdatedQuery(String newUpdatedQuery){
		updatedQuery = newUpdatedQuery;
	}

	public void setUpdatedLocationQuery(String newUpdatedLocationQuery){
		updatedLocationQuery = newUpdatedLocationQuery;
	}
	
}
