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

javax



.swing.JPanel; /** * GradientPanel is a class with a gradient background, put your non-opaque objects over it and enjoy. * @author Mohammad Abou-Basha * */ public class GradientPanel extends JPanel {

    private Color startColor;
    private Color endColor;

    public GradientPanel() {
        this(Color.GRAY, Color.WHITE);
    }

    public GradientPanel(Color startColor, Color endColor) {
        super();
        this.startColor = startColor;
        this.endColor = endColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int panelHeight = getHeight();
        int panelWidth = getWidth();
        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, panelWidth, panelHeight, endColor);
        if (g instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setPaint(gradientPaint);
            graphics2D.fillRect(0, 0, panelWidth, panelHeight);
        }
    }
}

public class GradientPanelDemo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private GradientPanel gradientPanel = null;

    /**
     * * This is the default constructor
     */
    public GradientPanelDemo() {
        super();
        initialize();
    }

    /**
     * * This method initializes this * * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    /**
     * * This method initializes jContentPane * * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getGradientPanel(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * * This method initializes gradientPanel * * @return
     * gradient.GradientPanel
     */
    private GradientPanel getGradientPanel() {
        if (gradientPanel == null) {
            gradientPanel = new GradientPanel();
        }
        return gradientPanel;
    }
}
