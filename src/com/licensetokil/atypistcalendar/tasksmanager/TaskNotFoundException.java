package com.licensetokil.atypistcalendar.tasksmanager;

public class TaskNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TaskNotFoundException(){
		
	}
	
	public TaskNotFoundException(String message){
		super(message);
	}
}
