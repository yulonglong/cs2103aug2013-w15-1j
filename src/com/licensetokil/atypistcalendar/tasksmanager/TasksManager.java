package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;

import java.util.*;

public class TasksManager {

	private LocalAction action;
	private static ArrayList<Schedule> schedule = new ArrayList<Schedule>();
	private static ArrayList<Deadline> deadline = new ArrayList<Deadline>();
	private static ArrayList<Todo> todo = new ArrayList<Todo>();
	
	private static ArrayList<Schedule> sch = new ArrayList<Schedule>();
	private static ArrayList<Deadline> dl = new ArrayList<Deadline>();
	private static ArrayList<Todo> toDo = new ArrayList<Todo>();
	
	private static Hashtable<Integer, Task> table = new Hashtable<Integer, Task>();
	
	private static int counter = 0;
	private static int count = 1;

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
		sch.clear();
		dl.clear();
		toDo.clear();
		table.clear();
		
		String output = new String("");
		count = 1;
		
		switch(ac.getDescription()){
		
		case "schedules": 
			for(Schedule s: schedule){
				if(s.getStartTime().after(ac.getStartTime()) && s.getEndTime().before(ac.getEndTime())){
					sch.add(s);
				}
			} break;
		case "deadlines":
			for (Deadline d: deadline){
				if(ac.getStatus().equals("")){
					if(d.getEndTime().after(ac.getStartTime()) && d.getEndTime().before(ac.getEndTime())){
						dl.add(d);
					}
				}
				else{
					if(d.getEndTime().after(ac.getStartTime()) && d.getEndTime().before(ac.getEndTime()) && d.getStatus().equals(ac.getStatus())){
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
				if(s.getStartTime().after(ac.getStartTime()) && s.getEndTime().before(ac.getEndTime())){
					sch.add(s);
				}
			}
			for (Deadline d: deadline){
				if(d.getEndTime().after(ac.getStartTime()) && d.getEndTime().before(ac.getEndTime())){
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
				if(d.getEndTime().after(ac.getStartTime()) && d.getEndTime().before(ac.getEndTime()) && d.getStatus().equals(ac.getStatus())){
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
				table.put(count, s);
			}
			count++;
		}
		
		if(!dl.isEmpty()){
			output = output + "Deadlines: \n";
			for(Deadline d: dl){
				output = output + count + ". " + "Event: " + d.getDescription() + "\n" 
						+ "Due by: " + d.getEndTime() + "\n"
						+ "Place " + d.getPlace();
				table.put(count, d);
			}
			count++;
		}
		
		if(!toDo.isEmpty()){
			output = output + "Todos: \n";
			for(Todo td: toDo){
				output = output + count + ". " + "Event: " + td.getDescription() + "\n"; 
				table.put(count, td);
			}
			count++;
		}
		
		return output;
	}
	
	public static String delete(DeleteAction ac) {
		
		for(Integer arr: ac.getReferenceNumber()){
			Task t = table.get(arr);
			
			if(t instanceof Schedule){
				schedule.remove(t);
				sch.remove(t);
			}
			
			else if(t instanceof Deadline){
				deadline.remove(t);
				dl.remove(t);
			}
			
			else if(t instanceof Todo){
				todo.remove(t);
				toDo.remove(t);
			}
			else{
				return "error";
			}				
		}
		
		return "delete successful";
		
	}

	public void update(UpdateAction ac) {
		
		Task t = table.get(ac.getReferenceNumber());
		
		if(t instanceof Schedule){
			((Schedule) t).setStartTime(ac.getUpdatedStartTime());
			((Schedule) t).setEndTime(ac.getUpdatedEndTime());
			((Schedule) t).setPlace(ac.getUpdatedLocationQuery());
			((Schedule) t).setDescription(ac.getUpdatedQuery());
			
			
		}
	}

	public static String search(SearchAction ac) {
		sch.clear();
		dl.clear();
		toDo.clear();
		table.clear();
		
		String output = new String("");
		count = 1;
		
		for(Schedule s: schedule){
			if(s.getDescription().contains(ac.getQuery())){
				if(s.getStartTime().after(ac.getStartTime()) && s.getEndTime().before(ac.getEndTime())){
					sch.add(s);
				}
			}
		}
		
		for(Deadline d: deadline){
			if(d.getDescription().contains(ac.getQuery())){
				if(d.getEndTime().after(ac.getStartTime()) && d.getEndTime().before(ac.getEndTime())){
					dl.add(d);
				}
			}
		}
		
		if(ac.getEndTime().get(Calendar.YEAR)==2099){
			for(Todo td: todo){
				if(td.getDescription().contains(ac.getQuery())){
					toDo.add(td);
				}
			}
		}
		
		output = "Search Matches: \n\n";
		if(!sch.isEmpty()){
			output = output + "Schedules: \n";
			for(Schedule s: sch){
				output = output + count + ". " + "Event: " + s.getDescription() + "\n" 
						+ "Starting Time: " + s.getStartTime() + "\n"
						+ "Ending Time: " + s.getEndTime() + "\n"
						+ "Place " + s.getPlace();
				table.put(count, s);
			}
			count++;
		}
		
		if(!dl.isEmpty()){
			output = output + "Deadlines: \n";
			for(Deadline d: dl){
				output = output + count + ". " + "Event: " + d.getDescription() + "\n" 
						+ "Due by: " + d.getEndTime() + "\n"
						+ "Place " + d.getPlace();
				table.put(count, d);
			}
			count++;
		}
		
		if(!toDo.isEmpty()){
			output = output + "Todos: \n";
			for(Todo td: toDo){
				output = output + count + ". " + "Event: " + td.getDescription() + "\n"; 
				table.put(count, td);
			}
			count++;
		}
		
		return output;
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
	
	public static String executeCommand(SearchAction ac){
		return search(ac);
	}
	
	public static String executeCommand(DeleteAction ac){
		return delete(ac);
	}
	
	public static void executeCommand(UpdateAction ac){
		
	}
}
