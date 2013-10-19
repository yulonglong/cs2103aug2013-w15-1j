package com.licensetokil.atypistcalendar.gcal;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

// TODO handle non 200 replies

public class HttpsConnectionHelper {
	public static String sendByPostReturningReplyAsString(String url, Map<String, String> parameters) throws IOException {
		URL urlObject = new URL(url);
		HttpsURLConnection httpsConnection = (HttpsURLConnection)urlObject.openConnection();

		httpsConnection.setRequestMethod("POST");

		String parametersAsString = "";
		Iterator<String> parametersKeyIterator = parameters.keySet().iterator();
		while (parametersKeyIterator.hasNext()) {
			String currentKey = parametersKeyIterator.next();
			parametersAsString += currentKey + "=" + parameters.get(currentKey) + "&";
		}

		httpsConnection.setDoOutput(true);
		DataOutputStream dataOutputStream = new DataOutputStream(httpsConnection.getOutputStream());
		dataOutputStream.writeBytes(parametersAsString);
		dataOutputStream.flush();
		dataOutputStream.close();

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
	
	public static JsonObject sendByPostReturningReplyAsJsonObject(String url, Map<String, String> parameters)
			throws IOException, IllegalStateException, JsonParseException {
		String serverReply = sendByPostReturningReplyAsString(url, parameters);
		JsonParser jsonParser = new JsonParser();
		return jsonParser.parse(serverReply).getAsJsonObject();
	}
}
