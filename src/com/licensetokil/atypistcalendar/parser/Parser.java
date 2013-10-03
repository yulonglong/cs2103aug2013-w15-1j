package com.licensetokil.atypistcalendar.parser;

import java.util.Calendar;
import java.util.StringTokenizer;

public class Parser {

	public static Action parse(String userInput){
		//tokenize user input
		StringTokenizer st = new StringTokenizer(userInput);
		//get the actionType by getting the first token (first word) from the user input
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
	//add swimming at CommunityClub on 30/12 from 1300 to 1400 (fixed not flexible format)
	//add swimming on 21/11 from 1300 to 1400 (without place)
	//add swimming at Bukit Batok Community Club Swimming Pool on 21/11 from 1300 to 1400 (long place string, separated by space)
	//now date can be detected either in single digits or double digits (e.g. 2/1, 02/01, 12/01, 12/2, 3/11, etc)
	//add swimming at BB CC on 2/1 from 1200 to 1300
	private static boolean addParser(StringTokenizer st, LocalAction userAction){

		userAction.setType(ActionType.ADD);

		Calendar startTimeCal = Calendar.getInstance();
		Calendar endTimeCal = Calendar.getInstance();

		String description = new String(st.nextToken());
		String place = new String();
		String prep = new String (st.nextToken());
		
		//check if place is included in user input
		if(isValidPlacePreposition(prep)){
			place = new String(st.nextToken());
			userAction.setPlace(place);
		}
		else{
			String tempUserInput = new String();
			tempUserInput = getRemainingTokens(st);
			tempUserInput = prep + " " + tempUserInput;
			st = new StringTokenizer(tempUserInput);
		}
		
		//check for place name, separated by space, and incorporate the proper place name
		prep = new String (st.nextToken());
		while(!isValidDayPreposition(prep)){
			place = place + " " + prep;
			userAction.setPlace(place);
			prep = new String (st.nextToken());
		}
		
		String date = new String(st.nextToken());
		int[] intDate = new int[3];
		getDate(intDate,date);
		

		prep = new String (st.nextToken());
		String startTime = new String(st.nextToken());
		startTime = startTime.substring(0,2);
		int intStartTime = Integer.parseInt(startTime);

		prep = new String (st.nextToken());
		String endTime = new String(st.nextToken());
		endTime = endTime.substring(0,2);
		int intEndTime = Integer.parseInt(endTime);

		startTimeCal.set(intDate[2], intDate[1], intDate[0], intStartTime, 0);
		endTimeCal.set(intDate[2], intDate[1], intDate[0], intEndTime, 0);


		userAction.setStartTime(startTimeCal);
		userAction.setEndTime(endTimeCal);
		userAction.setDescription(description);
		

		return true;
	}
	
	//pass a user string of date in any format
	//and return date in integer format
	//using intDate array integer to store date as integers.
	//intDate[0] is daydate,
	//intDate[1] is month
	//intDate[2] is year
	private static void getDate(int[] intDate, String date){
		int stringDateLength = date.length();
	
		int indexOfDelimiter = 0;
		
		//get the index of delimiter
		for(int i=0;(i<stringDateLength)&&(indexOfDelimiter==0);i++){
			if(!Character.isDigit(date.charAt(i))){
				indexOfDelimiter = i;
			}
		}
		
		String strday = new String();
		strday = date.substring(0,indexOfDelimiter);
		intDate[0] = Integer.parseInt(strday);
		
		String strmonth = new String();
		strmonth = date.substring(indexOfDelimiter+1);
		intDate[1] = Integer.parseInt(strmonth);
		
		intDate[1]--; // Calendar in java, stores month starting from 0 (january) to 11 (december)
		
		intDate[2] = 2013;
		
		return;
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
	
	//delete function : 0 %
	LocalAction deleteParser(StringTokenizer st){
		LocalAction userAction = new LocalAction();
		return userAction;
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
	

	
	
	private static boolean isValidPlacePreposition(String preposition){
		if((preposition.equalsIgnoreCase("in"))||(preposition.equalsIgnoreCase("at"))){
			return true;
		}
		return false;
	}
	
	private static boolean isValidDayPreposition(String preposition){
		if((preposition.equalsIgnoreCase("on"))||(preposition.equalsIgnoreCase(","))){
			return true;
		}
		return false;
	}
	
	private static boolean isValidTimePreposition(String preposition){
		if((preposition.equalsIgnoreCase("from"))||(preposition.equalsIgnoreCase("at"))||(preposition.equalsIgnoreCase(","))){
			return true;
		}
		return false;
	}
	
	private static String getRemainingTokens(StringTokenizer strRemaining){
		String strResult = new String();
		while (strRemaining.hasMoreTokens()) {
			strResult = strResult + " " +strRemaining.nextToken();
		}
		return strResult;
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
}
