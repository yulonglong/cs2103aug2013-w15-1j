package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class UpdateAction extends LocalAction{
	private int referenceNumber;
	private String updatedQuery;
	private Calendar updatedStartTime;
	private Calendar updatedEndTime;
	private String updatedLocationQuery;
	private String remoteTaskId;
	
	public UpdateAction(){
		type = LocalActionType.UPDATE;
		updatedStartTime = null;
		updatedEndTime = null;
		updatedQuery = new String();
		updatedLocationQuery = new String();
		remoteTaskId = null;
	}
	
	public String toString(){
		
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
		        "Updated Start Time	: " + stringUpdatedStartTime + "\n" +
		        "Updated End Time	: " + stringUpdatedEndTime + "\n" +
		        "Updated Query		: " + updatedQuery + "\n" +
		        "Updated Location Query	: " + updatedLocationQuery + "\n" +
				"RemoteTaskID	: " + remoteTaskId + "\n");
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
	
	public String getRemoteTaskId(){
		return remoteTaskId;
	}
	
	public LocalActionType getType(){
		return type;
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
	
	public void setRemoteTaskId(String newRemoteTaskId){
		remoteTaskId = newRemoteTaskId;
	}
}
