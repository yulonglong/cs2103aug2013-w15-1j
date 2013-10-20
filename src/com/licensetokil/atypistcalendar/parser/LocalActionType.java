package com.licensetokil.atypistcalendar.parser;

public enum LocalActionType {
	ADD ("add"), 
	DELETE ("delete"), 
	DISPLAY ("display"), 
	UPDATE ("update"), 
	SEARCH ("search"), 
	MARK ("mark"),
	INVALID ("invalid");
	
	private final String stringActionType;
	
	private LocalActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	public String getString(){
		return stringActionType;
	}
	 
}
