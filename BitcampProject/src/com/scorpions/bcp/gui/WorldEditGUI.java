package com.scorpions.bcp.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
import javax.swing.filechooser.FileNameExtensionFilter;

import com.scorpions.bcp.world.Structure;
import com.scorpions.bcp.world.World;

public class WorldEditGUI extends JFrame implements ActionListener {
	private World current;
	private DMGUI dmGUI;
	private int width, height;
	private JPanel panel, worldPanel;
	private JButton flipTile, addNPC, placeStruct, saveAsWorld, saveAsStruct, loadWorld, exit;
	private JTextField xField, yField;
	private JLabel xLabel, yLabel;
	private int selectedX, selectedY;
	private DefaultListModel<Structure> structureModel;
	private JList<Structure> structureList;

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
		panel.add(worldPanel);
		panel.add(structPane);
		
		
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
		worldPanel.setSize(500, 500);
		worldPanel.setLocation(450, 50);
		structPane.setSize(200, 300);
		structPane.setLocation(225, 50);

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
		if (arg0.getActionCommand().equals("Flip") && validCoords(selectedX,selectedY)) {
			current.getTile(selectedX, selectedY).setNavigable(!current.getTile(selectedX, selectedY).isNavigable());
			repaint();
		} else if (arg0.getActionCommand().equals("Add NPC") && validCoords(selectedX,selectedY)) {
			repaint();
		} else if (arg0.getActionCommand().equals("Place Struct")) {
			
			repaint();
		} else if (arg0.getActionCommand().equals("Save Struct")) {
			JFileChooser fc = new JFileChooser();
			int check = fc.showOpenDialog(new JFrame());
			
			if(check == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				Structure.exportStructure(current, new File(f.getAbsolutePath() + ".dts"));
			}
			
			repaint();
		} else if (arg0.getActionCommand().equals("Save World")) {
			
			JFileChooser fc = new JFileChooser();
			int check = fc.showOpenDialog(new JFrame());
			
			if(check == JFileChooser.APPROVE_OPTION) {
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
				if(s!=null) {
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
	
	public boolean checkIfCoordsValid() {
		try {
			int x = Integer.parseInt(xField.getText());
			int y = Integer.parseInt(yField.getText());
			if (validCoords(x,y)) {
				JOptionPane.showMessageDialog(null, "Invalid coordinates", "Error", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid coordinates", "Error", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		return true;
	}

	class WPanel extends JPanel {
		
		private 
		
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
					
					int tileX = xPixel/gridBlockSize;
					int tileY = yPixel/gridBlockSize;
					
					tileX-=offsetX;
					tileY-=offsetY;
					if(!(tileX < 0 || tileX >= current.getWorldWidth() || tileY < 0 || tileY >= current.getWorldHeight())) {
						selectedX = tileX;
						selectedY = tileY;
						xField.setText(""+selectedX);
						yField.setText(""+selectedY);
					}
					System.out.println("CLICK @(" + selectedX + ", " + selectedY + ") -- " + current.getTile(selectedX, selectedY));
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
							if(current.getTile(i, k).getCreature() != null) {
								g.setColor(new Color(211,23,208));
								int halfWidth = gridBlockSize/2;
								g.fillOval((drawPosX * gridBlockSize)+halfWidth/2, (drawPosY * gridBlockSize)+halfWidth/2, halfWidth,
										halfWidth);
							}
						} else {
							
							g.setColor(Color.DARK_GRAY);
							g.fillRect(drawPosX * gridBlockSize, drawPosY * gridBlockSize, gridBlockSize,
									gridBlockSize);
							if(current.getTile(i, k).getCreature() != null) {
								g.setColor(new Color(211,23,208));
								int halfWidth = gridBlockSize/2;
								g.fillOval((drawPosX * gridBlockSize)+halfWidth, (drawPosY * gridBlockSize)+halfWidth, halfWidth,
										halfWidth);
							}
						}
					}
				}
				g.setColor(Color.RED);
				g.drawRect(offsetX*gridBlockSize, offsetY*gridBlockSize, current.getWorldWidth()*gridBlockSize, current.getWorldHeight()*gridBlockSize);
				g.setColor(new Color(6,(9*16)+4,32));
				g.drawRect((selectedX + offsetX)*gridBlockSize, (selectedY + offsetY)*gridBlockSize, gridBlockSize, gridBlockSize);
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
