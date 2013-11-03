//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

public class GoogleAction extends Action{
	private GoogleActionType type;
	private String userInput;

	public GoogleAction(){
		type = null;
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

	public void setType(GoogleActionType newActionType){
		type = newActionType;
	}

	public void setUserInput(String newUserInput){
		userInput = newUserInput;
	}
}
