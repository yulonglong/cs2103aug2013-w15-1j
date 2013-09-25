package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;

public class Parser {;
	enum ACTION_TYPE {
		ADD, DELETE, DISPLAY, UPDATE, SEARCH, MARK, EXIT, GCAL, INVALID;
	};

	private static String ADD = "add";
	private static String DELETE = "delete";
	private static String DISPLAY = "display";
	private static String UPDATE = "update";
	private static String SEARCH = "search";
	private static String MARK = "mark";
	private static String EXIT = "exit";
	private static String GCAL = "gcal";
	
	private static ACTION_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");
		if (commandTypeString.equalsIgnoreCase(ADD)) {
			return ACTION_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase(DELETE)) {
			return ACTION_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase(DISPLAY)) {
			return ACTION_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase(UPDATE)) {
			return ACTION_TYPE.UPDATE;
		} else if (commandTypeString.equalsIgnoreCase(SEARCH)) {
			return ACTION_TYPE.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(MARK)) {
		 	return ACTION_TYPE.MARK;
		} else if (commandTypeString.equalsIgnoreCase(EXIT)) {
			return ACTION_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase(GCAL)) {
			return ACTION_TYPE.GCAL;
		} else {
			return ACTION_TYPE.INVALID;
		}
	}
	
	
	abstract class Action{
		ACTION_TYPE type;
	}
	class LocalAction extends Action{
		private ACTION_TYPE type;
		private Calendar startTime;
		private Calendar endTime;
		private String description;
		private String place;
		
		public LocalAction(){
			type = null;
			startTime = Calendar.getInstance();
			endTime = Calendar.getInstance();
			description = new String();
			place = new String();
		}
		
		public ACTION_TYPE getType(){
			return type;
		}
		
		public Calendar getStartTime(){
			return startTime;
		}
		
		public Calendar getEndTime(){
			return endTime;
		}
		
		public String getDescription(){
			return description;
		}
		
		public String place(){
			return place;
		}
		
		public void setType(ACTION_TYPE newActionType){
			type = newActionType;
		}
		
		public void setStartTime(Calendar newStartTime){
			startTime = newStartTime;
		}
		
		public void setEndTime(Calendar newEndTime){
			endTime = newEndTime;
		}
		
		public void setDescription(String newDescription){
			description = newDescription;
		}
		
		public void setPlace(String newPlace){
			place = newPlace;
		}
		
	}
	
	Action Parse(String userInput){
		Action userAction = new LocalAction();
		return userAction;
	}
	

}
