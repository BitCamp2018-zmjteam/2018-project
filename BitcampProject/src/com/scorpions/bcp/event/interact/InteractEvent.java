package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.event.Event;
import com.scorpions.bcp.world.Interactable;

public abstract class InteractEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3751070048052623581L;
	protected final Interactable initiator, receiver;
	
	public InteractEvent(Interactable actor, Interactable acted) {
		this.initiator = actor;
		this.receiver = acted;
	}
	
	public Interactable getInitiator() {
		return this.initiator;
	}
	
	public Interactable getReceiver() {
		return this.receiver;
	}
	
	@Override
	public void enact(Game g) { 
		receiver.interact(initiator);
	}
	
}
