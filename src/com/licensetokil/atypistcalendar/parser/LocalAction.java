package com.licensetokil.atypistcalendar.parser;

public abstract class LocalAction extends Action{
	protected LocalActionType type;
	
	public abstract LocalActionType getType();
}