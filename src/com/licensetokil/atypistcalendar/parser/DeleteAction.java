package com.licensetokil.atypistcalendar.parser;

import java.util.ArrayList;

public class DeleteAction extends LocalAction{
	private ArrayList<Integer> referenceNumber;
	private String remoteTaskId;
	
	public DeleteAction(){
		type = LocalActionType.DELETE;
		referenceNumber = null;
		remoteTaskId = null;
	}
	
	public String toString(){
		return ("Type	: " + type + "\n" +
				"Ref. Num	: " + referenceNumber + "\n" +
				"RemoteTaskID	: " + remoteTaskId + "\n");
	}
	
	public LocalActionType getType(){
		return type;
	}
	
	public ArrayList<Integer> getReferenceNumber(){
		return referenceNumber;
	}
	
	public String getRemoteTaskId(){
		return remoteTaskId;
	}

	public void setReferenceNumber(ArrayList<Integer> newReferenceNumber){
		referenceNumber = newReferenceNumber;
	}
	
	public void setRemoteTaskId(String newRemoteTaskId){
		remoteTaskId = newRemoteTaskId;
	}
}
