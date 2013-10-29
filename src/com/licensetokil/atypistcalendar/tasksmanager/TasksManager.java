package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;
import java.io.*;
import java.util.*;

public class TasksManager {

	private static final String ADD_UNSUCCESSFUL = "Add was unsuccessful. Please try again!";
	private static final String ADDED_SCHEDULE = "Added Schedule:\nEvent:  %s\nStarting Time: %s\nEnding Time: %s\n";
	private static final String APPEND_PLACE = "Place: %s\n";
	private static final String ADDED_DEADLINE = "Added Deadline\n"
			+ "Event: %s\nDue by: %s\n";
	private static final String ADDED_TODO = "Added\nEvent: %s\n";

	private static TasksManager TM;

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
	private static Task lastTaskCreated;

	private static Hashtable<Integer, Task> table = new Hashtable<Integer, Task>();

	private static int uniqueID = 0;
	private static int count = 1;

	private static File file = new File("ATC.txt");

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

	public ArrayList<Task> getAllTasks() {
		ArrayList<Task> allTasks = new ArrayList<Task>();
		allTasks.addAll(schedule);
		allTasks.addAll(deadline);
		allTasks.addAll(todo);
		return allTasks;
	}

	// Method to access a single instance
	public static TasksManager getInstance() {
		if (TM == null) {
			TM = new TasksManager();
		}
		return TM;
	}

	// private constructor
	private TasksManager() {
		initialize();
	}

	private void initialize() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currLine;
			StringTokenizer st;

			// To import all tasks from file to ArrayList
			while ((currLine = reader.readLine()) != null) {
				st = new StringTokenizer(currLine, "@");
				String token = st.nextToken();
				if (token.equals("Schedule")) {
					Schedule s = new Schedule(Integer.parseInt(st.nextToken()),
							convertTimeFromStringToCalendar(st.nextToken()),
							convertTimeFromStringToCalendar(st.nextToken()),
							st.nextToken(), st.nextToken());
					schedule.add(s);
				} else if (token.equals("Deadline")) {
					Deadline d = new Deadline(Integer.parseInt(st.nextToken()),
							convertTimeFromStringToCalendar(st.nextToken()),
							st.nextToken(), st.nextToken(), st.nextToken());
					deadline.add(d);
				} else if (token.equals("Todo")) {
					Todo td = new Todo(Integer.parseInt(st.nextToken()),
							st.nextToken(), st.nextToken(), st.nextToken());
					todo.add(td);
				} else if (token.equals("uniqueID")) {
					uniqueID = Integer.parseInt(st.nextToken()); // the last
																	// uniqueID
																	// is stored
																	// at the
																	// end of
																	// the file
																	// and hence
																	// needs to
																	// be
																	// retrieved
																	// when the
																	// application
																	// is
																	// reopened.
				}
			}

			fileSync();
			reader.close();
		} catch (Exception e) {

		}
	}

	private Calendar convertTimeFromStringToCalendar(String time) {
		StringTokenizer st = new StringTokenizer(time);
		st.nextToken(); // unused token: day is not stored.

		int month = determineMonth(st.nextToken());
		int date = Integer.parseInt(st.nextToken());

		StringTokenizer stHrAndMin = new StringTokenizer(st.nextToken(), ":");
		int hour = Integer.parseInt(stHrAndMin.nextToken());
		int min = Integer.parseInt(stHrAndMin.nextToken());
		int sec = Integer.parseInt(stHrAndMin.nextToken());

		st.nextToken(); // unused token SGT

		int year = Integer.parseInt(st.nextToken());

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, hour, min, sec);

		return cal;
	}

	// method to get the number that corresponds to the month of the year.
	// returns -1 if the string parameter is not determinable.
	private int determineMonth(String month) {
		switch (month) {
		case "Jan":
			return 0;
		case "Feb":
			return 1;
		case "Mar":
			return 2;
		case "Apr":
			return 3;
		case "May":
			return 4;
		case "Jun":
			return 5;
		case "Jul":
			return 6;
		case "Aug":
			return 7;
		case "Sep":
			return 8;
		case "Oct":
			return 9;
		case "Nov":
			return 10;
		case "Dec":
			return 11;
		}
		return -1;
	}

	// Method that classifies the user add input into three types of task:
	// schedules, deadlines, and todos. Deadlines and Todos are default undone.
	private Task classify(AddAction action) {
		Task t;
		if (action.getStartTime() == null) {
			if (action.getEndTime() == null) {
				t = new Todo(++uniqueID, action.getDescription(),
						action.getPlace(), "undone");
			} else {
				t = new Deadline(++uniqueID, action.getEndTime(),
						action.getDescription(), action.getPlace(), "undone");
			}
		} else {
			t = new Schedule(++uniqueID, action.getStartTime(),
					action.getEndTime(), action.getDescription(),
					action.getPlace());
		}

		return t;
	}

	private String appendIfPlaceNotNull(String output, Task t) {
		if (!t.getPlace().equals("")) {
			output = output + String.format(APPEND_PLACE, t.getPlace());
		}
		return output;
	}

	private String add(Task t) {

		try {
			String output = "";

			if (t.getTaskType().equals("schedule")) {
				schedule.add((Schedule) t);
				output = String.format(ADDED_SCHEDULE, t.getDescription(),
						((Schedule) t).getStartTime().getTime(), ((Schedule) t)
								.getEndTime().getTime());
			} else if (t.getTaskType().equals("deadline")) {
				deadline.add((Deadline) t);
				output = String.format(ADDED_DEADLINE, ((Deadline) t)
						.getDescription(), ((Deadline) t).getEndTime()
						.getTime());
			} else if (t.getTaskType().equals("todo")) {
				todo.add((Todo) t);
				output = String.format(ADDED_TODO, t.getDescription());
			}

			output = appendIfPlaceNotNull(output, t);

			lastTaskCreated = t;
			fileSync();

			return output;

		} catch (Exception e) {
			return ADD_UNSUCCESSFUL;
		}

	}

	private void addUndo() {
		schedule.remove(lastTaskCreated);
		deadline.remove(lastTaskCreated);
		todo.remove(lastTaskCreated);
		fileSync();
	}

	private String display(DisplayAction ac) {
		sch.clear();
		dl.clear();
		toDo.clear();
		table.clear();

		String output = new String("");
		count = 1;

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
				if (ac.getStatus().equals("")) {
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
					if (td.getStatus().equals(ac.getStatus())) {
						toDo.add(td);
					}
				}
			}
			System.out.println(ac.toString());
			break;
		}

		return outputString(output);
	}

	private String outputString(String output) {

		if (!sch.isEmpty()) {
			output = output + "Schedules: \n";
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
			output = output + "\n";
		}

		if (!dl.isEmpty()) {
			output = output + "Deadlines: \n";
			for (Deadline d : dl) {
				output = output + count + ". " + "Event: " + d.getDescription()
						+ "\n" + "Due by: " + d.getEndTime().getTime() + "\n"
						+ "Status: " + d.getStatus() + "\n";

				if (!d.getPlace().equals("")) {
					output = output + "Place: " + d.getPlace() + "\n";
				}
				table.put(count, d);
				count++;
			}

			output = output + "\n";
		}

		if (!toDo.isEmpty()) {
			output = output + "Todos: \n";
			for (Todo td : toDo) {
				output = output + count + ". " + "Event: "
						+ td.getDescription() + "\n" + "Status: "
						+ td.getStatus() + "\n";

				if (!td.getPlace().equals("")) {
					output = output + "Place: " + td.getPlace() + "\n";
				}
				table.put(count, td);
				count++;
			}

			output = output + "\n";
		}

		return output;
	}

	private String delete(DeleteAction ac) {

		try {
			for (Integer arr : ac.getReferenceNumber()) {
				Task t = table.get(arr);
				deletedTasks.add(t);

				if (t instanceof Schedule) {
					schedule.remove(t);
					sch.remove(t);
				} else if (t instanceof Deadline) {
					deadline.remove(t);
					dl.remove(t);
				} else if (t instanceof Todo) {
					todo.remove(t);
					toDo.remove(t);
				}
			}

			fileSync();

		} catch (Exception e) {
			return "Delete was unsuccessful. Please try again \n";
		}

		return "Deleted " + ac.getReferenceNumber() + " successfully \n";

	}

	private void deleteUndo() {
		try {
			for (Task t : deletedTasks) {
				uniqueID--;
				if (t instanceof Schedule) {
					schedule.add((Schedule) t);
				} else if (t instanceof Deadline) {
					deadline.add((Deadline) t);
				} else if (t instanceof Todo) {
					todo.add((Todo) t);
				}
			}
			fileSync();
		} catch (Exception e) {

		}
	}

	private String update(UpdateAction ac) {
		Task t = table.get(ac.getReferenceNumber());

		try {
			if (t instanceof Schedule) {
				updateOriginalTask = new Schedule((Schedule) t);
				((Schedule) t).setStartTime(ac.getUpdatedStartTime());
				((Schedule) t).setEndTime(ac.getUpdatedEndTime());
			} else if (t instanceof Deadline) {
				updateOriginalTask = new Deadline((Deadline) t);
				((Deadline) t).setEndTime(ac.getUpdatedEndTime());
			} else if (t instanceof Todo) {
				updateOriginalTask = new Todo((Todo) t);
			}

			t.setPlace(ac.getUpdatedLocationQuery());
			t.setDescription(ac.getUpdatedQuery());

			fileSync();

		} catch (Exception e) {
			return "Update was unsuccessful. Please try again\n";
		}

		return "Updated " + ac.getReferenceNumber() + " Successfully\n";
	}

	private void updateUndo() {
		try {
			for (Schedule s : schedule) {
				if (s.getUniqueID() == updateOriginalTask.getUniqueID()) {
					s = (Schedule) updateOriginalTask;
				}
			}
			for (Deadline d : deadline) {
				if (d.getUniqueID() == updateOriginalTask.getUniqueID()) {
					d = (Deadline) updateOriginalTask;
				}
			}
			for (Todo td : todo) {
				if (td.getUniqueID() == updateOriginalTask.getUniqueID()) {
					td = (Todo) updateOriginalTask;
				}
			}
			fileSync();
		} catch (Exception e) {

		}
	}

	private String search(SearchAction ac) {
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

		for (Todo td : todo) {
			if (td.getDescription().contains(ac.getQuery())) {
				toDo.add(td);
			}
		}

		output = "Search Matches: \n\n";

		if (todo.isEmpty() && deadline.isEmpty() && schedule.isEmpty()) {
			output = output + "None";
		}

		return outputString(output);
	}

	private String mark(MarkAction ac) {
		String numbers = "";
		markUndoList = new ArrayList<Task>();

		try {
			for (Integer num : ac.getReferenceNumber()) {
				Task t = table.get(num);
				markUndoList.add(t);
				numbers = numbers + num + " ";

				if (t instanceof Deadline) {
					((Deadline) t).setStatus(ac.getStatus());
				} else if (t instanceof Todo) {
					((Todo) t).setStatus(ac.getStatus());
				}
			}

			fileSync();

		} catch (Exception e) {
			return "Mark was unsuccessful. Please try again \n";
		}

		return "Marked " + numbers + "as " + ac.getStatus();
	}

	public void markUndo() {
		String status = ((MarkAction) lastAction).getStatus();
		String newStatus;

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

		fileSync();
	}

	public void fileSync() {
		try {
			BufferedWriter clearWriter = new BufferedWriter(
					new FileWriter(file));
			BufferedWriter appendWriter = new BufferedWriter(new FileWriter(
					file, true));

			for (Schedule s : schedule) {
				appendWriter.write(s.toString());
				appendWriter.newLine();
			}

			for (Deadline d : deadline) {
				appendWriter.write(d.toString());
				appendWriter.newLine();
			}

			for (Todo td : todo) {
				appendWriter.write(td.toString());
				appendWriter.newLine();
			}

			clearWriter.close();
			appendWriter.close();
		} catch (Exception e) {

		}
	}

	public String executeCommand(LocalAction ac) {
		if (ac.getType() == LocalActionType.ADD) {
			Task t = classify((AddAction) ac);
			lastAction = ac;
			System.out.println(ac.toString());
			return add(t);
		}

		else if (ac.getType() == LocalActionType.DISPLAY) {
			return display((DisplayAction) ac);
		}

		else if (ac.getType() == LocalActionType.SEARCH) {
			System.out.println(ac.toString());
			return search((SearchAction) ac);
		}

		else if (ac.getType() == LocalActionType.DELETE) {
			lastAction = ac;
			return delete((DeleteAction) ac);
		}

		else if (ac.getType() == LocalActionType.UPDATE) {
			lastAction = ac;
			return update((UpdateAction) ac);
		}

		else if (ac.getType() == LocalActionType.MARK) {
			lastAction = ac;
			return mark((MarkAction) ac);
		}

		else if (ac.getType() == LocalActionType.UNDO) {

			if (lastAction instanceof AddAction) {
				addUndo();
			} else if (lastAction instanceof UpdateAction) {
				updateUndo();
			} else if (lastAction instanceof DeleteAction) {
				deleteUndo();
			} else if (lastAction instanceof MarkAction) {
				markUndo();
			}

			return "Undo Successful";
		} else {
			return "ERROR";
		}
	}

	public static void exit() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,
					true));
			writer.write("@uniqueID@" + uniqueID);
			writer.newLine();
			writer.close();
		} catch (Exception e) {

		}
	}
}
