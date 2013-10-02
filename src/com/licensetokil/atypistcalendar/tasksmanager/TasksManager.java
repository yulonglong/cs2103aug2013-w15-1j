package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;

import java.util.*;

abstract class Task {
	protected int uniqueID;
	protected String description;

}

public class TasksManager {

	private static final int COMMAND_SUCCESSFUL = 1;
	private static final int COMMAND_UNSUCCESSFUL = 0;

	public class Schedule extends Task {
		private int uniqueID;
		private Calendar startTime;
		private Calendar endTime;
		private String description;
		private String place;

		public Schedule(Calendar startTime, Calendar endTime,
				String description, String place) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.description = description;
			this.place = place;
		}

		public int getUniqueID() {
			return uniqueID;
		}

		public Calendar getStartTime() {
			return startTime;
		}

		public Calendar getEndTime() {
			return endTime;
		}

		public String getDescription() {
			return description;
		}

		public String getPlace() {
			return place;
		}

		public String toString() {
			return "Schedule@" + uniqueID + "@" + startTime.toString() + "@"
					+ endTime.toString() + "@" + description + "@" + place;
		}
	}

	public class Deadline extends Task {
		private int uniqueID;
		private Calendar endTime;
		private String description;
		private String place;

		public Deadline(Calendar endTime, String description, String place) {
			this.endTime = endTime;
			this.description = description;
			this.place = place;
		}

		public int getUniqueID() {
			return uniqueID;
		}

		public Calendar getEndTime() {
			return endTime;
		}

		public String getDescription() {
			return description;
		}

		public String getPlace() {
			return place;
		}

		public String toString() {
			return "Deadline@" + uniqueID + "@" + endTime.toString() + "@"
					+ description + "@" + place;
		}
	}

	public class Todo extends Task {
		private int uniqueID;
		private String description;
		private String place;

		public Todo(String description, String place) {
			this.description = description;
			this.place = place;
		}

		public int getUniqueID() {
			return uniqueID;
		}

		public String getDescription() {
			return description;
		}

		public String getPlace() {
			return place;
		}

		public String toString() {
			return "Todo@" + uniqueID + "@" + description + "@" + place;
		}
	}

	private Parser.LocalAction action;
	private ArrayList<Task> memory;
	
	public TasksManager(Parser.LocalAction action) {
		this.action = action;
		memory = new ArrayList<Task>();
	}

	public void setAction(Parser.LocalAction action) {
		this.action = action;
	}

	public Parser.LocalAction getAction() {
		return action;
	}

	public ArrayList<Task> getMemory() {
		return memory;
	}

	public Task classify(Parser.LocalAction action) {
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

	public String executeCommand(Parser.LocalAction ac) {
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
