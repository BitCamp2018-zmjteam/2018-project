package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.world.Interactable;

public class CreatureInteractEvent extends InteractEvent {

	public CreatureInteractEvent(Creature actor, Interactable acted) {
		super(actor, acted);
	}
	
	public Creature getInitiator() {
		return (Creature) this.initiator;
	}

}
