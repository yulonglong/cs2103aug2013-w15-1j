package com.licensetokil.atypistcalendar.tasksmanager;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import com.licensetokil.atypistcalendar.parser.*;

public class JUnitTestDisplay {

	@Test
	public void testDisplay() {
		TasksManager.getInstance().initialize();
		AddAction addAction = new AddAction();
		DisplayAction displayAction = new DisplayAction();
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		
		startTime.set(2013, 10, 25, 12, 0, 0);
		endTime.set(2013, 10, 25, 13, 0, 0);
		addAction.setStartTime(startTime);
		addAction.setEndTime(endTime);
		addAction.setDescription("swimming");
		addAction.setPlace("Community Centre");
		TasksManager.getInstance().executeCommand(addAction);

		addAction.setDescription("reply Edward");
		addAction.setStartTime(null);
		addAction.setPlace("");
		TasksManager.getInstance().executeCommand(addAction);

		addAction.setEndTime(null);
		addAction.setDescription("clean my room");
		TasksManager.getInstance().executeCommand(addAction);

		// DISPLAY TESTS
		startTime.set(2013, 6, 25, 12, 0, 0);
		endTime.set(2099, 11, 25, 13, 0, 0);
		displayAction.setStartTime(startTime);
		displayAction.setEndTime(endTime);
		displayAction.setDescription("all");
		System.out.println(displayAction);

		// testDisplay1
		assertEquals(
				TasksManager.getInstance().executeCommand(displayAction),
				"Schedules: \n1. [Mon, Nov 25, '13] [12:00 PM - 01:00 PM] swimming at Community Centre\n\nDeadlines: \n2. [Mon, Nov 25, '13] [by 01:00 PM] [Status: undone] reply Edward\n\nTodos: \n3. [Status: undone] clean my room\n\n");

		// testDisplay2
		displayAction.setDescription("schedules");
		assertEquals(
				TasksManager.getInstance().executeCommand(displayAction),
				"Schedules: \n1. [Mon, Nov 25, '13] [12:00 PM - 01:00 PM] swimming at Community Centre\n\n");

		// testDisplay3
		displayAction.setDescription("deadlines");
		assertEquals(
				TasksManager.getInstance().executeCommand(displayAction),
				"Deadlines: \n1. [Mon, Nov 25, '13] [by 01:00 PM] [Status: undone] reply Edward\n\n");

		// testDisplay4
		displayAction.setDescription("todos");
		assertEquals(TasksManager.getInstance().executeCommand(displayAction),
				"Todos: \n1. [Status: undone] clean my room\n\n");

		System.out.println("All Display Tests pass");
	}

}
