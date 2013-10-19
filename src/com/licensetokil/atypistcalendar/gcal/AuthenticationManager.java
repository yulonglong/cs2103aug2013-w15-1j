package com.licensetokil.atypistcalendar.gcal;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.NSOption;

public class AuthenticationManager {
	private static AuthenticationManager instance = null;
	private String authenticationToken;
	private String access_token;
	private String refresh_token;
	
	private AuthenticationManager() {  
	}
	
	public static AuthenticationManager getInstance() {
		if(instance == null) {
			instance = new AuthenticationManager();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		AuthenticationManager.getInstance().openAuthenticationDialog();
	}
	
	private void openAuthenticationDialog() {
		NativeInterface.open();

		AuthenticationDialog authenticationDialog = new AuthenticationDialog();
		
		NativeInterface.runEventPump();
	}
}