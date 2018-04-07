package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.world.Interactable;

public class PlayerInteractEvent extends CreatureInteractEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6819577110244023700L;


	public PlayerInteractEvent(Player actor, Interactable acted) {
		super(actor, acted);
	}
	
	
	public Player getInitiator() {
		return (Player) this.initiator;
	}

}
