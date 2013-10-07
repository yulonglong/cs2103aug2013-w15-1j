package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;

import java.util.*;

public class TasksManager {

	private static final int COMMAND_SUCCESSFUL = 1;
	private static final int COMMAND_UNSUCCESSFUL = 0;

	private LocalAction action;
	private ArrayList<Task> memory;
	
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

	public Task classify(LocalAction action) {
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
			Schedule sch = new Schedule(action.getStartTime(),
					action.getEndTime(), action.getDescription(),
					action.getPlace());
			return sch;
		}
	}

	public void add(Task t) {
		memory.add(t);
	}

	public String display(Task t) {
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

	public String executeCommand(LocalAction ac) {
		Task t = classify(ac);

		try {
			switch (ac.getType()) {
			case ADD:
				add(t);
				return "added succesfully";
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
