//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

import java.util.ArrayList;

public class MarkAction extends LocalAction{
	private ArrayList<Integer> referenceNumber;
	private String status;
	
	public MarkAction(){
		type = LocalActionType.MARK;
		status = new String();
	}
	
	public String toString(){
		return ("Type	: " + type + "\n" +
				"status	: " + status + "\n" +
				"Ref. Num	: " + referenceNumber + "\n");
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

	public void setReferenceNumber(ArrayList<Integer> newReferenceNumber){
		referenceNumber = newReferenceNumber;
	}
	
	public void setStatus(String newStatus){
		status = newStatus;
	}
}
