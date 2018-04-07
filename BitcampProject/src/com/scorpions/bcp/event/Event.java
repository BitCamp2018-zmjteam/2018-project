package com.scorpions.bcp.event;

import java.io.Serializable;

import com.scorpions.bcp.Game;

public abstract class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7327815579969339862L;
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
	
	public void setCancelled(boolean c) {
		this.cancelled = c;
	}
	
	protected abstract void enact(Game g);
	
	public boolean complete() {
		return done;
	}
	
}
