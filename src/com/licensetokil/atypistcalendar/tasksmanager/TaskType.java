//@author A0103494J
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
//@author A0103494J