package com.licensetokil.atypistcalendar.parser;

public enum ActionType {
	ADD ("add"), 
	DELETE ("delete"), 
	DISPLAY ("display"), 
	UPDATE ("update"), 
	SEARCH ("search"), 
	MARK ("mark"), 
	EXIT ("exit"), 
	GCAL ("gcal"), 
	GCAL_SYNC ("gcal sync"), 
	GCAL_QUICK_ADD ("gcal quick add"), 
	INVALID ("invalid");
	
	private final String stringActionType;
	
	ActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	 public String getString(){
		return stringActionType;
	}
}
