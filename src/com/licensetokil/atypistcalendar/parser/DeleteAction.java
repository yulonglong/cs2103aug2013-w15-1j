package com.licensetokil.atypistcalendar.parser;

import java.util.ArrayList;
import java.util.Calendar;

public class DeleteAction extends LocalAction{
	private ArrayList<Integer> referenceNumber;
	private String query;
	private Calendar startTime;
	private Calendar endTime;
	private String locationQuery;
	
	public DeleteAction(){
		type = LocalActionType.DELETE;
		startTime = null;
		endTime = null;
		query = new String();
		locationQuery = new String();
		referenceNumber = null;
	}
	
	public String toString(){
		return ("Type        : " + type + "\n" +
				"Ref. Num	 : " + referenceNumber + "\n" +
			    "Start Time  : " + startTime.getTime() + "\n" +
		        "End Time    : " + endTime.getTime() + "\n" +
		        "Query       : " + query + "\n" +
		        "Place       : " + locationQuery + "\n");
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
	
	public LocalActionType getType(){
		return type;
	}
	
	public ArrayList<Integer> getReferenceNumber(){
		return referenceNumber;
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
	
	public void setReferenceNumber(ArrayList<Integer> newReferenceNumber){
		referenceNumber = newReferenceNumber;
	}
}
