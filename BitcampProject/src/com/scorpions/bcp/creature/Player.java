package com.scorpions.bcp.creature;

public class Player extends Creature {

	public Player(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
		super(strength, dexterity, constitution, intelligence, wisdom, charisma);
	}
  //Stub methods - add flags later
	public boolean hasFlag(String flag) {
		return true;
	}
	public boolean addFlag(String flag) {
		return true;
	}
}
