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
			return gcalParser(st);
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

	private static Action gcalParser(StringTokenizer st) throws MalformedUserInputException {
		String userInput = new String();
		String stringUserAction = new String("gcal");
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

	//<time> format is fully functional
	// add function : 90% done
	private static AddAction addParser(StringTokenizer st) {
		AddAction userAction = new AddAction();
		Calendar[] calendarArray = new Calendar[2];
		calendarArray[0] = null;
		calendarArray[1] = null;
		

		String tempDescription = new String();
		String description = new String(st.nextToken());
		userAction.setDescription(description);
		
		while(st.hasMoreTokens()){
			tempDescription = new String(st.nextToken());
			if((!isValidPlacePreposition(tempDescription))&&(!isValidDayPreposition(tempDescription))){
				description = description + " " + tempDescription;
				userAction.setDescription(description);
			}
			else{
				st = addStringToTokenizer(st,tempDescription);
				break;
			}
		}
		if(!st.hasMoreTokens()){
			return userAction;
		}
		String place = new String();
		String prep = new String(st.nextToken());

		// check if place is included in user input
		if (isValidPlacePreposition(prep)) {
			place = new String(st.nextToken());
			userAction.setPlace(place);
		} else {
			st = addStringToTokenizer(st,prep);
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


		// if there is a date field
		getCompleteDate(calendarArray,prep,st,LocalActionType.ADD);
		userAction.setStartTime(calendarArray[0]);
		userAction.setEndTime(calendarArray[1]);
		
	return userAction;
	}

	// <time> format is completed,
	// explanation will be the same as add function above

	// approved format:
	// "display" will return display, startTime today's date and time, and endTime 31/12/2099
	// "display <place preposition> <Place>" is now available
	// "display all at Bukit Batok"
	// "display all in Computing Hall"
	// "display <day prep> <date>  <timeframe>" is now available (however
	// timeframe is strict)
	// "display on 1/3 from 1200 to 1300"
	// it can also be combined with place
	// "display all at Bukit Batok on 1/3 from 1200 to 1300"
	// it accepts input without timeframe
	// "display <day prep> <date>"
	// "display all on 10/6"
	// can be combined with place
	// "display all in Korea on 10/12"

	// display function : 100 % done
	private static DisplayAction displayParser(StringTokenizer st) {
		DisplayAction userAction = new DisplayAction();

		
		Calendar[] calendarArray = new Calendar[2];
		calendarArray[0] = Calendar.getInstance();
		calendarArray[1] = null;

		// if command has more details after display
		// e.g. display "deadlines on monday"
		if (st.hasMoreTokens()) {

			String prep = new String(st.nextToken());
			
			if (isStringAll(prep)){
				userAction.setDescription("all");
				if(!st.hasMoreTokens()){
					calendarArray[1].set(2099, 11, 31, 23, 59, 59);
					
					userAction.setStartTime(calendarArray[0]);
					userAction.setEndTime(calendarArray[1]);
					return userAction;
				}
			}
			else {// if not then return back the string
				String tempUserInput = new String();
				tempUserInput = getRemainingTokens(st);
				tempUserInput = prep + " " + tempUserInput;
				st = new StringTokenizer(tempUserInput);
			}
			
			
			
			prep = new String(st.nextToken());
			// check if schedules/deadlines/todos is included in user input
			if (isValidTask(prep)) {
				userAction.setDescription(prep);
			}
			else if(isValidStatus(prep)){
				userAction.setStatus(prep);
			}
			else {// if not then return back the string
				String tempUserInput = new String();
				tempUserInput = getRemainingTokens(st);
				tempUserInput = prep + " " + tempUserInput;
				st = new StringTokenizer(tempUserInput);
			}
			
			String place = new String();
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
			getCompleteDate(calendarArray,prep,st,LocalActionType.DISPLAY);
			userAction.setStartTime(calendarArray[0]);
			userAction.setEndTime(calendarArray[1]);
		}
		else{
			calendarArray[1] = Calendar.getInstance();
			calendarArray[1].set(2099, 11, 31, 23, 59, 59);
			
			userAction.setStartTime(calendarArray[0]);
			userAction.setEndTime(calendarArray[1]);
			userAction.setDescription("all");
		}
		return userAction;
	}

	// can delete with reference number
	// can delete with query(task description)
	// e.g.
	// "delete #1 #7 #4"
	// can delete multiple items separated with space
	// delete function : 100 %
	private static DeleteAction deleteParser(StringTokenizer st) {
		ArrayList<Integer> referenceNumber = new ArrayList<Integer>();
		DeleteAction userAction = new DeleteAction();
		String temp = new String(st.nextToken());
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
		
	
		return userAction;
	}

	// update function: 100 %
	private static UpdateAction updateParser(StringTokenizer st) throws MalformedUserInputException {
		UpdateAction userAction = new UpdateAction();
		String temp = new String(st.nextToken());
		
		Calendar[] calendarArray = new Calendar[2];
		calendarArray[0] = Calendar.getInstance();
		calendarArray[1] = null;

		temp = temp.substring(1);
		int tempInt = Integer.parseInt(temp);
		userAction.setReferenceNumber(tempInt);
		
		temp = st.nextToken();
		while(!isValidUpdateDelimiter(temp)){
			throw new MalformedUserInputException ("Invalid Input!");
		}
		
		
		
		//after finding the delimiter ">>"
		String tempDescription = new String();
		String description = new String(st.nextToken());
		userAction.setUpdatedQuery(description);
		
		while(st.hasMoreTokens()){
			tempDescription = new String(st.nextToken());
			if((!isValidPlacePreposition(tempDescription))&&(!isValidDayPreposition(tempDescription))){
				description = description + " " + tempDescription;
				userAction.setUpdatedQuery(description);
			}
			else{
				st = addStringToTokenizer(st,tempDescription);
				break;
			}
		}
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
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

		getCompleteDate(calendarArray,prep,st,LocalActionType.UPDATE);
		userAction.setUpdatedStartTime(calendarArray[0]);
		userAction.setUpdatedEndTime(calendarArray[1]);

		return userAction;
	}

	// can search with exact same format as display,
	// but without description or query yet.
	// search function: 100 %
	private static SearchAction searchParser(StringTokenizer st) {
		SearchAction userAction = new SearchAction();
		
		Calendar[] calendarArray = new Calendar[2];
		calendarArray[0] = Calendar.getInstance();
		calendarArray[1] = null;

		// if command has more details after display
		// e.g. display "deadlines on monday"
		
		String query = new String(st.nextToken());
		userAction.setQuery(query);
		String tempQuery = new String();
		while(st.hasMoreTokens()){
			tempQuery = new String(st.nextToken());
			if((!isValidPlacePreposition(tempQuery))&&(!isValidDayPreposition(tempQuery))){
				query = query + " " + tempQuery;
				userAction.setQuery(query);
			}
			else{
				st = addStringToTokenizer(st,tempQuery);
				break;
			}
		}
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		String prep = new String();
		String place = new String();
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
		getCompleteDate(calendarArray,prep,st,LocalActionType.SEARCH);
		userAction.setStartTime(calendarArray[0]);
		userAction.setEndTime(calendarArray[1]);
		
		return userAction;
	}

	/*
	 * Mark accepts task id (#2,#12) 
	 * accepts specific descriptions to replace task id. "mark #1 as done"
	 * "mark #1 #2 as done"
	 */
	// mark function: 100 %
	private static MarkAction markParser(StringTokenizer st) {
		ArrayList<Integer> referenceNumber = new ArrayList<Integer>();
		MarkAction userAction = new MarkAction();
		String refNum = new String();
		String temp = new String(st.nextToken());
		temp = temp.substring(1);
		int tempInt = Integer.parseInt(temp);
		referenceNumber.add(tempInt);
		temp = new String(st.nextToken());
		while ((st.hasMoreTokens()) &&(!isValidMarkPreposition(temp))){
			refNum = new String(temp.substring(1));
			tempInt = Integer.parseInt(refNum);
			referenceNumber.add(tempInt);
			temp = new String(st.nextToken());
		}
		String status = new String(st.nextToken());
		userAction.setStatus(status);
		userAction.setReferenceNumber(referenceNumber);
		
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
		}
		else {
			int indexOfDelimiter = 0;
			// get the index of delimiter
			for (int i = 0; (i < stringTimeLength); i++) {
				if (!Character.isDigit(time.charAt(i))) {
					indexOfDelimiter = i;
					break;
				}
			}
			if(indexOfDelimiter + 3 <= stringTimeLength){
				time = time.substring(indexOfDelimiter + 1, indexOfDelimiter + 3);
				if((Character.isDigit(time.charAt(0)))&&(Character.isDigit(time.charAt(1)))){
					intTimeMinute = Integer.parseInt(time);	
					return intTimeMinute;
			    }
			}
			return 0;
		}
	}

	private static int getTimeHour(String time) {
		String suffix = new String();
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
				if(intTimeHour<8){
					//add 12 hrs based on assumption, 12:00-07.59, pm suffix will be assumed
					intTimeHour = intTimeHour + 12;
				}
				return intTimeHour;
			} else {
				time = time.substring(0, 2);
				intTimeHour = Integer.parseInt(time);
				return intTimeHour;
			}
		}
		else {
			int indexOfDelimiter = 0;
			int indexOfSuffixDelimiter = 0;
			// get the index of suffix delimiter
			for (int i = (stringTimeLength-1); (i >= 0); i--) {
				if (Character.isDigit(time.charAt(i))) {
					indexOfSuffixDelimiter = i+1;
					break;
				}
			}
			if(indexOfSuffixDelimiter!=stringTimeLength){
				suffix = time.substring(indexOfSuffixDelimiter);
			}
			
			//get the index of hour and minute delimiter
			for (int i = 0; (i < stringTimeLength); i++) {
				if (!Character.isDigit(time.charAt(i))) {
					indexOfDelimiter = i;
					break;
				}
			}
			
			if (indexOfDelimiter == 1) {
				//if only single digit hour
				time = time.substring(0, 1);
			}
			else{
				//if double digits hour
				time = time.substring(0, 2);
			}
			
			intTimeHour = Integer.parseInt(time);
			if (isPm(suffix)) {
				intTimeHour = intTimeHour + 12;
			}
			else if(suffix.equals("")){
				if(intTimeHour<8){
					//add 12 hrs based on assumption, 12:00-07.59, pm suffix will be assumed
					intTimeHour = intTimeHour + 12;
				}
			}
			
			return intTimeHour;
			
		}
	}
	
	private static int getDayOfWeek(String day){
		if((day.equalsIgnoreCase("sunday"))
		||(day.equalsIgnoreCase("sun"))){
			return 1;
		}
		if((day.equalsIgnoreCase("monday"))
				||(day.equalsIgnoreCase("mon"))){
			return 2;
		}
		if((day.equalsIgnoreCase("tuesday"))
				||(day.equalsIgnoreCase("tue"))){
			return 3;
		}
		if((day.equalsIgnoreCase("wednesday"))
				||(day.equalsIgnoreCase("wed"))){
			return 4;
		}
		if((day.equalsIgnoreCase("thursday"))
				||(day.equalsIgnoreCase("thu"))){
			return 5;
		}
		if((day.equalsIgnoreCase("friday"))
				||(day.equalsIgnoreCase("fri"))){
			return 6;
		}
		if((day.equalsIgnoreCase("saturday"))
				||(day.equalsIgnoreCase("sat"))){
			return 7;
		}
		else{
			return -1;
		}
	}
	
	private static void getDateFromDay(int[] intStartDate, String eventDay, int intEventTimeHours, int intEventTimeMinutes){
		Calendar currentDate = Calendar.getInstance();
		int intCurrentDayOfWeek = -1;
		intCurrentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
		int intCurrentTimeHours = -1;
		intCurrentTimeHours = currentDate.get(Calendar.HOUR);
		int intCurrentTimeMinutes = -1;
		intCurrentTimeMinutes = currentDate.get(Calendar.MINUTE);
		
		int intEventDayOfWeek = -1;
		intEventDayOfWeek = getDayOfWeek(eventDay);
		if(intCurrentDayOfWeek==intEventDayOfWeek){
			if(intEventTimeHours==intCurrentTimeHours){
				if(intEventTimeMinutes<=intCurrentTimeMinutes){
					intStartDate[0] = currentDate.get(Calendar.DATE)+7;
					intStartDate[1] = currentDate.get(Calendar.MONTH);
					intStartDate[2] = currentDate.get(Calendar.YEAR);
					return;
				}
				else{
					intStartDate[0] = currentDate.get(Calendar.DATE);
					intStartDate[1] = currentDate.get(Calendar.MONTH);
					intStartDate[2] = currentDate.get(Calendar.YEAR);
					return;
				}
			}
			else if(intEventTimeHours<=intCurrentTimeHours){
				intStartDate[0] = currentDate.get(Calendar.DATE)+7;
				intStartDate[1] = currentDate.get(Calendar.MONTH);
				intStartDate[2] = currentDate.get(Calendar.YEAR);
				return;
			}
			else{
				intStartDate[0] = currentDate.get(Calendar.DATE);
				intStartDate[1] = currentDate.get(Calendar.MONTH);
				intStartDate[2] = currentDate.get(Calendar.YEAR);
				return;
			}
		}
		//if the day stated is not the same as today's day
		else{
			int dayDifference = intEventDayOfWeek - intCurrentDayOfWeek;
			//to make sure the number is positive
			if(dayDifference<0){
				dayDifference = intEventDayOfWeek + 7 - intCurrentDayOfWeek;
			}
			intStartDate[0] = currentDate.get(Calendar.DATE)+dayDifference;
			intStartDate[1] = currentDate.get(Calendar.MONTH);
			intStartDate[2] = currentDate.get(Calendar.YEAR);
			return;
		}
	}
	
	private static void getDate(int[] intStartDate, String date){
		//get the date start
		int stringDateLength = date.length();
		int indexOfDelimiter = 0;
		int indexOfDelimiter2 = 0;

		// get the index of delimiter
	
		for (int i = 0; (i < stringDateLength) && (indexOfDelimiter == 0); i++) {
			if (!Character.isDigit(date.charAt(i))) {
				indexOfDelimiter = i;
			}
		}

		String strday = new String();
		strday = date.substring(0, indexOfDelimiter);
		intStartDate[0] = Integer.parseInt(strday);

		

		for (int i = indexOfDelimiter+1; (i < stringDateLength); i++) {
			if (!Character.isDigit(date.charAt(i))) {
				indexOfDelimiter2 = i;
			}
		}
		
		String strmonth = new String();
		String stryear = new String();
		//if there is a year format
		if(indexOfDelimiter2 != 0){
			strmonth = date.substring(indexOfDelimiter + 1, indexOfDelimiter2);
			
			intStartDate[1] = Integer.parseInt(strmonth);
			intStartDate[1]--; // Calendar in java, stores month starting from 0
							   // (january) to 11 (december)
			
			stryear = date.substring(indexOfDelimiter2 + 1);
			if(stryear.length()==2){
				stryear = "20" + stryear;
			}
			intStartDate[2] = Integer.parseInt(stryear);
		}
		//if there is no year (assume it is the current year);
		else{
			strmonth = date.substring(indexOfDelimiter + 1);
			
			intStartDate[1] = Integer.parseInt(strmonth);
			intStartDate[1]--; // Calendar in java, stores month starting from 0
							   // (january) to 11 (december)
			
			intStartDate[2] = Calendar.getInstance().get(Calendar.YEAR);
		}
		
		//get date end
	}
	
	//<time> format is fully functional
	//e.g. 5 pm, 5pm, 12pm, 1a.m., 1200, 0800, etc

	// pass a user string of date in any format
	// and return date in Calendar format
	// using calendarArray to store date as integers.
	// calendarArray[0] is startTime
	// calendarArray[1] is endTime
	private static void getCompleteDate(Calendar[] calendarArray, String preposition, StringTokenizer st, LocalActionType actionType) {
		Calendar startTimeCal = calendarArray[0];
		Calendar endTimeCal = calendarArray[1];
		int[] intStartDate = new int[3];
		int[] intEndDate = new int[3];
		intStartDate[2] = 2005;
		intStartDate[1] = 0;
		intStartDate[0] = 1;
		intEndDate[2] = 2099;
		intEndDate[1] = 11;
		intEndDate[0] = 31;

		// if there is a date field
		if (st.hasMoreTokens()) {
			String date = new String(st.nextToken());
			if(Character.isDigit(date.charAt(0))){
				getDate(intStartDate,date);
			}
			if (st.hasMoreTokens()) {
				String prep = new String(st.nextToken());
				if (isValidTimePreposition(prep)) {
					String startTime = new String(st.nextToken());
					String temp = new String();
					if(st.hasMoreTokens()){
						temp = new String(st.nextToken());
						if(isValidTimeSuffix(temp)){
							startTime = startTime + temp;
						}
						else{
							st = addStringToTokenizer(st,temp);
						}
					}
					int intStartHour = getTimeHour(startTime);
					int intStartMinute = getTimeMinute(startTime);

					if(!Character.isDigit(date.charAt(0))){
						getDateFromDay(intStartDate,date,intStartHour,intStartMinute);
					}
					
					if(st.hasMoreTokens()){
						prep = new String(st.nextToken());
						//if it is a duration time format
						if(isValidTimeDurationPreposition(prep)){
							String stringTime = new String();
							int intHourDuration = 0;
							int intMinuteDuration = 0;
							while(st.hasMoreTokens()){
								stringTime = new String(st.nextToken());
								String timeUnit = new String(st.nextToken());
								if(isStringHour(timeUnit)){
									intHourDuration = Integer.parseInt(stringTime);
								}
								if(isStringMinute(timeUnit)){
									intMinuteDuration = Integer.parseInt(stringTime);
								}
							}
							startTimeCal = Calendar.getInstance();
							endTimeCal = Calendar.getInstance();
		
							startTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], intStartHour, intStartMinute, 0);
							endTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], intStartHour+intHourDuration, intStartMinute+intMinuteDuration, 0);
						}
						//if it is another time format
						else{
							String endTime = new String(st.nextToken());
							if(st.hasMoreTokens()){
								temp = new String(st.nextToken());
								if(isValidTimeSuffix(temp)){
									endTime = endTime + temp;
								}
								else{
									st = addStringToTokenizer(st,temp);
								}
							}
							int intEndHour = getTimeHour(endTime);
							int intEndMinute = getTimeMinute(endTime);
							startTimeCal = Calendar.getInstance();
							endTimeCal = Calendar.getInstance();
		
							startTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], intStartHour, intStartMinute, 0);
							endTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], intEndHour, intEndMinute, 0);
						}
					}
					else if(isValidDeadlinePreposition(preposition)){
						endTimeCal = Calendar.getInstance();
						endTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], intStartHour, intStartMinute, 0);
					}
					else{
						startTimeCal = Calendar.getInstance();
						startTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], intStartHour, intStartMinute, 0);
						endTimeCal = Calendar.getInstance();
						endTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], intStartHour+1, intStartMinute, 0);
					}
				}
			}
			else if((actionType.getString().equalsIgnoreCase("add"))&&(!isValidDeadlinePreposition(preposition))){
				if(!Character.isDigit(date.charAt(0))){
					getDateFromDay(intStartDate,date,8,0);
				}
				startTimeCal = Calendar.getInstance();
				startTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], 8, 0, 0);
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], 9 ,0, 0);
			}
			else{
				if(!Character.isDigit(date.charAt(0))){
					getDateFromDay(intStartDate,date,0,0);
				}
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[2], intStartDate[1], intStartDate[0], 23, 59, 59);
			}
			
		}
		else{
			endTimeCal = Calendar.getInstance();
			endTimeCal.set(intEndDate[2], intEndDate[1], intEndDate[0], 23, 59, 59);
		}
		
		calendarArray[0] = startTimeCal;
		calendarArray[1] = endTimeCal;
		return;
	}

	private static String getRemainingTokens(StringTokenizer strRemaining) {
		String strResult = new String();
		while (strRemaining.hasMoreTokens()) {
			strResult = strResult + " " + strRemaining.nextToken();
		}
		return strResult;
	}
	
	private static StringTokenizer addStringToTokenizer(StringTokenizer st, String tempString){
		String tempUserInput = new String();
		tempUserInput = getRemainingTokens(st);
		tempUserInput = tempString + " " + tempUserInput;
		return new StringTokenizer(tempUserInput);
	}
	
	private static boolean isPm(String suffix){
		if((suffix.equalsIgnoreCase("pm"))
				||(suffix.equalsIgnoreCase("p.m."))){
			return true;
		}
		return false;
	}
	
	private static boolean isValidTimeSuffix(String suffix){
		if((suffix.equalsIgnoreCase("pm"))
			||(suffix.equalsIgnoreCase("p.m."))
			||(suffix.equalsIgnoreCase("am"))
			||(suffix.equalsIgnoreCase("a.m."))){
			return true;
		}
		return false;
	}
	
	private static boolean isStringMinute(String timeUnit){
		if ((timeUnit.equalsIgnoreCase("minute"))
			||(timeUnit.equalsIgnoreCase("minutes"))
			||(timeUnit.equalsIgnoreCase("min"))
			||(timeUnit.equalsIgnoreCase("mins"))) {
				return true;
		}
		return false;
	}
	
	private static boolean isStringHour(String timeUnit){
		if ((timeUnit.equalsIgnoreCase("hour"))
			||(timeUnit.equalsIgnoreCase("hours"))
			||(timeUnit.equalsIgnoreCase("hr"))
			||(timeUnit.equalsIgnoreCase("hrs"))) {
				return true;
		}
		return false;
	}
	
	private static boolean isStringAll(String task){
		if (task.equalsIgnoreCase("all")){
			return true;
		}
		return false;
	}
	
	private static boolean isValidStatus(String status){
		if ((status.equalsIgnoreCase("done"))
				|| (status.equalsIgnoreCase("undone"))) {
			return true;
		}
		return false;
	}

	private static boolean isValidTask(String task) {
		if ((task.equalsIgnoreCase("schedules"))
				|| (task.equalsIgnoreCase("deadlines"))
				|| (task.equalsIgnoreCase("todos"))
				|| (task.equalsIgnoreCase("all"))) {
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
	
	private static boolean isValidDeadlinePreposition(String preposition){
		if (preposition.equalsIgnoreCase("by")){
			return true;
		}
		return false;
	}

	private static boolean isValidDayPreposition(String preposition) {
		if ((preposition.equalsIgnoreCase("on"))
				|| (preposition.equalsIgnoreCase("by"))
				|| (preposition.equalsIgnoreCase(","))) {
			return true;
		}
		return false;
	}
	
	private static boolean isValidTimeDurationPreposition(String preposition){
		if(preposition.equalsIgnoreCase("for")){
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
