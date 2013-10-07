package com.licensetokil.atypistcalendar;
import java.util.Calendar;
import java.util.Scanner;
import com.licensetokil.atypistcalendar.parser.*;

import com.licensetokil.atypistcalendar.parser.Parser;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;

public class ATypistCalendar {
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		
		System.out.println("Welcome to a Typist Calendar!\n");
		System.out.println("Current time:");
		System.out.println(calendar.getTime());
		System.out.println();
		System.out.println(Parser.parse("add swimming on 30/12 from 1300 to 1400"));
		System.out.println(Parser.parse("add swimming at CommunityClub on 21/11 from 1400 to 1500"));
		System.out.println(Parser.parse("add swimming at Bukit Batok Community Club Swimming Pool on 22/11 from 1500 to 1600"));
		System.out.println(Parser.parse("add swimming at BB CC on 2/1 from 1.33pm to 3.20pm"));
		System.out.println(Parser.parse("display"));
		System.out.println(Parser.parse("display at Bukit Batok"));
		System.out.println(Parser.parse("display on 10/6"));
		System.out.println(Parser.parse("display in Korea on 10/12"));
		System.out.println(Parser.parse("display on 1/3 from 1200 to 1300"));
		System.out.println(Parser.parse("display at Bukit Batok on 1/3 from 1200 to 1300"));
		
		Action ac = Parser.parse("add swimming on 30/12 from 1300 to 1400");
		Action ac2 = Parser.parse("display");
		System.out.println(TasksManager.executeCommand((LocalAction)ac));
		System.out.println(TasksManager.executeCommand((LocalAction)ac2));
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
}
