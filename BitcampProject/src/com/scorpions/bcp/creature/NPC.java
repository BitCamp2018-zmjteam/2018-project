package com.scorpions.bcp.creature;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import com.scorpions.bcp.DialogTree;
import com.scorpions.bcp.world.Interactable;
/**
 * An NPC is an interactable creature - has a dialog tree as well as the other attributes of a Creature
 * @author Morgan
 *
 */
public class NPC extends Creature {
	private static final long serialVersionUID = 640702901064267395L;
	private DialogTree d;
	public NPC(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma, DialogTree d, String name) {
		super(strength, dexterity, constitution, intelligence, wisdom, charisma, name);
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
				try {
					d.converse((Player) i);
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
	}
	public void sendMessage(String message) {
		try {
			d.getPipedWriter().write(message);
			d.getPipedWriter().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
