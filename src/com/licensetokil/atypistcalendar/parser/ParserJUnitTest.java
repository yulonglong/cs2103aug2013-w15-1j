//@author A0080415N
//Name       : Steven Kester Yuwono
//Matric No. : A0080415N
//com.licensetokil.atypistcalendar.parser

package com.licensetokil.atypistcalendar.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;

public class ParserJUnitTest {

	@Test
	public void test() {
		int dayDifference = 0;
		ArrayList<Integer> intArrayList = new ArrayList<Integer>();
		AddAction newAc = new AddAction();
		DisplayAction newDc = new DisplayAction();
		SearchAction newSc = new SearchAction();
		DeleteAction newDelc = new DeleteAction();
		MarkAction newMc = new MarkAction();
		UpdateAction newUc = new UpdateAction();
		UndoAction newUndc = new UndoAction();
		AddGoogleAction newAddGc = new AddGoogleAction();
		SyncGoogleAction newSyncGc = new SyncGoogleAction();
		GoogleAction newGc = new GoogleAction();
		
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
		
		startTime.set(2000,0,1,0,0,0);
		newDc.setStartTime(startTime);
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
		
		
		//display schedules from 21/3 to 24/3
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
		startTime.set(Calendar.getInstance().get(Calendar.YEAR),2,21, 0, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR),2,24, 23, 59, 59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display schedules from 21/3 to 24/3");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//display undone todos
		//todos description
		//undone status
		//no time, assuming current time until the end of the world 31/12/2099 23.59.59
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setDescription("todos");
		newDc.setStatus("undone");
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();
		endTime.set(2099,11,31, 23, 59, 59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display undone todos");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//display done
		//done status
		//no time, assuming current time until the end of the world 31/12/2099 23.59.59
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setStatus("done");
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();
		endTime.set(2099,11,31, 23, 59, 59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display done");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());		
		
		
		//display all on friday
		//done status
		//no time, assuming current time until the end of the world 31/12/2099 23.59.59
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setDescription("all");
		startTime = Calendar.getInstance();
		dayDifference = 0;
		dayDifference = 6 - startTime.get(Calendar.DAY_OF_WEEK);
		if(dayDifference==0){
			if(0==startTime.get(Calendar.HOUR)){
				if(0<=startTime.get(Calendar.MINUTE)){
				}
				else{
					dayDifference = dayDifference+7;
				}
			}
			else if(0<startTime.get(Calendar.HOUR)){
				dayDifference = dayDifference+7;
			}
		}
		if(dayDifference<0){
			dayDifference = dayDifference+7;
		}
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 0, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 23, 59, 59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display all on friday");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//display schedules on next friday
		//done status
		//no time, assuming current time until the end of the world 31/12/2099 23.59.59
		newDc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newDc = new DisplayAction();
		newDc.setDescription("schedules");
		startTime = Calendar.getInstance();
		dayDifference = 0;
		dayDifference = 6 - startTime.get(Calendar.DAY_OF_WEEK);
		if(dayDifference>=0){
			dayDifference = dayDifference + 7;
		}
		else if(dayDifference<0){
			dayDifference = dayDifference+14;
		}
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 0, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 23, 59, 59);
		newDc.setStartTime(startTime);
		newDc.setEndTime(endTime);
		expectedAc = newDc;
		try{
			ac = Parser.parse("display schedules on next friday");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());	
		
		
		
		//........SEARCH TESTING...............................................
		
		
		//search meeting family in Korea on 10/6
		//multiple words description
		//single word place
		//dd/mm time format
		newSc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newSc = new SearchAction();
		newSc.setQuery("meeting family");
		newSc.setLocationQuery("Korea");
		startTime = Calendar.getInstance();
		startTime.set(Calendar.getInstance().get(Calendar.YEAR),5,10,0,0,0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR),5,10,23,59,59);
		newSc.setStartTime(startTime);
		newSc.setEndTime(endTime);
		expectedAc = newSc;
		try{
			ac = Parser.parse("search meeting family in Korea on 10/6");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//search swim in BBCC from 10/6 to 9/9
		//multiple words description
		//single word place
		//dd/mm time format
		newSc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newSc = new SearchAction();
		newSc.setQuery("swim");
		newSc.setLocationQuery("BBCC");
		startTime = Calendar.getInstance();
		startTime.set(Calendar.getInstance().get(Calendar.YEAR),5,10,0,0,0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR),8,9,23,59,59);
		newSc.setStartTime(startTime);
		newSc.setEndTime(endTime);
		expectedAc = newSc;
		try{
			ac = Parser.parse("search swim in BBCC from 10/6 to 9/9");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//search sports on mon from 1pm to 9pm
		//single word description
		//day, date format
		//timeframe flexible 12 hours format
		newSc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newSc = new SearchAction();
		newSc.setQuery("sports");
		startTime = Calendar.getInstance();
		dayDifference = 0;
		dayDifference = 2 - startTime.get(Calendar.DAY_OF_WEEK);
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
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 13, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 21, 0, 0);
		newSc.setStartTime(startTime);
		newSc.setEndTime(endTime);
		expectedAc = newSc;
		try{
			ac = Parser.parse("search sports on mon from 1pm to 9pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//search sports on wed from 1
		//single word description
		//day, date format
		//timeframe flexible 12 hours format
		newSc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newSc = new SearchAction();
		newSc.setQuery("sports");
		startTime = Calendar.getInstance();
		dayDifference = 0;
		dayDifference = 4 - startTime.get(Calendar.DAY_OF_WEEK);
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
		startTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 13, 0, 0);
		endTime = Calendar.getInstance();
		endTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + dayDifference, 23, 59, 59);
		newSc.setStartTime(startTime);
		newSc.setEndTime(endTime);
		expectedAc = newSc;
		try{
			ac = Parser.parse("search sports on wed from 1");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//.................. DELETE TESTING ................................
		

		//delete #3
		//single delete command
		newDelc=null;
		intArrayList = null;
		expectedAc=null;
		ac=null;
		
		
		newDelc = new DeleteAction();
		intArrayList = new ArrayList<Integer>();
		intArrayList.add(3);
		newDelc.setReferenceNumber(intArrayList);
		expectedAc = newDelc;
		try{
			ac = Parser.parse("delete #3");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//delete #3 #2 #11 #9
		//multiple delete command
		newDelc=null;
		intArrayList = null;
		expectedAc=null;
		ac=null;
		
		
		newDelc = new DeleteAction();
		intArrayList = new ArrayList<Integer>();
		intArrayList.add(3);
		intArrayList.add(2);
		intArrayList.add(11);
		intArrayList.add(9);
		newDelc.setReferenceNumber(intArrayList);
		expectedAc = newDelc;
		try{
			ac = Parser.parse("delete #3 #2 #11 #9");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		
		//delete all
		//single delete command
		newDelc=null;
		intArrayList = null;
		expectedAc=null;
		ac=null;
		
		
		newDelc = new DeleteAction();
		intArrayList = new ArrayList<Integer>();
		intArrayList.add(-1);
		newDelc.setReferenceNumber(intArrayList);
		expectedAc = newDelc;
		try{
			ac = Parser.parse("delete all");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());	

		//.................. MARK TESTING ....................................
		
		//mark #3 as done
		//single mark command
		newMc=null;
		intArrayList = null;
		expectedAc=null;
		ac=null;
		
		
		newMc = new MarkAction();
		intArrayList = new ArrayList<Integer>();
		intArrayList.add(3);
		newMc.setReferenceNumber(intArrayList);
		newMc.setStatus("done");
		expectedAc = newMc;
		try{
			ac = Parser.parse("mark #3 as done");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//mark #3 #21 #5 as undone
		//multiple mark command
		newMc=null;
		intArrayList = null;
		expectedAc=null;
		ac=null;
		
		
		newMc = new MarkAction();
		intArrayList = new ArrayList<Integer>();
		intArrayList.add(3);
		intArrayList.add(21);
		intArrayList.add(5);
		newMc.setReferenceNumber(intArrayList);
		newMc.setStatus("undone");
		expectedAc = newMc;
		try{
			ac = Parser.parse("mark #3 #21 #5 as undone");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		
		//.................. UPDATE TESTING ....................................
		
		//update format after the ">>" delimiter is exactly the same as add
		
		//update #2 >> pay hall fees at admin office on 21/1/2014 at 5
		//multiple words description
		//single word place
		//dd/mm time format
		newUc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newUc = new UpdateAction();
		newUc.setUpdatedQuery("pay hall fees");
		newUc.setUpdatedLocationQuery("admin office");
		newUc.setReferenceNumber(2);
		startTime = Calendar.getInstance();
		startTime.set(2014,0,21,17,0,0);
		endTime = Calendar.getInstance();
		endTime.set(2014,0,21,18,0,0);
		newUc.setUpdatedStartTime(startTime);
		newUc.setUpdatedEndTime(endTime);
		expectedAc = newUc;
		try{
			ac = Parser.parse("update #2 >> pay hall fees at admin office on 21/1/2014 at 5");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//update #2 >> sleep in my room on 21/1/2014 from 12am to 12pm
		//multiple words description
		//single word place
		//dd/mm time format
		newUc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newUc = new UpdateAction();
		newUc.setUpdatedQuery("sleep");
		newUc.setUpdatedLocationQuery("my room");
		newUc.setReferenceNumber(2);
		startTime = Calendar.getInstance();
		startTime.set(2014,0,21,0,0,0);
		endTime = Calendar.getInstance();
		endTime.set(2014,0,21,12,0,0);
		newUc.setUpdatedStartTime(startTime);
		newUc.setUpdatedEndTime(endTime);
		expectedAc = newUc;
		try{
			ac = Parser.parse("update #2 >> sleep in my room on 21/1/2014 from 12am to 12pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		
		
		//update format after the ">>" delimiter is exactly the same as add
		
		//update #3 >> jogging at park
		//multiple words description
		//single word place
		//dd/mm time format
		newUc=null;
		expectedAc=null;
		ac=null;
		startTime=null;
		endTime=null;
		
		newUc = new UpdateAction();
		newUc.setUpdatedQuery("jogging");
		newUc.setUpdatedLocationQuery("park");
		newUc.setReferenceNumber(3);
		newUc.setUpdatedStartTime(startTime);
		newUc.setUpdatedEndTime(endTime);
		expectedAc = newUc;
		try{
			ac = Parser.parse("update #3 >> jogging at park");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}
		
		assertEquals(expectedAc.toString(),ac.toString());
		
		//................... UNDO TESTING ..............................
		
		newUndc = null;
		expectedAc = null;
		ac = null;
		
		newUndc = new UndoAction();
		expectedAc = newUndc;
		try{
			ac = Parser.parse("undo");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}

		assertEquals(expectedAc.toString(),ac.toString());
		
		//.............. GCAL TESTING ...................
		
		//gcal quick add swimming monday at home 5 pm
		//gcal quick add command
		newGc = null;
		newAddGc = null;
		expectedAc = null;
		ac = null;
		
		newAddGc = new AddGoogleAction();
		newAddGc.setType(GoogleActionType.GOOGLE_ADD);
		newAddGc.setUserInput(" swimming monday at home 5 pm");
		expectedAc = newAddGc;
		try{
			ac = Parser.parse("google add swimming monday at home 5 pm");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}

		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//gcal sync latest calendar update on monday
		//gcal sync command
		newGc = null;
		newSyncGc = null;
		expectedAc = null;
		ac = null;
		
		newSyncGc = new SyncGoogleAction();
		newSyncGc.setType(GoogleActionType.GOOGLE_SYNC);
		expectedAc = newSyncGc;
		try{
			ac = Parser.parse("google sync");
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
		}

		assertEquals(expectedAc.toString(),ac.toString());
		
		
		//.............. ERROR HANDLING TESTING ...................
		
		
		
		boolean exceptionCaught;
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("Google asdf blah blah");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("addd blah blah");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
	
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday at 3as");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday a 3pm");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday from 3pm to 122pm");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday from 3pm to 1222pm");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		

		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday from 3pm to 5pm.");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday from 3pm twe 4pm");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on tues from 3pm - 400");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on mon , 32pm - 1500");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday, 12pm - 1500");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday at 12pm for 39 minsss");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday at 12pm for 2 hors 3 min");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on monday at 12pm fro 2 hours 3 min");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on mnoday");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 4dec");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on sdfec");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 4 dec 2403");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 4 dec2013");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on thursdy");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 21/21/2013");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 2/2/3");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 2/13");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		

		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 2//13");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add swimming on 2//13 from");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add reply mary by from 3pm to 4 pm");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add reply mary by mon 3pm");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("add reply mary by mon at 4432");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("display all on mon at 4432");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("display as");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("display undon on mon");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("display onn tues");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("display on tues a 5");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("delete 3");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("delete @3");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("delete #3#4");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("delete #3 , #2");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("delete #3 #2 ##1");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("update #1 >> task blah on mon at 332");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("update #1 to task blah on mon at 332");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("update 1 >> task blah on mon at 3");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("update !21 >> task blah on mon at 3");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("mark #1 3 as done");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("mark #1 ##3 as done");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("mark #1 #3 ass done");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("mark #1 as donee");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("mark #1 as 123done");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("mark #1 as uundone");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("search sw from adfsd");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
		
		
		
		exceptionCaught=false;
		ac=null;
		try{
			ac = Parser.parse("search sw on 3/3/12 at three");
			System.out.println(ac);
		}
		catch(MalformedUserInputException muie){
			System.out.println(muie);
			exceptionCaught=true;
		}
		assertEquals(true,exceptionCaught);
	}
}
