package com.licensetokil.atypistcalendar.gcal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

class AuthenticationManager {
	protected static final String GOOGLE_API_CLIENT_ID = "896350900683.apps.googleusercontent.com";
	protected static final String GOOGLE_API_CLIENT_SECRET = "MvKdYK7Ec74rMesvRvVfVdDF";

	private static final long TOKEN_EXPIRY_BUFFER = 120; //in seconds
	private static final File AUTHENTICATION_SAVE_FILE = new File("ATC_AUTH.txt");

	private static AuthenticationManager instance = null;

	private static Logger logger = Logger.getLogger(AuthenticationManager.class.getName());

	private String authenticationToken;
	private String accessToken;
	private String refreshToken;
	private Date accessTokenExpiry;

	private AuthenticationManager() {
		authenticationToken = "";
		accessToken = "";
		refreshToken = "";
		accessTokenExpiry = null;
	}

	//TODO delete this on production
	protected static void debug() {
		System.out.println(AuthenticationManager.getInstance().authenticationToken);
		System.out.println(AuthenticationManager.getInstance().accessToken);
		System.out.println(AuthenticationManager.getInstance().refreshToken);
		System.out.println(AuthenticationManager.getInstance().accessTokenExpiry);
	}

	protected static AuthenticationManager getInstance() {
		if(instance == null) {
			instance = new AuthenticationManager();
		}
		return instance;
	}

	protected boolean isAuthenticated() {
		if(authenticationToken.equals("")) {
			return false;
		}
		return true;
	}

	protected void authenticateUser() {
		AuthenticationManager.getInstance().openAuthenticationDialog();
	}

	//TODO refactor this to Dialog class?
	/**
	 * This should only be called by the AuthenticationDialog class
	 * This method is fail safe. The user will not be registered as authenticated should any exceptions occur.
	 * @param authenticationToken
	 */
	protected void authenticateUserSuccess(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	protected void authenticateUserFailed() {
		//TODO what to do? what to do?!?!
	}

	protected void forgetAuthenticationDetails() {
		authenticationToken = "";
		accessToken = "";
		refreshToken = "";
		accessTokenExpiry = null;
	}

	protected HashMap<String, String> getAuthorizationHeader()
			throws IllegalStateException, JsonParseException, IOException {
		HashMap<String, String> header = new HashMap<>();
		header.put("Authorization", "Bearer " + getAccessToken());
		return header;
	}

	private void fetchAccessTokenUsingAuthenticationToken()
			throws IllegalStateException, JsonParseException, IOException {
		assert !authenticationToken.equals("");

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("code", authenticationToken);
		parameters.put("client_id", GOOGLE_API_CLIENT_ID);
		parameters.put("client_secret", GOOGLE_API_CLIENT_SECRET);
		parameters.put("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
		parameters.put("grant_type", "authorization_code");

		JsonObject serverReply = Util.parseToJsonObject(
				Util.sendUrlencodedFormHttpsRequest(
						"https://accounts.google.com/o/oauth2/token",
						Util.REQUEST_METHOD_POST,
						null,
						parameters
				)
		);

		accessToken = serverReply.get("access_token").getAsString();
		refreshToken = serverReply.get("refresh_token").getAsString();

		long timeNow = new Date().getTime();
		long expiryTime = timeNow + (serverReply.get("expires_in").getAsLong() - TOKEN_EXPIRY_BUFFER) * 1000L;
		accessTokenExpiry = new Date(expiryTime);
	}

	private void fetchAccessTokenUsingRefreshToken()
			throws IllegalStateException, JsonParseException, IOException {
		assert !refreshToken.equals("");

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("refresh_token", refreshToken);
		parameters.put("client_id", GOOGLE_API_CLIENT_ID);
		parameters.put("client_secret", GOOGLE_API_CLIENT_SECRET);
		parameters.put("grant_type", "refresh_token");

		JsonObject serverReply = Util.parseToJsonObject(
				Util.sendUrlencodedFormHttpsRequest(
						"https://accounts.google.com/o/oauth2/token",
						Util.REQUEST_METHOD_POST,
						null,
						parameters
				)
		);

		accessToken = serverReply.get("access_token").getAsString();

		long timeNow = new Date().getTime();
		long expiryTime = timeNow + (serverReply.get("expires_in").getAsLong() - TOKEN_EXPIRY_BUFFER) * 1000L;
		accessTokenExpiry = new Date(expiryTime);
	}

	private void openAuthenticationDialog() {
		NativeInterface.open();
		new AuthenticationDialog();
	}

	/**
	 * ...
	 * This method bypasses the getter accessToken.
	 * @throws IOException
	 */
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

	/**
	 * ...
	 * Catches all IOExceptions and fails cleanly; upon failure, all tokens will be set to null.
	 */
	protected void readAuthenticationDetailsFromFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(AUTHENTICATION_SAVE_FILE));

			authenticationToken = reader.readLine();
			accessToken = reader.readLine();
			refreshToken = reader.readLine();

			if(accessToken.equals("")) {
				accessTokenExpiry = null;
			}
			else {
				accessTokenExpiry = new Date(0L); //assume the token has expired
			}

			reader.close();
		}
		catch(IOException e) {
			authenticationToken = "";
			accessToken = "";
			refreshToken = "";
			accessTokenExpiry = null;
		}
	}

	private String getAccessToken() throws IllegalStateException, JsonParseException, IOException {
		assert isAuthenticated();

		if(accessToken.equals("")) {
			fetchAccessTokenUsingAuthenticationToken();
			writeAuthenticationDetailsToFile();
		}
		else {
			final boolean accessTokenHasExpired = accessTokenExpiry.getTime() < new Date().getTime();
			if(accessTokenHasExpired) {
				fetchAccessTokenUsingRefreshToken();
				writeAuthenticationDetailsToFile();
			}
		}
		return accessToken;
	}
}

