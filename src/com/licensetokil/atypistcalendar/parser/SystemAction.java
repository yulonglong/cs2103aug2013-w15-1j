package com.licensetokil.atypistcalendar.parser;

public abstract class SystemAction extends Action{
	protected SystemActionType type;
	
	public abstract SystemActionType getType();
}
