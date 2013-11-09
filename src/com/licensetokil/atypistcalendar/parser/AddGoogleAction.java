//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;


public class AddGoogleAction extends GoogleAction{
	private String userInput;

	public AddGoogleAction(){
		type = GoogleActionType.GOOGLE_ADD;
		userInput = new String();
	}
	
	public String toString(){
		return ("Type	: " + type + "\n" +
		        "userInput	: " + userInput + "\n");
	}

	public GoogleActionType getType(){
		return type;
	}

	public String getUserInput(){
		return userInput;
	}

	public void setUserInput(String newUserInput){
		userInput = newUserInput;
	}
}
