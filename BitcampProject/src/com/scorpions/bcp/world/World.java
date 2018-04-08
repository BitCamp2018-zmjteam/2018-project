package com.scorpions.bcp.world;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.scorpions.bcp.creature.NPC;

public class World implements Serializable, Structure {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4342858946362283595L;
	private Tile[][] worldTiles;
	private List<Point> spawnPoints;
	private final String name;
	public static final String FILE_SUFFIX=".dtw";
	
	public World(int width, int height, String worldName) {
		worldTiles = new Tile[width][height];
		spawnPoints = new LinkedList<Point>();
		for(int i = 0; i < width; i++) {
			for(int k = 0; k < height; k++) {
				worldTiles[i][k] = new Tile(true);
			}
		}
		this.name = worldName;
	}
	
	public void addSpawnPoint(Point p) {
		spawnPoints.add(p);
	}
	
	public Point getRandomSpawn() {
		if(spawnPoints.isEmpty()) {
			return new Point(0,0);
		} else {
			return spawnPoints.get((int)(spawnPoints.size() * Math.random()));
		}
	}
	
	public List<Point> getSpawnPoints() {
		return spawnPoints;
	}
	
	public Tile getTile(int x, int y) {
		return worldTiles[x][y];
	}
	
	public Tile setTile(int x, int y, Tile t) {
		Tile c = worldTiles[x][y];
		worldTiles[x][y] = t;
		return c;
	}
	public int getWorldWidth() {
		return this.worldTiles.length;
	}
	
	public int getWorldHeight() {
		return this.worldTiles[0].length;
	}
	
	public boolean addStructure(Structure s, int xloc, int yloc) {
		try {
			Tile[][] toAdd = s.getTiles();
			for(int i = 0; i < toAdd.length; i++) {
				for (int k = 0; k < toAdd[0].length; k++) {
					worldTiles[xloc+i][yloc+k] = toAdd[i][k];
				}
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Expand the world in x and y directions
	 * @param expandWidth
	 * @param expandHeight
	 */
	public void expand(int expandWidth, int expandHeight) {
		Tile[][] newTiles = new Tile[getWorldWidth() + expandWidth][getWorldHeight() + expandHeight];
		for(int i = 0; i < getWorldWidth(); i++) {
			for(int k = 0; k < getWorldHeight(); k++) {
				newTiles[i][k] = worldTiles[i][k];
			}
		}
		if(expandWidth > 0) {
			for(int i = 0; i < expandWidth; i++) {
				for(int k = 0; k < getWorldHeight() + expandHeight; k++) {
					newTiles[getWorldWidth()+i][k] = null;
				}
			}
		}
		if(expandHeight > 0) {
			for(int i = 0; i < expandHeight; i++) {
				for(int k = 0; k < getWorldWidth() + expandWidth; k++) {
					newTiles[k][getWorldHeight() + i] = null;
				}
			}
		}
		this.worldTiles = newTiles;
	}
	
	/**
	 * Shrink the world by the specified lengths
	 * @param shrinkWidth
	 * @param shrinkHeight
	 */
	public void shrink(int shrinkWidth, int shrinkHeight) {
		int newWidth = getWorldWidth() - shrinkWidth;
		int newHeight = getWorldHeight() - shrinkHeight;
		Tile[][] newTiles = new Tile[newWidth][newHeight];
		for(int i = 0; i < newWidth; i++) {
			for(int k = 0; k < newHeight; k++) {
				newTiles[i][k] = worldTiles[i][k];
			}
		}
		worldTiles = newTiles;
	}
	
	
	public static World fromFile(File f) {
		try {
			if(f.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				Object o = ois.readObject();
				ois.close();
				if(o instanceof World) {
					return (World)o;
				}
				return null;
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean exportWorld(File f) {
		if(!f.getParentFile().exists()) {
			f.mkdirs();
		}
		try {
			if(!f.exists()) {
				f.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(this);
			oos.flush();
			oos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static File createDefaultWorld() {
		World w = new World(10, 5, "default");
		w.addStructure(new MerchantStructure(new NPC(0, 0, 0, 0, 0, 0, null, null)), 0, 0);
		w.addSpawnPoint(new Point(6,2));
		File exportFile = new File(System.getProperty("user.home") + File.separator + "default_world" + World.FILE_SUFFIX);
		if(w.exportWorld(exportFile)) {
			return exportFile;
		}
		return null;
	}

	@Override
	public Tile[][] getTiles() {
		return worldTiles;
	}

	@Override
	public String getName() {
		return "WORLD_ " + name;
	}
	
}
