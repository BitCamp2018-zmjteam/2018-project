package com.scorpions.bcp.event;

import java.awt.Point;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.Player;

public class PlayerMoveEvent extends Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8084333077297372848L;
	private final Player p;
	private final Point dest;
	
	public PlayerMoveEvent(Player p, Point newDestination) {
		this.p = p;
		this.dest = newDestination;
	}
	
	public Point getDestination() {
		return this.dest;
	}
	
	public Player getPlayer() {
		return this.p;
	}

	@Override
	protected void enact(Game g) {
		//if not cancelled
		g.getWorld().getTile(p.getPos().x, p.getPos().y).setCreature(null);
		g.getWorld().getTile(dest.x, dest.y).setCreature(p);
		p.setX(dest.x);
		p.setY(dest.y);
	}

}
