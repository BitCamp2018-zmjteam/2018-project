package com.scorpions.bcp.creature;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.scorpions.bcp.items.Item;
import com.scorpions.bcp.world.Interactable;

public abstract class Creature implements Interactable, Serializable {

	private static final long serialVersionUID = 5068550098409037271L;
	private int gp;
	private int creatureSize;
	private int prof;
	protected String name;
	private TreeMap<String, Boolean> skillProf;
	private TreeMap<String, Integer> stats;
	private TreeMap<String, String> skillBase;
	private ArrayList<Item> inventory;
	private Point position;
	private UUID id;
	private static Map<String,Creature> creatures = new HashMap<>();
	public Creature(int strength, int dexterity, int constitution,
			int intelligence, int wisdom, int charisma, String name) {
		id = UUID.randomUUID();
		
		gp = 0;
		
		prof = 2;
		
		inventory = new ArrayList<Item>();
		skillProf = new TreeMap<String, Boolean>();
		stats = new TreeMap<>();
		skillBase = new TreeMap<>();
		this.name = name;
		initMaps(strength, dexterity, constitution, intelligence, wisdom, charisma);
		
		position = new Point(-1, -1);
		creatures.put(id.toString(), this);
	}
	
	public static void registerCreature(Creature c) {
		creatures.put(c.getUUID().toString(), c);
	}
	
	public void initMaps(int str, int dex, int con, int intel, int wis, int cha) {
		stats.put("STR", str);
		stats.put("DEX", dex);
		stats.put("CON", con);
		stats.put("INT", intel);
		stats.put("WIS", wis);
		stats.put("CHA", cha);
		
		skillProf.put("Acrobatics", false);
		skillProf.put("Animal Handling", false);
		skillProf.put("Arcana", false);
		skillProf.put("Athletics", false);
		skillProf.put("Deception", false);
		skillProf.put("History", false);
		skillProf.put("Insight", false);
		skillProf.put("Intimidation", false);
		skillProf.put("Investigation", false);
		skillProf.put("Medicine", false);
		skillProf.put("Nature", false);
		skillProf.put("Perception", false);
		skillProf.put("Performance", false);
		skillProf.put("Persuasion", false);
		skillProf.put("Religion", false);
		skillProf.put("Sleight of Hand", false);
		skillProf.put("Stealth", false);
		skillProf.put("Survival", false);
		
		skillBase.put("Acrobatics", "DEX");
		skillBase.put("Animal Handling", "WIS");
		skillBase.put("Arcana", "INT");
		skillBase.put("Athletics", "STR");
		skillBase.put("Deception", "CHA");
		skillBase.put("History", "INT");
		skillBase.put("Insight", "WIS");
		skillBase.put("Intimidation", "CHA");
		skillBase.put("Investigation", "INT");
		skillBase.put("Medicine", "WIS");
		skillBase.put("Nature", "INT");
		skillBase.put("Perception", "WIS");
		skillBase.put("Performance", "CHA");
		skillBase.put("Persuasion", "CHA");
		skillBase.put("Religion", "INT");
		skillBase.put("Sleight of Hand", "DEX");
		skillBase.put("Stealth", "DEX");
		skillBase.put("Survival", "WIS");
	}
	
	public int getStat(String stat) {
		return stats.get(stat);
	}
	
	public int getStatMod(String stat) {
		return (int) Math.floor((stats.get(stat)-10)/2);
	}
	
	
	public void changeStat(String stat, int amount) {
		if(stats.containsKey(stat)) {
			stats.put(stat, stats.get(stat) + amount);
		}
		else {
			System.out.println("Error when adjusting stat: " + stat);
		}
	}
	
	public int getSkillMod(String skill) {
		if(skillProf.get(skill)) {
			return prof + getStatMod(skillBase.get(skill));
		}
		return getStatMod(skillBase.get(skill));
	}
	
	public int getPassiveSkill(String skill) {
		return 10 + getSkillMod(skill);
	}
	
	public void changeGP(int amount) {
		this.gp += amount;
	}	
	
	public int getGP() {
		return this.gp;
	}
	
	public void setProf(int prof) {
		this.prof = prof;
	}
	
	public void addItem(Item item) {
		inventory.add(item);
	}
	
	public String getName(){
		return this.name;
	}

	public Point getPos() {
		return this.position;
	}

	public void setX(int x) {
		position.setLocation(x, position.getY());
	}
	
	public void setY(int y) {
		position.setLocation(position.getX(), y);
	}
	public UUID getUUID() {
		return id;
	}
	public static Creature getCreature(String id) {
		return creatures.get(id);
	}
}
