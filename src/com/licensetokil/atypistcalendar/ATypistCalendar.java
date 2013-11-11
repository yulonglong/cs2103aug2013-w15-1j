package com.licensetokil.atypistcalendar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.licensetokil.atypistcalendar.gcal.GoogleCalendarManager;
import com.licensetokil.atypistcalendar.parser.Action;
import com.licensetokil.atypistcalendar.parser.DisplayAction;
import com.licensetokil.atypistcalendar.parser.ExitAction;
import com.licensetokil.atypistcalendar.parser.GoogleAction;
import com.licensetokil.atypistcalendar.parser.LocalAction;
import com.licensetokil.atypistcalendar.parser.MalformedUserInputException;
import com.licensetokil.atypistcalendar.parser.Parser;
import com.licensetokil.atypistcalendar.parser.SearchAction;
import com.licensetokil.atypistcalendar.parser.SystemAction;
import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.TaskNotFoundException;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.ui.ATCGUI;

public class ATypistCalendar {
	private static final String EMPTY_STRING = "";

	private static final String ERROR_DIALOG_TITLE = "Fatal Error";
	private static final String ERROR_DIALOG_MESSAGE = "A Typist's Calendar is unable to load the SWT library that is required for running. Exiting...";

	private static final String RESOURCE_PATH_SWT_X64_JAR = "/swt_x64.jar";
	private static final String RESOURCE_PATH_SWT_X86_JAR = "/swt_x86.jar";

	private static final String JAVA_VM_NAME = "java.vm.name";
	private static final String JAVA_VM_NAME_KEYWORD_64_BIT = "64-Bit";

	private static final String PROGRAM_ARGUMENT_LOG_TO_FILE = "-logToFile";

	private static final String ATC_ERROR_STREAM_LOG_FILE_NAME = "atc_errorStream.log";

	private static final String MESSAGE_USER_INPUT_FEEDBACK_UPON_SUCCESSFUL_COMMAND = "Your previous command: %s";
	private static final String MESSAGE_USER_INPUT_FEEDBACK_UPON_FAILED_COMMAND = "Your previous (erroneous) command: %s";

	private static ATypistCalendar instance = null;

	private static Logger masterLogger = Logger.getLogger(ATypistCalendar.class.getPackage().getName());
	private static Logger logger = Logger.getLogger(ATypistCalendar.class.getName());

	private ATCGUI gui;

	private ATypistCalendar() {
	}

	public static ATypistCalendar getInstance() {
		if(instance == null) {
			instance = new ATypistCalendar();
		}
		return instance;
	}

	public static void main(String[] args) {
		//No logging allowed until we set the System.err stream (if needed)
		//Logging before setting the stream will make the logging system hook on the original System.err stream.
		if(containsStringIgnoreCase(args, PROGRAM_ARGUMENT_LOG_TO_FILE)) {
			redirectSystemErrorStream();
		}
		else {
			//TODO delete before production
			redirectSystemErrorStream();
		}
		ATypistCalendar.getInstance().initialize(args);
	}

	private static boolean containsStringIgnoreCase(String[] args, String string) {
		if(args.length == 0) {
			return false;
		}

		for(String element : args) {
			if(element.equalsIgnoreCase(string)) {
				return true;
			}
		}

		return false;
	}

	private static void redirectSystemErrorStream() {
		try {
			PrintStream errStream = new PrintStream(ATC_ERROR_STREAM_LOG_FILE_NAME);
			System.setErr(errStream);

			//We can start logging from here onwards.
			//TODO delete start
			try {
				FileHandler fHandler = new FileHandler("LOGGED.xml");
				masterLogger.addHandler(fHandler);
			} catch (SecurityException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO TODO delete end


			logger.info("Successfully redirected System.err to file.");
		} catch (FileNotFoundException e) {
			logger.severe("Unable to redirect System.err to file! Continuing with default System.err. (Quiet failure)");
			e.printStackTrace();
		}
	}

	private void initialize(String[] args) {
		logger.fine("initialize called.");

		logger.info("Loading SWT library.");
		loadSwtLibrary();

		logger.info("Creating new GUI instance.");
		gui = new ATCGUI();
		gui.setVisible(true);

		logger.info("Greeting the user.");
		Calendar calendar = Calendar.getInstance();
		gui.outputWithNewline("Welcome to a Typist Calendar!\n\nCurrent time:\n" + calendar.getTime().toString());

		logger.info("Initializing modules.");
		TasksManager.getInstance().initialize();
		GoogleCalendarManager.getInstance().initialize();

		logger.info("Initiallizing done.");
	}

	private void loadSwtLibrary() {
		logger.fine("loadSwtLibrary called.");

		logger.info("Deciding which version (32- or 64-bit) of the SWT library to include.");
		URL pathToSWT;
		if(System.getProperty(JAVA_VM_NAME).contains(JAVA_VM_NAME_KEYWORD_64_BIT)) {
			logger.info("Finding path to 64-bit SWT library.");
			pathToSWT = getClass().getResource(RESOURCE_PATH_SWT_X64_JAR);
			logger.info("Path to 64-bit SWT library found.");
		}
		else {
			logger.info("Finding path to 32-bit SWT library.");
			pathToSWT = getClass().getResource(RESOURCE_PATH_SWT_X86_JAR);
			logger.info("Path to 32-bit SWT library found.");
		}

		try {
			logger.info("Reflecting the URLClassLoader.addURL method to expose it (protected -> public)");
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			method.setAccessible(true);

			logger.info("Invoking the URLClassLoader.addURL method of the ATypistCalendar class'es ClassLoader.");
			method.invoke((URLClassLoader)ATypistCalendar.class.getClassLoader(), pathToSWT);
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.severe("Unable to load the SWT library! Displaying error message and exiting.");
			e.printStackTrace();
			JOptionPane.showMessageDialog(new JDialog(), ERROR_DIALOG_MESSAGE, ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		logger.info("SWT library loaded successful.");
	}



	public void userInput(String input) {
		String reply = EMPTY_STRING;
		Action action;
		try {
			action = Parser.parse(input);
		} catch (MalformedUserInputException muie) {
			gui.outputUserInput(String.format(MESSAGE_USER_INPUT_FEEDBACK_UPON_FAILED_COMMAND, input));
			gui.outputWithNewline(muie.getMessage());
			return;
		}

		if(action instanceof LocalAction) {
			reply = TasksManager.getInstance().executeCommand((LocalAction)action);
			if(!(action instanceof DisplayAction || action instanceof SearchAction)) {
				GoogleCalendarManager.getInstance().doCompleteSync();
			}
		}
		else if(action instanceof GoogleAction) {
			reply = GoogleCalendarManager.getInstance().executeCommand((GoogleAction)action);
		}
		else if(action instanceof SystemAction) {
			reply = executeCommand((SystemAction)action);
		}
		else {
			logger.severe("Unknown sub-class of Action returned from Parser!");
			assert false;
		}

		gui.outputUserInput(String.format(MESSAGE_USER_INPUT_FEEDBACK_UPON_SUCCESSFUL_COMMAND, input));
		gui.outputWithNewline(reply);
	}

	public void cleanUp() {
		TasksManager.getInstance().exit();
	}

	public ArrayList<Task> getCopyOfAllLocalTasks() {
		return TasksManager.getInstance().cloneAllTasks();
	}

	public void updateLocalTaskWithCorrespondingTaskRemoteId(int localTaskId, String remoteTaskId) {
		try {
			TasksManager.getInstance().updateCorrespondingTaskRemoteId(localTaskId, remoteTaskId);
		} catch (TaskNotFoundException e) {
			logger.warning("Local task not found. Nothing to do - discrepancies will be handle by next sync.");
		}
	}

	public void updateTasksManagerWithUpdatedLocalTask(Task updatedLocalTask) {
		try {
			TasksManager.getInstance().updateGoogleTask(updatedLocalTask);
		} catch (TaskNotFoundException e) {
			logger.warning("Local task could not found. Nothing to do - discrepancies will be handle by next sync.");
		}
	}

	public void deleteLocalTaskfromTasksManager(int localTaskId) {
		try {
			TasksManager.getInstance().deleteGoogleTask(localTaskId);
		} catch (TaskNotFoundException e) {
			logger.warning("Local task could not found. Nothing to do - discrepancies will be handle by next sync.");
		}
	}

	public Task insertLocalTaskIntoTasksManager(String description, String location, Calendar lastModifiedDate, String remoteTaskId) {
		return TasksManager.getInstance().addTodoFromGoogle(description, location, lastModifiedDate, remoteTaskId);
	}

	public Task insertLocalTaskIntoTasksManager(String description, String location, Calendar lastModifiedDate, String remoteTaskId, Calendar endTime) {
		return TasksManager.getInstance().addDeadlineFromGoogle(endTime, description, location, lastModifiedDate, remoteTaskId);
	}

	public Task insertLocalTaskIntoTasksManager(String description, String location, Calendar lastModifiedDate, String remoteTaskId, Calendar endTime, Calendar startTime) {
		return TasksManager.getInstance().addScheduleFromGoogle(startTime, endTime, description, location, lastModifiedDate, remoteTaskId);
	}

	public void setSyncerStatus(boolean syncerIsExecuting) {
		gui.changeWindowIcon(syncerIsExecuting);
	}

	private String executeCommand(SystemAction action) {
		if(action instanceof ExitAction) {
			gui.dispatchWindowClosingEvent();
			return EMPTY_STRING;
		}
		else {
			logger.severe("Unknown sub-class of SystemAction!");
			assert false;
			return EMPTY_STRING;
		}
	}
}
























































































































/*
userInput("add swimming on 30/12 from 1300 to 1400");
userInput("add swimming at CommunityClub on 21/11 from 1400 to 1500");
userInput("add swimming at Bukit Batok Community Club Swimming Pool on 22/11 from 1500 to 1600");
userInput("add swimming at BB CC on 2/11 from 1.33pm to 3.20 p.m.");
userInput("add light joggin at Science Park 2 on 4/4 from 11.33 to 2");
userInput("add light joggin at Science Park 2 on 4/4/14 at 5 for 1 hr 20 mins");
userInput("add light cyclin at Park 2 tmr");
userInput("add light cyclin at Park 2 today at 7 for 12 mins");
userInput("add strolling tgt at park 1 on 1/7");
userInput("add dota funfair at pgp R1 on mon at 5");
userInput("add school event at NUS 12 on 14 dec at 5");
userInput("add clean my room");
userInput("add reply janet by 12/1");
userInput("add reply Mary by 1/12 at 5 pm");

userInput("display"); userInput("display schedules at Bukit Batok");
userInput("display all on 10/6");
userInput("display all in Korea on 10/12");
userInput("display all on 1/3 from 1200 to 1300");
userInput("display all at Bukit Batok on 1/3 from 1200 to 1300");
userInput("display deadlines on 10/6");
userInput("display schedules on 5/3 from 3pm to 1900");
userInput("display todos"); userInput("display schedules");
userInput("display undone"); userInput("display done");

userInput("abcd");

userInput("mark #1 as done"); userInput("mark #1 #2 as done");

userInput("delete #1 #2 #4");

userInput("undo");
userInput("search swimming tgt at bt batok on 10/6");
userInput("search badminton on 5/3 from 3pm to 1900");

userInput("update #3 >> badminton with Ian on 2/1 from 1200 to 1300");
*/

/*
* Scanner sc = new Scanner(System.in); //TasksManager tm = new
* TasksManager(null); while(true) { Parser.Action action =
* Parser.Parse(sc.nextLine()); //add swimming at CommunityClub on 21/11
* from 1300 to 1400
* System.out.println(tm.executeCommand((Parser.LocalAction)action)); }
*/
