package com.licensetokil.atypistcalendar;

import java.util.ArrayList;
import java.util.Calendar;

import com.licensetokil.atypistcalendar.gcal.GoogleCalendarManager;
import com.licensetokil.atypistcalendar.parser.Action;
import com.licensetokil.atypistcalendar.parser.GoogleAction;
import com.licensetokil.atypistcalendar.parser.LocalAction;
import com.licensetokil.atypistcalendar.parser.MalformedUserInputException;
import com.licensetokil.atypistcalendar.parser.Parser;
import com.licensetokil.atypistcalendar.tasksmanager.Task;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.ui.ATCGUI;

public class ATypistCalendar {
	private static ATypistCalendar instance = null;
	public static ATCGUI gui;

	private ATypistCalendar() {
	}

	public static ATypistCalendar getInstance() {
		if(instance == null) {
			instance = new ATypistCalendar();
		}
		return instance;
	}

	public static void main(String[] args) {
		TasksManager TM = TasksManager.getInstance();
		Calendar calendar = Calendar.getInstance();

		gui = new ATCGUI();
		gui.setVisible(true);

		gui.outputWithNewline("Welcome to a Typist Calendar!\n");
		gui.outputWithNewline("Current time:");
		gui.outputWithNewline(calendar.getTime().toString());
		gui.outputWithNewline("");

		//GoogleCalendarManager.getInstance().initialise();

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

	}

	public static void userInput(String input) {
		String reply = "";
		Action action;
		try {
			action = Parser.parse(input);
		} catch (MalformedUserInputException muie) {
			gui.outputWithNewline(muie.getMessage());
			return;
		}

		if(action instanceof LocalAction) {
			reply = TasksManager.getInstance().executeCommand((LocalAction)action, true);
			//TODO we shouldnt be doing a complete sync each time we do a command, but this is a temporary measure
			//GoogleCalendarManager.getInstance().doCompleteSync();
		}
//		else if(action instanceof GoogleAction) {
//			reply = GoogleCalendarManager.getInstance().executeCommand((GoogleAction)action);
//		}
		else {
			//logger.servere("Unknown sub-class of Action returned from Parser!");
			assert false;
		}

		gui.outputUserInput("Your previous command: " + input);
		gui.outputWithNewline(reply);
	}

	public ArrayList<Task> getCopyOfAllLocalTasks() {
		return TasksManager.getInstance().getAllTasks();
	}
}
