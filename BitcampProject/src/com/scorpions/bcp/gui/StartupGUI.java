package com.scorpions.bcp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.scorpions.bcp.creature.TestDriver;

public class StartupGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 367899772406520597L;
	private JPanel panel;
	private JButton playerB, DMB;
	private JLabel prompt;
	private int width, height;
	public StartupGUI() {
		super("Bitcamp 2018 Project Startup");
		
		width = 400;
		height = 225;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playerB = new JButton("Player");
		DMB = new JButton("DM");
		
		prompt = new JLabel("Are you a player or a DM?");
		
		panel = new JPanel();
		panel.add(playerB);
		panel.add(DMB);
		panel.add(prompt);
		panel.setLayout(null);
		
		playerB.setLocation(90, 145);
		playerB.setSize(100, 40);
		playerB.setActionCommand("Player");
		playerB.addActionListener(this);
		DMB.setLocation(210, 145);
		DMB.setSize(100, 40);
		DMB.setActionCommand("DM");
		DMB.addActionListener(this);
		
		prompt.setLocation(50, 25);
		prompt.setSize(400, 100);
		
		this.add(panel);
		this.setSize(width, height);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocation((1920 - width)/2, (1080 - height)/2);
	}	
	
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("Player")) {
			this.setVisible(false);
			TestDriver.launchPlayerCreationGUI();
		}
		else if(event.getActionCommand().equals("DM")) {
			this.setVisible(false);
			TestDriver.launchDMGUI();
		}
	}
}
