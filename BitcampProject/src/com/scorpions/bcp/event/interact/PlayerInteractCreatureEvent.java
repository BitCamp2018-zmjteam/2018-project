package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.creature.Player;

public class PlayerInteractCreatureEvent extends CreatureInteractCreatureEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3530491615546399637L;

	public PlayerInteractCreatureEvent(Player actor, Creature acted) {
		super(actor, acted);
	}
	
	public Player getInitiator() {
		return (Player) this.initiator;
	}
	

}
