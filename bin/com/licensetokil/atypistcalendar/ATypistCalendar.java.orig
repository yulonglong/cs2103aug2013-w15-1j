package com.licensetokil.atypistcalendar;
import java.util.Scanner;

import com.licensetokil.atypistcalendar.parser.Parser;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;

public class ATypistCalendar {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		TasksManager tm = new TasksManager(null);
		while(true) {
			Parser.Action action = Parser.Parse(sc.nextLine());
			//add swimming at CommunityClub on 21/11 from 1300 to 1400
			System.out.println(tm.executeCommand((Parser.LocalAction)action));
		}
	}
}
