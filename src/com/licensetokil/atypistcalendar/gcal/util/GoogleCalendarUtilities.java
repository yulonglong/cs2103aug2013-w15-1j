package com.licensetokil.atypistcalendar.gcal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class GoogleCalendarUtilities {
	private static final String RFC3339_FORMAT_WITH_MILLISECONDS_STRING_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	private static final String RFC3339_FORMAT_WITHOUT_MILLISECONDS_STRING_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
	private static final String DATE_FORMAT_STRING_PATTERN = "yyyy-MM-dd";

	private static final JsonParser jsonParser = new JsonParser();

	public static SimpleDateFormat getRfc3339FormatWithMilliseconds() {
		return new SimpleDateFormat(RFC3339_FORMAT_WITH_MILLISECONDS_STRING_PATTERN);
	}

	public static SimpleDateFormat getRfc3339FormatWithoutMilliseconds() {
		return new SimpleDateFormat(RFC3339_FORMAT_WITHOUT_MILLISECONDS_STRING_PATTERN);
	}

	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(DATE_FORMAT_STRING_PATTERN);
	}

	public static JsonObject createExtendedPropertiesObject(int localTaskID) {
		JsonObject privateExtendedProperties = new JsonObject();
		privateExtendedProperties.addProperty("atc_localTaskID", Integer.toString(localTaskID));

		JsonObject extendedProperties = new JsonObject();
		extendedProperties.add("private", privateExtendedProperties);

		return extendedProperties;
	}

	public static Calendar getCalendarObjectFromDateTimeObject(JsonObject dateTimeObject)
			throws ParseException {
		return getCalendarObjectFromDateTimeObject(dateTimeObject, getRfc3339FormatWithoutMilliseconds());
	}

	//refactoring of names needed
	public static Calendar getCalendarObjectFromDateTimeObject(JsonObject dateTimeObject, SimpleDateFormat format)
			throws ParseException {
		//Throw some kind exceptino for of ill-formatted JsonObject
		String dateTimeString = dateTimeObject.get("dateTime").getAsString();
		Date date = format.parse(dateTimeString);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

	public static JsonObject createDateTimeObject(Calendar date) {
		return createGoogleTimeObject(date, getRfc3339FormatWithoutMilliseconds(), "dateTime");
	}

	public static JsonObject createDateTimeObject(Calendar date, SimpleDateFormat format) {
		return createGoogleTimeObject(date, format, "dateTime");
	}

	public static JsonObject createDateObject(Calendar date) {
		return createGoogleTimeObject(date, getDateFormat(), "date");
	}

	public static JsonObject createGoogleTimeObject(Calendar date, SimpleDateFormat format, String propertyName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(propertyName, format.format(date.getTime()));

		return jsonObject;
	}

	public static JsonObject parseToJsonObject(String stringToParse)
			throws JsonParseException, IllegalStateException {
		return (JsonObject)jsonParser.parse(stringToParse);
	}
}
