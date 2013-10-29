package com.licensetokil.atypistcalendar.parser;


public class SyncGoogleAction extends GoogleAction{
	private GoogleActionType type;

	public SyncGoogleAction(){
		type = GoogleActionType.GOOGLE_SYNC;
	}
	
	public String toString(){
		return ("Type	: " + type + "\n");
	}

	public GoogleActionType getType(){
		return type;
	}
}
