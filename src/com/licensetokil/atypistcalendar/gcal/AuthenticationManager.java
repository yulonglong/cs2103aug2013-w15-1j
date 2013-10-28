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
import com.google.gson.JsonParser;
import com.licensetokil.atypistcalendar.gcal.util.HttpsConnectionHelper;

class AuthenticationManager {
	protected static final String GOOGLE_API_CLIENT_ID = "896350900683.apps.googleusercontent.com";
	protected static final String GOOGLE_API_CLIENT_SECRET = "MvKdYK7Ec74rMesvRvVfVdDF";

	private static final long TOKEN_EXPIRY_BUFFER = 120; //in seconds
	private static final File AUTHENTICATION_SAVE_FILE = new File("ATC_AUTH.txt");

	private static AuthenticationManager instance = null;
	private String authenticationToken;
	private String accessToken;
	private String refreshToken;
	private Date accessTokenExpiry;

	private AuthenticationManager() {
		setAuthenticationToken(null);
		setAccessToken(null);
		setRefreshToken(null);
		setAccessTokenExpiry(null);
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
		if(getAuthenticationToken() == null) {
			return false;
		}
		return true;
	}

	protected void authenticateUser() {
		AuthenticationManager.getInstance().openAuthenticationDialog();
	}

	/**
	 * ...
	 * This method is fail safe. The user will not be registered as authenticated should any exceptions occur.
	 * @param authenticationToken
	 */
	protected void authenticateUserSuccess(String authenticationToken) {
		setAuthenticationToken(authenticationToken);
		try {
			fetchAccessTokenUsingAuthenticationToken();
			writeAuthenticationDetailsToFile();
		} catch (IllegalStateException | JsonParseException | IOException e) {
			setAuthenticationToken(null);
		}
	}

	protected void authenticateUserFailed() {
		//TODO what to do? what to do?!?!
	}

	protected HashMap<String, String> getAuthorizationHeader()
			throws IllegalStateException, JsonParseException, IOException {
		HashMap<String, String> header = new HashMap<>();
		header.put("Authorization", "Bearer " + this.getAccessToken());
		return header;
	}

	private void fetchAccessTokenUsingAuthenticationToken()
			throws IllegalStateException, JsonParseException, IOException {
		assert getAuthenticationToken() != null;

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("code", this.getAuthenticationToken());
		parameters.put("client_id", GOOGLE_API_CLIENT_ID);
		parameters.put("client_secret", GOOGLE_API_CLIENT_SECRET);
		parameters.put("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
		parameters.put("grant_type", "authorization_code");

		String serverReplyAsString = HttpsConnectionHelper.sendUrlencodedFormRequest(
				"https://accounts.google.com/o/oauth2/token",
				HttpsConnectionHelper.REQUEST_METHOD_POST,
				null,
				parameters);
		JsonObject serverReply = (JsonObject)new JsonParser().parse(serverReplyAsString);

		this.setAccessToken(serverReply.get("access_token").getAsString());
		this.setRefreshToken(serverReply.get("refresh_token").getAsString());

		long timeNowAsLong = new Date().getTime();
		long expiryTime = timeNowAsLong + (serverReply.get("expires_in").getAsLong() - TOKEN_EXPIRY_BUFFER) * 1000L;
		this.setAccessTokenExpiry(new Date(expiryTime));
	}

	private void fetchAccessTokenUsingRefreshToken()
			throws IllegalStateException, JsonParseException, IOException {
		assert getRefreshToken() != null;

		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("refresh_token", this.getRefreshToken());
		parameters.put("client_id", GOOGLE_API_CLIENT_ID);
		parameters.put("client_secret", GOOGLE_API_CLIENT_SECRET);
		parameters.put("grant_type", "refresh_token");

		String serverReplyAsString = HttpsConnectionHelper.sendUrlencodedFormRequest(
				"https://accounts.google.com/o/oauth2/token",
				HttpsConnectionHelper.REQUEST_METHOD_POST,
				null,
				parameters);
		JsonObject serverReply = (JsonObject)new JsonParser().parse(serverReplyAsString);

		this.setAccessToken(serverReply.get("access_token").getAsString());

		Date timeNow = new Date();
		long expiryTime = timeNow.getTime() + (serverReply.get("expires_in").getAsLong() - TOKEN_EXPIRY_BUFFER) * 1000L;
		this.setAccessTokenExpiry(new Date(expiryTime));
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

		writer.write(getAuthenticationToken());
		writer.newLine();
		writer.write(accessToken);
		writer.newLine();
		writer.write(getRefreshToken());
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

			String readAuthenticationToken = reader.readLine();
			if(readAuthenticationToken.isEmpty()) {
				setAuthenticationToken(null);
			}
			else {
				setAuthenticationToken(readAuthenticationToken);
			}

			String readAccessToken = reader.readLine();
			if(readAccessToken.isEmpty()) {
				setAccessToken(null);
				setAccessTokenExpiry(null);
			}
			else {
				setAccessToken(readAccessToken);
				setAccessTokenExpiry(new Date(0L)); //assume the token has expired
			}

			String readRefreshToken = reader.readLine();
			if(readRefreshToken.isEmpty()) {
				setRefreshToken(null);
			}
			else {
				setRefreshToken(readRefreshToken);
			}

			reader.close();
		}
		catch(IOException e) {
			setAuthenticationToken(null);
			setAccessToken(null);
			setRefreshToken(null);
			setAccessTokenExpiry(null);
		}
	}

	//Special setters and getters

	private String getAccessToken() throws IllegalStateException, JsonParseException, IOException {
		assert this.isAuthenticated();

		final boolean accessTokenHasExpired = this.getAccessTokenExpiry().getTime() < new Date().getTime();
		if(accessTokenHasExpired) {
			fetchAccessTokenUsingRefreshToken();
			writeAuthenticationDetailsToFile();
		}
		return accessToken;
	}

	//Default setters and getters

	private String getAuthenticationToken() {
		return authenticationToken;
	}

	private void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	private String getRefreshToken() {
		return refreshToken;
	}

	private void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	private void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	private Date getAccessTokenExpiry() {
		return accessTokenExpiry;
	}

	private void setAccessTokenExpiry(Date accessTokenExpiry) {
		this.accessTokenExpiry = accessTokenExpiry;
	}
}

