package com.licensetokil.atypistcalendar.parser;

import java.util.ArrayList;
import java.util.Calendar;

public class MarkAction extends LocalAction{
	private ArrayList<Integer> referenceNumber;
	private String query;
	private Calendar startTime;
	private Calendar endTime;
	private String locationQuery;
	private String status;
	
	public MarkAction(){
		type = LocalActionType.MARK;
		startTime = null;
		endTime = null;
		query = new String();
		locationQuery = new String();
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
				"status	: " + status + "\n" +
				"Ref. Num	: " + referenceNumber + "\n" +
			    "Start Time	: " + stringStartTime + "\n" +
		        "End Time	: " + stringEndTime + "\n" +
		        "Query	: " + query + "\n" +
		        "Place	: " + locationQuery + "\n");
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
	
	public String getStatus(){
		return status;
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
	
	public void setStatus(String newStatus){
		status = newStatus;
	}
}
