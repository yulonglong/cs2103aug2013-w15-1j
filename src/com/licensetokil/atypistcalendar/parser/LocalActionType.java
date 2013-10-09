package com.licensetokil.atypistcalendar.parser;

public enum LocalActionType {
	ADD ("add"), 
	DELETE ("delete"), 
	DISPLAY ("display"), 
	UPDATE ("update"), 
	SEARCH ("search"), 
	MARK ("mark");
	
	private final String stringActionType;
	
	LocalActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	 public String getString(){
		return stringActionType;
	}
}
