package com.scorpions.bcp.world;

public class World {

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
	
	
}
