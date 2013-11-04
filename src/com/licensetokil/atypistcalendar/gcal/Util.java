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
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class Util {
	private static Logger logger = Logger.getLogger(Util.class.getName());

	public static final String REQUEST_METHOD_POST = "POST";
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String REQUEST_METHOD_DELETE = "DELETE";
	public static final String REQUEST_METHOD_PUT = "PUT";

	public static final String CONTENT_TYPE_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String CONTENT_TYPE_JSON = "application/json";

	private static final SimpleDateFormat RFC3339_FORMAT_WITH_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	private static final SimpleDateFormat RFC3339_FORMAT_WITHOUT_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	private static final SimpleDateFormat GOOGLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private static final JsonParser jsonParser = new JsonParser();

	public static SimpleDateFormat getRfc3339FormatWithMilliseconds() {
		logger.fine("getRfc3339FormatWithMilliseconds method called.");
		return (SimpleDateFormat) RFC3339_FORMAT_WITH_MILLISECONDS.clone();
	}

	public static SimpleDateFormat getRfc3339FormatWithoutMilliseconds() {
		logger.fine("getRfc3339FormatWithoutMilliseconds method called.");
		return (SimpleDateFormat) RFC3339_FORMAT_WITHOUT_MILLISECONDS.clone();
	}

	public static SimpleDateFormat getDateFormat() {
		logger.fine("getDateFormat method called.");
		return (SimpleDateFormat) GOOGLE_DATE_FORMAT.clone();
	}

	public static Calendar parseGoogleDateTimeObject(JsonObject dateTimeObject)
			throws ParseException {
		logger.fine("parseGoogleDateTimeObject method called.");
		return parseGenericGoogleDateObject(dateTimeObject, RFC3339_FORMAT_WITHOUT_MILLISECONDS, "dateTime");
	}

	public static Calendar parseGoogleDateObject(JsonObject dateObject)
			throws ParseException {
		logger.fine("parseGoogleDateObject method called.");
		return parseGenericGoogleDateObject(dateObject, GOOGLE_DATE_FORMAT, "date");
	}

	public static Calendar parseGenericGoogleDateObject(JsonObject dateTimeObject, SimpleDateFormat format, String propertyName)
			throws ParseException {
		logger.fine("parseGenericGoogleDateObject method called.");
		//TODO Throw some kind exception for of ill-formatted JsonObject
		String gooleDateString = dateTimeObject.get(propertyName).getAsString();
		return parseGenericGoogleDateString(gooleDateString, format);
	}

	public static Calendar parseGenericGoogleDateString(String dateTimeString, SimpleDateFormat format)
			throws ParseException {
		logger.fine("parseGenericGoogleDateString method called.");
		Date date = format.parse(dateTimeString);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

	public static JsonObject createGoogleDateTimeObject(Calendar date) {
		logger.fine("createGoogleDateTimeObject method called.");
		return createGenericGoogleDateObject(date, RFC3339_FORMAT_WITHOUT_MILLISECONDS, "dateTime");
	}

	public static JsonObject createGoogleDateObject(Calendar date) {
		logger.fine("createGoogleDateObject method called.");
		return createGenericGoogleDateObject(date, GOOGLE_DATE_FORMAT, "date");
	}

	public static JsonObject createGenericGoogleDateObject(Calendar date, SimpleDateFormat format, String propertyName) {
		logger.fine("createGenericGoogleDateObject method called.");
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(propertyName, createGenericGoogleDateString(date, format));

		return jsonObject;
	}

	public static String createGenericGoogleDateString(Calendar date, SimpleDateFormat format) {
		logger.fine("createGenericGoogleDateString method called.");
		return format.format(date.getTime());
	}

	public static JsonObject parseToJsonObject(String stringToParse)
			throws JsonParseException, IllegalStateException {
		logger.fine("parseToJsonObject method called.");
		return (JsonObject)jsonParser.parse(stringToParse);
	}

	public static String sendJsonHttpsRequest(String url, String requestMethod, Map<String, String> additionalHeaders, JsonObject requestBody)
			throws IOException {
		logger.fine("sendJsonHttpsRequest method called.");

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
		logger.fine("sendUrlencodedFormHttpsRequest method called.");

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
		logger.fine("addHeaders method called.");

		Iterator<String> headersIterator = headers.keySet().iterator();
		while (headersIterator.hasNext()) {
			String currentKey = headersIterator.next();
			httpsConnection.setRequestProperty(currentKey, headers.get(currentKey));
		}
	}

	private static String sendAndReceiveRequest(HttpsURLConnection httpsConnection, String content)
			throws IOException {
		logger.fine("sendAndReceiveRequest method called.");

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
		logger.fine("getJsonObjectValueOrEmptyString method called.");

		if(jsonObject.get(key) != null) {
			return jsonObject.get(key).getAsString();
		}
		else {
			return "";
		}
	}
}
