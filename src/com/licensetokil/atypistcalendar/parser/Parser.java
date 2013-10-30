package com.licensetokil.atypistcalendar.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class Parser {
	
	private static final String MESSAGE_INVALID = "Invalid input!";
	private static final String MESSAGE_INVALID_REF_NUMBER = "Invalid Reference Number entered!";
	private static final String MESSAGE_INVALID_GCAL = "Invalid Google Calendar Command!";
	
	private static final int DELETE_ALL_REF_NUMBER = -1;

	private static final int FIRST_INDEX = 0;
	private static final int SECOND_INDEX = 1;
	private static final int THIRD_INDEX = 2;
	private static final int FOURTH_INDEX = 3;
	private static final int FIFTH_INDEX = 4;
	private static final String WHITE_SPACE = " ";
	private static final String SLASH = "/";
	private static final String HEX = "#";
	private static final String EMPTY_STRING ="";
	
	private static final String PREP_FOR = "for";
	private static final String PREP_AS = "as";
	private static final String PREP_ON = "on";
	private static final String PREP_IN = "in";
	private static final String PREP_AT = "at";
	private static final String PREP_BY = "by";
	private static final String PREP_FROM = "from";
	private static final String PREP_COMMA = ",";
	private static final String PREP_DASH = "-";
	private static final String PREP_UPDATE = ">>";
	
	private static final int INVALID_INT_VALUE = -1;
	private static final int INIT_INT_VALUE = 0;
	
	private static final int INDEX_START_TIME = 0;
	private static final int INDEX_END_TIME = 1;
	private static final int INDEX_ST = 0;
	private static final int DEFAULT_CAL_ARR_SIZE = 2;
	private static final int DEFAULT_DATE_ARR_SIZE = 3;
	private static final int DEFAULT_ST_ARR_SIZE = 1;
	
	private static final int INDEX_YEAR = 2;
	private static final int INDEX_MONTH = 1;
	private static final int INDEX_DAY = 0;
	
	private static final int DEFAULT_YEAR_LENGTH = 2;
	private static final int COMPLETE_YEAR_LENGTH = 4;
	private static final int DEFAULT_HOUR_LENGTH = 2;
	private static final int DEFAULT_MINUTE_LENGTH = 2;
	private static final int DEFAULT_START_HOUR = 8;
	private static final int DEFAULT_START_MINUTE = 0;
	private static final int DEFAULT_START_SECOND = 0;
	private static final int DEFAULT_END_HOUR = 9;
	private static final int DEFAULT_END_MINUTE = 0;
	private static final int DEFAULT_END_SECOND = 0;
	private static final int DEFAULT_DURATION_HOUR = 1;
	
	private static final int MIN_HOUR = 0;
	private static final int MIN_MINUTE = 0;
	private static final int MIN_SECOND = 0;
	private static final int MIN_DAY = 1;
	private static final int MIN_MONTH = 0;
	private static final int MIN_YEAR = 2000;
	private static final int MIN_MILLISECOND = 0;
	
	private static final int MAX_HOUR = 23;
	private static final int MAX_MINUTE = 59;
	private static final int MAX_SECOND = 59;
	private static final int MAX_DAY = 31;
	private static final int MAX_MONTH = 11;
	private static final int MAX_YEAR = 2099;
	
	private static final String YEAR_PREFIX = "20";
	
	private static final int TIME_FORMAT_DIFF = 12;
	private static final int TIME_DEFAULT_MIN_AM = 8;
	private static final int TIME_DEFAULT_MAX_AM = 11;
	private static final int TIME_DEFAULT_MIDNIGHT = 0;
	private static final int TIME_DEFAULT_MIN_PM = 12;
	private static final int TIME_DEFAULT_MAX_PM = 7;
	
	private static final String AM_SHORT = "am";
	private static final String PM_SHORT = "pm";
	private static final String AM_LONG = "a.m.";
	private static final String PM_LONG ="p.m.";
	
	private static final String ALL = "all";
	private static final String SCHEDULES = "schedules";
	private static final String SCHEDULES_SINGULAR = "schedule";
	private static final String SCHEDULES_SHORT = "sch";
	private static final String DEADLINES = "deadlines";
	private static final String DEADLINES_SINGULAR = "deadline";
	private static final String DEADLINES_SHORT = "dl";
	private static final String TODOS = "todos";
	private static final String TODOS_SINGULAR = "todo";
	private static final String TODOS_SHORT = "td";
	private static final String UNDONE = "undone";
	private static final String DONE = "done";
	
	private static final String JANUARY = "january";
	private static final String FEBRUARY = "february";
	private static final String MARCH = "march";
	private static final String APRIL = "april";
	private static final String MAY = "may";
	private static final String JUNE = "june";
	private static final String JULY = "july";
	private static final String AUGUST = "august";
	private static final String SEPTEMBER = "september";
	private static final String OCTOBER = "october";
	private static final String NOVEMBER = "november";
	private static final String DECEMBER = "december";
	
	private static final String MONDAY = "monday";
	private static final String TUESDAY = "tuesday";
	private static final String WEDNESDAY = "wednesday";
	private static final String THURSDAY = "thursday";
	private static final String FRIDAY = "friday";
	private static final String SATURDAY = "saturday";
	private static final String SUNDAY = "sunday";
	
	private static final String TOMORROW_LONG = "tomorrow";
	private static final String TOMORROW_MEDIUM = "tmrw";
	private static final String TOMORROW_SHORT = "tmr";
	private static final String TODAY_LONG = "today";
	private static final String TODAY_SHORT = "tdy";
	private static final String HOURS_LONG = "hours";
	private static final String HOUR_LONG = "hour";
	private static final String HOURS_SHORT = "hrs";
	private static final String HOUR_SHORT = "hr";
	private static final String MINUTES_LONG = "minutes";
	private static final String MINUTE_LONG = "minute";
	private static final String MINUTES_SHORT = "mins";
	private static final String MINUTE_SHORT = "min";


	
	public static Action parse(String userInput) throws MalformedUserInputException {

		// tokenize user input
		StringTokenizer st = new StringTokenizer(userInput);
		// get the actionType by getting the first token (first word) from the
		// user input
		String stringUserAction = new String(st.nextToken());
		GoogleActionType googleActionType = determineGoogleActionType(stringUserAction);
		// if GCAL type
		if (googleActionType == GoogleActionType.GOOGLE) {
			return gcalParser(st);
		}
		// if not GCAL type (means Local type)
		else {
			SystemActionType systemActionType = determineSystemActionType(stringUserAction);
			if (systemActionType == SystemActionType.EXIT) {
				return new ExitAction();
			}
			else {
				LocalActionType localActionType = determineLocalActionType(stringUserAction);
				if (localActionType == LocalActionType.INVALID) {
					throw new MalformedUserInputException(MESSAGE_INVALID);
				}
				else {
					return localParser(st, localActionType);
				}
			}
		}
	}

	private static Action gcalParser(StringTokenizer st) throws MalformedUserInputException {
		String userInput = new String();
		String stringUserAction = new String(GoogleActionType.GOOGLE.getString());
		AddGoogleAction userActionAdd = new AddGoogleAction();
		stringUserAction = stringUserAction + WHITE_SPACE + st.nextToken();
		GoogleActionType actionType = determineGoogleActionType(stringUserAction);
		
		switch (actionType) {
		case GOOGLE_ADD:
			userInput = new String(getRemainingTokens(st));
			userActionAdd.setUserInput(userInput);
			return userActionAdd;
		case GOOGLE_SYNC:
			return new SyncGoogleAction();
		case GOOGLE_LOGIN:
			return new LoginGoogleAction();
		case GOOGLE_LOGOUT:
			return new LogoutGoogleAction();
		default:
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
	}

	private static LocalAction localParser(StringTokenizer st, LocalActionType actionType) throws MalformedUserInputException {
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
		case UNDO:
			return (new UndoAction());
		default:
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
	}

	/*add parser accepts the following format
	 * add <task description> at <place> on <date/day> from <time> to <time>
	 * add <task description> at <place> on <date/day> at <time>
	 * 
	 * e.g. add swimming at my apartment on sunday from 2pm to 3pm
	 * 
	 * the parser is flexible, for more information please refer to the user guide
	 */
	private static AddAction addParser(StringTokenizer st) throws MalformedUserInputException{
		AddAction userAction = new AddAction();
		StringTokenizer[] tempSt = new StringTokenizer[DEFAULT_ST_ARR_SIZE];
		Calendar[] calendarArray = new Calendar[DEFAULT_CAL_ARR_SIZE];
		calendarArray[INDEX_START_TIME] = null;
		calendarArray[INDEX_END_TIME] = null;
		String description = new String();
		String place = new String();
		
		//get the description of the task
		description = getDescription(st,tempSt);
		st = tempSt[INDEX_ST];
		userAction.setDescription(description);

		//return action if there is no more tokens
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		//get the place of the task
		place=getPlace(st,tempSt);
		st= tempSt[INDEX_ST];
		userAction.setPlace(place);
		
		//return action if there is no more tokens
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		//get the timeframe of the task
		getCompleteDate(calendarArray,st,LocalActionType.ADD);
		userAction.setStartTime(calendarArray[INDEX_START_TIME]);
		userAction.setEndTime(calendarArray[INDEX_END_TIME]);
		
		return userAction;
	}

	/* display parser accepts the following format
	 * display <all/schedules/deadlines/todos/done/undone> at <place> on <timeframe>
	 * 
	 * e.g.
	 * display all at home on monday from 3pm to 9pm
	 * 
	 * the parser is flexible, for more information please refer to the user guide
	 */
	private static DisplayAction displayParser(StringTokenizer st) throws MalformedUserInputException{
		DisplayAction userAction = new DisplayAction();

		StringTokenizer[] tempSt = new StringTokenizer[DEFAULT_ST_ARR_SIZE];
		Calendar[] calendarArray = new Calendar[DEFAULT_CAL_ARR_SIZE];
		calendarArray[INDEX_START_TIME] = Calendar.getInstance();
		calendarArray[INDEX_START_TIME].set(Calendar.MILLISECOND, MIN_MILLISECOND);
		calendarArray[INDEX_END_TIME] = null;
		
		setEndTimeMax(calendarArray);
		userAction.setStartTime(calendarArray[INDEX_START_TIME]);
		userAction.setEndTime(calendarArray[INDEX_END_TIME]);
		
		String place = new String();
		String all = null;
		String status = null;
		String task = null;

		// if command has no more details after display
		if (!st.hasMoreTokens()) {
			userAction.setDescription(ALL);
			return userAction;
		}
		
		//check if the description is all
		all=getStringAll(st,tempSt);
		st= tempSt[INDEX_ST];
		userAction.setDescription(all);
		
		if(!st.hasMoreTokens()){
			calendarArray[INDEX_START_TIME].set(MIN_YEAR,MIN_MONTH,MIN_DAY,MIN_HOUR,MIN_MINUTE,MIN_SECOND);
			userAction.setStartTime(calendarArray[INDEX_START_TIME]);
			return userAction;
		}
		
		// check if done/undone is included in user input
		status=getStatus(st,tempSt);
		st= tempSt[INDEX_ST];
		userAction.setStatus(status);
		
		//if no more elements
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		//check if schedules/deadlines/todos is included in user input
		task=getTask(st,tempSt);
		st= tempSt[INDEX_ST];
		if(!task.equals(EMPTY_STRING)){
			userAction.setDescription(task);
		}
		
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		//get place start
		place=getPlace(st,tempSt);
		st= tempSt[INDEX_ST];
		userAction.setPlace(place);
		
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		// if there is a date field
		getCompleteDate(calendarArray,st,LocalActionType.DISPLAY);
		userAction.setStartTime(calendarArray[INDEX_START_TIME]);
		userAction.setEndTime(calendarArray[INDEX_END_TIME]);
	
		return userAction;
	}

	// can delete with reference number
	// can delete with query(task description)
	// e.g.
	// "delete #1 #7 #4"
	// can delete multiple items separated with space
	// delete function : 100 %
	private static DeleteAction deleteParser(StringTokenizer st) throws MalformedUserInputException{
		DeleteAction userAction = new DeleteAction();
		StringTokenizer[] tempSt = new StringTokenizer[DEFAULT_ST_ARR_SIZE];
		ArrayList<Integer> referenceNumber = null;
		String expectAll = new String();
		
		if(!st.hasMoreTokens()){
			throw new MalformedUserInputException(MESSAGE_INVALID_REF_NUMBER);
		}

		expectAll = getStringAll(st,tempSt);
		st = tempSt[INDEX_ST];
		if(isStringAll(expectAll)){
			referenceNumber = new ArrayList<Integer>();
			referenceNumber.add(DELETE_ALL_REF_NUMBER);
			userAction.setReferenceNumber(referenceNumber);
			return userAction;
		}

		//get the numbers
		referenceNumber = getMultipleReferenceNumber(st);
		userAction.setReferenceNumber(referenceNumber);
	
		return userAction;
	}

	// update function: 100 %
	private static UpdateAction updateParser(StringTokenizer st) throws MalformedUserInputException {
		UpdateAction userAction = new UpdateAction();
		
		StringTokenizer[] tempSt = new StringTokenizer[DEFAULT_ST_ARR_SIZE];
		Calendar[] calendarArray = new Calendar[DEFAULT_CAL_ARR_SIZE];
		calendarArray[INDEX_START_TIME] = Calendar.getInstance();
		calendarArray[INDEX_START_TIME].set(Calendar.MILLISECOND, MIN_MILLISECOND);
		calendarArray[INDEX_END_TIME] = null;
		String description = null;
		String place = null;
		String updateDelimiter = null;
		int referenceNumber = INIT_INT_VALUE;
		
		//get reference number (e.g. #3)
		referenceNumber = getSingleReferenceNumber(st);
		userAction.setReferenceNumber(referenceNumber);
		
		//check is there ">>" delimiter after the reference number
		updateDelimiter = st.nextToken();
		if (!isValidUpdateDelimiter(updateDelimiter)){
			throw new MalformedUserInputException (MESSAGE_INVALID);
		}
				
		//after finding the delimiter ">>"
		
		//get the description
		description = getDescription(st,tempSt);
		st = tempSt[INDEX_ST];
		userAction.setUpdatedQuery(description);

		if(!st.hasMoreTokens()){
			return userAction;
		}

		//get place to be updated
		place=getPlace(st,tempSt);
		st= tempSt[INDEX_ST];
		userAction.setUpdatedLocationQuery(place);
		
		if(!st.hasMoreTokens()){
			return userAction;
		}

		getCompleteDate(calendarArray,st,LocalActionType.UPDATE);
		userAction.setUpdatedStartTime(calendarArray[INDEX_START_TIME]);
		userAction.setUpdatedEndTime(calendarArray[INDEX_END_TIME]);

		return userAction;
	}

	private static SearchAction searchParser(StringTokenizer st) throws MalformedUserInputException {
		SearchAction userAction = new SearchAction();

		StringTokenizer[] tempSt = new StringTokenizer[DEFAULT_ST_ARR_SIZE];
		Calendar[] calendarArray = new Calendar[DEFAULT_CAL_ARR_SIZE];
		calendarArray[INDEX_START_TIME] = Calendar.getInstance();
		calendarArray[INDEX_START_TIME].set(Calendar.MILLISECOND, MIN_MILLISECOND);
		calendarArray[INDEX_END_TIME] = null;
		
		setEndTimeMax(calendarArray);
		userAction.setStartTime(calendarArray[INDEX_START_TIME]);
		userAction.setEndTime(calendarArray[INDEX_END_TIME]);
		
		String description = new String();
		String place = new String();
		
		//get the task description / query
		description = getDescription(st,tempSt);
		st = tempSt[INDEX_ST];
		userAction.setQuery(description);

		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		//get the place query, if any
		place=getPlace(st,tempSt);
		st= tempSt[INDEX_ST];
		userAction.setLocationQuery(place);
		
		if(!st.hasMoreTokens()){
			return userAction;
		}
		
		getCompleteDate(calendarArray,st,LocalActionType.SEARCH);
		userAction.setStartTime(calendarArray[INDEX_START_TIME]);
		userAction.setEndTime(calendarArray[INDEX_END_TIME]);
		
		return userAction;
	}

	/*
	 * Mark accepts task id (#2,#12) 
	 * accepts specific descriptions to replace task id. "mark #1 as done"
	 * "mark #1 #2 as done"
	 */
	// mark function: 100 %
	private static MarkAction markParser(StringTokenizer st) throws MalformedUserInputException{
		MarkAction userAction = new MarkAction();
		ArrayList<Integer> referenceNumber = null;
		String status = new String();
		
		//get reference number array list
		referenceNumber = getMultipleReferenceNumber(st);
		userAction.setReferenceNumber(referenceNumber);
		
		if(st.hasMoreTokens()){
			status = new String(st.nextToken());
		}
		if(isValidStatus(status)){
			userAction.setStatus(status);
		}
		else{
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		return userAction;
	}
	
	private static void setEndTimeMax (Calendar[] calendarArray) {
		calendarArray[INDEX_END_TIME] = Calendar.getInstance();
		calendarArray[INDEX_END_TIME].set(Calendar.MILLISECOND, MIN_MILLISECOND);
		calendarArray[INDEX_END_TIME].set(MAX_YEAR,MAX_MONTH,MAX_DAY,MAX_HOUR,MAX_MINUTE,MAX_SECOND);
		return;
	}
	
	private static String getStringAll(StringTokenizer st, StringTokenizer[] tempSt) {
		String all = new String(st.nextToken());
		if (!isStringAll(all)){
			st = addStringToTokenizer(st,all);
			all= new String();
		}
		tempSt[INDEX_ST]=st;
		return all;
	}
	
	private static String getTask(StringTokenizer st, StringTokenizer[] tempSt) {
		String task = new String(st.nextToken());
		// if not then return back the string
		if(!isValidTask(task)){
			st = addStringToTokenizer(st,task);
			task = new String();
		}
		else{
			if(isStringSchedules(task)){
				task = "schedules";
			}
			if(isStringDeadlines(task)){
				task = "deadlines";
			}
			if(isStringTodos(task)){
				task = "todos";
			}
		}
		tempSt[INDEX_ST] = st;
		return task;
	}
	
	private static String getStatus(StringTokenizer st, StringTokenizer[] tempSt){
		String status = new String(st.nextToken());
		// if not then return back the string
		if(!isValidStatus(status)){
			st = addStringToTokenizer(st,status);
			status = new String();
		}
		tempSt[INDEX_ST] = st;
		return status;
	}
	
	private static int getSingleReferenceNumber(StringTokenizer st) throws MalformedUserInputException {
		int referenceNumber = INIT_INT_VALUE; 
		String temp = new String();
		String expectHex = new String();
		int tempInt = 0;
		
		if (!st.hasMoreTokens()) {
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		try{
			temp = new String (st.nextToken());
			
			expectHex = temp.substring(FIRST_INDEX,SECOND_INDEX);
			//if the first character is not hex, throw error
			if(!isStringHex(expectHex)){
				throw new MalformedUserInputException(MESSAGE_INVALID);
			}
			
			temp = temp.substring(SECOND_INDEX); //retrieve the integer and remove the hex (#)
			tempInt = Integer.parseInt(temp);
			referenceNumber = tempInt;
		}
		catch (Exception e){
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		
		
		return referenceNumber;
	}
	
	private static ArrayList<Integer> getMultipleReferenceNumber(StringTokenizer st) throws MalformedUserInputException{
		ArrayList<Integer> referenceNumber = new ArrayList<Integer>();
		String temp = new String();
		String expectHex = new String();
		int tempInt = 0;
		
		while (st.hasMoreTokens()) {
			try{
				temp = new String (st.nextToken());
				//check whether it reaches preposition "as"
				if (isValidMarkPreposition(temp)){
					return referenceNumber;
				}
				
				expectHex = temp.substring(FIRST_INDEX,SECOND_INDEX);
				//if the first character is not hex, throw error
				if(!isStringHex(expectHex)){
					throw new MalformedUserInputException(MESSAGE_INVALID);
				}
				
				temp = temp.substring(SECOND_INDEX); //retrieve the integer and remove the hex (#)
				tempInt = Integer.parseInt(temp);
				referenceNumber.add(tempInt);
			}
			catch (Exception e){
				throw new MalformedUserInputException(MESSAGE_INVALID);
			}
		}
		
		return referenceNumber;
	}
	
	private static String getDescription(StringTokenizer st, StringTokenizer[] tempSt){

		String tempDescription = new String();
		String description = new String(st.nextToken());
		
		while(st.hasMoreTokens()){
			tempDescription = new String(st.nextToken());
			if((!isValidPlacePreposition(tempDescription))&&(!isValidDayPreposition(tempDescription))){
				description = description + WHITE_SPACE + tempDescription;
			}
			else{
				st = addStringToTokenizer(st,tempDescription);
				break;
			}
		}
		tempSt[INDEX_ST]=st;
		return description;
	}
	
	
	
	private static String getPlace(StringTokenizer st, StringTokenizer[] tempSt){
		String prep = new String(st.nextToken());
		String place = new String();

		// check if place is included in user input
		if (isValidPlacePreposition(prep)) {
			place = new String(st.nextToken());
		} else {
			st = addStringToTokenizer(st,prep);
			place = new String();
			tempSt[INDEX_ST]=st;
			return place;
		}

		// check for place name, separated by space, and incorporate the proper
		// place name
		if(st.hasMoreTokens()){
			prep = new String(st.nextToken());
			while (!isValidDayPreposition(prep)) {
				place = place + WHITE_SPACE + prep;
				if (st.hasMoreTokens()) {
					prep = new String(st.nextToken());
				} else {
					break;
				}
			}
		}
		
		if(isValidDayPreposition(prep)){
			st=addStringToTokenizer(st,prep);
		}
		tempSt[INDEX_ST]=st;
		return place;
	}

	private static int getDayOfMonth(String month){
		month=month.toLowerCase();
		if(JANUARY.contains(month)){
			return 1;
		}
		else if(FEBRUARY.contains(month)){
			return 2;
		}
		else if(MARCH.contains(month)){
			return 3;
		}
		else if(APRIL.contains(month)){
			return 4;
		}
		else if(MAY.contains(month)){
			return 5;
		}
		else if(JUNE.contains(month)){
			return 6;
		}
		else if(JULY.contains(month)){
			return 7;
		}
		else if(AUGUST.contains(month)){
			return 8;
		}
		else if(SEPTEMBER.contains(month)){
			return 9;
		}
		else if(OCTOBER.contains(month)){
			return 10;
		}
		else if(NOVEMBER.contains(month)){
			return 11;
		}
		else if(DECEMBER.contains(month)){
			return 12;
		}
		else{
			return INVALID_INT_VALUE;
		}
		
	}
	
	private static int getDayOfWeek(String day){
		day=day.toLowerCase();
		if(SUNDAY.contains(day)){
			return 1;
		}
		if(MONDAY.contains(day)){
			return 2;
		}
		if(TUESDAY.contains(day)){
			return 3;
		}
		if(WEDNESDAY.contains(day)){
			return 4;
		}
		if(THURSDAY.contains(day)){
			return 5;
		}
		if(FRIDAY.contains(day)){
			return 6;
		}
		if(SATURDAY.contains(day)){
			return 7;
		}
		else{
			return INVALID_INT_VALUE;
		}
	}
	
	private static int getIndexOfDelimiter(String input, int startIndex){
		int indexOfDelimiter = INIT_INT_VALUE;
		for (int i = startIndex; (i < input.length()); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				indexOfDelimiter = i;
				break;
			}
		}
		return indexOfDelimiter;
	}
	
	private static int getIndexOfSuffixDelimiter(String input, int endIndex){
		int indexOfSuffixDelimiter = INIT_INT_VALUE;
		for (int i = endIndex; (i >= 0); i--) {
			if (Character.isDigit(input.charAt(i))) {
				indexOfSuffixDelimiter = i+1;
				break;
			}
		}
		return indexOfSuffixDelimiter;
	}
	
	private static int getAdjustedHour(int intTimeHour){
		if((intTimeHour < TIME_DEFAULT_MIN_AM)&&(intTimeHour > TIME_DEFAULT_MIDNIGHT)) {
			//add 12 hrs based on assumption, 12:00-07.59, pm suffix will be assumed
			intTimeHour = intTimeHour + TIME_FORMAT_DIFF;
		}
		return intTimeHour;
	}
	
	private static int getTimeMinute24Hour(String time){
		int intTimeMinute = INIT_INT_VALUE;
		int stringTimeLength = time.length();
		if (stringTimeLength <= DEFAULT_MINUTE_LENGTH) {
			return intTimeMinute;
		} else {
			time = time.substring(THIRD_INDEX, FIFTH_INDEX);
			intTimeMinute = Integer.parseInt(time);
			return intTimeMinute;
		}
	}
	
	private static int getTimeMinute12Hour(String time){
		int intTimeMinute = INIT_INT_VALUE;
		int stringTimeLength = time.length();
		int indexOfDelimiter = INIT_INT_VALUE;
		// get the index of delimiter
		indexOfDelimiter = getIndexOfDelimiter(time,FIRST_INDEX);
		
		if(indexOfDelimiter + 3 <= stringTimeLength){
			time = time.substring(indexOfDelimiter + 1, indexOfDelimiter + 3);
			if((Character.isDigit(time.charAt(FIRST_INDEX)))&&(Character.isDigit(time.charAt(SECOND_INDEX)))){
				intTimeMinute = Integer.parseInt(time);	
				return intTimeMinute;
		    }
		}
		return intTimeMinute;
	}
	
	private static int getTimeMinute(String time) {
		int intTimeMinute = INIT_INT_VALUE;
		if (isAllDigits(time)) {
			intTimeMinute = getTimeMinute24Hour(time);
			return intTimeMinute;
		}
		else {
			intTimeMinute = getTimeMinute12Hour(time);
			return intTimeMinute;
		}
	}
	
	private static int getTimeHour24Hour(String time){
		int intTimeHour = INIT_INT_VALUE;
		int stringTimeLength = time.length();
		if (stringTimeLength <= DEFAULT_HOUR_LENGTH) {
			intTimeHour = Integer.parseInt(time);
			intTimeHour = getAdjustedHour(intTimeHour);
			return intTimeHour;
		}
		else {
			time = time.substring(FIRST_INDEX, THIRD_INDEX);
			intTimeHour = Integer.parseInt(time);
			return intTimeHour;
		}
	}
	
	private static int getTimeHour12Hour(String time){
		String suffix = new String();
		int stringTimeLength = time.length();
		int intTimeHour = INIT_INT_VALUE;
		int indexOfDelimiter = INIT_INT_VALUE;
		int indexOfSuffixDelimiter = INIT_INT_VALUE;
		
		//get the index of suffix delimiter
		indexOfSuffixDelimiter = getIndexOfSuffixDelimiter(time,stringTimeLength-1);
		//get suffix (am or pm)
		suffix = time.substring(indexOfSuffixDelimiter);
		//get the index of hour and minute delimiter
		indexOfDelimiter = getIndexOfDelimiter(time,FIRST_INDEX);
		
		time = time.substring(FIRST_INDEX, indexOfDelimiter);
		intTimeHour = Integer.parseInt(time);
		
		//if it's 12 am , convert to 00
		if ((isAm(suffix))&&(intTimeHour==12)){
			intTimeHour = intTimeHour - TIME_FORMAT_DIFF;
		}
		//if it's pm , add 12
		else if ((isPm(suffix))&&(intTimeHour!=12)) {
			intTimeHour = intTimeHour + TIME_FORMAT_DIFF;
		}
		else if(suffix.equals(EMPTY_STRING)){
			intTimeHour = getAdjustedHour(intTimeHour);
		}
		return intTimeHour;
	}

	private static int getTimeHour(String time) {
		int intTimeHour = INIT_INT_VALUE;
		if (isAllDigits(time)) {
			intTimeHour = getTimeHour24Hour(time);
			return intTimeHour;
		}
		else {
			intTimeHour = getTimeHour12Hour(time);
			return intTimeHour;
		}
	}
	
	
	private static void setDateToday(int[] intStartDate){
		intStartDate[INDEX_DAY] = Calendar.getInstance().get(Calendar.DATE);
		intStartDate[INDEX_MONTH] = Calendar.getInstance().get(Calendar.MONTH);
		intStartDate[INDEX_YEAR] = Calendar.getInstance().get(Calendar.YEAR);
		return;
	}
	private static void setDateTomorrow(int[] intStartDate){
		intStartDate[INDEX_DAY] = Calendar.getInstance().get(Calendar.DATE)+1;
		intStartDate[INDEX_MONTH] = Calendar.getInstance().get(Calendar.MONTH);
		intStartDate[INDEX_YEAR] = Calendar.getInstance().get(Calendar.YEAR);
		return;
	}
	private static void setDateNextWeek(int[] intStartDate){
		intStartDate[INDEX_DAY] = Calendar.getInstance().get(Calendar.DATE)+7;
		intStartDate[INDEX_MONTH] = Calendar.getInstance().get(Calendar.MONTH);
		intStartDate[INDEX_YEAR] = Calendar.getInstance().get(Calendar.YEAR);
		return;
	}
	private static void setDateWithDayDifference(int[] intStartDate, int dayDifference){
		if(dayDifference<0){
			dayDifference = dayDifference + 7;
		}
		intStartDate[INDEX_DAY] = Calendar.getInstance().get(Calendar.DATE) + dayDifference;
		intStartDate[INDEX_MONTH] = Calendar.getInstance().get(Calendar.MONTH);
		intStartDate[INDEX_YEAR] = Calendar.getInstance().get(Calendar.YEAR);
		return;
	}
	
	private static void getDateFromDay(int[] intStartDate, String eventDay, int intEventTimeHours, int intEventTimeMinutes) throws MalformedUserInputException{
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.MILLISECOND, MIN_MILLISECOND);
		int intCurrentDayOfWeek = INVALID_INT_VALUE;
		intCurrentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
		int intCurrentTimeHours = INVALID_INT_VALUE;
		intCurrentTimeHours = currentDate.get(Calendar.HOUR);
		int intCurrentTimeMinutes = INVALID_INT_VALUE;
		intCurrentTimeMinutes = currentDate.get(Calendar.MINUTE);
		if(isStringToday(eventDay)){
			setDateToday(intStartDate);
			return;
		}
		else if (isStringTomorrow(eventDay)){
			setDateTomorrow(intStartDate);
			return;
		}
		
		int intEventDayOfWeek = INVALID_INT_VALUE;
		intEventDayOfWeek = getDayOfWeek(eventDay);
		
		if(intEventDayOfWeek==INVALID_INT_VALUE){
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		//if the day stated is not the same as today's day
		else if(intCurrentDayOfWeek!=intEventDayOfWeek){
			int dayDifference = intEventDayOfWeek - intCurrentDayOfWeek;
			setDateWithDayDifference(intStartDate,dayDifference);	
		}
		//if the day input is the same as today
		else if (intCurrentDayOfWeek==intEventDayOfWeek) {
			//compare hours whether time has passed
			if(intEventTimeHours==intCurrentTimeHours){
				//compare minutes whether time has passed
				if(intEventTimeMinutes<=intCurrentTimeMinutes){
					setDateNextWeek(intStartDate);
				}
				else{
					setDateToday(intStartDate);
				}
			}
			else if(intEventTimeHours<=intCurrentTimeHours){
				setDateNextWeek(intStartDate);
			}
			else{
				setDateToday(intStartDate);
			}
		}
		else{
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		return;
	}
	
	private static void setDayDate(int[] intStartDate, String date, int indexOfDelimiter){
		String strDay = new String();
		strDay = date.substring(FIRST_INDEX, indexOfDelimiter);
		intStartDate[INDEX_DAY] = Integer.parseInt(strDay);
		return;
	}
	
	private static void setMonthDate(int[] intStartDate, String date, int indexOfDelimiter, int indexOfDelimiter2){
		String strMonth = new String();
		if(indexOfDelimiter2 != INIT_INT_VALUE){
			strMonth = date.substring(indexOfDelimiter + 1, indexOfDelimiter2);
		}
		else{
			strMonth = date.substring(indexOfDelimiter + 1);
		}
		// Calendar in java, stores month starting from 0 (january) to 11 (december)
		intStartDate[INDEX_MONTH] = Integer.parseInt(strMonth);
		intStartDate[INDEX_MONTH]--; 
		return;
	}
	private static void setYearDate(int[] intStartDate, String date, int indexOfDelimiter, int indexOfDelimiter2, LocalActionType type) {
		String strYear = new String();
		if (indexOfDelimiter2 != INIT_INT_VALUE) {
			strYear = date.substring(indexOfDelimiter2 + 1);
			
			if(strYear.length()== DEFAULT_YEAR_LENGTH){
				strYear = YEAR_PREFIX + strYear;
			}
			intStartDate[INDEX_YEAR] = Integer.parseInt(strYear);
		}
		//if there is no year (assume it is the current year if the date is valid, else next year);
		else{			 
			intStartDate[INDEX_YEAR] = Calendar.getInstance().get(Calendar.YEAR);
			
			if(type==LocalActionType.ADD){
				if(intStartDate[INDEX_MONTH]+1 == Calendar.getInstance().get(Calendar.MONTH)){
					if(intStartDate[INDEX_DAY] < Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
						intStartDate[INDEX_YEAR]++;
					}
				}
				else if(intStartDate[INDEX_MONTH]+1 < Calendar.getInstance().get(Calendar.MONTH)){
						intStartDate[INDEX_YEAR]++;
				}
			}
		}
		return;
	}


	private static void getDate(int[] intStartDate, String date, LocalActionType type){
		//get the date start
		int indexOfDelimiter = INIT_INT_VALUE;
		int indexOfDelimiter2 = INIT_INT_VALUE;

		// get the index of delimiter
		indexOfDelimiter = getIndexOfDelimiter(date,FIRST_INDEX);
		indexOfDelimiter2 = getIndexOfDelimiter(date , indexOfDelimiter + 1);
		
		setDayDate(intStartDate,date,indexOfDelimiter);
		setMonthDate(intStartDate,date,indexOfDelimiter,indexOfDelimiter2);
		setYearDate(intStartDate,date,indexOfDelimiter,indexOfDelimiter2,type);
		//get date end
	}
	
	private static String getStringDate(StringTokenizer st, StringTokenizer[] tempSt) throws MalformedUserInputException{
		String date = new String(st.nextToken());
		
		if(!st.hasMoreTokens()){
			tempSt[INDEX_ST]=st;
			return date;
		}
		
		if(date.length()>2){
			tempSt[INDEX_ST]=st;
			return date;
		}
		
		//retrieve month (e.g. January february etc)
		String stringMonth = new String(st.nextToken());
		Integer intMonth = getDayOfMonth(stringMonth);
		if(intMonth!=INVALID_INT_VALUE){
			date = date + SLASH + intMonth.toString();	
		}
		else{
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		
		if(!st.hasMoreTokens()){
			tempSt[INDEX_ST]=st;
			return date;
		}
		
		//retrieve year, after month,  (e.g. 2013)
		String stringYear = new String(st.nextToken());
		if(isAllDigits(stringYear)){
			date = date + SLASH + stringYear;
		}
		else{
			st = addStringToTokenizer(st,stringYear);
		}
	
		tempSt[INDEX_ST]=st;
		return date;
	}
	
	//<time> format is fully functional
	//e.g. 5 pm, 5pm, 12pm, 1a.m., 1200, 0800, etc

	// pass a user string of date in any format
	// and return date in Calendar format
	// using calendarArray to store date as integers.
	// calendarArray[0] is startTime
	// calendarArray[1] is endTime
	private static void getCompleteDate(Calendar[] calendarArray, StringTokenizer st, LocalActionType actionType) throws MalformedUserInputException {
		StringTokenizer[] tempSt = new StringTokenizer[DEFAULT_ST_ARR_SIZE];
		Calendar startTimeCal = calendarArray[INDEX_START_TIME];
		Calendar endTimeCal = calendarArray[INDEX_END_TIME];
		int[] intStartDate = new int[DEFAULT_DATE_ARR_SIZE];
		int[] intEndDate = new int[DEFAULT_DATE_ARR_SIZE];
		intStartDate[INDEX_YEAR] = MIN_YEAR;
		intStartDate[INDEX_MONTH] = MIN_MONTH;
		intStartDate[INDEX_DAY] = MIN_DAY;
		intEndDate[INDEX_YEAR] = MAX_YEAR;
		intEndDate[INDEX_MONTH] = MAX_MONTH;
		intEndDate[INDEX_DAY] = MAX_DAY;
		String date = new String();
		String preposition = new String(st.nextToken());
		
		//return back the day/date if it's "today" or "tomorrow"
		if((isStringToday(preposition))||(isStringTomorrow(preposition))){
			st = addStringToTokenizer(st,preposition);
		}
		else if(!isValidDayPreposition(preposition)){
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}

		// if there is no date field
		if(!st.hasMoreTokens()){
			endTimeCal = Calendar.getInstance();
			endTimeCal.set(intEndDate[INDEX_YEAR], intEndDate[INDEX_MONTH], intEndDate[INDEX_DAY], MAX_HOUR, MAX_MINUTE, MAX_SECOND);
			calendarArray[INDEX_START_TIME] = startTimeCal;
			calendarArray[INDEX_END_TIME] = endTimeCal;
			return;
		}
		//get String Date, convert (12 Jan) format to default format (12/1)
		date = getStringDate(st, tempSt);
		st = tempSt[INDEX_ST];
		
		//begin check if the date entered is valid
		if(!Character.isDigit(date.charAt(FIRST_INDEX))){
			getDateFromDay(intStartDate,date,MIN_HOUR,MIN_MINUTE);
		}
		else{
			getDate(intStartDate,date,actionType);
		}
		if(!isValidDate(intStartDate)){
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		//end check if the date entered is valid
		
		//if there is no time field (only date)
		if(!st.hasMoreTokens()){
			if((actionType==LocalActionType.DISPLAY)||(actionType==LocalActionType.SEARCH)){
				if(!Character.isDigit(date.charAt(FIRST_INDEX))){
					getDateFromDay(intStartDate,date,MIN_HOUR,MIN_MINUTE);
				}
				else{
					getDate(intStartDate,date,actionType);
				}
				startTimeCal = Calendar.getInstance();
				startTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], MIN_HOUR, MIN_MINUTE, MIN_SECOND);
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], MAX_HOUR, MAX_MINUTE, MAX_SECOND);
			}
			//if it is add or update schedule
			else if(((actionType==LocalActionType.ADD)||(actionType==LocalActionType.UPDATE))&&(!isValidDeadlinePreposition(preposition))){
				if(!Character.isDigit(date.charAt(FIRST_INDEX))){
					getDateFromDay(intStartDate,date,DEFAULT_START_HOUR,DEFAULT_START_MINUTE);
				}
				else{
					getDate(intStartDate,date,actionType);
				}
				startTimeCal = Calendar.getInstance();
				startTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], DEFAULT_START_HOUR, DEFAULT_START_MINUTE, DEFAULT_START_SECOND);
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], DEFAULT_END_HOUR , DEFAULT_END_MINUTE,  DEFAULT_END_SECOND);
			}
			else{
				if(!Character.isDigit(date.charAt(FIRST_INDEX))){
					getDateFromDay(intStartDate,date,MIN_HOUR,MIN_MINUTE);
				}
				else{
					getDate(intStartDate,date,actionType);
				}
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], MAX_HOUR, MAX_MINUTE, MAX_SECOND);
			}
			calendarArray[INDEX_START_TIME] = startTimeCal;
			calendarArray[INDEX_END_TIME] = endTimeCal;
			return;
		}
		
		
		//if there is time field
		String prep = new String(st.nextToken());
		if (!isValidTimePreposition(prep)) {
			throw new MalformedUserInputException(MESSAGE_INVALID);
		}
		
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

		if(!Character.isDigit(date.charAt(FIRST_INDEX))){
			getDateFromDay(intStartDate,date,intStartHour,intStartMinute);
		}
		else{
			getDate(intStartDate,date,actionType);
		}
		
		if(!st.hasMoreTokens()){
			if(isValidDeadlinePreposition(preposition)){
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intStartHour, intStartMinute, MIN_SECOND);
			}
			else if((actionType==LocalActionType.ADD)||(actionType==LocalActionType.UPDATE)){
				startTimeCal = Calendar.getInstance();
				startTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intStartHour, intStartMinute, MIN_SECOND);
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intStartHour + DEFAULT_DURATION_HOUR, intStartMinute, MIN_SECOND);
			}
			else{
				startTimeCal = Calendar.getInstance();
				startTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intStartHour, intStartMinute, MIN_SECOND);
				endTimeCal = Calendar.getInstance();
				endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], MAX_HOUR, MAX_MINUTE, MAX_SECOND);
			}
			calendarArray[INDEX_START_TIME] = startTimeCal;
			calendarArray[INDEX_END_TIME] = endTimeCal;
			return;
		}
		
		prep = new String(st.nextToken());
		//if it is a duration time format
		if(isValidTimeDurationPreposition(prep)){
			String stringTime = new String();
			int intHourDuration = INIT_INT_VALUE;
			int intMinuteDuration = INIT_INT_VALUE;
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

			startTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intStartHour, intStartMinute, MIN_SECOND);
			endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intStartHour+intHourDuration, intStartMinute+intMinuteDuration, MIN_SECOND);
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

			startTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intStartHour, intStartMinute, MIN_SECOND);
			endTimeCal.set(intStartDate[INDEX_YEAR], intStartDate[INDEX_MONTH], intStartDate[INDEX_DAY], intEndHour, intEndMinute, MIN_SECOND);
		}
	
		startTimeCal.set(Calendar.MILLISECOND, MIN_MILLISECOND);
		endTimeCal.set(Calendar.MILLISECOND, MIN_MILLISECOND);
		calendarArray[INDEX_START_TIME] = startTimeCal;
		calendarArray[INDEX_END_TIME] = endTimeCal;
		return;
	}

	private static String getRemainingTokens(StringTokenizer strRemaining) {
		String strResult = new String();
		while (strRemaining.hasMoreTokens()) {
			strResult = strResult + WHITE_SPACE + strRemaining.nextToken();
		}
		return strResult;
	}
	
	private static StringTokenizer addStringToTokenizer(StringTokenizer st, String tempString){
		String tempUserInput = new String();
		tempUserInput = getRemainingTokens(st);
		tempUserInput = tempString + WHITE_SPACE + tempUserInput;
		return new StringTokenizer(tempUserInput);
	}
	
	private static boolean isAllDigits(String input){
		for (int i = 0; i < input.length(); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	private static boolean isStringHex(String input){
		if(input.equals(HEX)){
			return true;
		}
		return false;
	}
	
	private static boolean isStringMinute(String timeUnit){
		if ((timeUnit.equalsIgnoreCase(MINUTE_LONG))
			||(timeUnit.equalsIgnoreCase(MINUTES_LONG))
			||(timeUnit.equalsIgnoreCase(MINUTE_SHORT))
			||(timeUnit.equalsIgnoreCase(MINUTES_SHORT))) {
				return true;
		}
		return false;
	}
	
	private static boolean isStringHour(String timeUnit){
		if ((timeUnit.equalsIgnoreCase(HOUR_LONG))
			||(timeUnit.equalsIgnoreCase(HOURS_LONG))
			||(timeUnit.equalsIgnoreCase(HOUR_SHORT))
			||(timeUnit.equalsIgnoreCase(HOURS_SHORT))) {
				return true;
		}
		return false;
	}
	
	private static boolean isStringAll(String task){
		if (task.equalsIgnoreCase(ALL)){
			return true;
		}
		return false;
	}
	
	private static boolean isStringToday(String day){
		if((day.equalsIgnoreCase(TODAY_LONG))
			||(day.equalsIgnoreCase(TODAY_SHORT))){
			return true;
		}
		return false;
	}
	
	private static boolean isStringTomorrow(String day){
		if((day.equalsIgnoreCase(TOMORROW_LONG))
			||(day.equalsIgnoreCase(TOMORROW_MEDIUM))
			||(day.equalsIgnoreCase(TOMORROW_SHORT))){
			return true;
		}
		return false;
	}
	
	private static boolean isAm(String suffix){
		if((suffix.equalsIgnoreCase(AM_SHORT))
				||(suffix.equalsIgnoreCase(AM_LONG))){
			return true;
		}
		return false;
	}
	
	private static boolean isPm(String suffix){
		if((suffix.equalsIgnoreCase(PM_SHORT))
				||(suffix.equalsIgnoreCase(PM_LONG))){
			return true;
		}
		return false;
	}
	
	private static boolean isValidDate(int[] intTime){
		if((intTime[INDEX_YEAR]<2000)||(intTime[INDEX_YEAR]>2099)){
			return false;
		}
		if((intTime[INDEX_MONTH]<0)||(intTime[INDEX_MONTH]>11)){
			return false;
		}
		if((intTime[INDEX_DAY]<1)||(intTime[INDEX_DAY]>38)){
			return false;
		}
		return true;
	}
	
	private static boolean isValidTimeSuffix(String suffix){
		if((suffix.equalsIgnoreCase(PM_SHORT))
			||(suffix.equalsIgnoreCase(PM_LONG))
			||(suffix.equalsIgnoreCase(AM_SHORT))
			||(suffix.equalsIgnoreCase(AM_LONG))){
			return true;
		}
		return false;
	}
	
	private static boolean isValidStatus(String status){
		if ((status.equalsIgnoreCase(DONE))
				|| (status.equalsIgnoreCase(UNDONE))) {
			return true;
		}
		return false;
	}
	
	private static boolean isStringSchedules(String task){
		if ((task.equalsIgnoreCase(SCHEDULES))
				|| (task.equalsIgnoreCase(SCHEDULES_SINGULAR))
				|| (task.equalsIgnoreCase(SCHEDULES_SHORT))){
			return true;
		}
		return false;
	}
	
	private static boolean isStringDeadlines(String task){
		if ((task.equalsIgnoreCase(DEADLINES))
				|| (task.equalsIgnoreCase(DEADLINES_SINGULAR))
				|| (task.equalsIgnoreCase(DEADLINES_SHORT))){
			return true;
		}
		return false;
	}
	
	private static boolean isStringTodos(String task){
		if ( (task.equalsIgnoreCase(TODOS))
				|| (task.equalsIgnoreCase(TODOS_SINGULAR))
				|| (task.equalsIgnoreCase(TODOS_SHORT))) {
			return true;
		}
		return false;
	}

	private static boolean isValidTask(String task) {
		if ((task.equalsIgnoreCase(SCHEDULES))
				|| (task.equalsIgnoreCase(SCHEDULES_SINGULAR))
				|| (task.equalsIgnoreCase(SCHEDULES_SHORT))
				|| (task.equalsIgnoreCase(DEADLINES))
				|| (task.equalsIgnoreCase(DEADLINES_SINGULAR))
				|| (task.equalsIgnoreCase(DEADLINES_SHORT))
				|| (task.equalsIgnoreCase(TODOS))
				|| (task.equalsIgnoreCase(TODOS_SINGULAR))
				|| (task.equalsIgnoreCase(TODOS_SHORT))) {
			return true;
		}
		return false;
	}
	
	private static boolean isValidUpdateDelimiter(String delimiter) {
		if (delimiter.equalsIgnoreCase(PREP_UPDATE)) {
			return true;
		}
		return false;
	}

	private static boolean isValidMarkPreposition(String preposition) {
		if (preposition.equalsIgnoreCase(PREP_AS)) {
			return true;
		}
		return false;
	}

	private static boolean isValidPlacePreposition(String preposition) {
		if ((preposition.equalsIgnoreCase(PREP_IN))
				|| (preposition.equalsIgnoreCase(PREP_AT))) {
			return true;
		}
		return false;
	}
	
	private static boolean isValidDeadlinePreposition(String preposition){
		if (preposition.equalsIgnoreCase(PREP_BY)){
			return true;
		}
		return false;
	}

	private static boolean isValidDayPreposition(String preposition) {
		if ((preposition.equalsIgnoreCase(PREP_ON))
				|| (preposition.equalsIgnoreCase(PREP_BY))
				|| (preposition.equalsIgnoreCase(PREP_COMMA))
				|| (isStringToday(preposition))
				|| (isStringTomorrow(preposition))){
			return true;
		}
		return false;
	}
	
	private static boolean isValidTimeDurationPreposition(String preposition){
		if(preposition.equalsIgnoreCase(PREP_FOR)){
			return true;
		}
		return false;
	}

	private static boolean isValidTimePreposition(String preposition) {
		if ((preposition.equalsIgnoreCase(PREP_FROM))
				|| (preposition.equalsIgnoreCase(PREP_AT))
				|| (preposition.equalsIgnoreCase(PREP_COMMA))) {
			return true;
		}
		return false;
	}

	private static LocalActionType determineLocalActionType(
			String commandTypeString) {
		if (commandTypeString == null)
			throw new Error(MESSAGE_INVALID);
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
		} else if (commandTypeString.equalsIgnoreCase(LocalActionType.UNDO
				.getString())) {
			return LocalActionType.UNDO;
		}else {
			return LocalActionType.INVALID;
		}
	}

	private static GoogleActionType determineGoogleActionType(
			String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error(MESSAGE_INVALID);
		} else if (commandTypeString.equalsIgnoreCase(GoogleActionType.GOOGLE
				.getString())) {
			return GoogleActionType.GOOGLE;
		} else if (commandTypeString.equalsIgnoreCase(GoogleActionType.GOOGLE_LOGIN
				.getString())) {
			return GoogleActionType.GOOGLE_LOGIN;
		} else if (commandTypeString.equalsIgnoreCase(GoogleActionType.GOOGLE_LOGOUT
				.getString())) {
			return GoogleActionType.GOOGLE_LOGOUT;
		} else if (commandTypeString
				.equalsIgnoreCase(GoogleActionType.GOOGLE_SYNC.getString())) {
			return GoogleActionType.GOOGLE_SYNC;
		} else if (commandTypeString
				.equalsIgnoreCase(GoogleActionType.GOOGLE_ADD.getString())) {
			return GoogleActionType.GOOGLE_ADD;
		} else {
			return GoogleActionType.INVALID;
		}
	}

	private static SystemActionType determineSystemActionType(
			String commandTypeString) {
		if (commandTypeString == null)
			throw new Error(MESSAGE_INVALID);
		else if (commandTypeString.equalsIgnoreCase(SystemActionType.EXIT
				.getString())) {
			return SystemActionType.EXIT;
		} else {
			return SystemActionType.INVALID;
		}
	}
}
