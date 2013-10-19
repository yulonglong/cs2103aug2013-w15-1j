package com.licensetokil.atypistcalendar.gcal;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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
		//parametersAsString = parametersAsString.substring(0, parametersAsString.length() - 1);
		System.out.println(parametersAsString);

		httpsConnection.setDoOutput(true);
		DataOutputStream dataOutputStream = new DataOutputStream(httpsConnection.getOutputStream());
		dataOutputStream.writeBytes(parametersAsString);
		dataOutputStream.flush();
		dataOutputStream.close();

		BufferedInputStream bufferedInputStream = new BufferedInputStream(httpsConnection.getInputStream(), 10000);
		StringBuilder returnStringBuilder = new StringBuilder(10000);

		int characterFromInputStream = bufferedInputStream.read();
		while (characterFromInputStream != -1) {
			returnStringBuilder.append((char)characterFromInputStream);
			characterFromInputStream = bufferedInputStream.read();
		}
		bufferedInputStream.close();
		return returnStringBuilder.toString();
	}
}
