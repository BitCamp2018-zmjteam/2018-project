package com.scorpions.bcp.gui;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.scorpions.bcp.net.GameClientPlayer;
import com.scorpions.bcp.net.Request;
import com.scorpions.bcp.net.RequestType;
import com.scorpions.bcp.world.TileDirection;

public class PlayerGUI extends JFrame implements KeyListener {
	private static final long serialVersionUID = -5100004801987415763L;
	private JPanel panel;
	private int width, height;
	private JTextField input;
	private JTextArea log;
	private JScrollPane scroll;
	private GameClientPlayer client;

	public PlayerGUI(GameClientPlayer c) {
		super("Player");

		this.client = c;

		width = 800;
		height = 450;

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				client.disconnect();
				windowEvent.getWindow().dispose();
			}
		});

		panel = new JPanel();
		panel.setLayout(null);

		input = new JTextField();
		log = new JTextArea();
		scroll = new JScrollPane();

		scroll.setViewportView(log);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		panel.add(scroll);
		panel.add(input);

		scroll.setLocation(0, 0);

		scroll.setSize(width - 6, height - 75);
		log.setSize(scroll.getSize());
		log.setEditable(false);
		log.setColumns(20);
		log.setLineWrap(true);
		log.setWrapStyleWord(true);

		input.setLocation(0, height - 75);
		input.setSize(width, 50);
		input.addKeyListener(this);

		this.add(panel);
		this.setSize(width, height);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocation((1920 - width) / 2, (1080 - height) / 2);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		if (arg0.getKeyChar() == KeyEvent.VK_ENTER && !input.getText().trim().equals("")) {
			String line = input.getText().trim();
			input.setText("");
			parseInput(line);
		}

	}

	public void parseInput(String input) {
		String[] cmds = input.split(" ");
		if (cmds.length <= 1) {
			updateLog("Invalid input");
			return;
		}

		String cmd = cmds[0].toLowerCase().trim();
		if (cmd.equals("move")) {
			if (cmds.length > 2) {
				updateLog("Too many arguments");
				return;
			}
			String dir = cmds[1].toLowerCase().trim();
			if (!dir.equals("north") && !dir.equals("south") && !dir.equals("east") && !dir.equals("west")) {
				updateLog("Invalid direction");
				return;
			} else {
				Map<String, Object> dirMap = new HashMap<String, Object>();
				dirMap.put("direction", TileDirection.fromString(dir));
				dirMap.put("playerid", client.getPlayer().getUUID().toString());
				client.sendRequest(new Request(RequestType.PLAYER_MOVE, dirMap));
			}
		} else if (cmd.equals("interact")) {
			if (cmds.length > 2) {
				updateLog("Too many arguments");
				return;
			}
			else {
				Map<String, Object> targetMap = new HashMap<String, Object>();
				targetMap.put("target", cmds[1]);
				client.sendRequest(new Request(RequestType.PLAYER_MOVE, targetMap));
			}
		} else if(cmd.equals("look")) {
				if(cmds.length > 1) {
					updateLog("Too many arguments");
					return;
				}
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put("position", client.getPlayer().getPos());
				client.sendRequest(new Request(RequestType.PLAYER_MOVE, reqMap));
		} else {
			updateLog("Invalid input");
		}
	}

	public void updateLog(String line) {
		if (log.getText().trim().equals("")) {
			log.setText("> " + line);
		} else {
			log.setText(log.getText() + " \n> " + line);
		}
	}
}