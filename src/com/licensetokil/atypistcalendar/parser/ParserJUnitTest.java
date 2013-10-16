package com.licensetokil.atypistcalendar.parser;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class ParserJUnitTest {

	@Test
	public void test() {
		//;
		AddAction newAddAction = new AddAction();
		newAddAction.setDescription("swimming");
		newAddAction.setPlace("Community Club");
		Calendar startTime = Calendar.getInstance();
		startTime.set(2013, 10, 21, 14, 0, 0);
		Calendar endTime = Calendar.getInstance();
		endTime.set(2013, 10, 21, 15, 0, 0);
		newAddAction.setStartTime(startTime);
		newAddAction.setEndTime(endTime);
		Action ac = null;
		try{
			ac = Parser.parse("add swimming at Community Club on 21/11 from 1400 to 1500");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(newAddAction,(AddAction)ac);
		
		fail("Not yet implemented");
	}

}
