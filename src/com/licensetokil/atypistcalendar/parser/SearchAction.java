//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class SearchAction extends LocalAction {
	private String query;
	private String taskType;
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
		        "Query	: " + query + "\n" +
		        "location	: " + locationQuery + "\n");
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
	
	public String getTaskType(){
		return taskType;
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
	
	public void setTaskType(String newTaskType){
		taskType = newTaskType;
	}
}
