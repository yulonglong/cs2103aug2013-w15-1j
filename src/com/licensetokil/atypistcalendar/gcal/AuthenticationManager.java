package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

public class AuthenticationManager {
	protected static final String GOOGLE_API_CLIENT_ID = "933956973767-2e8udf2em20mgl6djjesu5ggpot4um19.apps.googleusercontent.com";
	protected static final String GOOGLE_API_CLIENT_SECRET = "JM25HSU3mBxWiGdSblG7Iqq5";
	private static final long TOKEN_EXPIRY_BUFFER = 120; //in seconds

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

	protected static AuthenticationManager getInstance() {
		if(instance == null) {
			instance = new AuthenticationManager();
		}
		return instance;
	}

	protected boolean isAuthenticated() {
		if(authenticationToken == null) {
			return false;
		}
		return true;
	}

	protected void authenticateUser() {
		AuthenticationManager.getInstance().openAuthenticationDialog();
	}

	protected void authenticateUserSuccess(String authenticationToken) {
		setAuthenticationToken(authenticationToken);
	}

	protected void authenticateUserFailed() {
		;
	}

	private void fetchAccessTokenUsingAuthenticationToken()
			throws IllegalStateException, JsonParseException, IOException {
		HashMap<String, String> parameters = new HashMap<>();
		parameters.put("code", this.getAuthenticationToken());
		parameters.put("client_id", GOOGLE_API_CLIENT_ID);
		parameters.put("client_secret", GOOGLE_API_CLIENT_SECRET);
		parameters.put("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
		parameters.put("grant_type", "authorization_code");

		JsonObject serverReply = HttpsConnectionHelper.sendByPostReturningReplyAsJsonObject("https://accounts.google.com/o/oauth2/token", parameters);

		this.setAccessToken(serverReply.get("access_token").getAsString());
		this.setRefreshToken(serverReply.get("refresh_token").getAsString());

		Date timeNow = new Date();
		long expiryTime = timeNow.getTime() + (serverReply.get("expires_in").getAsLong() - TOKEN_EXPIRY_BUFFER) * 1000L;
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

		JsonObject serverReply = HttpsConnectionHelper.sendByPostReturningReplyAsJsonObject("https://accounts.google.com/o/oauth2/token", parameters);

		this.setAccessToken(serverReply.get("access_token").getAsString());

		Date timeNow = new Date();
		long expiryTime = timeNow.getTime() + (serverReply.get("expires_in").getAsLong() - TOKEN_EXPIRY_BUFFER) * 1000L;
		this.setAccessTokenExpiry(new Date(expiryTime));
	}

	private void openAuthenticationDialog() {
		NativeInterface.open();
		new AuthenticationDialog();
	}

	//Special setters and getters

	protected String getAccessToken() throws IllegalStateException, JsonParseException, IOException {
		assert this.isAuthenticated();

		final boolean accessTokenIsNull = accessToken == null;
		final boolean accessTokenHasExpired = this.getAccessTokenExpiry().getTime() < new Date().getTime();
		if(accessTokenIsNull) {
			fetchAccessTokenUsingAuthenticationToken();
		}
		else if(accessTokenHasExpired) {
			fetchAccessTokenUsingRefreshToken();
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

