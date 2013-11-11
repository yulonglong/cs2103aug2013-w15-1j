package com.licensetokil.atypistcalendar.tasksmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.licensetokil.atypistcalendar.parser.AddAction;
import com.licensetokil.atypistcalendar.parser.DeleteAction;
import com.licensetokil.atypistcalendar.parser.DisplayAction;
import com.licensetokil.atypistcalendar.parser.LocalAction;
import com.licensetokil.atypistcalendar.parser.LocalActionType;
import com.licensetokil.atypistcalendar.parser.MarkAction;
import com.licensetokil.atypistcalendar.parser.SearchAction;
import com.licensetokil.atypistcalendar.parser.UpdateAction;

/**
 * 
 * @author Fan Yuxin Lacie A0103494J
 * 
 */
public class TasksManager {

	private static Logger logger = Logger.getLogger("TasksManager");

	private static final String ADD_WARNING_CLASH = "Warning: schedule clashes with the following event(s):\n";
	private static final String ADD_PREFIX = "Added: \n";

	// Strings for display function
	private static final String DISPLAY_NO_MATCHES = "No display matches! Nothing to be displayed\n\n";
	private static final String DISPLAY_TYPE_SCHEDULE = "schedules";
	private static final String DISPLAY_TYPE_DEADLINE = "deadlines";
	private static final String DISPLAY_TYPE_TODO = "todos";
	private static final String DISPLAY_TYPE_ALL = "all";
	private static final String DISPLAY_TYPE_EMPTY = "";
	private static final String DISPLAY_SCHEDULE_PREFIX = "Schedules: \n";
	private static final String DISPLAY_DEADLINE_PREFIX = "Deadlines: \n";
	private static final String DISPLAY_TODO_PREFIX = "Todos: \n";
	private static final String DISPLAY_ALIGNMENT_DOT = ". ";
	private static final String DISPLAY_NONALIGNMENT_DOT = ".";

	private static final String DELETE_SUCCESSFUL = "Deleted %s successfully \n\n";
	private static final String DELETE_ALL = "Deleted all successfully\n\n";
	private static final String INVALID_NUMBER_INPUT = "Your number input is invalid and out of range. Please try again!\n";

	// Strings for update function
	private static final String UPDATE_TYPE_CLASH = "Update was unsuccessful due to clash in event type. Please try again!\n\n";
	private static final String UPDATE_SUCCESSFUL = "Updated %s to %s successfully\n\n";
	private static final String UPDATE_WARNING_CLASH = "Warning: The following events clashes after update:\n";

	private static final String MARK_SUCCESSFUL = "Marked %s as %s\n\n";
	private static final String MARK_SCHEDULE_ERROR = "The event that you are trying to mark is a schedule. Please try again!\n\n";
	private static final String MARK_UNDONE = "undone";

	// Strings for search function
	private static final String SEARCH_UNFOUND = "No search matches found!\n\n";
	private static final String SEARCH_PREFIX = "Search Matches: \n\n";

	// Strings for undo function
	private static final String UNDO_DELETE_SUCCESSFUL = "Delete command successfully undone\n\n";
	private static final String UNDO_MARK_SUCCESSFUL = "Mark command successfully undone\n\n";
	private static final String UNDO_UPDATE_SUCCESSFUL = "Update command successfully undone\n\n";
	private static final String UNDO_ADD_SUCCESSFUL = "Add command successfully undone\n\n";

	private static final String UNDO_DISALLOWED = "Undo command is not allowed\n\n";

	// Miscellaneous Strings
	private static final String EMPTY_STRING = "";
	private static final String BLANK_SPACE = " ";
	private static final String NULL_STRING = "null";
	private static final String DELIMITER = "@s";
	private static final String NEWLINE = "\n";
	private static final String UNIQUEID = "uniqueId";

	private static final String ERROR_MESSAGE = "Error in executing your command. Please try again! \n\n";

	private static TasksManager TM;

	private static ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();
	private static ArrayList<Deadline> allDeadlines = new ArrayList<Deadline>();
	private static ArrayList<Todo> allTodos = new ArrayList<Todo>();

	private static ArrayList<Schedule> requestedSchedules = new ArrayList<Schedule>();
	private static ArrayList<Deadline> requestedDeadlines = new ArrayList<Deadline>();
	private static ArrayList<Todo> requestedTodos = new ArrayList<Todo>();

	private static ArrayList<Task> deletedTasks = new ArrayList<Task>();

	private static ArrayList<Task> markUndoList;
	private static Task updateOriginalTask;

	private static LocalAction lastAction;
	private static Task lastTaskCreated;

	private static Hashtable<Integer, Task> selectedTasks = new Hashtable<Integer, Task>();

	private static int uniqueId = 0;

	private static File file = new File("ATC.txt");

	// private constructor
	private TasksManager() {
	}

	// Method to access a single instance
	public static TasksManager getInstance() {
		if (TM == null) {
			TM = new TasksManager();
		}
		return TM;
	}

	// function for google calendar
	public ArrayList<Task> cloneAllTasks() {
		logger.log(Level.INFO, "In cloneAllTasks");
		ArrayList<Task> allTasks = new ArrayList<Task>();

		for (Schedule s : allSchedules) {
			logger.log(Level.INFO, "cloning schedule " + s);
			allTasks.add((Schedule) s.clone());
		}

		for (Deadline d : allDeadlines) {
			logger.log(Level.INFO, "cloning deadline " + d);
			allTasks.add((Deadline) d.clone());
		}

		for (Todo t : allTodos) {
			logger.log(Level.INFO, "cloning todo " + t);
			allTasks.add((Todo) t.clone());
		}

		return allTasks;
	}
	
	public void initialize() {
		try {
			logger.log(Level.INFO,
					"Initialising files and transferring data from files to temporary memory");
	
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String currLine;
			String[] fileData;
	
			// To import all tasks from file to ArrayList
			while ((currLine = reader.readLine()) != null) {
				fileData = currLine.split(DELIMITER);
				if (fileData[0].equals(TaskType.SCHEDULE.toString())) {
					Schedule s = new Schedule(Integer.parseInt(fileData[1]),
							convertTimeFromStringToCalendar(fileData[2]),
							convertTimeFromStringToCalendar(fileData[3]),
							fileData[4], fileData[5],
							convertTimeFromStringToCalendar(fileData[6]));
					allSchedules.add(s);
	
					logger.log(Level.INFO,
							"Added schedule into temporary memory");
					if (fileData[5].equals(BLANK_SPACE)) {
						logger.log(Level.INFO,
								"No place indicated. Setting place to empty string");
						s.setPlace(EMPTY_STRING);
	
					}
					if (fileData[7].equals(NULL_STRING)) {
						logger.log(Level.INFO,
								"No remote ID for schedule indicated. Setting remoteId to null");
						s.setRemoteId(null);
	
					} else {
						s.setRemoteId(fileData[7]);
					}
				} else if (fileData[0].equals(TaskType.DEADLINE.toString())) {
					Deadline d = new Deadline(Integer.parseInt(fileData[1]),
							convertTimeFromStringToCalendar(fileData[2]),
							fileData[3], fileData[4], fileData[5],
							convertTimeFromStringToCalendar(fileData[6]));
					allDeadlines.add(d);
					logger.log(Level.INFO,
							"Added deadline into temporary memory");
					if (fileData[4].equals(BLANK_SPACE)) {
						logger.log(Level.INFO,
								"No place indicated. Setting place to empty string");
						d.setPlace(EMPTY_STRING);
	
					}
					if (fileData[7].equals(NULL_STRING)) {
						logger.log(Level.INFO,
								"No remote ID for deadline indicated. Setting remoteId to null");
						d.setRemoteId(null);
					} else {
						logger.log(Level.INFO, "setting remote ID");
						d.setRemoteId(fileData[7]);
					}
				} else if (fileData[0].equals(TaskType.TODO.toString())) {
					Todo td = new Todo(Integer.parseInt(fileData[1]),
							fileData[2], fileData[3], fileData[4],
							convertTimeFromStringToCalendar(fileData[5]));
					allTodos.add(td);
					logger.log(Level.INFO, "Added todo into temporary memory");
					if (fileData[3].equals(BLANK_SPACE)) {
						logger.log(Level.INFO,
								"No place indicated. Setting place to empty string");
						td.setPlace(EMPTY_STRING);
	
					}
					if (fileData[6].equals(NULL_STRING)) {
						logger.log(Level.INFO,
								"No remote ID for deadline indicated. Setting remoteId to null");
						td.setRemoteId(null);
					} else {
						logger.log(Level.INFO, "setting remote ID");
						td.setRemoteId(fileData[6]);
					}
				} else if (fileData[0].equals(UNIQUEID)) {
					uniqueId = Integer.parseInt(fileData[1]); // the last
																// uniqueId
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
					logger.log(Level.INFO, "Tracking the last uniqueID");
				}
			}
			fileSync();
			reader.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception detected");
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void updateCorrespondingTaskRemoteId(int uniqueId, String remoteId)
			throws TaskNotFoundException {
		logger.log(Level.INFO, "In updateCorrespondingTaskRemoteId function");
		Task t = findTaskFromUniqueId(uniqueId);
		t.setRemoteId(remoteId);
	}

	public String executeCommand(LocalAction ac) {
		logger.log(Level.INFO, "In executeCommand function");
	
		if (ac.getType() == LocalActionType.ADD) {
			logger.log(Level.INFO, "Command identified as add");
			Task t = classify((AddAction) ac);
			lastAction = ac;
			return add(t);
		}
	
		else if (ac.getType() == LocalActionType.DISPLAY) {
			logger.log(Level.INFO, "Command identified as display");
			return display((DisplayAction) ac);
		}
	
		else if (ac.getType() == LocalActionType.SEARCH) {
			logger.log(Level.INFO, "Command identified as search");
			return search((SearchAction) ac);
		}
	
		else if (ac.getType() == LocalActionType.DELETE) {
			logger.log(Level.INFO, "Command identified as delete");
			String output = delete((DeleteAction) ac);
			if (!output.equals(INVALID_NUMBER_INPUT)) {
				lastAction = ac;
			}
			return output;
		}
	
		else if (ac.getType() == LocalActionType.UPDATE) {
			logger.log(Level.INFO, "Command identified as update");
			lastAction = ac;
			return update((UpdateAction) ac);
		}
	
		else if (ac.getType() == LocalActionType.MARK) {
			logger.log(Level.INFO, "Command identified as mark");
			String output = mark((MarkAction) ac);
			if (!output.equals(INVALID_NUMBER_INPUT)
					&& !output.equals(MARK_SCHEDULE_ERROR)) {
				lastAction = ac;
			}
			return output;
		}
	
		else if (ac.getType() == LocalActionType.UNDO) {
			logger.log(Level.INFO, "Command identified as undo");
			if (lastAction instanceof AddAction) {
				lastAction = ac;
				return addUndo();
			} else if (lastAction instanceof UpdateAction) {
				lastAction = ac;
				return updateUndo();
			} else if (lastAction instanceof DeleteAction) {
				lastAction = ac;
				return deleteUndo();
			} else if (lastAction instanceof MarkAction) {
				lastAction = ac;
				return markUndo();
			} else {
				return UNDO_DISALLOWED;
			}
		}
		return ERROR_MESSAGE;
	
	}

	public Todo addTodoFromGoogle(String description, String location,
			Calendar lastModifiedDate, String remoteId) {
		logger.log(Level.INFO, "In addTodoFromGoogle function");
		Todo todo = new Todo(++uniqueId, description, location, MARK_UNDONE,
				lastModifiedDate);
		todo.setRemoteId(remoteId);
		add(todo);
		return todo;
	}

	public Deadline addDeadlineFromGoogle(Calendar endTime, String description,
			String location, Calendar lastModifiedDate, String remoteId) {
		logger.log(Level.INFO, "In addDeadlineFromGoogle function");
		Deadline deadline = new Deadline(++uniqueId, endTime, description,
				location, MARK_UNDONE, lastModifiedDate);
		deadline.setRemoteId(remoteId);
		add(deadline);
		return deadline;
	}

	public Schedule addScheduleFromGoogle(Calendar startTime, Calendar endTime,
			String description, String location, Calendar lastModifiedDate,
			String remoteId) {
		logger.log(Level.INFO, "In addScheduleFromGoogle function");
		Schedule schedule = new Schedule(++uniqueId, startTime, endTime,
				description, location, lastModifiedDate);
		schedule.setRemoteId(remoteId);
		add(schedule);
		return schedule;
	}

	public void deleteGoogleTask(int uniqueId) throws TaskNotFoundException {
		logger.log(Level.INFO, "In deleteGoogleTask function");
		Task t = findTaskFromUniqueId(uniqueId);
		if (t.getTaskType() == TaskType.SCHEDULE) {
			logger.log(Level.INFO, "Removing schedule " + t);
			allSchedules.remove(t);
		} else if (t.getTaskType() == TaskType.DEADLINE) {
			logger.log(Level.INFO, "Removing deadline " + t);
			allDeadlines.remove(t);
		} else if (t.getTaskType() == TaskType.TODO) {
			logger.log(Level.INFO, "Removing todo " + t);
			allTodos.remove(t);
		} else {
			logger.log(Level.WARNING, "Task not found");
			throw new TaskNotFoundException();
		}
	
		logger.log(Level.INFO, "Preparing for file sync");
		fileSync();
	}

	public void updateGoogleTask(Task t) throws TaskNotFoundException {
		logger.log(Level.INFO, "In updateGoogleTask");
		Task updateTask = findTaskFromUniqueId(t.getUniqueId());
		if (t.getTaskType() == TaskType.SCHEDULE) {
			logger.log(Level.INFO, "Updating schedule " + t);
			allSchedules.set(allSchedules.indexOf(updateTask), (Schedule) t);
		} else if (t.getTaskType() == TaskType.DEADLINE) {
			logger.log(Level.INFO, "Updating deadline " + t);
			allDeadlines.set(allDeadlines.indexOf(updateTask), (Deadline) t);
		} else if (t.getTaskType() == TaskType.TODO) {
			logger.log(Level.INFO, "Updating todo " + t);
			allTodos.set(allTodos.indexOf(updateTask), (Todo) t);
		} else {
			logger.log(Level.WARNING, "Task not found");
			throw new TaskNotFoundException();
		}
	}

	public ArrayList<Schedule> checkForScheduleClashes(Schedule s) {
		logger.log(Level.INFO, "In check for schedule clashes");
		ArrayList<Schedule> clashedSchedules = new ArrayList<Schedule>();
		for (Schedule sc : allSchedules) {
			if ((s.getStartTime().after(sc.getStartTime()) && s.getEndTime()
					.before(sc.getEndTime()))
					|| (s.getStartTime().compareTo(sc.getStartTime()) == 0 || (s
							.getEndTime().compareTo(sc.getEndTime()) == 0))) {
				logger.log(Level.INFO, "Clashed schedule found: " + sc);
				if (updateOriginalTask != null
						&& updateOriginalTask.getUniqueId() != sc.getUniqueId()) {
					clashedSchedules.add(sc);
				}
			}
		}
	
		return clashedSchedules;
	}

	public void exit() {
		logger.log(Level.INFO, "In exit function");
		BufferedWriter writer = null;
		try {
			logger.log(Level.INFO, "Preparing for file sync");
			fileSync();
			writer = new BufferedWriter(new FileWriter(file, true));
			logger.log(Level.INFO, "Writing into file the last uniqueId"
					+ uniqueId);
			writer.write(UNIQUEID + DELIMITER + uniqueId);
			writer.newLine();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error detected " + e.getMessage());
		} finally {
			try {
				logger.log(Level.INFO, "Closing buffered writer");
				writer.close();
			} catch (IOException e) {
				logger.log(
						Level.WARNING,
						"Unable to close buffered writer due to error: "
								+ e.getMessage());
			}
		}
	}

	private Calendar convertTimeFromStringToCalendar(String time) {
		StringTokenizer st = new StringTokenizer(time);
		st.nextToken(); // unused token: day is not stored.

		logger.log(Level.INFO,
				"Converting time from calendar type to string type");
		int month = determineMonth(st.nextToken());
		int date = Integer.parseInt(st.nextToken());

		logger.log(Level.INFO, "Converted month and date");

		StringTokenizer stHrAndMin = new StringTokenizer(st.nextToken(), ":");
		int hour = Integer.parseInt(stHrAndMin.nextToken());
		int min = Integer.parseInt(stHrAndMin.nextToken());
		int sec = Integer.parseInt(stHrAndMin.nextToken());

		logger.log(Level.INFO, "Converted hour and min");
		st.nextToken(); // unused token SGT

		int year = Integer.parseInt(st.nextToken());

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, hour, min, sec);
		cal.set(Calendar.MILLISECOND, 0); // for google calendar purpose

		return cal;
	}

	// method to get the number that corresponds to the month of the year.
	// returns -1 if the string parameter is not determinable.
	private int determineMonth(String month) {
		logger.log(Level.INFO, "Converting month from String to int");
		switch (month) {
		case "Jan":
			logger.log(Level.INFO, "Detected month as JAN");
			return 0;
		case "Feb":
			logger.log(Level.INFO, "Detected month as FEB");
			return 1;
		case "Mar":
			logger.log(Level.INFO, "Detected month as MAR");
			return 2;
		case "Apr":
			logger.log(Level.INFO, "Detected month as APR");
			return 3;
		case "May":
			logger.log(Level.INFO, "Detected month as MAY");
			return 4;
		case "Jun":
			logger.log(Level.INFO, "Detected month as JUN");
			return 5;
		case "Jul":
			logger.log(Level.INFO, "Detected month as JUL");
			return 6;
		case "Aug":
			logger.log(Level.INFO, "Detected month as AUG");
			return 7;
		case "Sep":
			logger.log(Level.INFO, "Detected month as SEP");
			return 8;
		case "Oct":
			logger.log(Level.INFO, "Detected month as OCT");
			return 9;
		case "Nov":
			logger.log(Level.INFO, "Detected month as NOV");
			return 10;
		case "Dec":
			logger.log(Level.INFO, "Detected month as DEC");
			return 11;
		default:
			logger.log(Level.WARNING, "No month detected");
			return -1;
		}
	}

	// Method that classifies the user add input into three types of task:
	// schedules, deadlines, and todos. Deadlines and Todos are default undone.
	private Task classify(AddAction action) {
		Task t;
		if (action.getStartTime() == null) {
			if (action.getEndTime() == null) {
				logger.log(Level.INFO,
						"Creating todo. No start time and end time detected");
				t = new Todo(++uniqueId, action.getDescription(),
						action.getPlace(), MARK_UNDONE, Calendar.getInstance());
			} else {
				logger.log(Level.INFO,
						"Creating deadline. No start time detected");
				t = new Deadline(++uniqueId, action.getEndTime(),
						action.getDescription(), action.getPlace(),
						MARK_UNDONE, Calendar.getInstance());
			}
		} else {
			logger.log(Level.INFO,
					"Creating schedule. Both start time and end time detected");
			t = new Schedule(++uniqueId, action.getStartTime(),
					action.getEndTime(), action.getDescription(),
					action.getPlace(), Calendar.getInstance());
		}

		return t;
	}

	// Method for adding classified tasks into the calendar
	private String add(Task t) {

		logger.log(Level.INFO, "In TM add function, adding task");

		String output = EMPTY_STRING;
		ArrayList<Schedule> sc;

		output = ADD_PREFIX + t.outputStringForDisplay() + NEWLINE;

		if (t.getTaskType().equals(TaskType.SCHEDULE)) {
			if (!(sc = checkForScheduleClashes((Schedule) t)).isEmpty()) {
				logger.log(Level.INFO, "Clashed schedules detected");
				output += NEWLINE + ADD_WARNING_CLASH;
				for (Schedule s : sc) {
					logger.log(Level.INFO, "Printing all clashed schedules");
					output += s.outputStringForDisplay() + NEWLINE;
				}
			}
			logger.log(Level.INFO, "Adding schedule");
			allSchedules.add((Schedule) t);
		} else if (t.getTaskType().equals(TaskType.DEADLINE)) {
			logger.log(Level.INFO, "Adding deadline");
			allDeadlines.add((Deadline) t);

		} else if (t.getTaskType().equals(TaskType.TODO)) {
			logger.log(Level.INFO, "Adding todo");
			allTodos.add((Todo) t);
		}

		lastTaskCreated = t;
		fileSync();

		return output + NEWLINE;

	}

	private String addUndo() {

		logger.log(
				Level.INFO,
				"In add undo function. Removing the last task added into the function from arraylist.");
		allSchedules.remove(lastTaskCreated);
		allDeadlines.remove(lastTaskCreated);
		allTodos.remove(lastTaskCreated);

		logger.log(Level.INFO, "Preparing for file sync");
		fileSync();

		return UNDO_ADD_SUCCESSFUL;
	}
	
	private String display(DisplayAction ac) {
		logger.log(Level.INFO,
				"In display function. Clearing all previously requested tasks by the user");
		requestedSchedules.clear();
		requestedDeadlines.clear();
		requestedTodos.clear();
		selectedTasks.clear();

		String output = EMPTY_STRING;

		switch (ac.getDescription()) {

		// If user wants to display schedules only
		case DISPLAY_TYPE_SCHEDULE:
			logger.log(Level.INFO, "Display schedules requested");
			for (Schedule s : allSchedules) {
				if ((s.getStartTime().after(ac.getStartTime()) && s
						.getEndTime().before(ac.getEndTime()))
						|| (s.getStartTime().compareTo(ac.getStartTime()) == 0 || (s
								.getEndTime().compareTo(ac.getEndTime()) == 0))) {
					logger.log(Level.INFO,
							"schedule within specified timeframe found. Adding in particular schedule");
					requestedSchedules.add(s);
				}
			}
			break;

		// If user wants to display deadlines only
		case DISPLAY_TYPE_DEADLINE:
			logger.log(Level.INFO, "Display deadlines requested");
			for (Deadline d : allDeadlines) {
				if (ac.getStatus().equals(EMPTY_STRING)) {
					if ((d.getEndTime().after(ac.getStartTime()) && d
							.getEndTime().before(ac.getEndTime()))
							|| (d.getEndTime().compareTo(ac.getStartTime()) == 0)
							|| (d.getEndTime().compareTo(ac.getEndTime()) == 0)) {
						logger.log(Level.INFO,
								"deadline within specified timeframe found. Adding in particular deadline");
						requestedDeadlines.add(d);
					}
				} else {
					if (((d.getEndTime().after(ac.getStartTime()) && d
							.getEndTime().before(ac.getEndTime()))
							|| (d.getEndTime().compareTo(ac.getStartTime()) == 0) || (d
							.getEndTime().compareTo(ac.getEndTime()) == 0))
							&& d.getStatus().equals(ac.getStatus())) {
						logger.log(
								Level.INFO,
								"deadline within specified timeframe and status found. Adding in particular deadline");
						requestedDeadlines.add(d);
					}
				}
			}
			break;

		// If user wants to display todos only
		case DISPLAY_TYPE_TODO:
			logger.log(Level.INFO, "Display todos requested");
			for (Todo td : allTodos) {
				if (ac.getStatus().equals(EMPTY_STRING)) {
					logger.log(Level.INFO,
							"Adding all todos with no specified status");
					requestedTodos.add(td);
				} else {
					if (td.getStatus().equals(ac.getStatus())) {
						logger.log(Level.INFO,
								"Adding all todos with specified status");
						requestedTodos.add(td);
					}
				}
			}
			break;

		// If user wants to display all types
		case DISPLAY_TYPE_ALL:
			logger.log(Level.INFO, "Display all requested");
			for (Schedule s : allSchedules) {
				if ((s.getStartTime().after(ac.getStartTime()) && s
						.getEndTime().before(ac.getEndTime()))
						|| (s.getStartTime().compareTo(ac.getStartTime()) == 0 || (s
								.getEndTime().compareTo(ac.getEndTime()) == 0))) {
					logger.log(Level.INFO,
							"schedule within specified timeframe found. Adding in particular schedule");
					requestedSchedules.add(s);
				}
			}
			for (Deadline d : allDeadlines) {
				if ((d.getEndTime().after(ac.getStartTime()) && d
						.getEndTime().before(ac.getEndTime()))
						|| (d.getEndTime().compareTo(ac.getStartTime()) == 0)
						|| (d.getEndTime().compareTo(ac.getEndTime()) == 0)) {
					logger.log(Level.INFO,
							"deadline within specified timeframe found. Adding in particular deadline");
					requestedDeadlines.add(d);
				}
			}
			if (ac.getEndTime().get(Calendar.YEAR) == 2099) {
				for (Todo td : allTodos) {
					logger.log(Level.INFO, "adding all todos");
					requestedTodos.add(td);
				}
			}
			break;
		case DISPLAY_TYPE_EMPTY:
			logger.log(Level.INFO, "Display done or undone requested");
			for (Deadline d : allDeadlines) {
				if (d.getStatus().equals(ac.getStatus())) {
					requestedDeadlines.add(d);
				}
			}
			for (Todo td : allTodos) {
				if (td.getStatus().equals(ac.getStatus())) {
					requestedTodos.add(td);
				}
			}

		default:
			break;
		}

		return displayOutput(output);
	}

	private String alignNumbersAndText(int count) {
		String strCount;
		if (count < 10) {
			logger.log(Level.INFO,
					"Count is less than 10. Aligning output text");
			strCount = count + DISPLAY_ALIGNMENT_DOT;
		} else {
			logger.log(Level.INFO, "Count is now more than 10");
			strCount = count + DISPLAY_NONALIGNMENT_DOT;
		}
		return strCount;
	}

	private String displayOutput(String output) {
		int count = 1;
		String strCount = EMPTY_STRING;

		logger.log(Level.INFO, "Sorting schedules in temporary memory");
		Collections.sort(requestedSchedules);
		Collections.sort(requestedDeadlines);
		Collections.sort(requestedTodos);

		if (!requestedSchedules.isEmpty()) {
			logger.log(Level.INFO,
					"Adding schedules to be displayed into output String");
			output = output + DISPLAY_SCHEDULE_PREFIX;
			for (Schedule s : requestedSchedules) {
				strCount = alignNumbersAndText(count);
				output = output + strCount + s.outputStringForDisplay()
						+ NEWLINE;
				selectedTasks.put(count, s);
				count++;
			}
			output = output + NEWLINE;
		}

		if (!requestedDeadlines.isEmpty()) {
			logger.log(Level.INFO,
					"Adding deadlines to be displayed into output String");
			output = output + DISPLAY_DEADLINE_PREFIX;
			for (Deadline d : requestedDeadlines) {
				strCount = alignNumbersAndText(count);
				output = output + strCount + d.outputStringForDisplay()
						+ NEWLINE;
				selectedTasks.put(count, d);
				count++;
			}

			output = output + NEWLINE;
		}

		if (!requestedTodos.isEmpty()) {
			logger.log(Level.INFO,
					"Adding todos to be displayed into output String");
			output = output + DISPLAY_TODO_PREFIX;
			for (Todo td : requestedTodos) {
				strCount = alignNumbersAndText(count);
				output = output + strCount + td.outputStringForDisplay()
						+ NEWLINE;
				selectedTasks.put(count, td);
				count++;
			}

			output = output + NEWLINE;
		}

		if (output.equals(EMPTY_STRING)) {
			logger.log(Level.INFO,
					"No display matches detected. Reuturning no match");
			output = DISPLAY_NO_MATCHES;
		}

		return output;
	}

	private boolean checkValidReferenceNumbers(ArrayList<Integer> refNum) {
		for (Integer num : refNum) {
			if (num > selectedTasks.size() || num <= 0) {
				return false;
			}
		}
		return true;
	}

	private String delete(DeleteAction ac) {
		logger.log(Level.INFO, "In delete function");
		deletedTasks.clear();

		if (ac.getReferenceNumber().get(0) != -1) {
			logger.log(Level.INFO, "Normal delete function");
			if (checkValidReferenceNumbers(ac.getReferenceNumber())) {
				for (Integer num : ac.getReferenceNumber()) {

					Task t = selectedTasks.get(num);
					deletedTasks.add(t);

					if (t instanceof Schedule) {
						logger.log(Level.INFO, "Removing schedule: " + t);
						allSchedules.remove(t);
						requestedSchedules.remove(t);
					} else if (t instanceof Deadline) {
						logger.log(Level.INFO, "Removing deadline: " + t);
						allDeadlines.remove(t);
						requestedDeadlines.remove(t);
					} else if (t instanceof Todo) {
						logger.log(Level.INFO, "Removing todo: " + t);
						allTodos.remove(t);
						requestedTodos.remove(t);
					}

					t.setLastModifiedDate(Calendar.getInstance());
				}
			} else {
				logger.log(Level.INFO,
						"Number input exceeds displayed number of output. Returning error");
				return INVALID_NUMBER_INPUT;
			}
		} else {
			logger.log(Level.INFO, "Delete all command called");
			for (Integer i : selectedTasks.keySet()) {
				Task t = selectedTasks.get(i);
				logger.log(Level.INFO, "Removing every trace of " + t);
				deletedTasks.add((Task) t.clone());
				allSchedules.remove(t);
				allDeadlines.remove(t);
				allTodos.remove(t);
				requestedSchedules.remove(t);
				requestedDeadlines.remove(t);
				requestedTodos.remove(t);
			}

			logger.log(Level.INFO, "Preparing for file sync");
			fileSync();
			return DELETE_ALL;
		}

		logger.log(Level.INFO, "Preparing for file sync");
		fileSync();

		return String.format(DELETE_SUCCESSFUL, ac.getReferenceNumber());

	}

	private String deleteUndo() {
		logger.log(Level.INFO, "In delete undo function");
		System.out.println(deletedTasks);
		for (Task t : deletedTasks) {
			// uniqueId--;
			if (t instanceof Schedule) {
				logger.log(Level.INFO, "Recovering deleted schedule: " + t);
				allSchedules.add((Schedule) t);
			} else if (t instanceof Deadline) {
				logger.log(Level.INFO, "Recovering deleted deadline: " + t);
				allDeadlines.add((Deadline) t);
			} else if (t instanceof Todo) {
				logger.log(Level.INFO, "Recovering deleted todo: " + t);
				allTodos.add((Todo) t);
			}

			t.setLastModifiedDate(Calendar.getInstance());
			t.setRemoteId(null);
		}

		logger.log(Level.INFO, "Preparing for file sync");
		fileSync();

		return UNDO_DELETE_SUCCESSFUL;
	}

	private TaskType classifyUpdate(UpdateAction ac) {
		if (ac.getUpdatedStartTime() == null) {
			if (ac.getUpdatedEndTime() == null) {
				return TaskType.TODO;
			} else {
				return TaskType.DEADLINE;
			}
		} else {
			return TaskType.SCHEDULE;
		}

	}

	private String update(UpdateAction ac) {
		logger.log(Level.INFO, "In update function");
		ArrayList<Schedule> sc = new ArrayList<Schedule>();
		String output = String.format(UPDATE_SUCCESSFUL,
				ac.getReferenceNumber(), ac.getUpdatedQuery());
		System.out.println(ac);
		if (ac.getReferenceNumber() <= selectedTasks.size()
				&& ac.getReferenceNumber() > 0) {
			Task t = selectedTasks.get(ac.getReferenceNumber());
			if (t.getTaskType() != classifyUpdate(ac)) {
				return UPDATE_TYPE_CLASH;
			}

			if (t instanceof Schedule) {
				logger.log(Level.INFO, "Updating schedule: " + t);
				updateOriginalTask = (Schedule) t.clone();
				((Schedule) t).setStartTime(ac.getUpdatedStartTime());
				((Schedule) t).setEndTime(ac.getUpdatedEndTime());
				if (!(sc = checkForScheduleClashes((Schedule) t)).isEmpty()) {
					logger.log(Level.INFO, "Clashed schedules found");
					output += NEWLINE + UPDATE_WARNING_CLASH;
					for (Schedule s : sc) {
						output += s.outputStringForDisplay() + NEWLINE;
					}
				}
			} else if (t instanceof Deadline) {
				logger.log(Level.INFO, "Updating schedule: " + t);
				updateOriginalTask = (Deadline) t.clone();
				((Deadline) t).setEndTime(ac.getUpdatedEndTime());
			} else if (t instanceof Todo) {
				logger.log(Level.INFO, "Updating todo: " + t);
				updateOriginalTask = (Todo) t.clone();
			}

			// common attributes to all schedules deadlines and todos
			t.setPlace(ac.getUpdatedLocationQuery());
			t.setDescription(ac.getUpdatedQuery());
			t.setLastModifiedDate(Calendar.getInstance());

			fileSync();
			return output;
		}

		return INVALID_NUMBER_INPUT;
	}

	private String updateUndo() {
		logger.log(Level.INFO, "In update undo function");

		logger.log(Level.INFO, "undoing update back to task:  "
				+ updateOriginalTask);
		
		Schedule scheduleToRemove = null;
		Schedule scheduleToAdd = null;
		for (Schedule s : allSchedules) {
			if (s.getUniqueId() == updateOriginalTask.getUniqueId()) {
				scheduleToRemove = s;
				scheduleToAdd = (Schedule) updateOriginalTask.clone();
				break;
			}
		}
		if(scheduleToRemove != null) {
			allSchedules.remove(scheduleToRemove);

			scheduleToAdd.setLastModifiedDate(Calendar.getInstance());
			allSchedules.add(scheduleToAdd);
		}
		
		Deadline deadlineToRemove = null;
		Deadline deadlineToAdd = null;
		for (Deadline d : allDeadlines) {
			if (d.getUniqueId() == updateOriginalTask.getUniqueId()) {
				deadlineToRemove = d;
				deadlineToAdd = (Deadline) updateOriginalTask.clone();
				break;
			}
		}
		if(deadlineToRemove != null) {
			allDeadlines.remove(deadlineToRemove);

			deadlineToAdd.setLastModifiedDate(Calendar.getInstance());
			allDeadlines.add(deadlineToAdd);
		}
		
		Todo todoToRemove = null;
		Todo todoToAdd = null;
		for (Todo td : allTodos) {
			if (td.getUniqueId() == updateOriginalTask.getUniqueId()) {
				todoToRemove = td;
				todoToAdd = (Todo) updateOriginalTask.clone();
				break;
			}
		}
		if(todoToRemove != null) {
			allTodos.remove(todoToRemove);

			todoToAdd.setLastModifiedDate(Calendar.getInstance());
			allTodos.add(todoToAdd);
		}

		fileSync();
		updateOriginalTask = null;

		return UNDO_UPDATE_SUCCESSFUL;
	}

	private String search(SearchAction ac) {
		logger.log(Level.INFO,
				"In search function. Clearing all previously requested tasks from memory");
		requestedSchedules.clear();
		requestedDeadlines.clear();
		requestedTodos.clear();
		selectedTasks.clear();

		String output = EMPTY_STRING;

		for (Schedule s : allSchedules) {
			if (((s.getDescription()).toLowerCase()).contains((ac.getQuery()
					.toLowerCase()))) {
				if ((s.getStartTime().after(ac.getStartTime()) && s
						.getEndTime().before(ac.getEndTime()))
						|| (s.getStartTime().compareTo(ac.getStartTime()) == 0 || (s
								.getEndTime().compareTo(ac.getEndTime()) == 0))) {
					logger.log(Level.INFO, "Found schedule match");
					requestedSchedules.add(s);
				}
			}
		}

		for (Deadline d : allDeadlines) {
			if (((d.getDescription()).toLowerCase()).contains((ac.getQuery()
					.toLowerCase()))) {
				if ((d.getEndTime().after(ac.getStartTime()) && d
						.getEndTime().before(ac.getEndTime()))
						|| (d.getEndTime().compareTo(ac.getStartTime()) == 0)
						|| (d.getEndTime().compareTo(ac.getEndTime()) == 0)) {
					logger.log(Level.INFO, "Found deadline match");
					requestedDeadlines.add(d);
				}
			}
		}

		for (Todo td : allTodos) {
			if (((td.getDescription()).toLowerCase()).contains((ac.getQuery()
					.toLowerCase()))) {
				logger.log(Level.INFO, "Found todo match");
				requestedTodos.add(td);
			}
		}

		output = SEARCH_PREFIX;

		if (requestedTodos.isEmpty() && requestedDeadlines.isEmpty()
				&& requestedSchedules.isEmpty()) {
			logger.log(Level.INFO, "No search matches found");
			output = output + SEARCH_UNFOUND;
		}

		return displayOutput(output);
	}

	private String mark(MarkAction ac) {
		logger.log(Level.INFO, "In mark function");
		String numbers = EMPTY_STRING;
		markUndoList = new ArrayList<Task>();

		if (checkValidReferenceNumbers(ac.getReferenceNumber())) {
			for (Integer num : ac.getReferenceNumber()) {

				logger.log(Level.INFO, "Number input within range");
				Task t = selectedTasks.get(num);
				markUndoList.add((Task) t.clone());
				numbers = numbers + num + BLANK_SPACE;

				if (t instanceof Deadline) {
					logger.log(Level.INFO,
							"Marking deadline as " + ac.getStatus());
					((Deadline) t).setStatus(ac.getStatus());
				} else if (t instanceof Todo) {
					logger.log(Level.INFO, "Marking todo as " + ac.getStatus());
					((Todo) t).setStatus(ac.getStatus());
				} else {
					logger.log(Level.INFO,
							"User trying to mark schedule. Returning error");
					return MARK_SCHEDULE_ERROR;
				}

				t.setLastModifiedDate(Calendar.getInstance());
			}
		} else {
			logger.log(Level.INFO, "Number input out of range. Returning error");
			return INVALID_NUMBER_INPUT;
		}

		logger.log(Level.INFO, "Preparing for file sync");
		fileSync();

		return String.format(MARK_SUCCESSFUL, numbers, ac.getStatus());
	}

	private String markUndo() {
		logger.log(Level.INFO, "In mark undo function");

		for (Task t : markUndoList) {
			for (Deadline d : allDeadlines) {
				if (d.getUniqueId() == t.getUniqueId()) {
					logger.log(Level.INFO, "undoing status of deadline: " + d);
					d.setStatus(((Deadline) t).getStatus());
				}
			}
			for (Todo td : allTodos) {
				if (td.getUniqueId() == t.getUniqueId()) {
					logger.log(Level.INFO, "undoing status of todo: " + td);
					td.setStatus(((Todo) t).getStatus());
				}
			}
			t.setLastModifiedDate(Calendar.getInstance());
		}

		logger.log(Level.INFO, "Preparing for file sync");
		fileSync();

		return UNDO_MARK_SUCCESSFUL;
	}

	private void fileSync() {
		BufferedWriter clearWriter = null;
		BufferedWriter appendWriter = null;
		try {
			clearWriter = new BufferedWriter(new FileWriter(file));
			logger.log(Level.INFO, "Clearing file");
			clearWriter.write(EMPTY_STRING);
			appendWriter = new BufferedWriter(new FileWriter(file, true));

			for (Schedule s : allSchedules) {
				logger.log(Level.INFO, "Appending schedule " + s);
				appendWriter.write(s.toString());
				appendWriter.newLine();
			}

			for (Deadline d : allDeadlines) {
				logger.log(Level.INFO, "Appending deadline " + d);
				appendWriter.write(d.toString());
				appendWriter.newLine();
			}

			for (Todo td : allTodos) {
				logger.log(Level.INFO, "Appending td " + td);
				appendWriter.write(td.toString());
				appendWriter.newLine();
			}

		} catch (IOException e) {
			logger.log(Level.WARNING, "Error detected" + e.getMessage());
			e.printStackTrace();
		} finally {
			logger.log(Level.INFO, "Closing buffered writers");
			try {
				clearWriter.close();
				appendWriter.close();
			} catch (IOException e) {
				logger.log(
						Level.WARNING,
						"Unable to close buffered writers due to error: "
								+ e.getMessage());
			}
		}
	}

	private Task findTaskFromUniqueId(int uniqueId)
			throws TaskNotFoundException {
		logger.log(Level.INFO, "In find task from unique id function");
		for (Schedule s : allSchedules) {
			if (s.getUniqueId() == uniqueId) {
				logger.log(Level.INFO, "Task found is a schedule" + s);
				return s;
			}
		}
		for (Deadline d : allDeadlines) {
			if (d.getUniqueId() == uniqueId) {
				logger.log(Level.INFO, "Task found is a deadline" + d);
				return d;
			}
		}
		for (Todo td : allTodos) {
			if (td.getUniqueId() == uniqueId) {
				logger.log(Level.INFO, "Task found is a todo" + td);
				return td;
			}
		}

		logger.log(Level.WARNING, "Task is not found");
		throw new TaskNotFoundException("Task not found");
	}
}
