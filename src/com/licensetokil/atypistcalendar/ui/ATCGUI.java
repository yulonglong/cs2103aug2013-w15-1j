/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.licensetokil.atypistcalendar.ui;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;

import com.licensetokil.atypistcalendar.ATypistCalendar;

public class ATCGUI extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	private final static String MESSAGE_WELCOME = "Welcome to a Typist's Calendar!\n";
	private final static String MESSAGE_WELCOME_FORMAT = "<b>%s</b><br><hr>";
	private final static String MESSAGE_TITLE = "A Typist's Calendar";
	private final static String MESSAGE_COMMAND = "Type your command here:";

	private final static String KEY_DOWN = "DOWN";
	private final static String KEY_UP = "UP";
	private final static String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
	private final static String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
	private final static String EMPTY_STRING = "";

	private final static String NEWLINE_REGEX = "\\r|\\n";
	private final static String NEWLINE_HTML = "<br>";

	private final static String OUTPUT_FORMAT = "%s<hr><br>";

	private Image defaultIcon;
	private Image syncingIcon;

	private static Logger logger = Logger.getLogger("ATCGUI");

	//public constructor
	public ATCGUI() {
		try {
			logger.log(Level.INFO, "Set to cross-platform Nimbus Look and Feel");
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error detected: " + e.getMessage());
		}

		getResources();

		initComponents();
		this.setContentPane(jPanel);
		addWindowListener(this);
	}

	private void getResources() {
		logger.log(Level.INFO, "Getting resources");

		defaultIcon = (new ImageIcon(getClass().getResource("/icon128.png"))).getImage();
		syncingIcon = (new ImageIcon(getClass().getResource("/icon128sync.png"))).getImage();
	}

	//initialising function
	private void initComponents() {

		logger.log(Level.INFO, "Initialising various components of GUI");
		jPanel = new JPanel();
		jTextField = new JTextField();
		jLabel = new JLabel();
		editorScrollPane = new JScrollPane();
		jEditorPane = new JEditorPane("text/html", null);
		areaScrollPane = new JScrollPane();
		jTextArea = new JTextArea();
		htmlEditorKit = new HTMLEditorKit();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle(MESSAGE_TITLE);
		setIconImage(defaultIcon);

		jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jTextField1KeyReleased(evt);
			}
		});

		setKeyScrolling();

		logger.log(Level.INFO, "Configuring jLabel");
		jLabel.setFont(new java.awt.Font("Consolas", 0, 14));
		jLabel.setText(MESSAGE_COMMAND);

		logger.log(Level.INFO, "Configuring editorScrollPane");
		editorScrollPane.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, MESSAGE_TITLE,
						javax.swing.border.TitledBorder.CENTER,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Consolas", 0, 18)));
		editorScrollPane.setMaximumSize(jPanel.getMaximumSize());
		editorScrollPane.setMinimumSize(jPanel.getMinimumSize());
		editorScrollPane.setViewportView(jEditorPane);

		logger.log(Level.INFO, "Configuring areaScrollPane");
		areaScrollPane.setBorder(null);
		areaScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		areaScrollPane.setAutoscrolls(true);

		logger.log(Level.INFO, "Configuring editorPane");
		jEditorPane.setFont(new Font("Consolas", Font.PLAIN, 14));
		jEditorPane.setEditorKit(htmlEditorKit);
		jEditorPane.setEditable(false);

		logger.log(Level.INFO, "Configuring textArea");
		jTextArea.setEditable(false);
		jTextArea.setBackground(new java.awt.Color(222, 222, 222));
		jTextArea.setColumns(20);
		jTextArea.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
		jTextArea.setRows(5);
		jTextArea.setBorder(null);
		areaScrollPane.setViewportView(jTextArea);

		logger.log(Level.INFO, "Configuring various layout and outlook");
		GroupLayout jPanel1Layout = new GroupLayout(jPanel);
		jPanel.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addComponent(
																editorScrollPane,
																GroupLayout.DEFAULT_SIZE,
																700,
																Short.MAX_VALUE)
														.addComponent(
																jTextField)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel)
																		.addGap(0,
																				0,
																				Short.MAX_VALUE))
														.addComponent(
																areaScrollPane))
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(editorScrollPane,
												GroupLayout.PREFERRED_SIZE,
												402, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jTextField,
												GroupLayout.PREFERRED_SIZE, 33,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(areaScrollPane,
												GroupLayout.PREFERRED_SIZE, 29,
												GroupLayout.PREFERRED_SIZE)
										.addGap(5, 5, 5)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jPanel,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jPanel,
				GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
				GroupLayout.PREFERRED_SIZE));

		pack();
	}

	// Function for attaching up and down keys to up and down scrolling of
	// editor pane
	private void setKeyScrolling() {
		logger.log(Level.INFO, "Setting key scrolling with editorPane");
		JScrollBar scrollBar = editorScrollPane.getVerticalScrollBar();
		InputMap inputMap = scrollBar
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KEY_DOWN), POSITIVE_UNIT_INCREMENT);
		inputMap.put(KeyStroke.getKeyStroke(KEY_UP), NEGATIVE_UNIT_INCREMENT);
	}

	// function to respond to an enter key in jtextfield
	private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
			if (jTextField.getText().equals(EMPTY_STRING)) {
				logger.log(Level.INFO, "No input detected. Returning");
				return;
			}
			logger.log(Level.INFO,
					"Getting user input. Setting text field to empty");
			ATypistCalendar.getInstance().userInput(jTextField.getText());
			jTextField.setText(EMPTY_STRING);
		}
	}

	// function that displays responses from user input
	public void outputWithNewline(String text) {
		logger.log(Level.INFO, "In outputWithNewline function");
		StringReader reader;

		if (text.contains(MESSAGE_WELCOME)) {
			logger.log(Level.INFO, "Displaying Welcome Message");
			text = text.replaceAll(NEWLINE_REGEX, NEWLINE_HTML);
			jEditorPane.setText(String.format(MESSAGE_WELCOME_FORMAT, text));
		}

		else {
			logger.log(Level.INFO, "Displaying all other output");
			text = text.replaceAll(NEWLINE_REGEX, NEWLINE_HTML);
			reader = new StringReader(String.format(OUTPUT_FORMAT, text));
			try {
				htmlEditorKit.read(reader, jEditorPane.getDocument(),
						jEditorPane.getDocument().getLength());
			} catch (Exception e) {
				logger.log(Level.WARNING, "Error detected: " + e.getMessage());
			}
		}

		jEditorPane.setCaretPosition(jEditorPane.getDocument().getLength());
		jTextField.requestFocus();
	}

	//for window closing
	public void dispatchWindowClosingEvent() {
		logger.log(Level.INFO, "In dispatchWindowClosingEventFunction");
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	//function to output the previous user input for convenience purposes
	public void outputUserInput(String input) {
		logger.log(Level.INFO, "In outputUserInput function");
		jTextArea.setText(input);
	}

	@Override
	//function to save data into files when window is closed
	public void windowClosing(WindowEvent e) {
		logger.log(Level.INFO, "In windowClosing function");
		ATypistCalendar.getInstance().cleanUp();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	public void changeWindowIcon(boolean useSyncingIcon) {
		logger.log(Level.INFO, "Changing window icon");

		if(useSyncingIcon) {
			logger.log(Level.INFO, "Using syncing icon");
			setIconImage(syncingIcon);
		}
		else {
			logger.log(Level.INFO, "Using default icon");
			setIconImage(defaultIcon);
		}
	}

	// Variables declaration
	private JLabel jLabel;
	private JPanel jPanel;
	private JScrollPane editorScrollPane;
	private JScrollPane areaScrollPane;
	private JTextArea jTextArea;
	private JTextField jTextField;
	private JEditorPane jEditorPane;
	private HTMLEditorKit htmlEditorKit;

}
