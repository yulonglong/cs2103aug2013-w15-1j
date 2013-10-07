package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;

import java.util.*;

public class TasksManager {

	private LocalAction action;
	private static ArrayList<Task> memory = new ArrayList<Task>();
	private static int counter = 0;
	
	public TasksManager(LocalAction action) {
		this.action = action;
		memory = new ArrayList<Task>();
	}

	public void setAction(LocalAction action) {
		this.action = action;
	}

	public LocalAction getAction() {
		return action;
	}

	public ArrayList<Task> getMemory() {
		return memory;
	}

	public static Task classify(LocalAction action) {
		if (action.getStartTime() == null) {
			if (action.getEndTime() == null) {
				Todo td = new Todo(action.getDescription(), action.getPlace());
				return td;
			} else {
				Deadline dl = new Deadline(action.getEndTime(),
						action.getDescription(), action.getPlace());
				return dl;
			}
		} else {
			Schedule sch = new Schedule(counter++, action.getStartTime(),
					action.getEndTime(), action.getDescription(),
					action.getPlace());
			return sch;
		}
	}

	public static String add(Task t) {
		//System.out.println(t.toString());
		memory.add(t);
		
		if(t.getTaskType().equals("schedule")){
			return "added " + t.getDescription() +" on " + t.getStartTime().getTime() + " to " + t.getEndTime().getTime() +" successfully";
		}
		
		else if(t.getTaskType().equals("deadline")){
			return "added " + t.getDescription() + " by " + t.getEndTime().getTime() + " successfully";
		}
		
		else if(t.getTaskType().equals("todo")){
			return "added " + t.getDescription() + " successfully";
		}
		
		else{
			return "error";
		}

	}

	public static String display(Task t) {
		String str = "";
		for (Task task : memory) {
			str = str + (task.toString()).replaceAll("@", " ") + "\n";
		}
		return str;
	}

	public void delete(Task t) {

	}

	public void update(Task t) {

	}

	public void search(Task t) {

	}

	public void mark(Task t) {

	}

	public static String executeCommand(LocalAction ac) {
		Task t = classify(ac);

		try {
			switch (ac.getType()) {
			case ADD:

				return add(t);			
				//break;
			case DISPLAY:
				return display(t);
				//break;
			case DELETE:
			case UPDATE:
			case SEARCH:
			case MARK:
			case EXIT:
			case GCAL:
			case GCAL_SYNC:
			case GCAL_QUICK_ADD:
			case INVALID:
			}
			//return COMMAND_SUCCESSFUL;
		} catch (Exception e) {
			//return COMMAND_UNSUCCESSFUL;
		}
		return "error";
	}
}
