package com.scorpions.bcp.world;

import java.io.Serializable;

import com.scorpions.bcp.creature.Creature;

public class Tile implements Serializable {
	
	//Tile has nothing because on its own it is nothing
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2770188295999462483L;
	private Creature currentCreature;
	private boolean navigable;
	
	/**
	 * Create a new tile with a creature present
	 * @param occupier The creature on the tile
	 * @param navigable Can creatures occupy this tile
	 */
	public Tile(boolean navigable, Creature occupier) {
		this.currentCreature = occupier;
		this.navigable = navigable;
	}
	
	/**
	 * Create a new tile with no creature present
	 * @param navigable Can creatures occupy this tile
	 */
	public Tile(boolean navigable) {
		this.currentCreature = null;
		this.navigable = navigable;
	}
	
	public Creature getCreature() {
		return this.currentCreature;
	}
	
	public boolean isNavigable() {
		return this.navigable;
	}
	
	
	public Creature setCreature(Creature c) {
		Creature lastCreature = this.currentCreature;
		this.currentCreature = c;
		return lastCreature;
	}
	
	
	/**
	 * Set navigable
	 * @param n
	 */
	public void setNavigable(boolean n) {
		this.navigable = n;
	}
	
	@Override
	public String toString() {
		return "TILE (" + currentCreature + ")";
	}
	
}
