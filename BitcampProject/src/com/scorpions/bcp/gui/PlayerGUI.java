package com.scorpions.bcp.gui;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.scorpions.bcp.net.GameClientPlayer;

public class PlayerGUI extends JFrame implements KeyListener {
	private static final long serialVersionUID = -5100004801987415763L;
	private JPanel panel;
	private int width, height;
	private JTextField input;
	private JTextArea log;
	private JScrollPane scroll;
	
	public PlayerGUI() {
		super("Player");
		
		width = 800;
		height = 450;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		
		scroll.setSize(width-6, height - 75);
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
			if(log.getText().trim().equals("")) {
				log.setText("> " + line);
			}
			else {
				log.setText(log.getText() + "\n> " + line);		
			}

		}

	}

}