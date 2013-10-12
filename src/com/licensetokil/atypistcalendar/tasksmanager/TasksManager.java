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

	public static Task classify(AddAction action) {
		Task t;
		if(action.getStartTime()==null){
			if(action.getEndTime()==null){
				t = new Todo(++counter, action.getDescription(), action.getPlace(), "");
			}
			else{
				t = new Deadline(++counter, action.getEndTime(), action.getDescription(), action.getPlace(), "");
			}
		}
		else{
			t = new Schedule(++counter, action.getStartTime(), action.getEndTime(), action.getDescription(), action.getPlace());
		}
		
		return t;
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

	public static String display(DisplayAction ac) {
		ArrayList<Schedule> sch = new ArrayList<Schedule>();
		ArrayList<Deadline> dl = new ArrayList<Deadline>();
		ArrayList<Todo> toDo = new ArrayList<Todo>();
		String output = new String("");
		int count = 1;
		
		switch(ac.getDescription()){
		
		case "schedules": 
			for(Schedule s: schedule){
				if(checkTimeRange(s, ac)){
					sch.add(s);
				}
			} break;
		case "deadlines":
			for (Deadline d: deadline){
				if(ac.getStatus().equals("")){
					if(checkTimeRange(d, ac)){
						dl.add(d);
					}
				}
				else{
					if(checkTimeRange(d, ac) && d.getStatus().equals(ac.getStatus())){
						dl.add(d);
					}
				}
			} break;
		case "todos":
			for (Todo td: todo){
				if(td.getStatus().equals("")){
					toDo.add(td);
				}
				else{
					if(td.getStatus().equals(ac.getStatus())){
						toDo.add(td);
					}
				}
			} break;
		case "all":
			for (Schedule s: schedule){
				if(checkTimeRange(s, ac)){
					sch.add(s);
				}
			}
			for (Deadline d: deadline){
				if(checkTimeRange(d, ac)){
					dl.add(d);
				}
			} 
			if(ac.getEndTime().get(Calendar.YEAR)==2099){
				for(Todo td: todo){
					toDo.add(td);
				}
			}break;
			
		case "": 
			for(Deadline d: deadline){
				if(checkTimeRange(d, ac) && d.getStatus().equals(ac.getStatus())){
					dl.add(d);
				}
			}
			if(ac.getEndTime().get(Calendar.YEAR)==2099){
				for(Todo td: todo){
					toDo.add(td);
				}
			}
			 break;
		}
		
		if(!sch.isEmpty()){
			output = "Schedules: \n";
			for(Schedule s: sch){
				output = output + count + ". " + "Event: " + s.getDescription() + "\n" 
						+ "Starting Time: " + s.getStartTime() + "\n"
						+ "Ending Time: " + s.getEndTime() + "\n"
						+ "Place " + s.getPlace();
			}
		}
		
		if(!dl.isEmpty()){
			output = output + "Deadlines: \n";
			for(Deadline d: dl){
				output = output + count + ". " + "Event: " + d.getDescription() + "\n" 
						+ "Due by: " + d.getEndTime() + "\n"
						+ "Place " + d.getPlace();
			}
		}
		
		if(!toDo.isEmpty()){
			output = output + "Todos: \n";
			for(Todo td: toDo){
				output = output + count + ". " + "Event: " + td.getDescription() + "\n"; 
			}
		}
		
		return output;
	}
	
	public static boolean checkTimeRange(Task t, DisplayAction ac){
		return (t.getStartTime().after(ac.getStartTime()) && t.getEndTime().before(ac.getEndTime()));
	}
	

	public void delete(Task t) {
		
	}

	public void update(Task t) {

	}

	public void search(Task t) {

	}

	public void mark(Task t) {

	}

	public static String executeCommand(AddAction ac) {
		Task t = classify(ac);	
		return add(t);
	}
	
	public static String executeCommand(DisplayAction ac){
		return display(ac);
	}
}
