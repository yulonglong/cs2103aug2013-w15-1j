package com.licensetokil.atypistcalendar;

import static org.junit.Assert.*;
import java.util.Calendar;
import com.licensetokil.atypistcalendar.gcal.AuthenticationManager;
import com.licensetokil.atypistcalendar.gcal.GoogleCalendarManager;
import com.licensetokil.atypistcalendar.parser.*;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.ui.DefaultGUI;

import org.junit.Test;

public class JUnitTestLogic {

	@Test
	public void test() {
		/*
		//TestAddSchedule
		ATypistCalendar.gui = new DefaultGUI();
		assertEquals(ATypistCalendar.userInput("add swimming on 30/12 from 1300 to 1400"),"Added:\nEvent: swimming\nStarting Time: Mon Dec 30 13:00:00 SGT 2013\nEnding Time: Mon Dec 30 14:00:00 SGT 2013\n");
		
		//TestDisplay
		assertEquals(ATypistCalendar.userInput("display"),"Schedules: \n1. Event: swimming\nStarting Time: Mon Dec 30 13:00:00 SGT 2013\nEnding Time: Mon Dec 30 14:00:00 SGT 2013\n\n");
		
		//TestAddTodo
		assertEquals(ATypistCalendar.userInput("add clean my room"), "Added\nEvent: clean my room\n");
		
		//TestMark
		ATypistCalendar.userInput("display");
		assertEquals(ATypistCalendar.userInput("mark #2 as done"),"Marked 2 as done");
		*/
		
		TasksManager.fileToArray();
	}

}
