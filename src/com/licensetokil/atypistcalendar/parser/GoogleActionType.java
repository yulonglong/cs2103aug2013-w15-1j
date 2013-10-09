package com.licensetokil.atypistcalendar.parser;

public enum GoogleActionType { 
	GCAL ("gcal"), 
	GCAL_SYNC ("gcal sync"), 
	GCAL_QUICK_ADD ("gcal quick add"), 
	EXIT ("exit");
	
	private final String stringActionType;
	
	GoogleActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	 public String getString(){
		return stringActionType;
	}
}