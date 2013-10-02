package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;
import java.util.StringTokenizer;

public class Parser {

	public static Action parse(String userInput){
		StringTokenizer st = new StringTokenizer(userInput);
		String stringUserAction = new String(st.nextToken());
		ActionType actionType = determineActionType(stringUserAction);
		
		//if GCAL type
		if(actionType == ActionType.GCAL){
			return gcalParser(st,userInput,stringUserAction);
		}
		//if not GCAL type (means Local type)
		else{
			return localParser(st,actionType);
		}
	}

	private static ActionType determineActionType(String commandTypeString) {
		//i am so sorry Ian, i cannot use switch case, because they need constant value
		//if i want to use my string value from the Enum, it has to use if (because the String has a non constant value)
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");
		if (commandTypeString.equalsIgnoreCase(ActionType.ADD.getString())) {
			return ActionType.ADD;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.DELETE.getString())) {
			return ActionType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.DISPLAY.getString())) {
			return ActionType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.UPDATE.getString())) {
			return ActionType.UPDATE;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.SEARCH.getString())) {
			return ActionType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.MARK.getString())) {
			return ActionType.MARK;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.EXIT.getString())) {
			return ActionType.EXIT;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.GCAL.getString())) {
			return ActionType.GCAL;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.GCAL_SYNC.getString())) {
			return ActionType.GCAL_SYNC;
		} else if (commandTypeString.equalsIgnoreCase(ActionType.GCAL_QUICK_ADD.getString())) {
			return ActionType.GCAL_QUICK_ADD;
		} else {
			return ActionType.INVALID;
		}
	}

	private static String getRemainingTokens(StringTokenizer strRemaining){
		String strResult = new String();
		while (strRemaining.hasMoreTokens()) {
			strResult = strResult + " " +strRemaining.nextToken();
		}
		return strResult;
	}
	
	private static Action gcalParser(StringTokenizer st, String userInput, String stringUserAction){
		GoogleAction userAction = new GoogleAction();
		stringUserAction = stringUserAction + " " + st.nextToken();
		ActionType actionType = determineActionType(stringUserAction);

		if(actionType == ActionType.GCAL_SYNC){
			userInput = new String(getRemainingTokens(st));
			userAction.setUserInput(userInput);
			return userAction;
		}
		else{
			stringUserAction = stringUserAction + " " + st.nextToken();
			actionType = determineActionType(stringUserAction);

			if(actionType == ActionType.GCAL_QUICK_ADD){
				userAction.setType(ActionType.GCAL_QUICK_ADD);
				userInput = new String(getRemainingTokens(st));
				userAction.setUserInput(userInput);
				return userAction;
			}
			else{
				System.out.println("Error! Invalid GCAL Command!");
				userAction.setType(ActionType.INVALID);
				return userAction;
			}
		}
	}
	
	private static LocalAction localParser(StringTokenizer st, ActionType actionType){
		LocalAction userAction = new LocalAction();
		switch(actionType){
		case ADD:
			addParser(st,userAction);
			break;
		case DELETE:
			break;
		case DISPLAY:
			displayParser(st,userAction);
			break;
		case UPDATE:
			break;
		case SEARCH:
			break;
		case MARK:
			break;
		case EXIT:
			userAction.setType(ActionType.EXIT);
			break;
		default:
			userAction.setType(ActionType.INVALID);
			break;
		} 
		return userAction;
	}
	
	//add function : 10% done
	
	//list of approved format:
	//add swimming at CommunityClub on 21/11 from 1300 to 1400
	private static boolean addParser(StringTokenizer st, LocalAction userAction){

		userAction.setType(ActionType.ADD);

		Calendar startTimeCal = Calendar.getInstance();
		Calendar endTimeCal = Calendar.getInstance();

		String description = new String(st.nextToken());

		String prep = new String (st.nextToken());
		String place = new String(st.nextToken());


		prep = new String (st.nextToken());
		String date = new String(st.nextToken());
		String strday = new String();
		strday = date.substring(0,2);
		int day = Integer.parseInt(strday);
		String strmonth = new String();
		strmonth = date.substring(3,5);
		int month = Integer.parseInt(strmonth);
		int year = 2013;

		prep = new String (st.nextToken());
		String startTime = new String(st.nextToken());
		startTime = startTime.substring(0,2);
		int intStartTime = Integer.parseInt(startTime);

		prep = new String (st.nextToken());
		String endTime = new String(st.nextToken());
		endTime = endTime.substring(0,2);
		int intEndTime = Integer.parseInt(endTime);

		startTimeCal.set(year, month, day, intStartTime, 0);
		endTimeCal.set(year, month, day, intEndTime, 0);


		userAction.setStartTime(startTimeCal);
		userAction.setEndTime(endTimeCal);
		userAction.setDescription(description);
		userAction.setPlace(place);

		return true;
	}
	
	//delete function : 0 %
	LocalAction deleteParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}
	
	//display function : 10 %
	private static boolean displayParser(StringTokenizer st, LocalAction userAction){
		userAction.setType(ActionType.DISPLAY);

		Calendar startTimeCal = Calendar.getInstance();
		Calendar endTimeCal = Calendar.getInstance();

		String prep = new String (st.nextToken());
		String date = new String(st.nextToken());
		String strday = new String();
		strday = date.substring(0,2);
		int day = Integer.parseInt(strday);
		String strmonth = new String();
		strmonth = date.substring(3,5);
		int month = Integer.parseInt(strmonth);
		int year = 2013;

		prep = new String (st.nextToken());
		String startTime = new String(st.nextToken());
		startTime = startTime.substring(0,2);
		int intStartTime = Integer.parseInt(startTime);

		prep = new String (st.nextToken());
		String endTime = new String(st.nextToken());
		endTime = endTime.substring(0,2);
		int intEndTime = Integer.parseInt(endTime);

		startTimeCal.set(year, month, day, intStartTime, 0);
		endTimeCal.set(year, month, day, intEndTime, 0);


		userAction.setStartTime(startTimeCal);
		userAction.setEndTime(endTimeCal);

		return true;
	}
	//update function: 0 %
	LocalAction updateParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}
	//search function: 0 %
	LocalAction searchParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}
	//mark function: 0 %
	LocalAction markParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}
}
