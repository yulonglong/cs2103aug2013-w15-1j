package com.licensetokil.atypistcalendar.parser;

public class GoogleAction extends Action{
	private LocalActionType type;
	private String userInput;

	public GoogleAction(){
		type = null;
		userInput = new String();
	}

	public LocalActionType getType(){
		return type;
	}

	public String getUserInput(){
		return userInput;
	}

	public void setType(LocalActionType newActionType){
		type = newActionType;
	}

	public void setUserInput(String newUserInput){
		userInput = newUserInput;
	}
}
