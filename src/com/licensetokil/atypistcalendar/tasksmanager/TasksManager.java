package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;

import java.util.*;

public class TasksManager {

	private LocalAction action;
	private static ArrayList<Schedule> schedule = new ArrayList<Schedule>();
	private static ArrayList<Deadline> deadline = new ArrayList<Deadline>();
	private static ArrayList<Todo> todo = new ArrayList<Todo>();
	
	private static int counter = 0;

	public void setAction(LocalAction action) {
		this.action = action;
	}

	public LocalAction getAction() {
		return action;
	}

	public ArrayList<Schedule> getSchedule() {
		return schedule;
	}
	
	public ArrayList<Deadline> getDeadline(){
		return deadline;
	}
	
	public ArrayList<Todo> getTodo(){
		return todo;
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
		
		if(t.getTaskType().equals("schedule")){
			schedule.add((Schedule)t);
			return "added " + t.getDescription() +" on " + t.getStartTime().getTime() + " to " + t.getEndTime().getTime() +" successfully";
		}
		
		else if(t.getTaskType().equals("deadline")){
			deadline.add((Deadline)t);
			return "added " + t.getDescription() + " by " + t.getEndTime().getTime() + " successfully";
		}
		
		else if(t.getTaskType().equals("todo")){
			todo.add((Todo)t);
			return "added " + t.getDescription() + " successfully";
		}
		
		else{
			return "add unsuccessful";
		}

	}

	public static String display(Task t) {
		String message = "";
		if(t.getDescription().equals("")){
			for(Schedule s: schedule){
				message = message + s.toString().replaceAll("@", " ") + "\n";
			}
			for(Deadline d: deadline){
				message = message + d.toString().replaceAll("@", " ") + "\n";
			}
			for(Todo td: todo){
				message = message + td.toString().replaceAll("@", " ") + "\n";
			}
			return message;
		}
		
		else if(t.getDescription().equalsIgnoreCase("schedules")){
			for(Schedule s: schedule){
				message = message + s.toString().replaceAll("@", " ") + "\n";
			}
			return message;
		}
		
		else if(t.getDescription().equalsIgnoreCase("deadlines")){
			for(Deadline d: deadline){
				message = message + d.toString().replaceAll("@", " ") + "\n";
			}
			return message;
		}
		
		else if(t.getDescription().equalsIgnoreCase("todos")){
			for(Todo td: todo){
				message = message + td.toString().replaceAll("@", " ") + "\n";
			}
			return message;
		}
		else
			return "error";	

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
