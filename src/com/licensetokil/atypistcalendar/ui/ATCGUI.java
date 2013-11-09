/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.licensetokil.atypistcalendar.ui;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.StringReader;

import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import com.licensetokil.atypistcalendar.ATypistCalendar;

/**
 *
 * @author Fan
 */
public class ATCGUI extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	private final static String WELCOME_MESSAGE = "Welcome to a Typist Calendar!\n";

	public ATCGUI() {
		try {
			// Set to cross-platform Nimbus Look and Feel
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // JDK
																					// 1.7
		} catch (Exception e) {
			e.printStackTrace();
		}
		// getContentPane().setBackground(Color.RED);
		initComponents();
		this.setContentPane(jPanel1);
		addWindowListener(this);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jTextField1 = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jEditorPane1 = new javax.swing.JEditorPane("text/html", null);
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea2 = new javax.swing.JTextArea();
		htmlEditorKit = new HTMLEditorKit();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("A Typist's Calendar");
		setIconImage(new ImageIcon(getClass().getResource("/icon128.png")).getImage());

		jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jTextField1KeyReleased(evt);
			}
		});

		JScrollBar scrollBar = jScrollPane1.getVerticalScrollBar();
		InputMap inputMap = scrollBar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "positiveUnitIncrement");
		inputMap.put(KeyStroke.getKeyStroke("UP"), "negativeUnitIncrement");

		jLabel1.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
		jLabel1.setText("Type your command here:");

		jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "A Typist's Calendar",
				javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Consolas", 0, 18))); // NOI18N
		jScrollPane1.setMaximumSize(jPanel1.getMaximumSize());
		jScrollPane1.setMinimumSize(jPanel1.getMinimumSize());
		jScrollPane1.setViewportView(jEditorPane1);

		jScrollPane2.setBorder(null);
		jScrollPane2
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane2.setAutoscrolls(true);
		jEditorPane1.setFont(new Font("Consolas", Font.PLAIN, 14));
		jEditorPane1.setEditorKit(htmlEditorKit);
		jEditorPane1.setEditable(false);

		jTextArea2.setEditable(false);
		jTextArea2.setBackground(new java.awt.Color(222, 222, 222));
		jTextArea2.setColumns(20);
		jTextArea2.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
		jTextArea2.setRows(5);
		jTextArea2.setBorder(null);
		jScrollPane2.setViewportView(jTextArea2);

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jScrollPane1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																601,
																Short.MAX_VALUE)
														.addComponent(
																jTextField1)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel1)
																		.addGap(0,
																				0,
																				Short.MAX_VALUE))
														.addComponent(
																jScrollPane2))
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jScrollPane1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												402, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jTextField1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												33,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												29,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(5, 5, 5)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.PREFERRED_SIZE));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
			if (jTextField1.getText().equals("")) {
				return;
			}
			ATypistCalendar.getInstance().userInput(jTextField1.getText());
			jTextField1.setText("");
		}
	}

	public void outputWithNewline(String text) {
		StringReader reader;

		if (text.contains(WELCOME_MESSAGE)) {
			text = text.replaceAll("\\r|\\n", "<br>");
			jEditorPane1.setText("<b>" + text + "</b><br><hr>");
		}

		else {
			if (text.contains("Schedules: \n")) {
				text = text.replaceAll("\\r|\\n", "<br>");
				reader = new StringReader("<div align = 'justify'>" + text + "</div><hr><br>");

			}
			else {
				text = text.replaceAll("\\r|\\n", "<br>");
				reader = new StringReader(text + "<hr><br>");
			}
			try {
				htmlEditorKit.read(reader, jEditorPane1.getDocument(), jEditorPane1
						.getDocument().getLength());
			} catch (Exception e) {

			}
		}

		// jEditorPane1.setText(jEditorPane1.getText() + "\n" + text);
		jEditorPane1.setCaretPosition(jEditorPane1.getDocument().getLength());
		jTextField1.requestFocus();
	}

	public void dispatchWindowClosingEvent() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	/*public String formatDisplaySchedule(String text){
		String[] splitString = text.split("<br>");
		ArrayList<String> listOfSchedules = new ArrayList<String>();
		String output = "Schedules: <br>";

		for(int i=1; i<splitString.length; i++){
			if (splitString[i]!="Deadlines: "){
				listOfSchedules.add(splitString[i]);
			}
			else{
				break;
			}
		}
		for(String s: listOfSchedules){
			output = "<"
		}

	}

	public String formatDisplayDeadline(String text){

	}

	public String formatDisplayTodo(String text){

	}*/

	public void outputUserInput(String input) {
		jTextArea2.setText(input);
	}

	@Override
	public void windowClosing(WindowEvent e) {
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

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea jTextArea2;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JEditorPane jEditorPane1;
	private HTMLEditorKit htmlEditorKit;
	private Document doc;
	// End of variables declaration//GEN-END:variables
}
