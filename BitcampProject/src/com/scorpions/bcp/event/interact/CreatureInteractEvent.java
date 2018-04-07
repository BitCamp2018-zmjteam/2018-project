package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.world.Interactable;

public class CreatureInteractEvent extends InteractEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 509685714222715428L;

	public CreatureInteractEvent(Creature actor, Interactable acted) {
		super(actor, acted);
	}
	
	public Creature getInitiator() {
		return (Creature) this.initiator;
	}

}
