package com.scorpions.bcp.creature;

import com.scorpions.bcp.DialogTree;
/**
 * An NPC is an interactable creature - has a dialog tree as well as the other attributes of a Creature
 * @author Morgan
 *
 */
public class NPC extends Creature {
	private DialogTree d;
	public NPC(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma, DialogTree d) {
		super(strength, dexterity, constitution, intelligence, wisdom, charisma);
		this.d = d;
	}
	public DialogTree getDialogTree() {
		return d;
	}
	public void setDialogTree(DialogTree d) {
		this.d = d;
	}
	public void converse(Player p) {
		d.converse(p);
	}
}
