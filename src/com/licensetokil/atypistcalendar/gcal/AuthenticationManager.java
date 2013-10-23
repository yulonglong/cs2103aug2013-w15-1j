package com.licensetokil.atypistcalendar.gcal;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

public class AuthenticationManager {
	protected static final String GOOGLE_API_CLIENT_ID = "896350900683.apps.googleusercontent.com";
	protected static final String GOOGLE_API_CLIENT_SECRET = "MvKdYK7Ec74rMesvRvVfVdDF";
	private static final long TOKEN_EXPIRY_BUFFER = 120; //in seconds

	private static AuthenticationManager instance = null;
	private String authenticationToken;
	private String accessToken;
	private String refreshToken;
	private Date accessTokenExpiry;
	
	private AuthenticationManager() {
		/*
		setAuthenticationToken("4/F696Cw4XXidjxbNX1rVrVdyhvZ3F");
		setAccessToken("ya29.AHES6ZTK0KTseILcLhfUOC3WAcjPaQMEUWf1FWAJl4Hkgw");
		setRefreshToken("1/IzrgDGXLnEV7OMb38K8puKJMRM_dPWrRGfQuE30JRn0");
		setAccessTokenExpiry(new Date(0));*/
		setAuthenticationToken(null);
		setAccessToken(null);
		setRefreshToken(null);
		setAccessTokenExpiry(null);
	}
	
	public static void debug() {
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

	//Special setters and getters
	
	private String getAccessToken() throws IllegalStateException, JsonParseException, IOException {
		assert this.isAuthenticated();
		
		if(accessToken == null) {
			fetchAccessTokenUsingAuthenticationToken();
		}
		else {
			final boolean accessTokenHasExpired = this.getAccessTokenExpiry().getTime() < new Date().getTime();
			if(accessTokenHasExpired) {
				fetchAccessTokenUsingRefreshToken();
			}
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

