package com.example;

public class Creature {
	private int strength, dexterity, constitution, intelligence, wisdom, charisma;
	
	public int getStr() {
		return strength;
	}
	public int getDex() {
		return dexterity;
	}
	public int getCon() {
		return constitution;
	}
	public int getInt() {
		return intelligence;
	}
	public int getWis() {
		return wisdom;
	}
	public int getCha() {
		return charisma;
	}
	
	public int getStrMod() {
		return (int) Math.floor((strength-10)/2);
	}
	public int getDexMod() {
		return (int) Math.floor((dexterity-10)/2);
	}
	public int getConMod() {
		return (int) Math.floor((constitution-10)/2);
	}
	public int getIntMod() {
		return (int) Math.floor((intelligence-10)/2);
	}
	public int getWisMod() {
		return (int) Math.floor((wisdom-10)/2);
	}
	public int getChaMod() {
		return (int) Math.floor((charisma-10)/2);
	}
	
}
