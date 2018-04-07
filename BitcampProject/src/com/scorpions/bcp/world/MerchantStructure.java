package com.scorpions.bcp.world;

import com.scorpions.bcp.creature.Creature;

public class MerchantStructure implements Structure {

	private Creature merchant;
	private Tile[][] structureTiles;
	private TileDirection openingDirection;
	
	public MerchantStructure() {
		merchant = null;
		openingDirection = TileDirection.BOTTOM;
		initStructure();
	}
	
	/**
	 * Opening direction
	 * @param merchant Merchant
	 * @param t opening direction
	 */
	public MerchantStructure(Creature merchant, TileDirection t) {
		this.merchant = merchant;
		this.openingDirection = t;
		initStructure();
	}
	
	public MerchantStructure(Creature merchant) {
		this.merchant = merchant;
		this.openingDirection = TileDirection.BOTTOM;
		initStructure();
	}
	
	public MerchantStructure(TileDirection t) {
		this.merchant = null;
		this.openingDirection = t;
		initStructure();
	}

	
	private void initStructure() {
		structureTiles = new Tile[5][5];
		for(int i = 0; i < 5; i++) {
			structureTiles[0][i] = new Tile(false);
		}
		for(int i = 0; i < 5; i++) {
			structureTiles[i][0] = new Tile(false);
			structureTiles[i][4] = new Tile(false);
		}
		for(int i = 0; i < 5; i++) {
			structureTiles[4][i] = new Tile(false);
		}
		for(int i = 0; i < 5; i++) {
			for(int k = 0; k <5; k++) {
				if(structureTiles[i][k] == null) {
					structureTiles[i][k] = new Tile(true);
				}
			}
		}
		structureTiles[2][2].setCreature(merchant);
		switch(openingDirection) {
		case TOP: 
			structureTiles[0][2] = new Tile(true);
			break;
		case RIGHT:
			structureTiles[2][4] = new Tile(true);
			break;
		case BOTTOM:
			structureTiles[4][2] = new Tile(true);
			break;
		case LEFT:
			structureTiles[2][0] = new Tile(true);
			break;
		default:
			structureTiles[4][2] = new Tile(true);
		}
	}

	@Override
	public Tile[][] getTiles() {
		return structureTiles;
	}
	
}
