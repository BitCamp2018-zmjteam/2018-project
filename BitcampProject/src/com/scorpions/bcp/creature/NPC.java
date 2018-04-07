package com.scorpions.bcp.creature;

import com.scorpions.bcp.DialogTree;
import com.scorpions.bcp.world.Interactable;
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
	@Override
	public void interact(Interactable i) {
		if (i instanceof Player)
			if (d.canStart((Player) i))
				d.converse((Player) i);
	}
}
