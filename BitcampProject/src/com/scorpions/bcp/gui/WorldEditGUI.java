package com.scorpions.bcp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.scorpions.bcp.world.World;

public class WorldEditGUI extends JFrame implements ActionListener {
	private World current;
	private DMGUI dmGUI;
	private int width, height;
	private JPanel panel;
	private JButton flipTile, addNPC, placeStruct, saveAsWorld, saveAsStruct, loadWorld, exit;
	private JTextField xField, yField;
	private JLabel xLabel, yLabel;

	public WorldEditGUI(DMGUI dmGUI) {
		super("World Edit");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.dmGUI = dmGUI;
		width = 1200;
		height = 675;

		panel = new JPanel();
		xLabel = new JLabel("X: ");
		yLabel = new JLabel("Y: ");
		xField = new JTextField();
		yField = new JTextField();
		flipTile = new JButton("Flip Tile");
		addNPC = new JButton("Add NPC");
		placeStruct = new JButton("Place Structure");
		saveAsWorld = new JButton("Save as World");
		saveAsStruct = new JButton("Save as Structure");
		loadWorld = new JButton("Load World");
		exit = new JButton("Exit");

		flipTile.setActionCommand("Flip");
		flipTile.addActionListener(this);
		addNPC.setActionCommand("Add NPC");
		addNPC.addActionListener(this);
		placeStruct.setActionCommand("Place Struct");
		placeStruct.addActionListener(this);
		saveAsWorld.setActionCommand("Save World");
		saveAsWorld.addActionListener(this);
		saveAsWorld.setActionCommand("Save Struct");
		saveAsWorld.addActionListener(this);
		loadWorld.setActionCommand("Load");
		loadWorld.addActionListener(this);
		exit.setActionCommand("Exit");
		exit.addActionListener(this);

		panel.add(xLabel);
		panel.add(yLabel);
		panel.add(xField);
		panel.add(yField);
		panel.add(flipTile);
		panel.add(placeStruct);
		panel.add(addNPC);
		panel.add(saveAsWorld);
		panel.add(saveAsStruct);
		panel.add(loadWorld);
		panel.add(exit);

		xLabel.setLocation(10, 10);
		xLabel.setSize(100, 40);
		yLabel.setLocation(10, 60);
		yLabel.setSize(100, 40);
		xField.setLocation(50, 10);
		xField.setSize(100, 40);
		yField.setLocation(50, 60);
		yField.setSize(100, 40);
		flipTile.setLocation(10, 110);
		flipTile.setSize(150, 40);
		addNPC.setLocation(10, 160);
		addNPC.setSize(150, 40);
		placeStruct.setLocation(10, 210);
		placeStruct.setSize(170, 40);
		saveAsWorld.setLocation(10, 260);
		saveAsWorld.setSize(200, 40);
		saveAsStruct.setLocation(10, 310);
		saveAsStruct.setSize(200, 40);
		loadWorld.setLocation(10, 360);
		loadWorld.setSize(150, 40);
		exit.setLocation(10, 410);
		exit.setSize(150, 40);

		panel.setLayout(null);
		this.add(panel);

		this.setSize(width, height);
		this.setResizable(false);
		this.setLocation((1920 - width) / 2, (1080 - height) / 2);
		this.setVisible(false);
	}

	public void load(World world) {
		current = world;
		dmGUI.setVisible(false);
		this.setVisible(true);
	}

	public void close() {
		this.setVisible(false);
		dmGUI.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Flip") && checkIfCoordsValid()) {

		} else if (arg0.getActionCommand().equals("Add NPC") && checkIfCoordsValid()) {

		} else if (arg0.getActionCommand().equals("Place Struct")) {

		} else if (arg0.getActionCommand().equals("Save Struct")) {

		} else if (arg0.getActionCommand().equals("Save World")) {

		} else if (arg0.getActionCommand().equals("Load")) {
			File f;
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("World files", ".dtw");
			fc.setFileFilter(filter);
			int check = fc.showOpenDialog(new JFrame());
			if (check == JFileChooser.APPROVE_OPTION) {
				f = fc.getSelectedFile();
				// current = world from file
			}

		}
		else if(arg0.getActionCommand().equals("Exit")) {
			this.close();
		}

	}

	public boolean checkIfCoordsValid() {
		try {
			int x = Integer.parseInt(xField.getText());
			int y = Integer.parseInt(yField.getText());
			if(x < 0 || y < 0 || x >= current.getWorldWidth() || y >= current.getWorldHeight()) {
				JOptionPane.showMessageDialog(null, "Invalid coordinates", "Error",
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}	
		catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid coordinates", "Error",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		return true;
	}
}
