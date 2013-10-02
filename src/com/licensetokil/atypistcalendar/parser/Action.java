package com.licensetokil.atypistcalendar.parser;

public abstract class Action{
	ActionType type;

	abstract ActionType getType();
	abstract void setType (ActionType newActionType);
}
