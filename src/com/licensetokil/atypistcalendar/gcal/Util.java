package com.licensetokil.atypistcalendar.gcal;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Util {
	public static final String REQUEST_METHOD_POST = "POST";
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String REQUEST_METHOD_DELETE = "DELETE";
	public static final String REQUEST_METHOD_PUT = "PUT";

	public static final String CONTENT_TYPE_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String CONTENT_TYPE_JSON = "application/json";

	private static final SimpleDateFormat RFC3339_FORMAT_WITH_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	private static final SimpleDateFormat RFC3339_FORMAT_WITHOUT_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private static final JsonParser jsonParser = new JsonParser();

	public static SimpleDateFormat getRfc3339FormatWithMilliseconds() {
		return (SimpleDateFormat) RFC3339_FORMAT_WITH_MILLISECONDS.clone();
	}

	public static SimpleDateFormat getRfc3339FormatWithoutMilliseconds() {
		return (SimpleDateFormat) RFC3339_FORMAT_WITHOUT_MILLISECONDS.clone();
	}

	public static SimpleDateFormat getDateFormat() {
		return (SimpleDateFormat) DATE_FORMAT.clone();
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
		//Throw some kind exception for of ill-formatted JsonObject
		String dateTimeString = dateTimeObject.get("dateTime").getAsString();
		Date date = format.parse(dateTimeString);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
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

	public static String sendJsonHttpsRequest(String url, String requestMethod, Map<String, String> additionalHeaders, JsonObject requestBody)
			throws IOException {
		//TODO handle get
		//TODO assert get means requestBody == null
		URL urlObject = new URL(url);
		HttpsURLConnection httpsConnection = (HttpsURLConnection)urlObject.openConnection();

		httpsConnection.setRequestMethod(requestMethod);
		httpsConnection.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);

		if(additionalHeaders != null) {
			addHeaders(httpsConnection, additionalHeaders);
		}

		if(requestBody != null) {
			return sendAndReceiveRequest(httpsConnection, requestBody.toString());
		}
		else {
			return sendAndReceiveRequest(httpsConnection, null);
		}
	}

	public static String sendUrlencodedFormHttpsRequest(String url, String requestMethod, Map<String, String> additionalHeaders, Map<String, String> formParameters)
			throws IOException {
		assert requestMethod == REQUEST_METHOD_GET;
		assert requestMethod == REQUEST_METHOD_POST;

		//Handle form parameters first, because if requestMethod == GET then we need to append it to the url.
		String formParametersAsString = "";
		if(formParameters != null) {
			Iterator<String> parametersKeyIterator = formParameters.keySet().iterator();
			while (parametersKeyIterator.hasNext()) {
				String currentKey = parametersKeyIterator.next();
				formParametersAsString += currentKey + "=" + formParameters.get(currentKey) + "&";
			}
		}

		URL urlObject;
		if(requestMethod == REQUEST_METHOD_POST) {
			urlObject = new URL(url);
		}
		else {
			urlObject = new URL(url + "?" + formParametersAsString);
		}

		HttpsURLConnection httpsConnection = (HttpsURLConnection)urlObject.openConnection();

		httpsConnection.setRequestMethod(requestMethod);
		httpsConnection.setRequestProperty("Content-Type", CONTENT_TYPE_WWW_FORM_URLENCODED);

		if(additionalHeaders != null) {
			addHeaders(httpsConnection, additionalHeaders);
		}

		if(requestMethod == REQUEST_METHOD_POST) {
			return sendAndReceiveRequest(httpsConnection, formParametersAsString);
		}
		else {
			return sendAndReceiveRequest(httpsConnection, null);
		}
	}

	private static void addHeaders(HttpsURLConnection httpsConnection, Map<String, String> headers) {
		Iterator<String> headersIterator = headers.keySet().iterator();
		while (headersIterator.hasNext()) {
			String currentKey = headersIterator.next();
			httpsConnection.setRequestProperty(currentKey, headers.get(currentKey));
		}
	}

	private static String sendAndReceiveRequest(HttpsURLConnection httpsConnection, String content)
			throws IOException {
		if(content != null) {
			httpsConnection.setDoOutput(true);
			DataOutputStream dataOutputStream = new DataOutputStream(httpsConnection.getOutputStream());
			dataOutputStream.writeBytes(content);
			dataOutputStream.flush();
			dataOutputStream.close();
		}

		BufferedInputStream serverReplyBufferedInputStream = new BufferedInputStream(httpsConnection.getInputStream(), 10000);
		StringBuilder serverReplyStringBuilder = new StringBuilder(10000);

		int characterFromInputStream = serverReplyBufferedInputStream.read();
		while (characterFromInputStream != -1) {
			serverReplyStringBuilder.append((char)characterFromInputStream);
			characterFromInputStream = serverReplyBufferedInputStream.read();
		}
		serverReplyBufferedInputStream.close();
		return serverReplyStringBuilder.toString();
	}

	public static String getJsonObjectValueOrEmptyString(JsonObject jsonObject, String key) {
		if(jsonObject.get(key) != null) {
			return jsonObject.get(key).getAsString();
		}
		else {
			return "";
		}
	}
}
