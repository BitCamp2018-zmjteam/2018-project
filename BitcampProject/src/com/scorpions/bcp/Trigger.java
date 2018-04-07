package com.scorpions.bcp;

public abstract class Trigger {

	private Interaction interaction;
	
	public Trigger(Interaction interaction) {
		this.interaction = interaction;
	}
	
	public Interaction getInteraction() {
		return this.interaction;
	}
	
	abstract boolean isTriggered();
	
	abstract boolean canTrigger();
	
	abstract boolean complete();
	
}
