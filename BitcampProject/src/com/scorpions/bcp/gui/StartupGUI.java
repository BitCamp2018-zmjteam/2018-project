package com.scorpions.bcp.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartupGUI extends JFrame {
	private static final long serialVersionUID = 367899772406520597L;
	private JPanel panel;
	private JTextField inputBox;
	private JButton playerB, DMB;
	private JLabel prompt;
	public StartupGUI() {
		super("Bitcamp 2018 Project Startup");
		
		inputBox = new JTextField();
		
		playerB = new JButton("Player");
		DMB = new JButton("DM");
		
		prompt = new JLabel("Are you a player or a DM?");
		
		panel = new JPanel();
		panel.add(inputBox);
		panel.add(playerB);
		panel.add(DMB);
		panel.add(prompt);
		panel.setLayout(null);
		
		playerB.setLocation(90, 145);
		playerB.setSize(100, 40);
		DMB.setLocation(210, 145);
		DMB.setSize(100, 40);
		
		prompt.setLocation(50, 25);
		prompt.setSize(400, 100);
		
		this.add(panel);
		this.setSize(400, 225);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocation((1920 - this.getWidth())/2, (1080 - this.getHeight())/2);
	}	
}
