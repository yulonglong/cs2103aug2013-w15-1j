package com.licensetokil.atypistcalendar.parser;

public class GoogleAction extends Action{
	private ActionType type;
	private String userInput;

	public GoogleAction(){
		type = null;
		userInput = new String();
	}

	public ActionType getType(){
		return type;
	}

	public String getUserInput(){
		return userInput;
	}

	public void setType(ActionType newActionType){
		type = newActionType;
	}

	public void setUserInput(String newUserInput){
		userInput = newUserInput;
	}
}
