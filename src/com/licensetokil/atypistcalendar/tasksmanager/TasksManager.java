package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;
import java.io.*;
import java.util.*;

public class TasksManager {

	private LocalAction action;
	private static ArrayList<Schedule> schedule = new ArrayList<Schedule>();
	private static ArrayList<Deadline> deadline = new ArrayList<Deadline>();
	private static ArrayList<Todo> todo = new ArrayList<Todo>();

	private static ArrayList<Schedule> sch = new ArrayList<Schedule>();
	private static ArrayList<Deadline> dl = new ArrayList<Deadline>();
	private static ArrayList<Todo> toDo = new ArrayList<Todo>();

	private static ArrayList<Task> deletedTasks = new ArrayList<Task>();

	private static ArrayList<Task> markUndoList;
	private static Task updateOriginalTask;

	private static LocalAction lastAction;

	private static Hashtable<Integer, Task> table = new Hashtable<Integer, Task>();

	private static int counter = 0;
	private static int count = 1;
	private static int ID;

	private static File file = new File("ATC");

	public void setAction(LocalAction action) {
		this.action = action;
	}

	public LocalAction getAction() {
		return action;
	}

	public ArrayList<Schedule> getSchedule() {
		return schedule;
	}

	public ArrayList<Deadline> getDeadline() {
		return deadline;
	}

	public ArrayList<Todo> getTodo() {
		return todo;
	}

	private static Task classify(AddAction action) {
		Task t;
		if (action.getStartTime() == null) {
			if (action.getEndTime() == null) {
				t = new Todo(++counter, action.getDescription(),
						action.getPlace(), "");
			} else {
				t = new Deadline(++counter, action.getEndTime(),
						action.getDescription(), action.getPlace(), "");
			}
		} else {
			t = new Schedule(++counter, action.getStartTime(),
					action.getEndTime(), action.getDescription(),
					action.getPlace());
		}

		return t;
	}

	private static String add(Task t) {

		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(file));

			if (t.getTaskType().equals("schedule")) {
				schedule.add((Schedule) t);
				w.write(((Schedule) t).toString());
				w.close();
				return "Added " + t.getDescription() + " on "
						+ ((Schedule) t).getStartTime().getTime() + " to "
						+ ((Schedule) t).getEndTime().getTime()
						+ " successfully";
			}

			else if (t.getTaskType().equals("deadline")) {
				deadline.add((Deadline) t);
				w.write(((Deadline) t).toString());
				w.close();
				return "Added " + ((Deadline) t).getDescription() + " by "
						+ ((Deadline) t).getEndTime().getTime()
						+ " successfully";
			}

			else if (t.getTaskType().equals("todo")) {
				todo.add((Todo) t);
				w.write(((Todo) t).toString());
				w.close();
				return "Added " + t.getDescription() + " successfully";
			}

			else {
				w.close();
				return "Add unsuccessful";
			}

		} catch (Exception e) {
			return e.getMessage();
		}

	}

	private static void addUndo() {
		Task t = classify((AddAction) lastAction);
		schedule.remove(t);
		deadline.remove(t);
		todo.remove(t);
	}

	private static String display(DisplayAction ac) {
		sch.clear();
		dl.clear();
		toDo.clear();
		table.clear();

		String output = new String("");
		count = 1;

		assert ac.getDescription().equals("schedules")
				|| ac.getDescription().equals("deadlines")
				|| ac.getDescription().equals("todos")
				|| ac.getDescription().equals("all")
				|| ac.getDescription().equals("");

		switch (ac.getDescription()) {

		case "schedules":
			for (Schedule s : schedule) {
				if (s.getStartTime().after(ac.getStartTime())
						&& s.getEndTime().before(ac.getEndTime())) {
					sch.add(s);
				}
			}
			break;
		case "deadlines":
			for (Deadline d : deadline) {
				if (ac.getStatus().equals("")) {
					if (d.getEndTime().after(ac.getStartTime())
							&& d.getEndTime().before(ac.getEndTime())) {
						dl.add(d);
					}
				} else {
					if (d.getEndTime().after(ac.getStartTime())
							&& d.getEndTime().before(ac.getEndTime())
							&& d.getStatus().equals(ac.getStatus())) {
						dl.add(d);
					}
				}
			}
			break;
		case "todos":
			for (Todo td : todo) {
				if (td.getStatus().equals("")) {
					toDo.add(td);
				} else {
					if (td.getStatus().equals(ac.getStatus())) {
						toDo.add(td);
					}
				}
			}
			break;
		case "all":
			for (Schedule s : schedule) {
				if (s.getStartTime().after(ac.getStartTime())
						&& s.getEndTime().before(ac.getEndTime())) {
					sch.add(s);
				}
			}
			for (Deadline d : deadline) {
				if (d.getEndTime().after(ac.getStartTime())
						&& d.getEndTime().before(ac.getEndTime())) {
					dl.add(d);
				}
			}
			if (ac.getEndTime().get(Calendar.YEAR) == 2099) {
				for (Todo td : todo) {
					toDo.add(td);
				}
			}
			break;

		case "":
			for (Deadline d : deadline) {
				if (d.getEndTime().after(ac.getStartTime())
						&& d.getEndTime().before(ac.getEndTime())
						&& d.getStatus().equals(ac.getStatus())) {
					dl.add(d);
				}
			}
			if (ac.getEndTime().get(Calendar.YEAR) == 2099) {
				for (Todo td : todo) {
					toDo.add(td);
				}
			}
			break;
		}

		if (!sch.isEmpty()) {
			output = "Schedules: \n";
			for (Schedule s : sch) {
				output = output + count + ". " + "Event: " + s.getDescription()
						+ "\n" + "Starting Time: " + s.getStartTime().getTime()
						+ "\n" + "Ending Time: " + s.getEndTime().getTime()
						+ "\n";

				if (!s.getPlace().equals("")) {
					output = output + "Place: " + s.getPlace() + "\n";
				}
				table.put(count, s);
				count++;
			}
		}

		if (!dl.isEmpty()) {
			output = output + "Deadlines: \n";
			for (Deadline d : dl) {
				output = output + count + ". " + "Event: " + d.getDescription()
						+ "\n" + "Due by: " + d.getEndTime() + "\n" + "Place "
						+ d.getPlace() + "\n";
				table.put(count, d);
				count++;
			}
		}

		if (!toDo.isEmpty()) {
			output = output + "Todos: \n";
			for (Todo td : toDo) {
				output = output + count + ". " + "Event: "
						+ td.getDescription() + "\n";
				table.put(count, td);
				count++;
			}
		}

		return output;
	}

	private static String delete(DeleteAction ac) {

		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			BufferedReader r = new BufferedReader(new FileReader(file));
			String currLine;

			for (Integer arr : ac.getReferenceNumber()) {
				Task t = table.get(arr);
				deletedTasks.add(t);

				while ((currLine = r.readLine()) != null) {
					if (!t.toString().equals(currLine)) {
						w.write("");
					}
				}

				if (t instanceof Schedule) {
					schedule.remove(t);
					sch.remove(t);
				}

				else if (t instanceof Deadline) {
					deadline.remove(t);
					dl.remove(t);
				}

				else if (t instanceof Todo) {
					todo.remove(t);
					toDo.remove(t);
				} else {
					w.close();
					r.close();
					return "error";
				}

				w.close();
				r.close();

			}
		} catch (Exception e) {

		}

		return "Deleted " + ac.getReferenceNumber() + " successfully";

	}

	private static void deleteUndo() {
		for (Task t : deletedTasks) {
			if (t instanceof Schedule) {
				schedule.add((Schedule) t);
			} else if (t instanceof Deadline) {
				deadline.add((Deadline) t);
			} else if (t instanceof Todo) {
				todo.add((Todo) t);
			}
		}
	}

	private static String update(UpdateAction ac) {

		Task t = table.get(ac.getReferenceNumber());
		updateOriginalTask = t;
		String updatedTask;
		String originalTask;
		String currLine;
		ID = updateOriginalTask.getUniqueID();

		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			BufferedReader r = new BufferedReader(new FileReader(file));
			originalTask = t.toString();

			if (t instanceof Schedule) {
				((Schedule) t).setStartTime(ac.getUpdatedStartTime());
				((Schedule) t).setEndTime(ac.getUpdatedEndTime());
				((Schedule) t).setPlace(ac.getUpdatedLocationQuery());
				((Schedule) t).setDescription(ac.getUpdatedQuery());
			}

			else if (t instanceof Deadline) {
				((Deadline) t).setEndTime(ac.getUpdatedEndTime());
				((Deadline) t).setPlace(ac.getUpdatedLocationQuery());
				((Deadline) t).setDescription(ac.getUpdatedQuery());
			}

			else if (t instanceof Todo) {
				((Todo) t).setPlace(ac.getUpdatedLocationQuery());
				((Todo) t).setDescription(ac.getUpdatedQuery());
			}

			else {
				w.close();
				r.close();
				return "Update unsuccessful";
			}

			updatedTask = t.toString();

			while ((currLine = r.readLine()) != null) {
				if (currLine.equals(originalTask)) {
					w.write(updatedTask);
				}
			}

			w.close();
			r.close();
		}

		catch (Exception e) {

		}

		return "Update Successful";
	}

	private static void updateUndo() {
		for (Schedule s : schedule) {
			if (s.getUniqueID() == ID) {
				s = (Schedule) updateOriginalTask;
			}
		}
		for (Deadline d : deadline) {
			if (d.getUniqueID() == ID) {
				d = (Deadline) updateOriginalTask;
			}
		}
		for (Todo td : todo) {
			if (td.getUniqueID() == ID) {
				td = (Todo) updateOriginalTask;
			}
		}
	}

	private static String search(SearchAction ac) {
		sch.clear();
		dl.clear();
		toDo.clear();
		table.clear();

		String output = new String("");
		count = 1;

		for (Schedule s : schedule) {
			if (s.getDescription().contains(ac.getQuery())) {
				if (s.getStartTime().after(ac.getStartTime())
						&& s.getEndTime().before(ac.getEndTime())) {
					sch.add(s);
				}
			}
		}

		for (Deadline d : deadline) {
			if (d.getDescription().contains(ac.getQuery())) {
				if (d.getEndTime().after(ac.getStartTime())
						&& d.getEndTime().before(ac.getEndTime())) {
					dl.add(d);
				}
			}
		}

		if (ac.getEndTime().get(Calendar.YEAR) == 2099) {
			for (Todo td : todo) {
				if (td.getDescription().contains(ac.getQuery())) {
					toDo.add(td);
				}
			}
		}

		output = "Search Matches: \n\n";
		if (!sch.isEmpty()) {
			output = output + "Schedules: \n";
			for (Schedule s : sch) {
				output = output + count + ". " + "Event: " + s.getDescription()
						+ "\n" + "Starting Time: " + s.getStartTime().getTime()
						+ "\n" + "Ending Time: " + s.getEndTime().getTime()
						+ "\n";

				if (!s.getPlace().equals("")) {
					output = output + "Place: " + s.getPlace();
				}
				table.put(count, s);
			}
			count++;
		}

		if (!dl.isEmpty()) {
			output = output + "Deadlines: \n";
			for (Deadline d : dl) {
				output = output + count + ". " + "Event: " + d.getDescription()
						+ "\n" + "Due by: " + d.getEndTime() + "\n" + "Place "
						+ d.getPlace();
				table.put(count, d);
			}
			count++;
		}

		if (!toDo.isEmpty()) {
			output = output + "Todos: \n";
			for (Todo td : toDo) {
				output = output + count + ". " + "Event: "
						+ td.getDescription() + "\n";
				table.put(count, td);
			}
			count++;
		}

		return output;
	}

	private static String mark(MarkAction ac) {
		String numbers = "";
		markUndoList = new ArrayList<Task>();
		String originalTask;
		String updatedTask;
		String currLine;

		try {
			
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			BufferedReader r = new BufferedReader(new FileReader(file));
			
			for (Integer num : ac.getReferenceNumber()) {
				Task t = table.get(num);
				originalTask = t.toString();
				markUndoList.add(t);
				numbers = numbers + num + " ";

				if (t instanceof Deadline) {
					((Deadline) t).setStatus(ac.getStatus());
				}

				else if (t instanceof Todo) {
					((Todo) t).setStatus(ac.getStatus());
				}

				else {
					w.close();
					r.close();
					return "Mark Unsuccessful";
				}
				
				updatedTask = t.toString();
				
				while((currLine = r.readLine())!=null){
					if(currLine == originalTask){
						w.write(updatedTask);
					}
				}
				
				w.close();
				r.close();
			}
		} catch (Exception e) {

		}

		return "Marked " + numbers + "as " + ac.getStatus();
	}

	public static void markUndo() {
		String status = ((MarkAction) lastAction).getStatus();
		String newStatus;

		assert status.equals("done") || status.equals("undone");

		if (status.equals("done")) {
			newStatus = "undone";
		} else {
			newStatus = "done";
		}
		for (Task t : markUndoList) {
			if (t instanceof Deadline) {
				((Deadline) t).setStatus(newStatus);
			} else {
				((Todo) t).setStatus(newStatus);
			}
		}
	}

	public static String executeCommand(AddAction ac) {
		Task t = classify(ac);
		lastAction = ac;
		System.out.println(ac.toString());
		return add(t);
	}

	public static String executeCommand(DisplayAction ac) {
		return display(ac);
	}

	public static String executeCommand(SearchAction ac) {
		return search(ac);
	}

	public static String executeCommand(DeleteAction ac) {
		lastAction = ac;
		return delete(ac);
	}

	public static String executeCommand(UpdateAction ac) {
		lastAction = ac;
		return update(ac);
	}

	public static String executeCommand(MarkAction ac) {
		lastAction = ac;
		return mark(ac);
	}

	public static String executeUndo(LocalAction ac) {
		if (ac instanceof AddAction) {
			addUndo();
		} else if (ac instanceof UpdateAction) {
			updateUndo();
		} else if (ac instanceof DeleteAction) {
			deleteUndo();
		} else if (ac instanceof MarkAction) {
			markUndo();
		}

		return "Undo Successful";
	}
}
