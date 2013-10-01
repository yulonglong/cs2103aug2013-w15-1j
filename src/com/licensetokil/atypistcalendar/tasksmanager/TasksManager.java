package com.licensetokil.atypistcalendar.tasksmanager;
import com.licensetokil.atypistcalendar.parser.*;
import java.util.*;

abstract class Task {
	protected int uniqueID;
	
}

public class TasksManager {
	
	private Parser.LocalAction action;
	private ArrayList<String> memory;
	
	public void TaskManager(Parser.LocalAction action){
		this.action = action;
		memory = new ArrayList<String>();
	}
	
	public void setAction(Parser.LocalAction action){
		this.action = action;
	}
		
	public Parser.LocalAction getAction(){
		return action;
	}
	
	public ArrayList<String> getMemory(){
		return memory;
	}
	
	public void addIntoMemoryAndFile(Parser.LocalAction action){
		this.getMemory().add(action.getType()+"@" + action.getStartTime() + "@" + action.getEndTime() + "@" + action.getDescription() + "@" + action.getPlace());
	}
	
	public String display(Parser.LocalAction ac){
		String str = "";
		for(String e: memory){
			str = str + e + "\n";
		}
		return str;
	}
	
	public void delete(Parser.LocalAction ac){
		
	}
	
	public int executeCommand(Parser.LocalAction ac){
		
		switch (ac.getType()){
		case ADD:
		case DISPLAY: display(ac);
		case DELETE:
		case UPDATE:
		case SEARCH:
		case MARK:
		case EXIT:
		case GCAL:
		case GCAL_SYNC:
		case GCAL_QUICK_ADD:
		}

	}
}
