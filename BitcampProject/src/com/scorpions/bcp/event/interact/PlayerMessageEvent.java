package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;

public class PlayerMessageEvent extends Event {
	private static final long serialVersionUID = -1101816794250950761L;
	private Player target;
	private String message;
	
	public PlayerMessageEvent(Player target, String message) {
		this.target = target;
		this.message = message;
	}
	
	protected void enact(Game g) {
		// TODO Auto-generated method stub
		
	}

}
