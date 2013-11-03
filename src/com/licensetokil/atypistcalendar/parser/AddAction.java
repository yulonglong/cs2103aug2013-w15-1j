//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class AddAction extends LocalAction {
	private String description;
	private Calendar startTime;
	private Calendar endTime;
	private String place;
	private String remoteTaskId;
	
	public AddAction(){
		type = LocalActionType.ADD;
		startTime = null;
		endTime = null;
		description = new String();
		place = new String();
		remoteTaskId = null;
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
			    "Start Time	: " + stringStartTime + "\n" +
		        "End Time	: " + stringEndTime + "\n" +
		        "Description	: " + description + "\n" +
		        "Place	: " + place + "\n" +
		        "RemoteTaskID	: " + remoteTaskId + "\n");
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

	public String getRemoteTaskId(){
		return remoteTaskId;
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
	
	public void setRemoteTaskId(String newRemoteTaskId){
		remoteTaskId = newRemoteTaskId;
	}
	
}
