package com.licensetokil.atypistcalendar.gcal;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.NSOption;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;

class AuthenticationDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JWebBrowser webBrowser;
	
	public AuthenticationDialog() {
		webBrowser = new JWebBrowser(new NSOption(null));
		webBrowser.setBarsVisible(false);
		
		webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
			public void locationChanged(WebBrowserNavigationEvent event) {
				webBrowserLocationChanged(event);
			}
		});

		this.setTitle("Google Calendar Authentication");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.getContentPane().add(webBrowser);
		
		this.navigateToURL("https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=933956973767-2e8udf2em20mgl6djjesu5ggpot4um19.apps.googleusercontent.com&redirect_uri=urn:ietf:wg:oauth:2.0:oob&scope=https://www.googleapis.com/auth/calendar");
	}

	private void navigateToURL(final String target) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				webBrowser.navigate(target);
			}
		});
	}
	
	private void webBrowserLocationChanged(WebBrowserNavigationEvent event) {
		if(webBrowser.getResourceLocation().indexOf("https://accounts.google.com/o/oauth2/approval") != -1) {
			String[] googleReplyDelimited = webBrowser.getPageTitle().split("=");
			this.dispose();
			NativeInterface.close();
			if(googleReplyDelimited[0].equals("Success code")) {
				//success flow
				String googleAuthorizationCode = googleReplyDelimited[1];
				System.out.println(googleAuthorizationCode);
			}
			else {
				//denied flow
				System.out.println("Denied");
			}
		}
	}
}