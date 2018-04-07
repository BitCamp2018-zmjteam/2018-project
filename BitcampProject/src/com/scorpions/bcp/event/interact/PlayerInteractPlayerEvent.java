package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Player;

public class PlayerInteractPlayerEvent extends PlayerInteractCreatureEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -454134541540985148L;

	public PlayerInteractPlayerEvent(Player actor, Player acted) {
		super(actor, acted);
	}
	
	public Player getReceiver() {
		return (Player) this.receiver;
	}

}
