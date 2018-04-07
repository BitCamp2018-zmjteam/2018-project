package com.scorpions.bcp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.scorpions.bcp.creature.Player;

public class PlayerCreationGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 2347827406606169493L;
	private JPanel panel;
	private int width, height;
	private JTextField strField, dexField, conField, intField, wisField, chaField, nameField;
	private JLabel str, dex, con, intel, wis, cha, name;
	private JComboBox<String> raceChoice, roleChoice;
	private String[] races = {"Human", "High-Elf", "Dwarf", "Half-Elf"};
	private String[] roles = {"Paladin", "Rogue", "Wizard", "Fighter"};
	private JButton finish;
	
	public PlayerCreationGUI() {
		super("Create a character");
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		width = 400;
		height = 600;
		
		panel = new JPanel();
		strField = new JTextField();
		dexField = new JTextField();
		conField = new JTextField();
		intField = new JTextField();
		wisField = new JTextField();
		chaField = new JTextField();
		nameField = new JTextField();
		str = new JLabel("Strength:");
		dex = new JLabel("Dexterity:");
		con = new JLabel("Constitution:");
		intel = new JLabel("Intelligence:");
		wis = new JLabel("Wisdom:");
		cha = new JLabel("Charisma:");
		name = new JLabel("Name:");
		raceChoice = new JComboBox<String>(races);
		roleChoice = new JComboBox<String>(roles);
		finish = new JButton("OK");
		
		finish.setActionCommand("Done");
		finish.addActionListener(this);
		
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
		panel.add(raceChoice);
		panel.add(roleChoice);
		panel.add(nameField);
		panel.add(name);
		panel.add(finish);
		
		str.setLocation(50, 5);
		str.setSize(150, 100);
		strField.setLocation(190, 35);
		strField.setSize(100, 30);
		dex.setLocation(50, 55);
		dex.setSize(150, 100);
		dexField.setLocation(190, 85);
		dexField.setSize(100, 30);
		con.setLocation(50, 105);
		con.setSize(150, 100);
		conField.setLocation(190, 135);
		conField.setSize(100, 30);
		intel.setLocation(50, 155);
		intel.setSize(150, 100);
		intField.setLocation(190, 185);
		intField.setSize(100, 30);
		wis.setLocation(50, 205);
		wis.setSize(150, 100);
		wisField.setLocation(190, 235);
		wisField.setSize(100, 30);
		cha.setLocation(50, 255);
		cha.setSize(150, 100);
		chaField.setLocation(190, 285);
		chaField.setSize(100, 30);
		raceChoice.setLocation(50, 335);
		raceChoice.setSize(200, 30);
		roleChoice.setLocation(50, 385);
		roleChoice.setSize(200, 30);
		nameField.setLocation(130, 445);
		nameField.setSize(200, 30);
		name.setLocation(50, 415);
		name.setSize(150, 100);
		finish.setLocation(150, 500);
		finish.setSize(100, 40);
		panel.setLayout(null);
		
		this.add(panel);
		this.setSize(width, height);
		this.setLocation((1920 - width) / 2, (1080 - height) / 2);
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int strInt = 0, dexInt = 0, conInt = 0, intInt = 0, wisInt = 0, chaInt = 0;
		if(event.getActionCommand().equals("Player")) {
			try {
				strInt = Integer.parseInt(strField.getText());
				dexInt = Integer.parseInt(dexField.getText());
				conInt = Integer.parseInt(conField.getText());
				intInt = Integer.parseInt(intField.getText());
				wisInt = Integer.parseInt(wisField.getText());
				chaInt = Integer.parseInt(chaField.getText());
			}
			catch(NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid stats, please enter valid numbers from 1-20.", "Error",
						JOptionPane.INFORMATION_MESSAGE);
			}
			if(strInt > 20 || strInt < 0 || dexInt > 20 || dexInt < 0 || conInt > 20 || conInt < 0 || intInt > 20 || intInt < 0 || wisInt > 20 || wisInt < 0 || chaInt < 0 || chaInt > 20) {
				JOptionPane.showMessageDialog(null, "Invalid stats, please enter valid numbers from 1-20.", "Error",
						JOptionPane.INFORMATION_MESSAGE);
			}
			if(nameField.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "You must enter a name.", "Error",
						JOptionPane.INFORMATION_MESSAGE);
			}
			
			Player player = new Player(strInt, dexInt, conInt, intInt, wisInt, chaInt, nameField.getText().trim(), (String) raceChoice.getSelectedItem(), (String) roleChoice.getSelectedItem());
			String ip = JOptionPane.showInputDialog(null, "Enter the DMs IP address:", "Connect To DM", JOptionPane.QUESTION_MESSAGE);

			
		}
		
	}
	
}
