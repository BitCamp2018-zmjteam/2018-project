package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Creature;

public class CreatureInteractCreatureEvent extends CreatureInteractEvent {

	public CreatureInteractCreatureEvent(Creature actor, Creature acted) {
		super(actor, acted);
	}
	
	public Creature getReceiver() {
		return (Creature) this.receiver;
	}

}
