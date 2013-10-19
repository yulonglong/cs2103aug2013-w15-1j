package com.licensetokil.atypistcalendar.gcal;

import java.util.Date;
import java.util.HashMap;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

public class AuthenticationManager {
	protected static final String GOOGLE_API_CLIENT_ID = "933956973767-2e8udf2em20mgl6djjesu5ggpot4um19.apps.googleusercontent.com";
	protected static final String GOOGLE_API_CLIENT_SECRET = "JM25HSU3mBxWiGdSblG7Iqq5";

	private static AuthenticationManager instance = null;
	private String authenticationToken;
	private String accessToken;
	private String refreshToken;

	private Date accessTokenExpiry;

	private AuthenticationManager() {
		authenticationToken = null;
		accessToken = null;
		refreshToken = null;
		accessTokenExpiry = null;
	}

	public static AuthenticationManager getInstance() {
		if(instance == null) {
			instance = new AuthenticationManager();
		}
		return instance;
	}

	public static void main(String[] args) {
		AuthenticationManager.getInstance().authenticateUser();
	}

	public void authenticateUser() {
		AuthenticationManager.getInstance().openAuthenticationDialog();
		System.out.println("hello");
	}

	public boolean isAuthenticated() {
		if(authenticationToken == null) {
			return false;
		}
		return true;
	}

	public String getAccessToken() {
		if(!this.isAuthenticated()) {
			this.authenticateUser();
		}

		final boolean accessTokenIsNull = accessToken == null;
		final boolean accessTokenHasExpired = accessTokenExpiry.getTime() < new Date().getTime();
		if(accessTokenIsNull) {
			;
		}
		else if(accessTokenHasExpired) {
			//refresh token
		}
		return accessToken;
	}

	private void openAuthenticationDialog() {
		NativeInterface.open();

		AuthenticationDialog authenticationDialog = new AuthenticationDialog();

		NativeInterface.runEventPump();
	}

	
	
	protected void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
		try {
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("code", authenticationToken);
			parameters.put("client_id", GOOGLE_API_CLIENT_ID);
			parameters.put("client_secret", GOOGLE_API_CLIENT_SECRET);
			parameters.put("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
			parameters.put("grant_type", "authorization_code");
			System.out.println(HttpsConnectionHelper.sendByPostReturningReplyAsString("https://accounts.google.com/o/oauth2/token", parameters));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

