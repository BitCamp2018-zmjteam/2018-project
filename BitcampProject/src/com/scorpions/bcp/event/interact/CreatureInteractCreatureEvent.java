package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Creature;

public class CreatureInteractCreatureEvent extends CreatureInteractEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8623389595071673975L;

	public CreatureInteractCreatureEvent(Creature actor, Creature acted) {
		super(actor, acted);
	}
	
	public Creature getReceiver() {
		return (Creature) this.receiver;
	}

}
