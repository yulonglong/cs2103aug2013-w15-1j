package com.licensetokil.atypistcalendar.parser;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class ParserJUnitTest {

	@Test
	public void test() {
		int dayDifference = 0;
		
		//add swimming at Community Club on 21/11 from 1400 to 1500
		//one word description
		//multiple words place
		//dd/mm date format
		//24 hours hours time format
		//from, and to preposition
		AddAction newAc = new AddAction();
		Action ac = null;
		newAc.setDescription("swimming");
		newAc.setPlace("Community Club");
		Calendar startTime = Calendar.getInstance();
		startTime.set(2013, 10, 21, 14, 0, 0);
		Calendar endTime = Calendar.getInstance();
		endTime.set(2013, 10, 21, 15, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		Action expectedAc = newAc;
		try{
			ac = Parser.parse("add swimming at Community Club on 21/11 from 1400 to 1500");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//add swimming training on 30/12 , 0700 - 0800
		//multiple words description
		//no place
		//dd//mm date format
		//24 hours time format
		//comma (,) and dash(-) delimiter
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("swimming training");
		startTime = Calendar.getInstance();
		startTime.set(2013, 11, 30, 7, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(2013, 11, 30, 8, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add swimming training on 30/12 , 0700 - 0800");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//add swimming training at Bukit Batok Community Club on 1/3 from 5 a.m. - 6 pm
		//multiple words description
		//multiple words place
		//d/m date format
		//flexible 12 hours time format
		//from and dash(-) delimiter
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("swimming training");
		startTime = Calendar.getInstance();
		newAc.setPlace("Bukit Batok Community Club");
		startTime.set(2013, 2, 1, 5, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(2013, 2, 1, 18, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add swimming training at Bukit Batok Community Club on 1/3 from 5 a.m. - 6 pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//add thanksgiving festival at home on 1/11/14 at 7pm
		//multiple words description
		//single word place
		//d/mm/yy date format
		//flexible 12 hours time format
		//if only start time is stated, assume it is an one hour event
		//at delimiter
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("thanksgiving festival");
		startTime = Calendar.getInstance();
		newAc.setPlace("home");
		startTime.set(2014, 10, 1, 19, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(2014, 10, 1, 20, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add thanksgiving festival at home on 1/11/14 at 7pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//add thanksgiving festival on 9/8/2014
		//multiple words description
		//no place place
		//d/m/yyyy date format
		//no time stated
		//if no time stated, assume it is an one hour event, from 8 - 9 am
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("thanksgiving festival");
		startTime = Calendar.getInstance();
		startTime.set(2014, 7, 9, 8, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(2014, 7, 9, 9, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add thanksgiving festival on 9/8/2014");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//add basketball carnival in SRC on friday from 8 to 11
		//multiple words description
		//single word place
		//day, date format
		//flexible 12 hours time format, without am and pm
		//it will be assumed that 8.00-11.59 is am.
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("basketball carnival");
		startTime = Calendar.getInstance();
		newAc.setPlace("SRC");
		dayDifference = 0;
		dayDifference = 6 - startTime.get(Calendar.DAY_OF_WEEK);
		if(dayDifference==0){
			if(8==startTime.get(Calendar.HOUR)){
				if(0<=startTime.get(Calendar.MINUTE)){
				}
				else{
					dayDifference = dayDifference+7;
				}
			}
			else if(8<startTime.get(Calendar.HOUR)){
				dayDifference = dayDifference+7;
			}
		}
		if(dayDifference<0){
			dayDifference = dayDifference+7;
		}
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 8, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 11, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add basketball carnival in SRC on friday from 8 to 11");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//add basketball carnival in SRC on fri from 12 to 7.59
		//multiple words description
		//single word place
		//day, date format
		//flexible 12 hours time format, without am and pm
		//it will be assumed that 12.00 - 7.59 is pm.
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("basketball carnival");
		startTime = Calendar.getInstance();
		newAc.setPlace("SRC");
		dayDifference = 0;
		dayDifference = 6 - startTime.get(Calendar.DAY_OF_WEEK);
		if(dayDifference==0){
			if(12==startTime.get(Calendar.HOUR)){
				if(0<=startTime.get(Calendar.MINUTE)){
				}
				else{
					dayDifference = dayDifference+7;
				}
			}
			else if(12<startTime.get(Calendar.HOUR)){
				dayDifference = dayDifference+7;
			}
		}
		if(dayDifference<0){
			dayDifference = dayDifference+7;
		}
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 12, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 19, 59, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add basketball carnival in SRC on fri from 12 to 7.59");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
	}

}
