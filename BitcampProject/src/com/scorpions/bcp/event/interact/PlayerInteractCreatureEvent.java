package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.creature.Player;

public class PlayerInteractCreatureEvent extends CreatureInteractCreatureEvent{

	public PlayerInteractCreatureEvent(Player actor, Creature acted) {
		super(actor, acted);
	}
	
	public Player getInitiator() {
		return (Player) this.initiator;
	}
	

}
