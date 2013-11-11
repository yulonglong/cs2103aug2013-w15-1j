package com.licensetokil.atypistcalendar.tasksmanager;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

import com.licensetokil.atypistcalendar.parser.*;

public class JUnitTestAdd {

	@Test
	public void testOutput() {
		
		// ADD TESTS
		AddAction addAction = new AddAction();
		Calendar addStartTime = Calendar.getInstance();
		Calendar addEndTime = Calendar.getInstance();
		addStartTime.set(2013, 10, 25, 12, 0, 0);
		addEndTime.set(2013, 10, 25, 13, 0, 0);
		addAction.setStartTime(addStartTime);
		addAction.setEndTime(addEndTime);
		addAction.setDescription("swimming");
		addAction.setPlace("Community Centre");

		// testAdd1
		assertEquals(
				TasksManager.getInstance().executeCommand(addAction),
				"Added: \n[Mon, Nov 25, '13] [12:00 PM - 01:00 PM] swimming at Community Centre\n\n");
		
		// testAdd2
		addAction.setDescription("reply Edward");
		addAction.setStartTime(null);
		addAction.setPlace("");

		assertEquals(TasksManager.getInstance().executeCommand(addAction),
				"Added: \n[Mon, Nov 25, '13] [by 01:00 PM] [Status: undone] reply Edward\n\n");
		
		// testAdd3
		addAction.setEndTime(null);
		addAction.setDescription("clean my room");

		assertEquals(TasksManager.getInstance().executeCommand(addAction),
				"Added: \n[Status: undone] clean my room\n\n");

		System.out.println("All Add Tests pass");
		
		//ADD UNDO TEST
		addAction.setDescription("jogging tonight");
		TasksManager.getInstance().executeCommand(addAction);
		assertEquals(TasksManager.getInstance().executeCommand(new UndoAction()), "Add command successfully undone\n\n");

		
		//DISPLAY TESTS
		DisplayAction displayAction = new DisplayAction();
		Calendar dispStartTime = Calendar.getInstance();
		Calendar dispEndTime = Calendar.getInstance();
		dispStartTime.set(2000, 0, 1, 0, 0, 0);
		dispEndTime.set(2099, 11, 31, 23,59, 59);
		displayAction.setStartTime(dispStartTime);
		displayAction.setEndTime(dispEndTime);
		
		// testDisplay1
		displayAction.setDescription("schedules");
		assertEquals(
				TasksManager.getInstance().executeCommand(displayAction),
				"Schedules: \n1. [Mon, Nov 25, '13] [12:00 PM - 01:00 PM] swimming at Community Centre\n\n");

		// testDisplay2
		displayAction.setDescription("deadlines");
		assertEquals(
				TasksManager.getInstance().executeCommand(displayAction),
				"Deadlines: \n1. [Mon, Nov 25, '13] [by 01:00 PM] [Status: undone] reply Edward\n\n");

		// testDisplay3
		displayAction.setDescription("todos");
		assertEquals(TasksManager.getInstance().executeCommand(displayAction),
				"Todos: \n1. [Status: undone] clean my room\n\n");
		
		// testDisplay4
		displayAction.setDescription("all");
				assertEquals(
						TasksManager.getInstance().executeCommand(displayAction),
						"Schedules: \n1. [Mon, Nov 25, '13] [12:00 PM - 01:00 PM] swimming at Community Centre\n\nDeadlines: \n2. [Mon, Nov 25, '13] [by 01:00 PM] [Status: undone] reply Edward\n\nTodos: \n3. [Status: undone] clean my room\n\n");


		System.out.println("All Display Tests pass");
		
		//MARK TEST
		MarkAction markAction = new MarkAction();
		markAction.setStatus("done");
		ArrayList<Integer> refNum = new ArrayList<Integer>();
		refNum.add(2);
		refNum.add(3);
		markAction.setReferenceNumber(refNum);
		
		//markTest1
		assertEquals(TasksManager.getInstance().executeCommand(markAction), "Marked 2 3  as done\n\n");
		
		//markTest2
		markAction.setStatus("undone");
		assertEquals(TasksManager.getInstance().executeCommand(markAction), "Marked 2 3  as undone\n\n");
		
		//MARK UNDO TEST
		assertEquals(TasksManager.getInstance().executeCommand(new UndoAction()), "Mark command successfully undone\n\n");
		
		//Adding in todos
		addAction.setDescription("learn swimming");
		TasksManager.getInstance().executeCommand(addAction);
		addAction.setDescription("Meet Edward for catch up session");
		TasksManager.getInstance().executeCommand(addAction);
		addEndTime.set(2013, 10, 16, 23, 59, 0);
		//Adding in deadline
		addAction.setDescription("complete CS2106 project");
		TasksManager.getInstance().executeCommand(addAction);
		
		
		//SEARCH TEST
		SearchAction searchAction = new SearchAction();
		Calendar searchStartTime = Calendar.getInstance();
		Calendar searchEndTime = Calendar.getInstance();
	
		//searchTest1
		searchAction.setQuery("swimming");
		searchStartTime.set(2000, 0, 1, 0, 0, 0);
		searchEndTime.set(2099, 11, 31, 23,59, 59);
		searchAction.setStartTime(searchStartTime);
		searchAction.setEndTime(searchEndTime);
		assertEquals(TasksManager.getInstance().executeCommand(searchAction), "Search Matches: \n\nSchedules: \n1. [Mon, Nov 25, '13] [12:00 PM - 11:59 PM] swimming at Community Centre\n\nTodos: \n2. [Status: undone] learn swimming\n\n");
		
		//searchTest2
		searchStartTime.set(2013, 10, 26, 0, 0, 0);
		assertEquals(TasksManager.getInstance().executeCommand(searchAction), "Search Matches: \n\nTodos: \n1. [Status: undone] learn swimming\n\n");
		
		//searchTest3
		searchAction.setQuery("Edward");
		searchStartTime.set(2013, 10, 26, 0, 0, 0);
		assertEquals(TasksManager.getInstance().executeCommand(searchAction), "Search Matches: \n\nTodos: \n1. [Status: undone] Meet Edward for catch up session\n\n");		
		
		//DELETE TESTS
		DeleteAction deleteAction = new DeleteAction();
		TasksManager.getInstance().executeCommand(displayAction);
		ArrayList<Integer> deleteRefNum = new ArrayList<Integer>();
		
		//deleteTest1
		deleteRefNum.add(3);
		deleteAction.setReferenceNumber(deleteRefNum);
		assertEquals(TasksManager.getInstance().executeCommand(deleteAction),"Deleted [3] successfully \n\n");
		
		//display
		assertEquals(TasksManager.getInstance().executeCommand(displayAction), "Schedules: \n1. [Mon, Nov 25, '13] [12:00 PM - 11:59 PM] swimming at Community Centre\n\nDeadlines: \n2. [Sat, Nov 16, '13] [by 11:59 PM] [Status: done&nbsp&nbsp] reply Edward\n\nTodos: \n3. [Status: done&nbsp&nbsp] clean my room\n4. [Status: undone] complete CS2106 project\n5. [Status: undone] learn swimming\n\n");
		
		//deleteTest2
		deleteRefNum.remove((Integer)3);
		deleteRefNum.add(1);
		deleteRefNum.add(2);
		assertEquals(TasksManager.getInstance().executeCommand(deleteAction),"Deleted [1, 2] successfully \n\n");
		
		//display
		assertEquals(TasksManager.getInstance().executeCommand(displayAction), "Todos: \n1. [Status: done&nbsp&nbsp] clean my room\n2. [Status: undone] complete CS2106 project\n3. [Status: undone] learn swimming\n\n");
		
		//DELETE UNDO TEST
		assertEquals(TasksManager.getInstance().executeCommand(new UndoAction()), "Delete command successfully undone\n\n");
		
		//display
		assertEquals(TasksManager.getInstance().executeCommand(displayAction), "Schedules: \n1. [Mon, Nov 25, '13] [12:00 PM - 11:59 PM] swimming at Community Centre\n\nDeadlines: \n2. [Sat, Nov 16, '13] [by 11:59 PM] [Status: done&nbsp&nbsp] reply Edward\n\nTodos: \n3. [Status: done&nbsp&nbsp] clean my room\n4. [Status: undone] complete CS2106 project\n5. [Status: undone] learn swimming\n\n");
		
		//UPDATE TESTS
		UpdateAction updateAction = new UpdateAction();
		int updateRefNum = 1;
		updateAction.setReferenceNumber(updateRefNum);
		Calendar updateStartTime = Calendar.getInstance();
		Calendar updateEndTime = Calendar.getInstance();
		updateStartTime.set(2013, 10, 31, 14, 0, 0);
		updateEndTime.set(2013, 10, 31, 15, 0, 0);
		updateAction.setUpdatedStartTime(updateStartTime);
		updateAction.setUpdatedEndTime(updateEndTime);
		updateAction.setUpdatedQuery("Gymming");
		updateAction.setUpdatedLocationQuery("gym");
		
		//updateTest1
		assertEquals(TasksManager.getInstance().executeCommand(updateAction), "Updated 1 to Gymming successfully\n\n");
		
		//display
		displayAction.setDescription("schedules");
		assertEquals(TasksManager.getInstance().executeCommand(displayAction), "Schedules: \n1. [Sun, Dec 01, '13] [02:00 PM - 03:00 PM] Gymming at gym\n\n");
	}
}
