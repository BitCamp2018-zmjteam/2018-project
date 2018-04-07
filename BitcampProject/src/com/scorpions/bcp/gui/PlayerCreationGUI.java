package com.scorpions.bcp.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayerCreationGUI extends JFrame {
	private static final long serialVersionUID = 2347827406606169493L;
	//String ip = JOptionPane.showInputDialog(null, "Enter the DMs IP address:", "Connect To DM", JOptionPane.QUESTION_MESSAGE);
	private JPanel panel;
	private int width, height;
	private JTextField strField, dexField, conField, intField, wisField, chaField;
	private JLabel str, dex, con, intel, wis, cha;
	public PlayerCreationGUI() {
		super("Create a character");
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		width = 400;
		height = 800;
		
		panel = new JPanel();
		strField = new JTextField();
		dexField = new JTextField();
		conField = new JTextField();
		intField = new JTextField();
		wisField = new JTextField();
		chaField = new JTextField();
		str = new JLabel("Strength:");
		dex = new JLabel("Dexterity:");
		con = new JLabel("Constitution:");
		intel = new JLabel("Intelligence:");
		wis = new JLabel("Wisdom:");
		cha = new JLabel("Charisma:");
		
		panel.add(strField);
		panel.add(dexField);
		panel.add(conField);
		panel.add(intField);
		panel.add(wisField);
		panel.add(chaField);
		panel.add(str);
		panel.add(dex);
		panel.add(con);
		panel.add(intel);
		panel.add(wis);
		panel.add(cha);
		
		str.setLocation(70, 25);
		str.setSize(100, 100);
		strField.setLocation(190, 55);
		strField.setSize(100, 30);
		
		
		panel.setLayout(null);
		
		this.add(panel);
		this.setSize(width, height);
		this.setLocation((1920 - width) / 2, (1080 - height) / 2);
		this.setResizable(false);
		this.setVisible(true);
	}
	
}
