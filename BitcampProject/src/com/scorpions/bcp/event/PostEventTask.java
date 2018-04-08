package com.scorpions.bcp.event;

public abstract class PostEventTask {
	
	protected final Event event;
	
	public PostEventTask(Event e) {
		this.event = e;
	}
	
	public abstract void run();
	
}
