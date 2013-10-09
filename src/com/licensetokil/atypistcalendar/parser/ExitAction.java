package com.licensetokil.atypistcalendar.parser;

public class ExitAction extends LocalAction{
	public ExitAction(){
		type = LocalActionType.EXIT;
	}
	public LocalActionType getType(){
		return type;
	}
}
