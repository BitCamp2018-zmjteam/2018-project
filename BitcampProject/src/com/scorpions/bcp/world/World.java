package com.scorpions.bcp.world;

import java.io.Serializable;

public class World implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4342858946362283595L;
	private Tile[][] worldTiles;
	
	public World(int width, int height) {
		worldTiles = new Tile[width][height];
	}
	
	
	public Tile getTile(int x, int y) {
		return worldTiles[x][y];
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
}
