package com.licensetokil.atypistcalendar.gcal;

import java.util.Date;
import java.util.HashMap;

import com.google.gson.JsonObject;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

public class AuthenticationManager {
	public static void main(String[] args) {
		AuthenticationManager.getInstance().authenticateUser();
	}
	
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
		System.out.println("hello");
	}
	
	protected void authenticateUserSuccessful(String authenticationToken) {
		setAuthenticationToken(authenticationToken);
		fetchAccessTokenUsingAuthenticationToken();
	}
	
	protected void authenticateUserFailed() {
		;
	}
	
	private void fetchAccessTokenUsingAuthenticationToken() {
		try {
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
			
			System.out.println(getAccessToken());
			System.out.println(getAuthenticationToken());
			System.out.println(getRefreshToken());
			System.out.println(getAccessTokenExpiry());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void fetchAccessTokenUsingRefreshToken() {
		;
	}
	
	private void openAuthenticationDialog() {
		NativeInterface.open();

		AuthenticationDialog authenticationDialog = new AuthenticationDialog();

		NativeInterface.runEventPump();
	}
	
	//Special setters and getters
	
	protected String getAccessToken() {
		if(!this.isAuthenticated()) {
			this.authenticateUser();
			System.out.println("hellooooo");
		}

		final boolean accessTokenHasExpired = this.getAccessTokenExpiry().getTime() < new Date().getTime();
		if(accessTokenHasExpired) {
			//refresh token
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

