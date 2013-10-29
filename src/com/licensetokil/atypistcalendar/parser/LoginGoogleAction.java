package com.licensetokil.atypistcalendar.parser;


public class LoginGoogleAction extends GoogleAction{
	private GoogleActionType type;

	public LoginGoogleAction(){
		type = GoogleActionType.GOOGLE_LOGIN;
	}
	
	public String toString(){
		return ("Type	: " + type + "\n");
	}

	public GoogleActionType getType(){
		return type;
	}
}
