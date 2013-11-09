//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;


public class LoginGoogleAction extends GoogleAction{

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
