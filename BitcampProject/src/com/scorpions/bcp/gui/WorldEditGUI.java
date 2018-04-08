package com.scorpions.bcp.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.scorpions.bcp.creature.NPC;
import com.scorpions.bcp.world.Structure;
import com.scorpions.bcp.world.Tile;
import com.scorpions.bcp.world.World;

public class WorldEditGUI extends JFrame implements ActionListener {
	private World current;
	private DMGUI dmGUI;
	private int width, height;
	private JPanel panel, worldPanel, previewPanel;
	private JButton flipTile, addNPC, placeStruct, saveAsWorld, saveAsStruct, loadWorld, exit;
	private JTextField xField, yField;
	private JLabel xLabel, yLabel;
	private int selectedX, selectedY;
	private DefaultListModel<Structure> structureModel;
	private JList<Structure> structureList;
	private TreeMap<String, NPC> npcs;

	public WorldEditGUI(DMGUI dmGUI) {
		super("World Edit");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.dmGUI = dmGUI;
		this.selectedX = this.selectedY = 0;
		width = 1200;
		height = 675;

		structureModel = new DefaultListModel<Structure>();
		structureList = new JList<Structure>(structureModel);
		JScrollPane structPane = new JScrollPane(structureList);

		panel = new JPanel();
		worldPanel = new WPanel();
		previewPanel = new SPanel();
		xLabel = new JLabel("X: ");
		yLabel = new JLabel("Y: ");
		xField = new JTextField();
		yField = new JTextField();
		flipTile = new JButton("Flip Tile");
		addNPC = new JButton("Add NPC");
		placeStruct = new JButton("Place Structure");
		saveAsWorld = new JButton("Save as World");
		saveAsStruct = new JButton("Save as Structure");
		loadWorld = new JButton("Load Structure");
		exit = new JButton("Exit");

		flipTile.setActionCommand("Flip");
		flipTile.addActionListener(this);
		addNPC.setActionCommand("Add NPC");
		addNPC.addActionListener(this);
		placeStruct.setActionCommand("Place Struct");
		placeStruct.addActionListener(this);
		saveAsWorld.setActionCommand("Save World");
		saveAsWorld.addActionListener(this);
		saveAsStruct.setActionCommand("Save Struct");
		saveAsStruct.addActionListener(this);
		loadWorld.setActionCommand("Load");
		loadWorld.addActionListener(this);
		exit.setActionCommand("Exit");
		exit.addActionListener(this);
		structureList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				repaint();
			}

		});

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
		panel.add(worldPanel);
		panel.add(structPane);
		panel.add(previewPanel);

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
		loadWorld.setSize(200, 40);
		exit.setLocation(10, 410);
		exit.setSize(150, 40);
		worldPanel.setSize(500, 500);
		worldPanel.setLocation(450, 50);
		structPane.setSize(200, 300);
		structPane.setLocation(225, 50);
		previewPanel.setSize(150, 150);
		previewPanel.setLocation(250, 375);

		panel.setLayout(null);
		this.add(panel);

		this.setSize(width, height);
		this.setResizable(false);
		this.setLocation((1920 - width) / 2, (1080 - height) / 2);
		this.setVisible(false);
	}

	public void load(World world, TreeMap<String, NPC> npcs) {
		current = world;
		this.npcs = npcs;
		dmGUI.setVisible(false);
		this.setVisible(true);
	}

	public void close() {
		npcs = null;
		current = null;
		this.setVisible(false);
		dmGUI.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("Flip") && validCoords(selectedX, selectedY)) {
			current.getTile(selectedX, selectedY).setNavigable(!current.getTile(selectedX, selectedY).isNavigable());
			repaint();
		} else if (arg0.getActionCommand().equals("Add NPC") && validCoords(selectedX, selectedY)) {
			addNPC(selectedX, selectedY);
			repaint();
		} else if (arg0.getActionCommand().equals("Place Struct")) {
			if (structureList.getSelectedValue() != null) {
				// check if can fit in world at point
				int height = structureList.getSelectedValue().getTiles().length;
				int width = structureList.getSelectedValue().getTiles()[0].length;
				int endPointX = selectedX + width;
				int endPointY = selectedY + height;
				if (endPointX <= current.getWorldWidth() && endPointY <= current.getWorldHeight()) {
					current.addStructure(structureList.getSelectedValue(), selectedX, selectedY);
				}
			}
			repaint();
		} else if (arg0.getActionCommand().equals("Save Struct")) {
			JFileChooser fc = new JFileChooser();
			int check = fc.showOpenDialog(new JFrame());

			if (check == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				Structure.exportStructure(current, new File(f.getAbsolutePath() + ".dts"));
			}

			repaint();
		} else if (arg0.getActionCommand().equals("Save World")) {

			JFileChooser fc = new JFileChooser();
			int check = fc.showOpenDialog(new JFrame());

			if (check == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				current.exportWorld(f);
			}

			repaint();
		} else if (arg0.getActionCommand().equals("Load")) {
			File f;
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Structure files", "dts");
			fc.setFileFilter(filter);
			int check = fc.showOpenDialog(new JFrame());
			if (check == JFileChooser.APPROVE_OPTION) {
				f = fc.getSelectedFile();
				// current = world from file
				Structure s = Structure.fromFile(f);
				if (s != null) {
					structureModel.addElement(s);
				}
			}

		} else if (arg0.getActionCommand().equals("Exit")) {
			this.close();
		}

	}

	public boolean validCoords(int x, int y) {
		if (x < 0 || y < 0 || x >= current.getWorldWidth() || y >= current.getWorldHeight()) {
			return false;
		}
		return true;
	}

	public void addNPC(int x, int y) {
		String name = JOptionPane.showInputDialog(null,
				"Enter the npc name from the npc list(enter null for an empty tile):", "Add NPC",
				JOptionPane.QUESTION_MESSAGE);
		NPC newNPC = npcs.get(name);
		current.getTile(x, y).setCreature(newNPC);
	}

	public boolean checkIfCoordsValid() {
		try {
			int x = Integer.parseInt(xField.getText());
			int y = Integer.parseInt(yField.getText());
			if (validCoords(x, y)) {
				JOptionPane.showMessageDialog(null, "Invalid coordinates", "Error", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid coordinates", "Error", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}

	class SPanel extends JPanel {

		@Override
		public void paint(Graphics g) {
			if (structureList.getSelectedValue() != null) {
				try {
					Structure current = structureList.getSelectedValue();
					Tile[][] tiles = current.getTiles();
					int structureH = current.getTiles().length;
					int structureW = current.getTiles()[0].length;
					int gridSize = (structureW > structureH) ? structureW : structureH;
					int offsetX = (gridSize - structureW) / 2;
					int offsetY = (gridSize - structureH) / 2;
					int gridBlockSize = (int) (getSize().getWidth() / gridSize);
					Color last = g.getColor();
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(0, 0, getWidth(), getHeight());
					for (int i = 0; i < structureW; i++) {
						for (int k = 0; k < structureH; k++) {
							int drawPosX = offsetX + i;
							int drawPosY = offsetY + k;
							if (tiles[i][k].isNavigable()) {
								g.setColor(Color.WHITE);
								g.drawRect(drawPosX * gridBlockSize, drawPosY * gridBlockSize, gridBlockSize,
										gridBlockSize);
								if (tiles[i][k].getCreature() != null) {
									g.setColor(new Color(211, 23, 208));
									int halfWidth = gridBlockSize / 2;
									g.fillOval((drawPosX * gridBlockSize) + halfWidth / 2,
											(drawPosY * gridBlockSize) + halfWidth / 2, halfWidth, halfWidth);
								}
							} else {

								g.setColor(Color.DARK_GRAY);
								g.fillRect(drawPosX * gridBlockSize, drawPosY * gridBlockSize, gridBlockSize,
										gridBlockSize);
								if (tiles[i][k].getCreature() != null) {
									g.setColor(new Color(211, 23, 208));
									int halfWidth = gridBlockSize / 2;
									g.fillOval((drawPosX * gridBlockSize) + halfWidth,
											(drawPosY * gridBlockSize) + halfWidth, halfWidth, halfWidth);
								}
							}
						}
					}
					g.setColor(Color.RED);
					g.drawRect(offsetX * gridBlockSize, offsetY * gridBlockSize, structureW * gridBlockSize,
							structureH * gridBlockSize);
					g.setColor(last);

				} catch (Exception e) {

				}
			} else {
				Color last = g.getColor();
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(last);
			}

		}
	}

	class WPanel extends JPanel {

		WPanel() {
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int xPixel = e.getX();
					int yPixel = e.getY();

					int gridSize = (current.getWorldWidth() > current.getWorldHeight()) ? current.getWorldWidth()
							: current.getWorldHeight();
					int offsetX = (gridSize - current.getWorldWidth()) / 2;
					int offsetY = (gridSize - current.getWorldHeight()) / 2;
					int gridBlockSize = (int) (getSize().getWidth() / gridSize);

					int tileX = xPixel / gridBlockSize;
					int tileY = yPixel / gridBlockSize;

					tileX -= offsetX;
					tileY -= offsetY;
					if (!(tileX < 0 || tileX >= current.getWorldWidth() || tileY < 0
							|| tileY >= current.getWorldHeight())) {
						selectedX = tileX;
						selectedY = tileY;
						xField.setText("" + selectedX);
						yField.setText("" + selectedY);
					}
					System.out.println("CLICK @(" + selectedX + ", " + selectedY + ") -- "
							+ current.getTile(selectedX, selectedY));
					repaint();
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			if (current != null) {
				int gridSize = (current.getWorldWidth() > current.getWorldHeight()) ? current.getWorldWidth()
						: current.getWorldHeight();
				int offsetX = (gridSize - current.getWorldWidth()) / 2;
				int offsetY = (gridSize - current.getWorldHeight()) / 2;
				int gridBlockSize = (int) (getSize().getWidth() / gridSize);
				Color last = g.getColor();
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
				for (int i = 0; i < current.getWorldWidth(); i++) {
					for (int k = 0; k < current.getWorldHeight(); k++) {
						int drawPosX = offsetX + i;
						int drawPosY = offsetY + k;
						if (current.getTile(i, k).isNavigable()) {
							g.setColor(Color.WHITE);
							g.drawRect(drawPosX * gridBlockSize, drawPosY * gridBlockSize, gridBlockSize,
									gridBlockSize);
							if (current.getTile(i, k).getCreature() != null) {
								g.setColor(new Color(211, 23, 208));
								int halfWidth = gridBlockSize / 2;
								g.fillOval((drawPosX * gridBlockSize) + halfWidth / 2,
										(drawPosY * gridBlockSize) + halfWidth / 2, halfWidth, halfWidth);
							}
						} else {

							g.setColor(Color.DARK_GRAY);
							g.fillRect(drawPosX * gridBlockSize, drawPosY * gridBlockSize, gridBlockSize,
									gridBlockSize);
							if (current.getTile(i, k).getCreature() != null) {
								g.setColor(new Color(211, 23, 208));
								int halfWidth = gridBlockSize / 2;
								g.fillOval((drawPosX * gridBlockSize) + halfWidth,
										(drawPosY * gridBlockSize) + halfWidth, halfWidth, halfWidth);
							}
						}
					}
				}
				g.setColor(Color.RED);
				g.drawRect(offsetX * gridBlockSize, offsetY * gridBlockSize, current.getWorldWidth() * gridBlockSize,
						current.getWorldHeight() * gridBlockSize);
				g.setColor(new Color(6, (9 * 16) + 4, 32));
				g.drawRect((selectedX + offsetX) * gridBlockSize, (selectedY + offsetY) * gridBlockSize, gridBlockSize,
						gridBlockSize);
				g.setColor(last);
			} else {
				Color last = g.getColor();
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(last);
			}
		}
	}
}
