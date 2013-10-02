package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;
import java.util.StringTokenizer;

public class Parser {

	public static Action parse(String userInput){
		StringTokenizer st = new StringTokenizer(userInput);
		String stringUserAction;
		stringUserAction = new String(st.nextToken());

		ActionType actionType = determineActionType(stringUserAction);

		//if GCAL type
		if(actionType == ActionType.GCAL){
			GoogleAction userAction = new GoogleAction();
			stringUserAction = stringUserAction + " " + st.nextToken();
			actionType = determineActionType(stringUserAction);

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
		//if not GCAL type (means Local type)
		else{
			LocalAction userAction = new LocalAction();
			switch(actionType){
			case ADD:
				addParser(st,userAction);
				break;
			case DELETE:
				//userAction = deleteParser(st);
				break;
			case DISPLAY:
				displayParser(st,userAction);
				break;
			case UPDATE:
				//userAction = updateParser(st);
				break;
			case SEARCH:
				//userAction = searchParser(st);
				break;
			case MARK:
				//userAction = markParser(st);
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
	}

	private static String ADD = "add";
	private static String DELETE = "delete";
	private static String DISPLAY = "display";
	private static String UPDATE = "update";
	private static String SEARCH = "search";
	private static String MARK = "mark";
	private static String EXIT = "exit";
	private static String GCAL = "gcal";
	private static String GCAL_SYNC = "gcal sync";
	private static String GCAL_QUICK_ADD = "gcal quick add";

	private static ActionType determineActionType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");
		if (commandTypeString.equalsIgnoreCase(ADD)) {
			return ActionType.ADD;
		} else if (commandTypeString.equalsIgnoreCase(DELETE)) {
			return ActionType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase(DISPLAY)) {
			return ActionType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase(UPDATE)) {
			return ActionType.UPDATE;
		} else if (commandTypeString.equalsIgnoreCase(SEARCH)) {
			return ActionType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(MARK)) {
			return ActionType.MARK;
		} else if (commandTypeString.equalsIgnoreCase(EXIT)) {
			return ActionType.EXIT;
		} else if (commandTypeString.equalsIgnoreCase(GCAL)) {
			return ActionType.GCAL;
		} else if (commandTypeString.equalsIgnoreCase(GCAL_SYNC)) {
			return ActionType.GCAL_SYNC;
		} else if (commandTypeString.equalsIgnoreCase(GCAL_QUICK_ADD)) {
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
	LocalAction deleteParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}
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
	LocalAction updateParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}
	LocalAction searchParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}
	LocalAction markParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
	}




}
