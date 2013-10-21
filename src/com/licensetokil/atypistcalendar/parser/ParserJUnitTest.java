package com.licensetokil.atypistcalendar.parser;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class ParserJUnitTest {

	@Test
	public void test() {
		int dayDifference = 0;
		AddAction newAc = new AddAction();
		DisplayAction newDc = new DisplayAction();
		Action ac = null;
		Action expectedAc = null;
		Calendar startTime = null;
		Calendar endTime = null;
		
		//...........ADD TESTING .........................................................
		
		//add swimming at Community Club on 21/12 from 1400 to 1500
		//one word description
		//multiple words place
		//dd/mm date format
		//24 hours hours time format
		//from, and to preposition
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("swimming");
		newAc.setPlace("Community Club");
		startTime = Calendar.getInstance();
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), 11, 21, 14, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), 11, 21, 15, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add swimming at Community Club on 21/12 from 1400 to 1500");
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
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), 11, 30, 7, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), 11, 30, 8, 0, 0);
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
		startTime.set(Calendar.getInstance().get(Calendar.YEAR)+1, 2, 1, 5, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR)+1, 2, 1, 18, 0, 0);
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
		
		//add go party at zouk today from 11pm
		//multiple words description
		//single word place
		//today, date format
		//flexible 12 hours time format.
		//assume 1 hour event
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("go party");
		startTime = Calendar.getInstance();
		newAc.setPlace("zouk");
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 23, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 24, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add go party at zouk today from 11pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//add cycling at Science park II tmr at 4 for 90 minutes
		//single words description
		//multiple word place
		//tmr, date format
		//flexible 12 hours time format.
		//time duration format
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("cycling");
		startTime = Calendar.getInstance();
		newAc.setPlace("Science park II");
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1, 16, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1, 17, 30, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add cycling at Science park II tmr at 4 for 90 minutes");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//add cycling at Science park II tomorrow at 4 for 1 hr 15 mins
		//single words description
		//multiple word place
		//tomorrow, date format
		//flexible 12 hours time format.
		//time duration format(hr and min and hrs and mins are accepted)
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("cycling");
		startTime = Calendar.getInstance();
		newAc.setPlace("Science park II");
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1, 16, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1, 17, 15, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add cycling at Science park II tomorrow at 4 for 1 hr 15 mins");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//add do homework by today
		//deadline
		//multiple words description
		//today, date format
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("do homework");
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add do homework by today");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		

		//add do homework in my room by tmr 9pm
		//deadline
		//multiple words description
		//tmr, date format
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("do homework");
		newAc.setPlace("my room");
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1, 21, 0, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add do homework in my room by tmr at 9pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//add reply mary by 4 Jan at 1720
		//deadline
		//multiple words description
		//day month, date format
		//24 hours time format
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("reply mary");
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR)+1, 0, 4 , 17, 20, 0);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add reply mary by 4 Jan at 1720");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//add prepare cv by 12 february 2014
		//deadline
		//multiple words description
		//day month year, date format
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("prepare cv");
		endTime = Calendar.getInstance();
		endTime.set(2014, 1, 12 , 23, 59, 59);
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add prepare cv by 12 february 2014");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//add clean my room
		//todos
		//multiple words description
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("clean my room");
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add clean my room");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());	
		
		//add exercise
		//todos
		//single word description
		newAc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newAc = new AddAction();
		newAc.setDescription("exercise");
		newAc.setStartTime(startTime);
		newAc.setEndTime(endTime);
		expectedAc = newAc;
		try{
			ac = Parser.parse("add exercise");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());	
		
		
		
		
		
		
		//................DISPLAY TESTING .........................................................
		
		//display
		//single command word display
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setDescription("all");
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();
		endTime.set(2099,11,31,23,59,59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//display all
		//display all will give same result as display
		try{
			ac = Parser.parse("display all");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//display all in Korea on 10/6
		//all description
		//single word place
		//dd/mm time format
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setDescription("all");
		newDc.setPlace("Korea");
		startTime = Calendar.getInstance();
		startTime.set(Calendar.getInstance().get(Calendar.YEAR),5,10,0,0,0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR),5,10,23,59,59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display all in Korea on 10/6");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//display deadlines today from 8am to 1pm
		//deadlines description
		//today date format
		//standard 12 hr time format
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setDescription("deadlines");
		startTime = Calendar.getInstance();
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 8, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 13, 0, 0);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display deadlines today from 8am to 1pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//display schedules on 21/3 from 8
		//schedules description
		//dd/mm date format
		//from time to (null), assuming until the end of the day 23.59.59
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setDescription("schedules");
		startTime = Calendar.getInstance();
		startTime.set(Calendar.getInstance().get(Calendar.YEAR),2,21, 8, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR),2,21, 23, 59, 59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display schedules on 21/3 from 8");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
	}

}
