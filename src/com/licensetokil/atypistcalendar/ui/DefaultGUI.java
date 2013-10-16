package com.licensetokil.atypistcalendar.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.licensetokil.atypistcalendar.ATypistCalendar;

public class DefaultGUI extends JFrame {
	private JScrollPane jScrollPane1;
	private JTextArea jTextArea1;
	private JTextField jTextField1;

	public DefaultGUI() {
		initComponents();
	}

	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jTextField1 = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("A Typist's Calendar");

		jTextArea1.setEditable(false);
		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jTextArea1.setLineWrap(true);
		jTextArea1.setWrapStyleWord(true);
		
		jScrollPane1.setViewportView(jTextArea1);

		jTextField1.setText("");
		jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				jTextField1KeyReleased(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
								.addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING))
								.addContainerGap())
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(6, 6, 6))
				);

		pack();
	}

	private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {                                        
		if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
			ATypistCalendar.userInput(jTextField1.getText());
			jTextField1.setText("");
		}
	}
	
	public void outputWithNewline(String text) {
		jTextArea1.append(text + "\n");
		jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
		jTextField1.requestFocus();
	}
}