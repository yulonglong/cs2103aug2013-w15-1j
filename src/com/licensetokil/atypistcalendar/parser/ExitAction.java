package com.licensetokil.atypistcalendar.parser;

public class ExitAction extends SystemAction{
	public ExitAction(){
		type = SystemActionType.EXIT;
	}
	public SystemActionType getType(){
		return type;
	}
}
