package com.licensetokil.atypistcalendar.gcal;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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

	public static final HashMap<String, String> EMPTY_ADDITIONAL_HEADERS = null;
	public static final HashMap<String, String> EMPTY_FORM_PARAMETERS = null;
	public static final JsonObject EMPTY_REQUEST_BODY = null;

	public static final SimpleDateFormat RFC3339_FORMAT_WITH_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	public static final SimpleDateFormat RFC3339_FORMAT_WITHOUT_MILLISECONDS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	public static final SimpleDateFormat GOOGLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private static final JsonParser jsonParser = new JsonParser();

	private static final String EMPTY_STRING = "";

	private static final String HTTP_HEADER_LABEL_CONTENT_TYPE = "Content-Type";

	private static final String LABEL_DATE = "date";
	private static final String LABEL_DATE_TIME = "dateTime";

	private static final String NO_CONTENT = null;

	public static Calendar parseGoogleDateTimeObject(JsonObject dateTimeObject)
			throws ParseException {
		return parseGenericGoogleDateObject(dateTimeObject, RFC3339_FORMAT_WITHOUT_MILLISECONDS, LABEL_DATE_TIME);
	}

	public static Calendar parseGoogleDateObject(JsonObject dateObject)
			throws ParseException {
		return parseGenericGoogleDateObject(dateObject, GOOGLE_DATE_FORMAT, LABEL_DATE);
	}

	public static Calendar parseGenericGoogleDateObject(
			JsonObject dateTimeObject,
			SimpleDateFormat format,
			String propertyName)
			throws ParseException {
		String googleDateString = getJsonObjectValueOrEmptyString(dateTimeObject, propertyName);
		return parseGenericGoogleDateString(googleDateString, format);
	}

	public static Calendar parseGenericGoogleDateString(String dateTimeString, SimpleDateFormat format)
			throws ParseException {

		Date date = format.parse(dateTimeString);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}

	public static JsonObject createGoogleDateTimeObject(Calendar date) {
		return createGenericGoogleDateObject(date, RFC3339_FORMAT_WITHOUT_MILLISECONDS, LABEL_DATE_TIME);
	}

	public static JsonObject createGoogleDateObject(Calendar date) {
		return createGenericGoogleDateObject(date, GOOGLE_DATE_FORMAT, LABEL_DATE);
	}

	public static JsonObject createGenericGoogleDateObject(
			Calendar date,
			SimpleDateFormat format,
			String propertyName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(propertyName, createGenericGoogleDateString(date, format));

		return jsonObject;
	}

	public static String createGenericGoogleDateString(Calendar date, SimpleDateFormat format) {
		return format.format(date.getTime());
	}

	public static JsonObject parseToJsonObject(String stringToParse)
			throws JsonParseException, IllegalStateException {
		return (JsonObject) jsonParser.parse(stringToParse);
	}

	public static String sendJsonHttpsRequest(
			String url,
			String requestMethod,
			HashMap<String, String> additionalHeaders,
			JsonObject requestBody)
			throws IOException {

		if (requestMethod == REQUEST_METHOD_GET) {
			assert requestBody == EMPTY_REQUEST_BODY;
		}

		URL urlObject = new URL(url);
		HttpsURLConnection httpsConnection = (HttpsURLConnection) urlObject.openConnection();

		httpsConnection.setRequestMethod(requestMethod);
		httpsConnection.setRequestProperty(HTTP_HEADER_LABEL_CONTENT_TYPE, CONTENT_TYPE_JSON);

		if (additionalHeaders != EMPTY_ADDITIONAL_HEADERS) {
			addHeaders(httpsConnection, additionalHeaders);
		}

		if (requestBody == EMPTY_REQUEST_BODY) {
			return sendAndReceiveRequest(httpsConnection, NO_CONTENT);
		} else {
			return sendAndReceiveRequest(httpsConnection, requestBody.toString());
		}
	}

	public static String sendUrlencodedFormHttpsRequest(
			String url,
			String requestMethod,
			HashMap<String, String> additionalHeaders,
			HashMap<String, String> formParameters)
			throws IOException {

		assert requestMethod == REQUEST_METHOD_GET || requestMethod == REQUEST_METHOD_POST;

		// Handle form parameters first, because if requestMethod == GET then we need to append it to the url.
		String formParametersAsString = EMPTY_STRING;
		if (formParameters != EMPTY_FORM_PARAMETERS) {
			for(String key : formParameters.keySet()) {
				formParametersAsString += key + "=" + formParameters.get(key) + "&";
			}
		}

		URL urlObject;
		if (requestMethod == REQUEST_METHOD_POST) {
			urlObject = new URL(url);
		} else {
			urlObject = new URL(url + "?" + formParametersAsString);
		}

		HttpsURLConnection httpsConnection = (HttpsURLConnection) urlObject.openConnection();

		httpsConnection.setRequestMethod(requestMethod);
		httpsConnection.setRequestProperty(HTTP_HEADER_LABEL_CONTENT_TYPE, CONTENT_TYPE_WWW_FORM_URLENCODED);

		if (additionalHeaders != EMPTY_ADDITIONAL_HEADERS) {
			addHeaders(httpsConnection, additionalHeaders);
		}

		if (requestMethod == REQUEST_METHOD_POST) {
			return sendAndReceiveRequest(httpsConnection, formParametersAsString);
		} else {
			return sendAndReceiveRequest(httpsConnection, NO_CONTENT);
		}
	}

	private static void addHeaders(HttpsURLConnection httpsConnection, HashMap<String, String> headers) {
		for(String key : headers.keySet()) {
			httpsConnection.setRequestProperty(key, headers.get(key));
		}
	}

	private static String sendAndReceiveRequest(HttpsURLConnection httpsConnection, String content)
			throws IOException {

		if (content != NO_CONTENT) {
			httpsConnection.setDoOutput(true);
			DataOutputStream dataOutputStream = new DataOutputStream(httpsConnection.getOutputStream());
			dataOutputStream.writeBytes(content);
			dataOutputStream.flush();
			dataOutputStream.close();
		}

		BufferedInputStream serverReplyBufferedInputStream = new BufferedInputStream(httpsConnection.getInputStream());
		StringBuilder serverReplyStringBuilder = new StringBuilder();

		int characterFromInputStream = serverReplyBufferedInputStream.read();
		while (characterFromInputStream != -1) {
			serverReplyStringBuilder.append((char) characterFromInputStream);
			characterFromInputStream = serverReplyBufferedInputStream.read();
		}
		serverReplyBufferedInputStream.close();
		return serverReplyStringBuilder.toString();
	}

	public static String getJsonObjectValueOrEmptyString(JsonObject jsonObject,String key) {
		if (jsonObject.get(key) != null) {
			return jsonObject.get(key).getAsString();
		} else {
			return EMPTY_STRING;
		}
	}
}
