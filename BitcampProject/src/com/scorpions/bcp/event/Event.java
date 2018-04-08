package com.scorpions.bcp.event;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import com.scorpions.bcp.Game;

public abstract class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7327815579969339862L;
	protected boolean cancelled;
	protected boolean done;
	protected Queue<PostEventTask> postCompleteTasks;
	
	public Event() {
		this.cancelled = false;
		done = false;
		postCompleteTasks = new LinkedList<PostEventTask>();
	}
	
	public void addPostCompleteTask(PostEventTask r) {
		this.postCompleteTasks.add(r);
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
	
	public boolean doPostCompleteTasks(boolean force) {
		if(!force && done) {
			for(PostEventTask t : postCompleteTasks) {
				t.run();
			}
			return false;
		} else if (force) {
			for(PostEventTask t : postCompleteTasks) {
				t.run();
			}
			return true;
		}
		return false;
	}
	
	public void setCancelled(boolean c) {
		this.cancelled = c;
	}
	
	protected abstract void enact(Game g);
	
	public boolean complete() {
		return done;
	}
	
}
