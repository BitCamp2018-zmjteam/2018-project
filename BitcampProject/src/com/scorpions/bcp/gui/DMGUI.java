package com.scorpions.bcp.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.util.TreeMap;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.scorpions.bcp.creature.NPC;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.items.Item;
import com.scorpions.bcp.net.GameServer;
import com.scorpions.bcp.world.Tile;
import com.scorpions.bcp.world.World;

public class DMGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = -6156179537660121688L;
	private JTabbedPane panel;
	private JPanel main, npcs, npcData, world, players, items, itemsData, playersData;
	private JLabel ipAddr, iNameLabel, iBPLabel, iSPLabel, iDescLabel, playerTitle, playerStr, playerDex, playerCon,
			playerInt, playerWis, playerCha, playerMoney, npcStrL, npcDexL, npcConL, npcIntL, npcWisL, npcChaL,
			npcNameL;
	private int width, height;
	private GameServer server;
	private String address;
	private JButton start, loadWorld, addItem, newWorld, addNpc;
	private JScrollPane npcPane, itemsPane, playersPane;
	private JList<String> npcList, itemsList;
	private JList<Player> playersList;
	private DefaultListModel<String> npcModel, itemsModel;
	private DefaultListModel<Player> playersModel;
	private JTextField itemName, itemBP, itemSP, npcStr, npcDex, npcCon, npcInt, npcWis, npcCha, npcName;
	private JTextArea itemDesc;
	private TreeMap<String, Item> itemsMap;
	private TreeMap<String, Player> playersMap;
	private ListSelectionModel itemsLSM, playersLSM, npcLSM;
	private TreeMap<String, NPC> npcMap;
	private World worldEdit;
	private WorldEditGUI wEG;

	public DMGUI(GameServer server) {
		super("DM Screen");

		wEG = new WorldEditGUI(this);

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

		itemsMap = new TreeMap<String, Item>();
		playersMap = new TreeMap<String, Player>();
		npcMap = new TreeMap<String, NPC>();
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
		addItem = new JButton("Add Item");
		newWorld = new JButton("New World");
		addNpc = new JButton("Add NPC");
		npcPane = new JScrollPane();
		npcModel = new DefaultListModel<String>();
		npcList = new JList<String>(npcModel);
		itemsPane = new JScrollPane();
		itemsModel = new DefaultListModel<String>();
		itemsList = new JList<String>(itemsModel);
		playersPane = new JScrollPane();
		playersModel = new DefaultListModel<Player>();
		playersList = new JList<Player>(playersModel);
		itemName = new JTextField();
		itemBP = new JTextField();
		itemSP = new JTextField();
		npcStr = new JTextField();
		npcDex = new JTextField();
		npcCon = new JTextField();
		npcInt = new JTextField();
		npcWis = new JTextField();
		npcCha = new JTextField();
		npcName = new JTextField();
		itemDesc = new JTextArea();
		iNameLabel = new JLabel("Item Name:");
		iBPLabel = new JLabel("Buy Price:");
		iSPLabel = new JLabel("Sell Price:");
		iDescLabel = new JLabel("Item Description:");
		playerTitle = new JLabel("No player loaded");
		playerStr = new JLabel("Strength: 0");
		playerDex = new JLabel("Dexterity: 0");
		playerCon = new JLabel("Consitution: 0");
		playerInt = new JLabel("Intelligence: 0");
		playerWis = new JLabel("Wisdom: 0");
		playerCha = new JLabel("Charisma: 0");
		playerMoney = new JLabel("GP: 0");
		npcStrL = new JLabel("Strength:");
		npcDexL = new JLabel("Dexterity:");
		npcConL = new JLabel("Constitution:");
		npcIntL = new JLabel("Intelligence:");
		npcWisL = new JLabel("Wisdom:");
		npcChaL = new JLabel("Charisma:");
		npcNameL = new JLabel("Name:");
		new JLabel("Width: ");
		new JLabel("Height: ");
		new JTextField();
		new JTextField();

		itemsLSM = itemsList.getSelectionModel();
		itemsLSM.addListSelectionListener(new itemsListListener());
		playersLSM = playersList.getSelectionModel();
		playersLSM.addListSelectionListener(new playerListListener());
		npcLSM = npcList.getSelectionModel();
		npcLSM.addListSelectionListener(new npcListListener());

		start.setActionCommand("Start");
		start.addActionListener(this);
		loadWorld.setActionCommand("Load");
		loadWorld.addActionListener(this);
		addItem.setActionCommand("Add Item");
		addItem.addActionListener(this);
		newWorld.setActionCommand("New World");
		newWorld.addActionListener(this);
		addNpc.setActionCommand("Add NPC");
		addNpc.addActionListener(this);

		main.setLayout(null);
		npcs.setLayout(null);
		world.setLayout(null);
		players.setLayout(null);
		items.setLayout(null);
		itemsData.setLayout(null);
		playersData.setLayout(null);
		npcData.setLayout(null);

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
		npcPane.setSize(200, this.height - 60);
		npcPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		npcData.setLocation(200, 0);
		npcData.setSize(this.width - 200, this.height - 60);
		npcData.add(npcNameL);
		npcData.add(npcName);
		npcData.add(npcStrL);
		npcData.add(npcDexL);
		npcData.add(npcConL);
		npcData.add(npcIntL);
		npcData.add(npcWisL);
		npcData.add(npcChaL);
		npcData.add(npcStr);
		npcData.add(npcDex);
		npcData.add(npcCon);
		npcData.add(npcInt);
		npcData.add(npcWis);
		npcData.add(npcCha);
		npcData.add(addNpc);
		npcNameL.setLocation(10, 10);
		npcNameL.setSize(140, 40);
		npcName.setLocation(150, 10);
		npcName.setSize(140, 40);
		npcStrL.setLocation(10, 80);
		npcStrL.setSize(140, 40);
		npcStr.setLocation(150, 80);
		npcStr.setSize(140, 40);
		npcDexL.setLocation(10, 150);
		npcDexL.setSize(140, 40);
		npcDex.setLocation(150, 150);
		npcDex.setSize(140, 40);
		npcConL.setLocation(10, 220);
		npcConL.setSize(140, 40);
		npcCon.setLocation(150, 220);
		npcCon.setSize(140, 40);
		npcIntL.setLocation(10, 290);
		npcIntL.setSize(140, 40);
		npcInt.setLocation(150, 290);
		npcInt.setSize(140, 40);
		npcWisL.setLocation(10, 360);
		npcWisL.setSize(140, 40);
		npcWis.setLocation(150, 360);
		npcWis.setSize(140, 40);
		npcChaL.setLocation(10, 430);
		npcChaL.setSize(140, 40);
		npcCha.setLocation(150, 430);
		npcCha.setSize(140, 40);
		addNpc.setLocation(425, 500);
		addNpc.setSize(150, 40);

		items.add(itemsPane);
		items.add(itemsData);
		itemsData.add(addItem);
		itemsData.add(itemName);
		itemsData.add(itemBP);
		itemsData.add(itemSP);
		itemsData.add(itemDesc);
		itemsData.add(iNameLabel);
		itemsData.add(iBPLabel);
		itemsData.add(iSPLabel);
		itemsData.add(iDescLabel);
		itemsPane.setViewportView(itemsList);
		itemsPane.setSize(0, 0);
		itemsPane.setSize(200, this.height - 60);
		itemsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		itemsData.setLocation(200, 0);
		itemsData.setSize(this.width - 200, this.height - 60);
		addItem.setLocation(425, 500);
		addItem.setSize(150, 40);
		iNameLabel.setLocation(10, 10);
		iNameLabel.setSize(150, 40);
		itemName.setLocation(170, 10);
		itemName.setSize(200, 40);
		iBPLabel.setLocation(10, 70);
		iBPLabel.setSize(150, 40);
		itemBP.setLocation(170, 70);
		itemBP.setSize(200, 40);
		iSPLabel.setLocation(10, 130);
		iSPLabel.setSize(150, 40);
		itemSP.setLocation(170, 130);
		itemSP.setSize(200, 40);
		iDescLabel.setLocation(10, 190);
		iDescLabel.setSize(200, 40);
		itemDesc.setLocation(10, 240);
		itemDesc.setSize(400, 200);
		itemDesc.setBackground(itemName.getBackground());
		itemDesc.setForeground(Color.BLACK);
		itemDesc.setFont(itemName.getFont());
		itemDesc.setColumns(20);
		itemDesc.setLineWrap(true);
		itemDesc.setWrapStyleWord(true);

		players.add(playersPane);
		players.add(playersData);
		playersData.add(playerTitle);
		playersData.add(playerStr);
		playersData.add(playerDex);
		playersData.add(playerCon);
		playersData.add(playerInt);
		playersData.add(playerWis);
		playersData.add(playerCha);
		playersData.add(playerMoney);
		playersPane.setViewportView(playersList);
		playersPane.setSize(0, 0);
		playersPane.setSize(200, this.height - 60);
		playersPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		playersData.setLocation(200, 0);
		playersData.setSize(this.width - 200, this.height - 60);
		playerTitle.setLocation(10, 10);
		playerTitle.setSize(400, 40);
		playerStr.setLocation(10, 80);
		playerStr.setSize(200, 40);
		playerDex.setLocation(10, 150);
		playerDex.setSize(200, 40);
		playerCon.setLocation(10, 220);
		playerCon.setSize(200, 40);
		playerInt.setLocation(10, 290);
		playerInt.setSize(200, 40);
		playerWis.setLocation(10, 360);
		playerWis.setSize(200, 40);
		playerCha.setLocation(10, 430);
		playerCha.setSize(200, 40);
		playerMoney.setLocation(10, 500);
		playerMoney.setSize(200, 40);

		world.add(loadWorld);
		world.add(newWorld);
		loadWorld.setLocation(415, 500);
		loadWorld.setSize(150, 40);
		newWorld.setLocation(595, 500);
		newWorld.setSize(150, 40);

		panel.addTab("Main", main);
		panel.addTab("NPCs", npcs);
		panel.addTab("World", world);
		panel.addTab("Players", players);
		panel.addTab("Items", items);
		this.add(panel);

		this.setSize(width, height);
		this.setResizable(false);
		this.setLocation((1920 - width) / 2, (1080 - height) / 2);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("Start")) {
			start.setVisible(false);
			loadWorld.setVisible(false);
			newWorld.setVisible(false);
			server.start();
		} else if (event.getActionCommand().equals("Add NPC")) {
			addNPC();
		} else if (event.getActionCommand().equals("Load")) {
			File f;
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("World files", World.FILE_SUFFIX);
			fc.setFileFilter(filter);
			int check = fc.showOpenDialog(new JFrame());
			if (check == JFileChooser.APPROVE_OPTION) {
				f = fc.getSelectedFile();
				World w = World.fromFile(f);
				if(w != null) {
					worldEdit = w;
					wEG.load(worldEdit, npcMap);
				}
			}
		} else if (event.getActionCommand().equals("Add Item")) {
			addItem();
			itemsPane.getVerticalScrollBar().setValue(itemsPane.getVerticalScrollBar().getMaximum());
		} else if (event.getActionCommand().equals("New World")) {
			int worldWidth = 0;
			int worldHeight = 0;
			String name = "";
			while (worldWidth <= 0) {
				try {
					worldWidth = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the world width:",
							"Create world", JOptionPane.QUESTION_MESSAGE));
				} catch (NumberFormatException e) {

				}
			}
			while (worldHeight <= 0) {
				try {
					worldHeight = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the world height:",
							"Create world", JOptionPane.QUESTION_MESSAGE));
				} catch (NumberFormatException e) {
				}
			}
			while (name.equals("")) {
				name = JOptionPane.showInputDialog(null, "Enter the world name:", "Create world",
						JOptionPane.QUESTION_MESSAGE);
			}
			worldEdit = new World(worldHeight, worldHeight, name);
			wEG.load(worldEdit, npcMap);
		}

	}

	public void addPlayer(Player pl) {
		playersModel.addElement(pl);
		playersMap.put(pl.getUUID().toString(), pl);
		playersPane.getVerticalScrollBar().setValue(playersPane.getVerticalScrollBar().getMaximum());
	}

	public void removePlayer(UUID id) {
		playersModel.removeElement(playersMap.remove(id.toString()));
	}

	public void addNPC() {
		try {
			int str = Integer.parseInt(npcStr.getText()), dex = Integer.parseInt(npcDex.getText()),
					con = Integer.parseInt(npcCon.getText()), intel = Integer.parseInt(npcInt.getText()),
					wis = Integer.parseInt(npcWis.getText()), cha = Integer.parseInt(npcCha.getText());
			String name = npcName.getText().trim();
			if (str < 0 || dex < 0 || con < 0 || intel < 0 || wis < 0 || cha < 0) {
				JOptionPane.showMessageDialog(null, "Invalid stats", "Error", JOptionPane.INFORMATION_MESSAGE);
				return;
			} else if (name.trim().equals("")) {
				JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
			if(npcModel.contains(name)) {
				JOptionPane.showMessageDialog(null, "NPC already exists", "Error", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			NPC newNPC = new NPC(str, dex, con, intel, wis, cha, null, name);
			npcModel.addElement(newNPC.getName());
			npcMap.put(name, newNPC);
			npcStr.setText("");
			npcDex.setText("");
			npcCon.setText("");
			npcInt.setText("");
			npcWis.setText("");
			npcCha.setText("");
			npcName.setText("");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid stats", "Error", JOptionPane.INFORMATION_MESSAGE);

		}

	}

	public void addItem() {
		if (!itemsModel.contains(itemName.getText().trim())) {
			try {
				int bp = Integer.parseInt(itemBP.getText());
				int sp = Integer.parseInt(itemSP.getText());
				String name = itemName.getText().trim();
				String desc = itemDesc.getText().trim();
				Item item = new Item(name, bp, sp);
				item.setDesc(desc);
				itemsMap.put(name, item);
				itemsModel.addElement(name);
				itemName.setText("");
				itemBP.setText("");
				itemSP.setText("");
				itemDesc.setText("");
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid price", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Item already exists", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private class itemsListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			Item i = itemsMap.get(itemsList.getSelectedValue());
			itemName.setText(i.getName());
			itemBP.setText(i.getBuyPrice() + "");
			itemSP.setText(i.getSellPrice() + "");
			itemDesc.setText(i.getDesc());
		}

	}

	private class playerListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			Player p = playersMap.get(playersList.getSelectedValue().getUUID().toString());
			playerTitle.setText(p.toString());
			playerStr.setText("Strength: " + p.getStat("STR"));
			playerDex.setText("Dexterity: " + p.getStat("DEX"));
			playerCon.setText("Constitution: " + p.getStat("CON"));
			playerInt.setText("Intelligence: " + p.getStat("INT"));
			playerWis.setText("Wisdom: " + p.getStat("WIS"));
			playerCha.setText("Charisma: " + p.getStat("CHA"));
			playerMoney.setText("GP: " + p.getGP());
		}

	}
	
	private class npcListListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			NPC npc = npcMap.get(npcList.getSelectedValue());
			npcName.setText(npc.getName());
			npcStr.setText(npc.getStat("STR") + "");
			npcDex.setText(npc.getStat("DEX") + "");
			npcCon.setText(npc.getStat("CON") + "");
			npcInt.setText(npc.getStat("INT") + "");
			npcWis.setText(npc.getStat("WIS") + "");
			npcCha.setText(npc.getStat("CHA") + "");
		}

	}
}
