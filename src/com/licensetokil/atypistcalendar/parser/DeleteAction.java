package com.licensetokil.atypistcalendar.parser;

import java.util.ArrayList;

public class DeleteAction extends LocalAction{
	private ArrayList<Integer> referenceNumber;
	
	public DeleteAction(){
		type = LocalActionType.DELETE;
		referenceNumber = null;
	}
	
	public String toString(){
		return ("Type	: " + type + "\n" +
				"Ref. Num	: " + referenceNumber + "\n");
	}
	
	public LocalActionType getType(){
		return type;
	}
	
	public ArrayList<Integer> getReferenceNumber(){
		return referenceNumber;
	}

	public void setReferenceNumber(ArrayList<Integer> newReferenceNumber){
		referenceNumber = newReferenceNumber;
	}
}
