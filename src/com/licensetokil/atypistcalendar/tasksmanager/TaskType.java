package com.licensetokil.atypistcalendar.tasksmanager;

public enum TaskType {
	SCHEDULE("schedule"),
	DEADLINE("deadline"),
	TODO("todo");
	
	private final String strTaskType;
	
	private TaskType(String newStrTaskType){
		strTaskType = newStrTaskType;
	}
	
	public String toString(){
		return strTaskType;
	}
}
