package com.licensetokil.atypistcalendar.tasksmanager;

import com.licensetokil.atypistcalendar.parser.*;
import java.io.*;
import java.util.*;

public class TasksManager {

	private static final String ADD_UNSUCCESSFUL = "Add was unsuccessful. Please try again!\n";

	private static final String DELETE_UNSUCCESSFUL = "Delete was unsuccessful. Please try again!\n";
	private static final String DELETE_SUCCESSFUL = "Deleted %s successfully \n";
	private static final String INVALID_NUMBER_INPUT = "Your number input is invalid and out of range. Please try again!";

	private static final String UPDATE_UNSUCCESSFUL = "Update was unsuccessful. Please try again!\n";
	private static final String UPDATE_SUCCESSFUL = "Updated %s to %s successfully\n";

	private static final String MARK_UNSUCCESSFUL = "Mark was unsuccessful. Please try again!\n";
	private static final String MARK_SUCCESSFUL = "Marked %s as %s\n";
	
	private static final String SEARCH_UNFOUND = "No search matches found!";

	private static final String UNDO_DELETE_SUCCESSFUL = "Delete command successfully undone\n";
	private static final String UNDO_MARK_SUCCESSFUL = "Mark command successfully undone\n";
	private static final String UNDO_UPDATE_SUCCESSFUL = "Update command successfully undone\n";
	private static final String UNDO_ADD_SUCCESSFUL = "Add command successfully undone\n";

	private static final String UNDO_DELETE_UNSUCCESSFUL = "Undo Delete command unsuccessful. Please try again!\n";
	private static final String UNDO_MARK_UNSUCCESSFUL = "Undo Mark command unsuccessful. Please try again!\n";
	private static final String UNDO_UPDATE_UNSUCCESSFUL = "Undo Update command unsuccessful. Please try again!\n";
	private static final String UNDO_ADD_UNSUCCESSFUL = "Undo Add command unsuccessful. Please try again!\n";
	
	private static final String UNDONE = "undone";
	private static final String DONE = "done";

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

	public static ArrayList<Task> getAllTasks() {
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
	
	/*private void convertFromDelimiters(){
		for(Task t: getAllTasks()){
			if((t.getDescription()).contains("?a!b$")){
				String newDescription = (t.getDescription()).replaceAll("\\b?a!b$\\b", "@s");
				t.setDescription(newDescription);
			}
			
			if((t.getPlace()).contains("?a!b$")){
				String newPlace = (t.getPlace()).replaceAll("\\b?a!b$\\b", "@s");
				t.setPlace(newPlace);
			}
		}
	}*/

	private void initialize() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currLine;
			String[] data;

			// To import all tasks from file to ArrayList
			while ((currLine = reader.readLine()) != null) {
				System.out.println("Before: " + currLine);
				data = currLine.split("@s");
				System.out.println(Arrays.toString(data));
				if (data[0].equals("Schedule")) {			
					Schedule s = new Schedule(Integer.parseInt(data[1]),
							convertTimeFromStringToCalendar(data[2]),
							convertTimeFromStringToCalendar(data[3]),
							data[4], data[5]);
					schedule.add(s);
					if(data[5].equals(" ")){
						s.setPlace("");
					}
				} else if (data[0].equals("Deadline")) {
					Deadline d = new Deadline(Integer.parseInt(data[1]),
							convertTimeFromStringToCalendar(data[2]),
							data[3], data[4], data[5]);
					deadline.add(d);
					if(data[4].equals(" ")){
						d.setPlace("");
					}
				} else if (data[0].equals("Todo")) {
					Todo td = new Todo(Integer.parseInt(data[1]),
							data[2], data[3], data[4]);
					todo.add(td);
					if(data[3].equals(" ")){
						td.setPlace("");
					}
				} else if (data[0].equals("uniqueID")) {
					uniqueID = Integer.parseInt(data[1]); // the last
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
			
			//convertFromDelimiters();
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
						action.getPlace(), UNDONE);
			} else {
				t = new Deadline(++uniqueID, action.getEndTime(),
						action.getDescription(), action.getPlace(), UNDONE);
			}
		} else {
			t = new Schedule(++uniqueID, action.getStartTime(),
					action.getEndTime(), action.getDescription(),
					action.getPlace());
		}

		return t;
	}

	private String add(Task t) {

		try {
			String output = new String("");

			if (t.getTaskType().equals("schedule")) {
				schedule.add((Schedule) t);

			} else if (t.getTaskType().equals("deadline")) {
				deadline.add((Deadline) t);

			} else if (t.getTaskType().equals("todo")) {
				todo.add((Todo) t);
			}

			output = "Added: \n" + t.outputStringForDisplay();

			lastTaskCreated = t;
			fileSync();

			return output;

		} catch (Exception e) {
			return ADD_UNSUCCESSFUL;
		}

	}
	
	/*private static void checkForDelimiters(Task t){
		if((t.getDescription()).contains("@s")){
			String newDescription = (t.getDescription()).replaceAll("\\b@s\\b", "?a!b$");
			t.setDescription(newDescription);
		}
		
		if((t.getPlace()).contains("@s")){
			String newPlace = (t.getPlace()).replaceAll("\\b@s\\b", "b?a!b$");
			t.setPlace(newPlace);
		}
	}*/

	private String addUndo() {
		try {
			schedule.remove(lastTaskCreated);
			deadline.remove(lastTaskCreated);
			todo.remove(lastTaskCreated);
			fileSync();
		} catch (Exception e) {
			return UNDO_ADD_UNSUCCESSFUL;
		}
		return UNDO_ADD_SUCCESSFUL;
	}

	private String display(DisplayAction ac) {
		sch.clear();
		dl.clear();
		toDo.clear();
		table.clear();

		String output = new String("");

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
			break;
		}

		return displayOutput(output);
	}

	private String displayOutput(String output) {
		int count = 1;

		if (!sch.isEmpty()) {
			output = output + "Schedules: \n";
			for (Schedule s : sch) {
				output = output + count + ". " + s.outputStringForDisplay();
				table.put(count, s);
				count++;
			}
			output = output + "\n";
		}

		if (!dl.isEmpty()) {
			output = output + "Deadlines: \n";
			for (Deadline d : dl) {
				output = output + count + ". " + d.outputStringForDisplay();
				table.put(count, d);
				count++;
			}

			output = output + "\n";
		}

		if (!toDo.isEmpty()) {
			output = output + "Todos: \n";
			for (Todo td : toDo) {
				output = output + count + ". " + td.outputStringForDisplay();
				table.put(count, td);
				count++;
			}

			output = output + "\n";
		}

		return output;
	}

	private String delete(DeleteAction ac) {

		try {
			for (Integer num : ac.getReferenceNumber()) {
				if (num <= table.size() && num > 0) {
					Task t = table.get(num);
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
				} else {
					return INVALID_NUMBER_INPUT;
				}
			}

			fileSync();

		} catch (Exception e) {
			return DELETE_UNSUCCESSFUL;
		}

		return String.format(DELETE_SUCCESSFUL, ac.getReferenceNumber());

	}

	private String deleteUndo() {
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
			return UNDO_DELETE_UNSUCCESSFUL;
		}

		return UNDO_DELETE_SUCCESSFUL;
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
			return UPDATE_UNSUCCESSFUL;
		}

		return String.format(UPDATE_SUCCESSFUL, ac.getReferenceNumber(),
				t.getDescription());
	}

	private String updateUndo() {
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
			return UNDO_UPDATE_UNSUCCESSFUL;
		}
		return UNDO_UPDATE_SUCCESSFUL;
	}

	private String search(SearchAction ac) {
		sch.clear();
		dl.clear();
		toDo.clear();
		table.clear();

		String output = new String("");

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
			output = output + SEARCH_UNFOUND;
		}

		return displayOutput(output);
	}

	private String mark(MarkAction ac) {
		String numbers = "";
		markUndoList = new ArrayList<Task>();

		try {
			for (Integer num : ac.getReferenceNumber()) {
				if (num <= table.size() && num > 0) {
					Task t = table.get(num);
					markUndoList.add(t);
					numbers = numbers + num + " ";

					if (t instanceof Deadline) {
						((Deadline) t).setStatus(ac.getStatus());
					} else if (t instanceof Todo) {
						((Todo) t).setStatus(ac.getStatus());
					}
				}
				else
					return INVALID_NUMBER_INPUT;
			}

			fileSync();

		} catch (Exception e) {
			return MARK_UNSUCCESSFUL;
		}

		return String.format(MARK_SUCCESSFUL, numbers, ac.getStatus());
	}

	public String markUndo() {
		try {
			String status = ((MarkAction) lastAction).getStatus();
			String newStatus;

			if (status.equals(DONE)) {
				newStatus = UNDONE;
			} else {
				newStatus = DONE;
			}
			for (Task t : markUndoList) {
				if (t instanceof Deadline) {
					((Deadline) t).setStatus(newStatus);
				} else {
					((Todo) t).setStatus(newStatus);
				}
			}

			fileSync();
		} catch (Exception e) {
			return UNDO_MARK_UNSUCCESSFUL;
		}

		return UNDO_MARK_SUCCESSFUL;
	}

	public static void fileSync() {
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
				return addUndo();
			} else if (lastAction instanceof UpdateAction) {
				return updateUndo();
			} else if (lastAction instanceof DeleteAction) {
				return deleteUndo();
			} else if (lastAction instanceof MarkAction) {
				return markUndo();
			}
		}
		return "ERROR";

	}

	public static void exit() {
		try {
			/*for(Task t: getAllTasks()){
				checkForDelimiters(t);
			}*/
			
			fileSync();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,
					true));
			
			writer.write("uniqueID@" + uniqueID);
			writer.newLine();
			writer.close();
		} catch (Exception e) {

		}
	}
}
