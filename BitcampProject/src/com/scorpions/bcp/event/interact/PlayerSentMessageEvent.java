package com.scorpions.bcp.event.interact;

import java.util.UUID;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.creature.NPC;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;

public class PlayerSentMessageEvent extends Event {
	private static final long serialVersionUID = 1573914512647069099L;

	String target;
	String message;
	Player origin;
	
	public PlayerSentMessageEvent(UUID target, String message, Player origin) {
		this.target = target.toString();
		this.message = message;
		this.origin = origin;
	}
	
	@Override
	protected void enact(Game g) {
		// TODO Auto-generated method stub
		NPC npcTarget = (NPC)Creature.getCreature(target);
		npcTarget.sendMessage(message);
	}

}
