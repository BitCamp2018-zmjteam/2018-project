package com.scorpions.bcp;

import java.util.HashSet;
import java.util.Set;

public abstract class Interaction {

	private Set<Trigger> triggers;
	
	public Interaction() {
		this.triggers = new HashSet<Trigger>();
	}
	
	/**
	 * Do whatever interaction was intended
	 */
	abstract void doInteraction();
	
	/**
	 * Get the possible triggers for this interaction
	 * @return Triggers
	 */
	public Set<Trigger> getTriggers() {
		return this.triggers;
	}
	
	/**
	 * Add a trigger
	 * @param t A new trigger
	 * @return true if successful
	 */
	public boolean addTrigger(Trigger t) {
		return this.triggers.add(t);
	}
	
	/**
	 * Remove a trigger
	 * @param t The trigger to remove
	 * @return true if successful
	 */
	public boolean removeTrigger(Trigger t) {
		return this.triggers.remove(t);
	}
	
	
}
