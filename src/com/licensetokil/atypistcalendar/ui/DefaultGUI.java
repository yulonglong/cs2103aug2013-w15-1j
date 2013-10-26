package com.licensetokil.atypistcalendar.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.licensetokil.atypistcalendar.ATypistCalendar;
import com.licensetokil.atypistcalendar.tasksmanager.TasksManager;

public class DefaultGUI extends JFrame implements WindowListener {

    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private JTextField jTextField1;

    private static Logger p = Logger.getLogger("atc");
    private static Logger logger = Logger.getLogger("atc.DefaultUI");
    private static TasksManager TM = new TasksManager();

    public DefaultGUI() {
        System.out.println(logger.getParent().getName());
        logger.setLevel(Level.OFF);
        logger.info("Creating new instance of DefaultGUI");
        initComponents();
        logger.info("New instance of DefaultGUI created");
        addWindowListener(this);
    }

    private void initComponents() {

        logger.info("Initialising components");

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
        logger.info("jTextField1 key released");
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            logger.info("jTextField1 key released = [Enter]");

            if (jTextField1.getText().equals("")) {
                logger.warning("jTextField1 is empty, returning.");
                return;
            }

            logger.warning("jTextField1 calling userInput of Logic");
            ATypistCalendar.userInput(jTextField1.getText());
            jTextField1.setText("");
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        TasksManager.exit();
    }

    public void outputWithNewline(String text) {
        logger.info("Output to user: " + text);

        jTextArea1.append(text + "\n");
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        jTextField1.requestFocus();
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
}
