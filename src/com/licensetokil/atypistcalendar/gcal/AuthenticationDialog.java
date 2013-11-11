//@author A0097142E
package com.licensetokil.atypistcalendar.gcal;

import java.awt.Dialog;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.NSOption;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;

class AuthenticationDialog extends JDialog {
	private static final boolean AUTHENTICATION_DIALOG_WEB_BROWSER_BARS_VISIBILITY = false;
	private static final String AUTHENTICATION_DIALOG_TITLE = "Google Calendar Authentication";
	private static final int AUTHENTICATION_DIALOG_WIDTH = 800;
	private static final int AUTHENTICATION_DIALOG_HEIGHT = 600;

	private static final String AUTHENICATION_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=" +
			AuthenticationManager.GOOGLE_API_CLIENT_ID +
			"&redirect_uri=urn:ietf:wg:oauth:2.0:oob&scope=https://www.googleapis.com/auth/calendar";

	private static final String REPLY_TARGET_URL = "https://accounts.google.com/o/oauth2/approval";
	private static final String REPLY_SUCCESSFUL_AUTHENTICATION_LABEL = "Success code";
	private static final String REPLY_DELIMITER = "=";
	private static final int REPLY_LABEL_INDEX = 0;
	private static final int REPLY_VALUE_INDEX = 1;

	private static final long serialVersionUID = 1L;

	private JWebBrowser webBrowser;

	protected AuthenticationDialog() {
		webBrowser = new JWebBrowser(new NSOption(null));
		webBrowser.setBarsVisible(AUTHENTICATION_DIALOG_WEB_BROWSER_BARS_VISIBILITY);

		webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
			public void locationChanged(WebBrowserNavigationEvent event) {
				webBrowserLocationChanged(event);
			}
		});

		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setTitle(AUTHENTICATION_DIALOG_TITLE);
		this.setSize(AUTHENTICATION_DIALOG_WIDTH, AUTHENTICATION_DIALOG_HEIGHT);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.add(webBrowser);

		this.navigateToURL(AUTHENICATION_URL);

		this.setVisible(true);
	}

	private void navigateToURL(final String target) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				webBrowser.navigate(target);
			}
		});
	}

	private void webBrowserLocationChanged(WebBrowserNavigationEvent event) {
		boolean webBrowserIsAtTargetUrl = webBrowser.getResourceLocation().contains(REPLY_TARGET_URL);
		if (webBrowserIsAtTargetUrl) {
			String[] googleReplyDelimited = webBrowser.getPageTitle().split(REPLY_DELIMITER);
			boolean replyLabelledAsSuccessful =
					googleReplyDelimited[REPLY_LABEL_INDEX].equals(REPLY_SUCCESSFUL_AUTHENTICATION_LABEL);
			if (replyLabelledAsSuccessful) {
				AuthenticationManager.getInstance().authenticateUserSuccess(googleReplyDelimited[REPLY_VALUE_INDEX]);
			} else {
				AuthenticationManager.getInstance().authenticateUserFailed();
			}
			NativeInterface.close();
			this.dispose();
		}
	}
}