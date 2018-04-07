package com.scorpions.bcp.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartupGUI extends JFrame {
	private static final long serialVersionUID = 367899772406520597L;
	private JPanel panel;
	private JTextField inputBox;
	private JButton playerB, DMB;
	public StartupGUI() {
		super("Bitcamp 2018 Project Startup");
		
		inputBox = new JTextField();
		
		playerB = new JButton("Player");
		DMB = new JButton("DM");
		
		panel = new JPanel();
		panel.add(inputBox);
		panel.add(playerB);
		panel.add(DMB);
		panel.setLayout(null);
		
		playerB.setLocation(100, 100);
		DMB.setLocation(100, 100);
		
		this.add(panel);
		this.setSize(400, 225);
		this.setResizable(false);
		this.setVisible(true);
	}	
}
