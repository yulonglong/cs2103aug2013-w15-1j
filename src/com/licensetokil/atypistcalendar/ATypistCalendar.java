package com.licensetokil.atypistcalendar;
import java.util.Calendar;

import com.licensetokil.atypistcalendar.parser.*;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.ui.DefaultGUI;

public class ATypistCalendar {
	private static DefaultGUI gui;
	
	public static void main(String[] args){
		
		
		Calendar calendar = Calendar.getInstance();
		
		gui = new DefaultGUI();
		gui.setVisible(true);
		
		gui.outputWithNewline("Welcome to a Typist Calendar!\n");
		gui.outputWithNewline("Current time:");
		gui.outputWithNewline(calendar.getTime().toString());
		gui.outputWithNewline("");
		userInput("add swimming on 30/12 from 1300 to 1400");
		userInput("add swimming at CommunityClub on 21/11 from 1400 to 1500");
		userInput("add swimming at Bukit Batok Community Club Swimming Pool on 22/11 from 1500 to 1600");
		userInput("add swimming at BB CC on 2/1 from 1.33pm to 3.20pm");
		userInput("display");
		userInput("display at Bukit Batok");
		userInput("display on 10/6");
		userInput("display in Korea on 10/12");
		userInput("display on 1/3 from 1200 to 1300");
		userInput("display at Bukit Batok on 1/3 from 1200 to 1300");
		userInput("display deadlines on 10/6");
		userInput("display schedules on 5/3 from 3pm to 1900");
		userInput("display todos");
		userInput("abcd");
		userInput("mark #1 as done");
		userInput("mark #1 #2 as done");
		userInput("mark swimming as done");
		userInput("mark group project meeting as undone");
		userInput("delete #1 #2 #4");
		userInput("delete group project meeting");
		userInput("search deadlines on 10/6");
		userInput("search schedules on 5/3 from 3pm to 1900");
		userInput("update swimming >> basketball on 2/1 from 1.33pm to 3.20pm");
		userInput("update #3 >> badminton on 2/1 from 1200 to 1300");
		/*
		userInput("add swimming on 30/12 from 1300 to 1400");
		userInput("display");
		userInput("display deadlines");
		*/
		
		/*
		Scanner sc = new Scanner(System.in);
		//TasksManager tm = new TasksManager(null);
		while(true) {
			Parser.Action action = Parser.Parse(sc.nextLine());
			//add swimming at CommunityClub on 21/11 from 1300 to 1400
			System.out.println(tm.executeCommand((Parser.LocalAction)action));
		}
		*/
	}
	
	public static void userInput(String input) {
		try {
			Action ac = Parser.parse(input);
			//String reply = TasksManager.executeCommand((LocalAction)ac);
			String reply = ac.toString();//kester using this to debug and try his parser
			gui.outputWithNewline(reply);
		}
		catch(MalformedUserInputException muie) {
			gui.outputWithNewline(muie.getMessage());
		}
	}
}
