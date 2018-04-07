package com.scorpions.bcp.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.scorpions.bcp.net.GameServer;

public class DMGUI extends JFrame {
	private JTabbedPane panel;
	private JPanel main, npcs;
	private int width, height;
	private GameServer server;

	public DMGUI(GameServer server) {
		super("DM Screen");
		
		this.server = server;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		width = 1200;
		height = 675;
		
		panel = new JTabbedPane();
		main = new JPanel();
		npcs = new JPanel();
		
		panel.addTab("Main", main);
		panel.addTab("NPCs", npcs);
		
		this.add(panel);
		
		this.setSize(width, height);
		this.setResizable(false);
		this.setLocation((1920 - width)/2, (1080 - height)/2);
		this.setVisible(true);
	}
	
	
}
