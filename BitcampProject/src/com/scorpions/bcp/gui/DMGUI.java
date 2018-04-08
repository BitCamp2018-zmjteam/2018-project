package com.scorpions.bcp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.scorpions.bcp.net.GameServer;

public class DMGUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = -6156179537660121688L;
	private JTabbedPane panel;
	private JPanel main, npcs, npcData, world, players, items, itemsData, playersData;
	private JLabel ipAddr;
	private int width, height;
	private GameServer server;
	private String address;
	private JButton start, loadWorld, saveWorld;
	private JScrollPane npcPane, itemsPane, playersPane;
	private JList<String> npcList, itemsList, playersList;
	private DefaultListModel<String> npcModel, itemsModel, playersModel;
	public DMGUI(GameServer server) {
		super("DM Screen");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			address = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();
			address = "Unknown";
		}
		this.server = server;

		width = 1200;
		height = 675;
		
		panel = new JTabbedPane();
		main = new JPanel();
		npcs = new JPanel();
		world = new JPanel();
		players = new JPanel();
		items = new JPanel();
		npcData = new JPanel();
		itemsData = new JPanel();
		playersData = new JPanel();
		ipAddr = new JLabel("IP: " + address);
		start = new JButton("Start server");
		loadWorld = new JButton("Load world");
		saveWorld = new JButton("Save world");
		npcPane = new JScrollPane();
		npcModel = new DefaultListModel<String>();
		npcList = new JList<String>(npcModel);
		itemsPane = new JScrollPane();
		itemsModel = new DefaultListModel<String>();
		itemsList = new JList<String>(itemsModel);
		playersPane = new JScrollPane();
		playersModel = new DefaultListModel<String>();
		playersList = new JList<String>(playersModel);

		
		start.setActionCommand("Start");
		start.addActionListener(this);
		loadWorld.setActionCommand("Load");
		loadWorld.addActionListener(this);
		saveWorld.setActionCommand("Save");
		saveWorld.addActionListener(this);
		
		main.setLayout(null);
		npcs.setLayout(null);
		world.setLayout(null);
		players.setLayout(null);
		items.setLayout(null);
		
		main.add(ipAddr);
		main.add(start);
		ipAddr.setLocation(10, 10);
		ipAddr.setSize(300, 30);
		start.setLocation(525, 500);
		start.setSize(150, 40);
		
		npcs.add(npcPane);
		npcs.add(npcData);
		npcPane.setViewportView(npcList);
		npcPane.setSize(0, 0);
		npcPane.setSize(200, this.height-80);
		npcPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		npcData.setLocation(200, 0);
		npcData.setSize(this.width-200, this.height-80);
		
		items.add(itemsPane);
		items.add(itemsData);
		itemsPane.setViewportView(itemsList);
		itemsPane.setSize(0, 0);
		itemsPane.setSize(200, this.height-80);
		itemsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		itemsData.setLocation(200, 0);
		itemsData.setSize(this.width-200, this.height-80);
		
		players.add(playersPane);
		players.add(playersData);
		playersPane.setViewportView(playersList);
		playersPane.setSize(0, 0);
		playersPane.setSize(200, this.height-80);
		playersPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		playersData.setLocation(200, 0);
		playersData.setSize(this.width-200, this.height-80);
		
		world.add(loadWorld);
		world.add(saveWorld);
		loadWorld.setLocation(415, 500);
		loadWorld.setSize(150, 40);
		saveWorld.setLocation(595, 500);
		saveWorld.setSize(150, 40);
		
		panel.addTab("Main", main);
		panel.addTab("NPCs", npcs);
		panel.addTab("World", world);
		panel.addTab("Players", players);
		panel.addTab("Items", items);
		this.add(panel);
		
		this.setSize(width, height);
		this.setResizable(false);
		this.setLocation((1920 - width)/2, (1080 - height)/2);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("Start")) {
			start.setVisible(false);
			loadWorld.setVisible(false);
			saveWorld.setVisible(false);
			server.start();
		}
		else if(event.getActionCommand().equals("Load")) {
			
		}
		else if(event.getActionCommand().equals("Save")) {
		}
		
	}

}
