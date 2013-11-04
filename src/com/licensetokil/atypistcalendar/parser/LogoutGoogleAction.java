//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;


public class LogoutGoogleAction extends GoogleAction{
	private GoogleActionType type;

	public LogoutGoogleAction(){
		type = GoogleActionType.GOOGLE_LOGOUT;
	}
	
	public String toString(){
		return ("Type	: " + type + "\n");
	}

	public GoogleActionType getType(){
		return type;
	}
}
