package com.scorpions.bcp.creature;

import java.util.ArrayList;

import com.scorpions.bcp.world.Interactable;

public class Player extends Creature {
	private ArrayList<String> flags;
	private String race, role;

	public Player(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma, String name, String race, String role) {
		super(strength, dexterity, constitution, intelligence, wisdom, charisma, name);
		flags = new ArrayList<>();
		this.race = race;
		this.role = role;
	}
	public boolean hasFlag(String flag) {
		return flags.contains(flag);
	}
	public boolean addFlag(String flag) {
		return flags.add(flag);
	}
	@Override
	public String toString() {
		return this.name + ", the " + this.race + " " + this.role;
	}
	@Override
	public void interact(Interactable i) {
		// TODO Auto-generated method stub
		
	}
}
