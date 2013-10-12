package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class UpdateAction extends LocalAction{
	private String query;
	private Calendar startTime;
	private Calendar endTime;
	private String locationQuery;
	private int referenceNumber;
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
		String stringUpdatedStartTime;
		if (updatedStartTime==null){
			stringUpdatedStartTime = new String("null");
		}
		else{
			stringUpdatedStartTime= updatedStartTime.getTime().toString();
		}
		String stringUpdatedEndTime;
		if (updatedEndTime==null){
			stringUpdatedEndTime = new String("null");
		}
		else{
			stringUpdatedEndTime= updatedEndTime.getTime().toString();
		}
		return ("Type	: " + type + "\n" +
				"Ref Number	: " + referenceNumber + "\n" +
			    "Start Time	: " + stringStartTime + "\n" +
		        "End Time	: " + stringEndTime + "\n" +
		        "Query	: " + query + "\n" +
		        "Location Query	: " + locationQuery + "\n" +
		        "Updated Start Time	: " + stringUpdatedStartTime + "\n" +
		        "Updated End Time	: " + stringUpdatedEndTime + "\n" +
		        "Updated Query		: " + updatedQuery + "\n" +
		        "Updated Location Query	: " + updatedLocationQuery + "\n");
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

	public String getUpdatedQuery(){
		return updatedQuery;
	}

	public String getUpdatedLocationQuery(){
		return updatedLocationQuery;
	}

	public int getReferenceNumber(){
		return referenceNumber;
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
	
	public void setReferenceNumber(int newReferenceNumber){
		referenceNumber = newReferenceNumber;
	}
	
}
