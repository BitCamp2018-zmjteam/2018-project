package com.scorpions.bcp.world;

import java.util.HashSet;
import java.util.Set;

import com.scorpions.bcp.Interaction;
import com.scorpions.bcp.creature.Creature;

public class Tile {
	
	//Tile has nothing because on its own it is nothing
	
	private Creature currentCreature;
	private boolean navigable;
	private Set<Interaction> interactions;
	
	/**
	 * Create a new tile with a creature present
	 * @param occupier The creature on the tile
	 * @param navigable Can creatures occupy this tile
	 */
	public Tile(boolean navigable, Creature occupier) {
		this.currentCreature = occupier;
		this.navigable = navigable;
		this.interactions = new HashSet<Interaction>();
	}
	
	/**
	 * Create a new tile with no creature present
	 * @param navigable Can creatures occupy this tile
	 */
	public Tile(boolean navigable) {
		this.currentCreature = null;
		this.navigable = navigable;
		this.interactions = new HashSet<Interaction>();
	}
	
	public Creature getCreature() {
		return this.currentCreature;
	}
	
	public boolean isNavigable() {
		return this.navigable;
	}
	
	public boolean isInteractable() {
		return this.interactions.size() > 0;
	}
	
	public void addInteraction(Interaction i) {
		this.interactions.add(i);
	}
	
	public Set<Interaction> getInteractions() {
		return this.interactions;
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
		return "TILE (" + currentCreature + ") [Interactions: " + this.interactions.size() +"]";
	}
	
}
