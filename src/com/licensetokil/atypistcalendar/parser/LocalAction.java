package com.licensetokil.atypistcalendar.parser;

public abstract class LocalAction extends Action{
	protected LocalActionType type;
	
	abstract public LocalActionType getType();
}