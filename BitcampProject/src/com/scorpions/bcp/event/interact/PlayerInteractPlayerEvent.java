package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Player;

public class PlayerInteractPlayerEvent extends PlayerInteractCreatureEvent {

	public PlayerInteractPlayerEvent(Player actor, Player acted) {
		super(actor, acted);
	}
	
	public Player getReceiver() {
		return (Player) this.receiver;
	}

}
