package com.licensetokil.atypistcalendar.gcal.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class GoogleCalendarUtilities {
	public static final SimpleDateFormat RFC3339_FORMAT_WITH_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	public static final SimpleDateFormat RFC3339_FORMAT_WITHOUT_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final JsonParser jsonParser = new JsonParser();
	
	public static JsonObject createExtendedPropertiesObject(int localTaskID) {
		JsonObject privateExtendedProperties = new JsonObject();
		privateExtendedProperties.addProperty("atc_localTaskID", Integer.toString(localTaskID));

		JsonObject extendedProperties = new JsonObject();
		extendedProperties.add("private", privateExtendedProperties);
		
		return extendedProperties;
	}
	
	public static JsonObject createDateTimeObject(Calendar date) {
		return createGoogleTimeObject(date, RFC3339_FORMAT_WITHOUT_MILLISECONDS, "dateTime");
	}
	
	public static JsonObject createDateTimeObject(Calendar date, SimpleDateFormat format) {
		return createGoogleTimeObject(date, format, "dateTime");
	}
	
	public static JsonObject createDateObject(Calendar date) {
		return createGoogleTimeObject(date, DATE_FORMAT, "date");
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
