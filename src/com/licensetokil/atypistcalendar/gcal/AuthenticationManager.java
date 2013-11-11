package com.licensetokil.atypistcalendar.gcal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

class AuthenticationManager {
	protected static final String GOOGLE_API_CLIENT_ID = "896350900683.apps.googleusercontent.com";
	protected static final String GOOGLE_API_CLIENT_SECRET = "MvKdYK7Ec74rMesvRvVfVdDF";

	private static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER_LABEL = "Authorization";

	private static final String NULL_TOKEN = "";

	private static final String GOOGLE_REQUEST_URL_FETCH_ACCESS_TOKEN = "https://accounts.google.com/o/oauth2/token";

	private static final String FETCH_ACCESS_TOKEN_REQUEST_LABEL_CODE = "code";
	private static final String FETCH_ACCESS_TOKEN_REQUEST_LABEL_CLIENT_ID = "client_id";
	private static final String FETCH_ACCESS_TOKEN_REQUEST_LABEL_CLIENT_SECRET = "client_secret";
	private static final String FETCH_ACCESS_TOKEN_REQUEST_LABEL_REDIRECT_URI = "redirect_uri";
	private static final String FETCH_ACCESS_TOKEN_REQUEST_LABEL_GRANT_TYPE = "grant_type";
	private static final String FETCH_ACCESS_TOKEN_REQUEST_LABEL_REFRESH_TOKEN = "refresh_token";

	private static final String FETCH_ACCESS_TOKEN_REQUEST_VALUE_GRANT_TYPE = "authorization_code";
	private static final String FETCH_ACCESS_TOKEN_REQUEST_VALUE_REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

	private static final String FETCH_ACCESS_TOKEN_REPLY_LABEL_ACCESS_TOKEN = "access_token";
	private static final String FETCH_ACCESS_TOKEN_REPLY_LABEL_REFRESH_TOKEN = "refresh_token";
	private static final String FETCH_ACCESS_TOKEN_REPLY_LABEL_EXPIRES_IN = "expires_in";

	private static final long ACCESS_TOKEN_EXPIRY_BUFFER = 120_000L; // in milliseconds
	private static final long ACCESS_TOKEN_EXPIRY_PERIOD_CONVERSION_FACTOR = 1_000L; // factor to multiply by to convert the period to milliseconds

	private static final Date UNIX_EPOCH = new Date(0L);

	private static final File AUTHENTICATION_SAVE_FILE = new File("ATC_AUTH.txt");

	private static AuthenticationManager instance = null;
	private String authenticationToken;
	private String accessToken;
	private String refreshToken;
	private Date accessTokenExpiry;

	private AuthenticationManager() {
		authenticationToken = NULL_TOKEN;
		accessToken = NULL_TOKEN;
		refreshToken = NULL_TOKEN;
		accessTokenExpiry = null;
	}

	protected static AuthenticationManager getInstance() {
		if (instance == null) {
			instance = new AuthenticationManager();
		}
		return instance;
	}

	protected boolean isAuthenticated() {
		if (authenticationToken.equals(NULL_TOKEN)) {
			return false;
		}
		return true;
	}

	protected void authenticateUser() {
		AuthenticationManager.getInstance().openAuthenticationDialog();
	}

	// The following two methods should only be called by the
	// AuthenticationDialog class
	protected void authenticateUserSuccess(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	// This method is a fail safe. The user will not be registered as
	// authenticated should any exceptions occur.
	protected void authenticateUserFailed() {
		try {
			deleteAuthenticationDetails();
		} catch (IOException e) {
			// Do nothing. (File could not be written)
			e.printStackTrace();
		}
	}

	protected void deleteAuthenticationDetails() throws IOException {
		authenticationToken = NULL_TOKEN;
		accessToken = NULL_TOKEN;
		refreshToken = NULL_TOKEN;
		accessTokenExpiry = null;
		writeAuthenticationDetailsToFile();
	}

	protected HashMap<String, String> getAuthorizationHeader()
			throws IllegalStateException, JsonParseException, IOException {
		HashMap<String, String> header = new HashMap<>();
		header.put(AUTHORIZATION_HEADER_LABEL,
				AUTHORIZATION_HEADER_VALUE_PREFIX + getAccessToken());
		return header;
	}

	//Catches all IOExceptions and fails cleanly.
	//Upon failure, all tokens will be set to null.
	protected void readAuthenticationDetailsFromFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(AUTHENTICATION_SAVE_FILE));
	
			authenticationToken = reader.readLine();
			accessToken = reader.readLine();
			refreshToken = reader.readLine();
	
			boolean accessTokenIsNull = accessToken.equals(NULL_TOKEN);
			if (accessTokenIsNull) {
				accessTokenExpiry = null;
			} else {
				// assume the token has expired
				accessTokenExpiry = UNIX_EPOCH;
			}
	
			reader.close();
		} catch (IOException e) {
			authenticationToken = NULL_TOKEN;
			accessToken = NULL_TOKEN;
			refreshToken = NULL_TOKEN;
			accessTokenExpiry = null;
		}
	}

	private void fetchAccessTokenUsingAuthenticationToken()
			throws IllegalStateException, JsonParseException, IOException {
		assert !authenticationToken.equals(NULL_TOKEN);

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_CODE, authenticationToken);
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_CLIENT_ID, GOOGLE_API_CLIENT_ID);
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_CLIENT_SECRET, GOOGLE_API_CLIENT_SECRET);
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_REDIRECT_URI, FETCH_ACCESS_TOKEN_REQUEST_VALUE_REDIRECT_URI);
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_GRANT_TYPE, FETCH_ACCESS_TOKEN_REQUEST_VALUE_GRANT_TYPE);

		JsonObject serverReply = Utilities.parseToJsonObject(
				Utilities.sendUrlencodedFormHttpsRequest(
						GOOGLE_REQUEST_URL_FETCH_ACCESS_TOKEN,
						Utilities.REQUEST_METHOD_POST,
						Utilities.EMPTY_ADDITIONAL_HEADERS,
						parameters
				)
		);

		accessToken = Utilities.getJsonObjectValueOrEmptyString(serverReply, FETCH_ACCESS_TOKEN_REPLY_LABEL_ACCESS_TOKEN);
		refreshToken = Utilities.getJsonObjectValueOrEmptyString(serverReply, FETCH_ACCESS_TOKEN_REPLY_LABEL_REFRESH_TOKEN);

		long timeNow = new Date().getTime();
		long accessTokenValidityPeriod = serverReply.get(FETCH_ACCESS_TOKEN_REPLY_LABEL_EXPIRES_IN).getAsLong() *
				ACCESS_TOKEN_EXPIRY_PERIOD_CONVERSION_FACTOR;
		long expiryTime = timeNow + accessTokenValidityPeriod - ACCESS_TOKEN_EXPIRY_BUFFER;
		accessTokenExpiry = new Date(expiryTime);
	}

	private void fetchAccessTokenUsingRefreshToken()
			throws IllegalStateException, JsonParseException, IOException {
		assert !refreshToken.equals(NULL_TOKEN);

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_REFRESH_TOKEN, refreshToken);
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_CLIENT_ID, GOOGLE_API_CLIENT_ID);
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_CLIENT_SECRET, GOOGLE_API_CLIENT_SECRET);
		parameters.put(FETCH_ACCESS_TOKEN_REQUEST_LABEL_GRANT_TYPE, FETCH_ACCESS_TOKEN_REQUEST_LABEL_REFRESH_TOKEN);

		JsonObject serverReply = Utilities.parseToJsonObject(
				Utilities.sendUrlencodedFormHttpsRequest(
						GOOGLE_REQUEST_URL_FETCH_ACCESS_TOKEN,
						Utilities.REQUEST_METHOD_POST,
						Utilities.EMPTY_ADDITIONAL_HEADERS,
						parameters
				)
		);

		accessToken = Utilities.getJsonObjectValueOrEmptyString(serverReply, FETCH_ACCESS_TOKEN_REPLY_LABEL_ACCESS_TOKEN);

		long timeNow = new Date().getTime();
		long accessTokenValidityPeriod = serverReply.get(FETCH_ACCESS_TOKEN_REPLY_LABEL_EXPIRES_IN).getAsLong() *
				ACCESS_TOKEN_EXPIRY_PERIOD_CONVERSION_FACTOR;
		long expiryTime = timeNow + accessTokenValidityPeriod - ACCESS_TOKEN_EXPIRY_BUFFER;
		accessTokenExpiry = new Date(expiryTime);
	}

	private void openAuthenticationDialog() {
		NativeInterface.open();
		new AuthenticationDialog();
	}

	private void writeAuthenticationDetailsToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(AUTHENTICATION_SAVE_FILE));

		writer.write(authenticationToken);
		writer.newLine();
		writer.write(accessToken);
		writer.newLine();
		writer.write(refreshToken);
		writer.newLine();

		writer.close();
	}

	private String getAccessToken()
			throws IllegalStateException, JsonParseException, IOException {
		assert isAuthenticated();

		boolean accessTokenIsNull = accessToken.equals(NULL_TOKEN);
		if (accessTokenIsNull) {
			fetchAccessTokenUsingAuthenticationToken();
			writeAuthenticationDetailsToFile();
		} else {
			boolean accessTokenHasExpired = accessTokenExpiry.getTime() < new Date().getTime();
			if (accessTokenHasExpired) {
				fetchAccessTokenUsingRefreshToken();
				writeAuthenticationDetailsToFile();
			}
		}
		return accessToken;
	}
}
