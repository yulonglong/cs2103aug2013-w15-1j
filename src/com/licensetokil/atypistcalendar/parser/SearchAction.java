package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class SearchAction extends LocalAction {
	private String query;
	private Calendar startTime;
	private Calendar endTime;
	private String locationQuery;
	private String status;
	
	public SearchAction(){
		type = LocalActionType.SEARCH;
		startTime = null;
		endTime = null;
		query = new String();
		locationQuery = new String();
		status = new String();
	}
	
	public String toString(){
		return ("Type        : " + type + "\n" +
				"Status      : " + status + "\n" +
			    "Start Time  : " + startTime.getTime() + "\n" +
		        "End Time    : " + endTime.getTime() + "\n" +
		        "Query       : " + query + "\n" +
		        "location    : " + locationQuery + "\n");
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
	
	public String getStatus(){
		return status;
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
	
	public void setStatus(String newStatus){
		status = newStatus;
	}
}
