package com.licensetokil.atypistcalendar.parser;

public enum GoogleActionType { 
	GOOGLE ("google"), 
	GOOGLE_SYNC ("google sync"),
	GOOGLE_LOGIN ("google login"), 
	GOOGLE_LOGOUT ("google logout"), 
	GOOGLE_ADD ("google add"), 
	INVALID ("invalid");
	
	private final String stringActionType;
	
	private GoogleActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	 public String getString(){
		return stringActionType;
	}
}
