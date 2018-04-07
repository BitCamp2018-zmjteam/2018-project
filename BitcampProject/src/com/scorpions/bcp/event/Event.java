package com.scorpions.bcp.event;

import com.scorpions.bcp.Game;

public abstract class Event {

	protected boolean cancelled;
	protected boolean done;
	
	public Event() {
		this.cancelled = false;
		done = false;
	}
	
	public boolean isCancellable() {
		return true;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void doEvent(Game g) {
		if(isCancellable() && isCancelled()) return;
		enact(g);
		done = true;
	}
	
	protected abstract void enact(Game g);
	
	public boolean complete() {
		return done;
	}
	
}
