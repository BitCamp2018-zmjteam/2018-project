package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.creature.NPC;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;

public class PlayerSentMessageEvent extends Event {
	private static final long serialVersionUID = 1573914512647069099L;

	private String target;
	private final String message;
	private final Player origin;
	
	public PlayerSentMessageEvent(String targetId, String message, Player origin) {
		this.target = targetId;
		this.message = message;
		this.origin = origin;
	}
	
	@Override
	protected void enact(Game g) {
		if(target != null) {
			Creature c = Creature.getCreature(target);
			if(c != null) {
				if(c instanceof NPC) {
					((NPC)c).sendMessage(message);
				} else {
					target = null;
				}
			} else {
				target = null;
			}
		}
		
	}
	
	public Player getSender() {
		return this.origin;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getTargetId() {
		return this.target;
	}
	
	

}
