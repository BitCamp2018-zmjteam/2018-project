package com.scorpions.bcp.creature;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

import com.scorpions.bcp.gui.StartupGUI;

public class TestDriver {
	public static void main(String args[]) {
		setGUI();
		StartupGUI start = new StartupGUI();
	}
	
	public static void setGUI() {
		Font myFont = new Font("Blackadder ITC", Font.PLAIN, 20);
		Font myFont2 = new Font("Blackadder ITC", Font.PLAIN, 30);
		Color myBrown = new Color(147, 88, 28);
		Color myWhite = new Color(242, 240, 215);

		UIManager.put("OptionPane.background", myBrown);
		UIManager.put("OptionPane.messageForeground", myWhite);
		UIManager.put("OptionPane.messageDialogTitle", myBrown);
		UIManager.put("Panel.background", myBrown);
		UIManager.put("OptionPane.messageFont", myFont);
		UIManager.put("Button.background", myBrown);
		UIManager.put("Button.foreground", myWhite);
		UIManager.put("Button.select", myWhite);
		UIManager.put("Button.font", myFont);
		UIManager.put("Menu.selectionBackground", myBrown);
		UIManager.put("Menu.selectionForeground", myBrown);
		UIManager.put("MenuItem.selectionBackground", myWhite);
		UIManager.put("MenuItem.selectionForeground", myBrown);
		UIManager.put("MenuItem.font", myFont);
		UIManager.put("Menu.font", myFont);
		UIManager.put("TextField.background", myWhite);
		UIManager.put("TextField.foreground", Color.BLACK);
		UIManager.put("TextField.font", myFont);
		
		UIManager.put("Label.background", myBrown);
		UIManager.put("Label.foreground", myWhite);
		UIManager.put("Label.font", myFont2);
	}
}
