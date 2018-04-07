package com.scorpions.bcp.world;

public class World {

	private Tile[][] worldTiles;
	
	public World(int width, int height) {
		worldTiles = new Tile[width][height];
	}
	
	
	public void resize(int newWidth, int newHeight) {
		//TODO resize
	}
	
}
