package com.licensetokil.atypistcalendar;

import java.util.Calendar;

import com.licensetokil.atypistcalendar.parser.*;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.ui.DefaultGUI;

public class ATypistCalendar {
	private static DefaultGUI gui;

	public static void main(String[] args) {

		Calendar calendar = Calendar.getInstance();

		gui = new DefaultGUI();
		gui.setVisible(true);

		gui.outputWithNewline("Welcome to a Typist Calendar!\n");
		gui.outputWithNewline("Current time:");
		gui.outputWithNewline(calendar.getTime().toString());
		gui.outputWithNewline("");

		
		/*
		 userInput("add swimming on 30/12 from 1300 to 1400");
		 userInput("add swimming at CommunityClub on 21/11 from 1400 to 1500");
		 userInput("add swimming at Bukit Batok Community Club Swimming Pool on 22/11 from 1500 to 1600");
		 userInput("add swimming at BB CC on 2/11 from 1.33pm to 3.20pm");
		 userInput("add clean my room");
		 userInput("add reply janet by 12/1");
		 userInput("add reply Mary by 1/12 at 5pm")
		 
		 * userInput("display"); userInput("display schedules at Bukit Batok");
		 * userInput("display all on 10/6");
		 * userInput("display all in Korea on 10/12");
		 * userInput("display all on 1/3 from 1200 to 1300");
		 * userInput("display all at Bukit Batok on 1/3 from 1200 to 1300");
		 * userInput("display deadlines on 10/6");
		 * userInput("display schedules on 5/3 from 3pm to 1900");
		 * userInput("display todos"); userInput("display schedules");
		 * userInput("display undone"); userInput("display done");
		 * 
		 * userInput("abcd");
		 * 
		 * userInput("mark #1 as done"); userInput("mark #1 #2 as done");
		 * 
		 * userInput("delete #1 #2 #4");
		 * 
		 * userInput("search swimming on 10/6");
		 * userInput("search badminton on 5/3 from 3pm to 1900");
		 * 
		 * userInput("update #3 >> badminton on 2/1 from 1200 to 1300");
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
		try {

			Action ac = Parser.parse(input);
			String reply;

			if (!input.equals("undo")) {
				if (ac.getClass().getName().contains("AddAction")) {
					reply = TasksManager.executeCommand((AddAction) ac);
				}

				else if (ac.getClass().getName().contains("DeleteAction")) {
					reply = TasksManager.executeCommand((DeleteAction) ac);
				}

				else if (ac.getClass().getName().contains("DisplayAction")) {
					reply = TasksManager.executeCommand((DisplayAction) ac);
				}

				else if (ac.getClass().getName().contains("UpdateAction")) {
					reply = TasksManager.executeCommand((UpdateAction) ac);
				}

				else if (ac.getClass().getName().contains("MarkAction")) {
					reply = TasksManager.executeCommand((MarkAction) ac);
				}

				else if (ac.getClass().getName().contains("SearchAction")) {
					reply = TasksManager.executeCommand((SearchAction) ac);
				}

				else {
					reply = ac.getClass().getName();
				}
			}

			else {
				reply = TasksManager.executeUndo((LocalAction) ac);
			}
			// String reply = ac.toString();//kester using this to debug and try
			// his parser
			gui.outputWithNewline(reply);
		} catch (MalformedUserInputException muie) {
			gui.outputWithNewline(muie.getMessage());
		}
	}
}
