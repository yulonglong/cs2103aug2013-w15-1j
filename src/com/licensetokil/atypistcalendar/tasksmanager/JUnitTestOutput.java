package com.licensetokil.atypistcalendar.tasksmanager;

import static org.junit.Assert.*;
import com.licensetokil.atypistcalendar.parser.*;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;
import com.licensetokil.atypistcalendar.ui.DefaultGUI;
import java.util.Calendar;
import org.junit.Test;

public class JUnitTestOutput {

	@Test
	public void testOutput() {
		//testAdd1
		//Equivalence partitioning: Schedule
		AddAction aa = new AddAction();
		Calendar startTime = Calendar.getInstance();
		Calendar endTime = Calendar.getInstance();
		startTime.set(2013, 9, 25, 12, 0, 0);
		endTime.set(2013, 9, 25, 13, 0, 0);
		aa.setStartTime(startTime);
		aa.setEndTime(endTime);
		aa.setDescription("swimming");
		aa.setPlace("Community Centre");
		
		assertEquals(TasksManager.executeCommand(aa), "Added:\nEvent: swimming\nStarting Time: Fri Oct 25 12:00:00 SGT 2013\nEnding Time: Fri Oct 25 13:00:00 SGT 2013\nPlace: Community Centre\n"); 
		
		//testAdd2
		//Equivalence partitioning: Deadline
		aa.setDescription("reply Edward");
		aa.setStartTime(null);
		aa.setPlace("");
		
		assertEquals(TasksManager.executeCommand(aa), "Added\nEvent: reply Edward\nDue by: Fri Oct 25 13:00:00 SGT 2013\n");
		
		//testAdd3
		//Equivalence partitioning: Todo
		aa.setEndTime(null);
		aa.setDescription("clean my room");
		
		assertEquals(TasksManager.executeCommand(aa), "Added\nEvent: clean my room\n");
	}

}
