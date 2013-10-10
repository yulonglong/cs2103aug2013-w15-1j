package com.licensetokil.atypistcalendar.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Parser {

	public static Action parse(String userInput)
			throws MalformedUserInputException {

		// tokenize user input
		StringTokenizer st = new StringTokenizer(userInput);
		// get the actionType by getting the first token (first word) from the
		// user input
		String stringUserAction = new String(st.nextToken());
		GoogleActionType googleActionType = determineGoogleActionType(stringUserAction);
		// if GCAL type
		if (googleActionType == GoogleActionType.GCAL) {
			return gcalParser(st, userInput, stringUserAction);
		}
		// if not GCAL type (means Local type)
		else {
			SystemActionType systemActionType = determineSystemActionType(stringUserAction);
			if (systemActionType == SystemActionType.EXIT) {
				return new ExitAction();
			} else {
				LocalActionType localActionType = determineLocalActionType(stringUserAction);
				if (localActionType == LocalActionType.INVALID) {
					throw new MalformedUserInputException("Invalid Input!");
				} else {
					return localParser(st, localActionType);
				}
			}
		}
	}

	private static Action gcalParser(StringTokenizer st, String userInput,
			String stringUserAction) throws MalformedUserInputException {
		GoogleAction userAction = new GoogleAction();
		stringUserAction = stringUserAction + " " + st.nextToken();
		GoogleActionType actionType = determineGoogleActionType(stringUserAction);

		if (actionType == GoogleActionType.GCAL_SYNC) {
			userInput = new String(getRemainingTokens(st));
			userAction.setUserInput(userInput);
			return userAction;
		} else {
			stringUserAction = stringUserAction + " " + st.nextToken();
			actionType = determineGoogleActionType(stringUserAction);

			if (actionType == GoogleActionType.GCAL_QUICK_ADD) {
				userAction.setType(GoogleActionType.GCAL_QUICK_ADD);
				userInput = new String(getRemainingTokens(st));
				userAction.setUserInput(userInput);
				return userAction;
			} else {
				System.out.println("Error! Invalid GCAL Command!");
				throw new MalformedUserInputException("Invalid input!");
			}
		}
	}

	private static LocalAction localParser(StringTokenizer st,
			LocalActionType actionType) throws MalformedUserInputException {
		switch (actionType) {
		case ADD:
			return addParser(st);
		case DELETE:
			return deleteParser(st);
		case DISPLAY:
			return displayParser(st);
		case UPDATE:
			return updateParser(st);
		case SEARCH:
			return searchParser(st);
		case MARK:
			return markParser(st);
		default:
			throw new MalformedUserInputException("Invalid input!");
		}
	}

	// <time> format is completed, can follow those in user guide.
	// however am and pm is strictly without dots (a.m. is not accepted)
	// there must be no space between time and am or pm (e.g 3pm, 2.30pm)
	//
	// list of approved format:
	// "add swimming at CommunityClub on 30/12 from 1300 to 1400" (fixed not
	// flexible format)
	// "add swimming on 21/11 from 1300 to 1400" (without place)
	// "add swimming at Bukit Batok Community Club Swimming Pool on 21/11 from 1300 to 1400"
	// (long place string, separated by space)
	// now date can be detected either in single digits or double digits (e.g.
	// 2/1, 02/01, 12/01, 12/2, 3/11, etc)
	// "add swimming at BB CC on 2/1 from 1200 to 1300"
	// completed <time> format
	// "add swimming at BB CC on 2/1 from 1.33pm to 3.20pm"

	// add function : 25% done
	private static AddAction addParser(StringTokenizer st) {
		AddAction userAction = new AddAction();

		Calendar startTimeCal = Calendar.getInstance();
		Calendar endTimeCal = Calendar.getInstance();

		String description = new String(st.nextToken());
		String place = new String();
		String prep = new String(st.nextToken());

		// check if place is included in user input
		if (isValidPlacePreposition(prep)) {
			place = new String(st.nextToken());
			userAction.setPlace(place);
		} else {
			String tempUserInput = new String();
			tempUserInput = getRemainingTokens(st);
			tempUserInput = prep + " " + tempUserInput;
			st = new StringTokenizer(tempUserInput);
		}

		// check for place name, separated by space, and incorporate the proper
		// place name
		prep = new String(st.nextToken());
		while (!isValidDayPreposition(prep)) {
			place = place + " " + prep;
			userAction.setPlace(place);
			if (st.hasMoreTokens()) {
				prep = new String(st.nextToken());
			} else {
				break;
			}
		}

		String date = new String(st.nextToken());
		int[] intDate = new int[3];
		getDate(intDate, date);

		prep = new String(st.nextToken());
		String startTime = new String(st.nextToken());
		int intStartHour = getTimeHour(startTime);
		int intStartMinute = getTimeMinute(startTime);

		prep = new String(st.nextToken());
		String endTime = new String(st.nextToken());
		int intEndHour = getTimeHour(endTime);
		int intEndMinute = getTimeMinute(endTime);

		startTimeCal.set(intDate[2], intDate[1], intDate[0], intStartHour,
				intStartMinute, 0);
		endTimeCal.set(intDate[2], intDate[1], intDate[0], intEndHour,
				intEndMinute, 0);

		userAction.setStartTime(startTimeCal);
		userAction.setEndTime(endTimeCal);
		userAction.setDescription(description);

		return userAction;
	}

	// <time> format is completed,
	// explanation will be the same as add function above

	// approved format:
	// "display" will return display, startTime 1/1/2005 and endTime 31/12/2020
	// "display <place preposition> <Place>" is now available
	// "display at Bukit Batok"
	// "display in Computing Hall"
	// "display <day prep> <date>  <timeframe>" is now available (however
	// timeframe is strict)
	// "display on 1/3 from 1200 to 1300"
	// it can also be combined with place
	// "display at Bukit Batok on 1/3 from 1200 to 1300"
	// it accepts input without timeframe
	// "display <day prep> <date>"
	// "display on 10/6"
	// can be combined with place
	// "display in Korea on 10/12"

	// display function : 40 % done
	private static DisplayAction displayParser(StringTokenizer st) {
		DisplayAction userAction = new DisplayAction();

		int[] intStartDate = new int[3];
		int[] intEndDate = new int[3];
		intStartDate[2] = 2005;
		intStartDate[1] = 0;
		intStartDate[0] = 1;
		intEndDate[2] = 2020;
		intEndDate[1] = 11;
		intEndDate[0] = 31;

		Calendar startTimeCal = Calendar.getInstance();
		Calendar endTimeCal = Calendar.getInstance();

		// if command has more details after display
		// e.g. display "deadlines on monday"
		if (st.hasMoreTokens()) {

			String place = new String();
			String prep = new String(st.nextToken());

			// check if schedules/deadlines/todos is included in user input
			if (isValidTask(prep)) {
				userAction.setTaskType(prep);
			} else {// if not then return back the string
				String tempUserInput = new String();
				tempUserInput = getRemainingTokens(st);
				tempUserInput = prep + " " + tempUserInput;
				st = new StringTokenizer(tempUserInput);
			}
			
			// string place retrieval BEGIN
			if (st.hasMoreTokens()) {
				prep = new String(st.nextToken());
				// check if place is included in user input
				if (isValidPlacePreposition(prep)) {
					place = new String(st.nextToken());
					userAction.setPlace(place);
				} else {// if not place then return back the string
					String tempUserInput = new String();
					tempUserInput = getRemainingTokens(st);
					tempUserInput = prep + " " + tempUserInput;
					st = new StringTokenizer(tempUserInput);
				}
				// check for place name, separated by space, and incorporate the
				// proper place name
				prep = new String(st.nextToken());
				while (!isValidDayPreposition(prep)) {
					place = place + " " + prep;
					userAction.setPlace(place);
					if (st.hasMoreTokens()) {
						prep = new String(st.nextToken());
					} else {
						break;
					}
				}
			}
			
			// string place retrieval END
			
			// if there is a date field
			if (st.hasMoreTokens()) {
				String date = new String(st.nextToken());
				getDate(intStartDate, date);

				if (st.hasMoreTokens()) {
					prep = new String(st.nextToken());
					if (isValidTimePreposition(prep)) {
						String startTime = new String(st.nextToken());
						int intStartHour = getTimeHour(startTime);
						int intStartMinute = getTimeMinute(startTime);

						prep = new String(st.nextToken());
						String endTime = new String(st.nextToken());
						int intEndHour = getTimeHour(endTime);
						int intEndMinute = getTimeMinute(endTime);

						startTimeCal.set(intStartDate[2], intStartDate[1],
								intStartDate[0], intStartHour, intStartMinute,
								0);
						endTimeCal.set(intStartDate[2], intStartDate[1],
								intStartDate[0], intEndHour, intEndMinute, 0);
						
						userAction.setStartTime(startTimeCal);
						userAction.setEndTime(endTimeCal);
					}
				}
				else{
				startTimeCal.set(intStartDate[2], intStartDate[1],
						intStartDate[0], 0, 0, 0);

				userAction.setStartTime(startTimeCal);
				}
				
			}
		}
		return userAction;
	}

	// can delete with reference number
	// can delete with query(task description)
	// e.g.
	// "delete #1 #7 #4"
	// "delete group meeting project"
	// can delete multiple items separated with space
	// delete function : 100 %
	private static DeleteAction deleteParser(StringTokenizer st) {
		ArrayList<Integer> referenceNumber = new ArrayList<Integer>();
		DeleteAction userAction = new DeleteAction();
		String query = new String();
		String temp = new String(st.nextToken());
		if (temp.substring(0, 1).equals("#")) {
			temp = temp.substring(1);
			int tempInt = Integer.parseInt(temp);
			referenceNumber.add(tempInt);
			while (st.hasMoreTokens()) {
				temp = new String (st.nextToken());
				temp = temp.substring(1);
				tempInt = Integer.parseInt(temp);
				referenceNumber.add(tempInt);
			}
			userAction.setReferenceNumber(referenceNumber);
		}
		else{
			query = query + temp + " ";
			while (st.hasMoreTokens()) {
				temp = st.nextToken();
				query = query + temp + " ";
			}
			query = query.substring(0, query.length() - 1);// remove the last white space
			userAction.setQuery(query);
		}
		return userAction;
	}

	// update function: 10 %
	private static UpdateAction updateParser(StringTokenizer st) throws MalformedUserInputException {
		UpdateAction userAction = new UpdateAction();
		String query = new String();
		String temp = new String(st.nextToken());
		if (temp.substring(0, 1).equals("#")) {
			temp = temp.substring(1);
			int tempInt = Integer.parseInt(temp);
			userAction.setReferenceNumber(tempInt);
			
			temp = st.nextToken();
			while(!isValidUpdateDelimiter(temp)){
				throw new MalformedUserInputException ("Invalid Input!");
			}
		}
		else{
			while (!isValidUpdateDelimiter(temp)) {
				query = query + temp + " ";
				temp = st.nextToken();
			}
			query = query.substring(0, query.length() - 1);// remove the last white space
			userAction.setQuery(query);
		}
		
		//after finding the delimiter ">>"
		
		Calendar startTimeCal = Calendar.getInstance();
		Calendar endTimeCal = Calendar.getInstance();

		String description = new String(st.nextToken());
		userAction.setUpdatedQuery(description);
		
		String place = new String();
		String prep = new String(st.nextToken());

		// check if place is included in user input
		if (isValidPlacePreposition(prep)) {
			place = new String(st.nextToken());
			userAction.setUpdatedLocationQuery(place);
		} else {
			String tempUserInput = new String();
			tempUserInput = getRemainingTokens(st);
			tempUserInput = prep + " " + tempUserInput;
			st = new StringTokenizer(tempUserInput);
		}

		// check for place name, separated by space, and incorporate the proper
		// place name
		prep = new String(st.nextToken());
		while (!isValidDayPreposition(prep)) {
			place = place + " " + prep;
			userAction.setUpdatedLocationQuery(place);
			if (st.hasMoreTokens()) {
				prep = new String(st.nextToken());
			} else {
				break;
			}
		}

		String date = new String(st.nextToken());
		int[] intDate = new int[3];
		getDate(intDate, date);

		prep = new String(st.nextToken());
		String startTime = new String(st.nextToken());
		int intStartHour = getTimeHour(startTime);
		int intStartMinute = getTimeMinute(startTime);

		prep = new String(st.nextToken());
		String endTime = new String(st.nextToken());
		int intEndHour = getTimeHour(endTime);
		int intEndMinute = getTimeMinute(endTime);

		startTimeCal.set(intDate[2], intDate[1], intDate[0], intStartHour,
				intStartMinute, 0);
		endTimeCal.set(intDate[2], intDate[1], intDate[0], intEndHour,
				intEndMinute, 0);

		userAction.setUpdatedStartTime(startTimeCal);
		userAction.setUpdatedEndTime(endTimeCal);
		


		return userAction;
	}

	// can search with exact same format as display,
	// but without description or query yet.
	// search function: 0 %
	private static SearchAction searchParser(StringTokenizer st) {
		SearchAction userAction = new SearchAction();

		int[] intStartDate = new int[3];
		int[] intEndDate = new int[3];
		intStartDate[2] = 2005;
		intStartDate[1] = 0;
		intStartDate[0] = 1;
		intEndDate[2] = 2020;
		intEndDate[1] = 11;
		intEndDate[0] = 31;

		Calendar startTimeCal = Calendar.getInstance();
		Calendar endTimeCal = Calendar.getInstance();

		// if command has more details after display
		// e.g. display "deadlines on monday"
		if (st.hasMoreTokens()) {

			String place = new String();
			String prep = new String(st.nextToken());

			// check if schedules/deadlines/todos is included in user input
			if (isValidTask(prep)) {
				userAction.setTaskType(prep);
			} else {// if not then return back the string
				String tempUserInput = new String();
				tempUserInput = getRemainingTokens(st);
				tempUserInput = prep + " " + tempUserInput;
				st = new StringTokenizer(tempUserInput);
			}
			
			// string place retrieval BEGIN
			if (st.hasMoreTokens()) {
				prep = new String(st.nextToken());
				// check if place is included in user input
				if (isValidPlacePreposition(prep)) {
					place = new String(st.nextToken());
					userAction.setLocationQuery(place);
				} else {// if not place then return back the string
					String tempUserInput = new String();
					tempUserInput = getRemainingTokens(st);
					tempUserInput = prep + " " + tempUserInput;
					st = new StringTokenizer(tempUserInput);
				}
				// check for place name, separated by space, and incorporate the
				// proper place name
				prep = new String(st.nextToken());
				while (!isValidDayPreposition(prep)) {
					place = place + " " + prep;
					userAction.setLocationQuery(place);
					if (st.hasMoreTokens()) {
						prep = new String(st.nextToken());
					} else {
						break;
					}
				}
			}
			
			// string place retrieval END
			
			// if there is a date field
			if (st.hasMoreTokens()) {
				String date = new String(st.nextToken());
				getDate(intStartDate, date);

				if (st.hasMoreTokens()) {
					prep = new String(st.nextToken());
					if (isValidTimePreposition(prep)) {
						String startTime = new String(st.nextToken());
						int intStartHour = getTimeHour(startTime);
						int intStartMinute = getTimeMinute(startTime);

						prep = new String(st.nextToken());
						String endTime = new String(st.nextToken());
						int intEndHour = getTimeHour(endTime);
						int intEndMinute = getTimeMinute(endTime);

						startTimeCal.set(intStartDate[2], intStartDate[1],
								intStartDate[0], intStartHour, intStartMinute,
								0);
						endTimeCal.set(intStartDate[2], intStartDate[1],
								intStartDate[0], intEndHour, intEndMinute, 0);
						
						userAction.setStartTime(startTimeCal);
						userAction.setEndTime(endTimeCal);
					}
				}
				else{
				startTimeCal.set(intStartDate[2], intStartDate[1],
						intStartDate[0], 0, 0, 0);

				userAction.setStartTime(startTimeCal);
				}
				
			}
		}
		return userAction;
	}

	/*
	 * Mark accepts task id (#2,#12) and using the keyword as done or as undone
	 * accepts specific descriptions to replace task id. "mark #1 as done"
	 * "mark #1 #2 as done" "mark swimming as done"
	 * "mark group project meeting as undone"
	 */
	// mark function: 100 %
	private static MarkAction markParser(StringTokenizer st) {
		ArrayList<Integer> referenceNumber = new ArrayList<Integer>();
		MarkAction userAction = new MarkAction();
		String query = new String();
		String refNum = new String();
		String temp = new String(st.nextToken());
		if (temp.substring(0, 1).equals("#")) {
			temp = temp.substring(1);
			int tempInt = Integer.parseInt(temp);
			referenceNumber.add(tempInt);
			temp = new String(st.nextToken());
			while ((st.hasMoreTokens()) && (!isValidMarkPreposition(temp))) {
				refNum = new String(temp.substring(1));
				tempInt = Integer.parseInt(refNum);
				referenceNumber.add(tempInt);
				temp = new String(st.nextToken());
			}
			String status = new String(st.nextToken());
			userAction.setStatus(status);
			userAction.setReferenceNumber(referenceNumber);
		} else {
			while ((!isValidMarkPreposition(temp)) && (st.hasMoreTokens())) {
				query = query + temp + " ";
				temp = st.nextToken();
			}
			query = query.substring(0, query.length() - 1);// remove the last
															// white space
			String status = new String(st.nextToken());
			userAction.setQuery(query);
			userAction.setStatus(status);
		}
		return userAction;
	}

	private static int getTimeMinute(String time) {
		int intTimeMinute = 0;
		int stringTimeLength = time.length();
		boolean allDigits = true;
		for (int i = 0; (i < stringTimeLength); i++) {
			if (!Character.isDigit(time.charAt(i))) {
				allDigits = false;
			}
		}
		if (allDigits == true) {
			if (stringTimeLength <= 2) {
				return 0;
			} else {
				time = time.substring(2, 4);
				intTimeMinute = Integer.parseInt(time);
				return intTimeMinute;
			}
		} else {
			if (stringTimeLength <= 4) {
				return 0;
			}
			int indexOfDelimiter = 0;
			// get the index of delimiter
			for (int i = 0; (i < stringTimeLength); i++) {
				if (!Character.isDigit(time.charAt(i))) {
					indexOfDelimiter = i;
					break;
				}
			}
			time = time.substring(indexOfDelimiter + 1, indexOfDelimiter + 3);
			intTimeMinute = Integer.parseInt(time);
			return intTimeMinute;

		}
	}

	private static int getTimeHour(String time) {
		int intTimeHour = 0;
		int stringTimeLength = time.length();
		boolean allDigits = true;
		for (int i = 0; (i < stringTimeLength); i++) {
			if (!Character.isDigit(time.charAt(i))) {
				allDigits = false;
			}
		}
		if (allDigits == true) {
			if (stringTimeLength <= 2) {
				intTimeHour = Integer.parseInt(time);
				return intTimeHour;
			} else {
				time = time.substring(0, 2);
				intTimeHour = Integer.parseInt(time);
				return intTimeHour;
			}
		} else {
			String day = time.substring(stringTimeLength - 2);

			int indexOfDelimiter = 0;
			// get the index of delimiter
			for (int i = 0; (i < stringTimeLength); i++) {
				if (!Character.isDigit(time.charAt(i))) {
					indexOfDelimiter = i;
					break;
				}
			}
			if (indexOfDelimiter == 1) {
				time = time.substring(0, 1);
				intTimeHour = Integer.parseInt(time);
				if (day.equals("pm")) {
					intTimeHour = intTimeHour + 12;
				}
				return intTimeHour;
			} else {
				time = time.substring(0, 2);
				intTimeHour = Integer.parseInt(time);
				if (day.equals("pm")) {
					intTimeHour = intTimeHour + 12;
				}
				return intTimeHour;
			}
		}
	}

	// pass a user string of date in any format
	// and return date in integer format
	// using intDate array integer to store date as integers.
	// intDate[0] is daydate,
	// intDate[1] is month
	// intDate[2] is year
	private static void getDate(int[] intDate, String date) {
		int stringDateLength = date.length();

		int indexOfDelimiter = 0;

		// get the index of delimiter
		for (int i = 0; (i < stringDateLength) && (indexOfDelimiter == 0); i++) {
			if (!Character.isDigit(date.charAt(i))) {
				indexOfDelimiter = i;
			}
		}

		String strday = new String();
		strday = date.substring(0, indexOfDelimiter);
		intDate[0] = Integer.parseInt(strday);

		String strmonth = new String();
		strmonth = date.substring(indexOfDelimiter + 1);
		intDate[1] = Integer.parseInt(strmonth);

		intDate[1]--; // Calendar in java, stores month starting from 0
						// (january) to 11 (december)

		intDate[2] = 2013;

		return;
	}

	private static String getRemainingTokens(StringTokenizer strRemaining) {
		String strResult = new String();
		while (strRemaining.hasMoreTokens()) {
			strResult = strResult + " " + strRemaining.nextToken();
		}
		return strResult;
	}

	private static boolean isValidTask(String task) {
		if ((task.equalsIgnoreCase("schedules"))
				|| (task.equalsIgnoreCase("deadlines"))
				|| (task.equalsIgnoreCase("todos"))) {
			return true;
		}
		return false;
	}
	
	private static boolean isValidUpdateDelimiter(String delimiter) {
		if (delimiter.equalsIgnoreCase(">>")) {
			return true;
		}
		return false;
	}

	private static boolean isValidMarkPreposition(String preposition) {
		if (preposition.equalsIgnoreCase("as")) {
			return true;
		}
		return false;
	}

	private static boolean isValidPlacePreposition(String preposition) {
		if ((preposition.equalsIgnoreCase("in"))
				|| (preposition.equalsIgnoreCase("at"))) {
			return true;
		}
		return false;
	}

	private static boolean isValidDayPreposition(String preposition) {
		if ((preposition.equalsIgnoreCase("on"))
				|| (preposition.equalsIgnoreCase(","))) {
			return true;
		}
		return false;
	}

	private static boolean isValidTimePreposition(String preposition) {
		if ((preposition.equalsIgnoreCase("from"))
				|| (preposition.equalsIgnoreCase("at"))
				|| (preposition.equalsIgnoreCase(","))) {
			return true;
		}
		return false;
	}

	private static LocalActionType determineLocalActionType(
			String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");
		if (commandTypeString.equalsIgnoreCase(LocalActionType.ADD.getString())) {
			return LocalActionType.ADD;
		} else if (commandTypeString.equalsIgnoreCase(LocalActionType.DELETE
				.getString())) {
			return LocalActionType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase(LocalActionType.DISPLAY
				.getString())) {
			return LocalActionType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase(LocalActionType.UPDATE
				.getString())) {
			return LocalActionType.UPDATE;
		} else if (commandTypeString.equalsIgnoreCase(LocalActionType.SEARCH
				.getString())) {
			return LocalActionType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase(LocalActionType.MARK
				.getString())) {
			return LocalActionType.MARK;
		} else {
			return LocalActionType.INVALID;
		}
	}

	private static GoogleActionType determineGoogleActionType(
			String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error("command type string cannot be null!");
		} else if (commandTypeString.equalsIgnoreCase(GoogleActionType.GCAL
				.getString())) {
			return GoogleActionType.GCAL;
		} else if (commandTypeString
				.equalsIgnoreCase(GoogleActionType.GCAL_SYNC.getString())) {
			return GoogleActionType.GCAL_SYNC;
		} else if (commandTypeString
				.equalsIgnoreCase(GoogleActionType.GCAL_QUICK_ADD.getString())) {
			return GoogleActionType.GCAL_QUICK_ADD;
		} else {
			return GoogleActionType.INVALID;
		}
	}

	private static SystemActionType determineSystemActionType(
			String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");
		else if (commandTypeString.equalsIgnoreCase(SystemActionType.EXIT
				.getString())) {
			return SystemActionType.EXIT;
		} else {
			return SystemActionType.INVALID;
		}
	}
}
