package com.licensetokil.atypistcalendar.parser;

public enum SystemActionType { 
	EXIT ("exit"), 
	INVALID ("invalid");
	
	private final String stringActionType;
	
	private SystemActionType(String newStringActionType) {
		stringActionType = newStringActionType;
	}
	
	 public String getString(){
		return stringActionType;
	}
}
