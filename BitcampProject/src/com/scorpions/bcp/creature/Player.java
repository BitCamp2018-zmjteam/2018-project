package com.scorpions.bcp.creature;

import java.util.ArrayList;

public class Player extends Creature {
	private ArrayList<String> flags;
	public Player(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
		super(strength, dexterity, constitution, intelligence, wisdom, charisma);
		flags = new ArrayList<>();
	}
  //Stub methods - add flags later
	public boolean hasFlag(String flag) {
		return flags.contains(flag);
	}
	public boolean addFlag(String flag) {
		return flags.add(flag);
	}
}
