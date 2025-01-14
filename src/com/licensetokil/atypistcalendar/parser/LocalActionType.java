//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

public enum LocalActionType {
	ADD ("add"), 
	DELETE ("delete"), 
	DISPLAY ("display"), 
	UPDATE ("update"), 
	SEARCH ("search"), 
	MARK ("mark"),
	UNDO ("undo"),
	INVALID ("invalid");
	
	private final String stringActionType;
	
	private LocalActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	public String getString(){
		return stringActionType;
	}
	 
}
