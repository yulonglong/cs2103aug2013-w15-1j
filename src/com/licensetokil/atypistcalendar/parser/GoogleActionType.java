package com.licensetokil.atypistcalendar.parser;

public enum GoogleActionType { 
	GCAL ("gcal"), 
	GCAL_SYNC ("gcal sync"), 
	GCAL_QUICK_ADD ("gcal quick add"), 
	INVALID ("invalid");
	
	private final String stringActionType;
	
	private GoogleActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	 public String getString(){
		return stringActionType;
	}
}
