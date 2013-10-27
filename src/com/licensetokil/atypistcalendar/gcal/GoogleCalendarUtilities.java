package com.licensetokil.atypistcalendar.gcal;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.gson.JsonObject;

public class GoogleCalendarUtilities {
	public static final SimpleDateFormat RFC3339_FORMAT_WITH_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	public static final SimpleDateFormat RFC3339_FORMAT_WITHOUT_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	
	public static JsonObject createExtendedPropertiesObject(int localTaskID) {
		JsonObject privateExtendedProperties = new JsonObject();
		privateExtendedProperties.addProperty("atc_localTaskID", Integer.toString(localTaskID));

		JsonObject extendedProperties = new JsonObject();
		extendedProperties.add("private", privateExtendedProperties);
		
		return extendedProperties;
	}
	
	public static JsonObject createDateTimeObject(Calendar date) {
		return createDateTimeObject(RFC3339_FORMAT_WITHOUT_MILLISECONDS, date);
	}
	
	public static JsonObject createDateTimeObject(SimpleDateFormat format, Calendar date) {
		JsonObject dateTimeObject = new JsonObject();
		dateTimeObject.addProperty("dateTime", format.format(date.getTime()));
		
		return dateTimeObject;
	}
}
