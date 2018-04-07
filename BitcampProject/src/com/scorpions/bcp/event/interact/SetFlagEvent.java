package com.scorpions.bcp.event.interact;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;

public class SetFlagEvent extends Event {
	Player p;
	String flagToSet;
	public SetFlagEvent(Player p, String flag) {
		this.p = p;
		this.flagToSet = flag;
	}
	@Override
	public boolean isCancellable() {
		return false;
	}
	@Override
	protected void enact(Game g) {
		p.addFlag(flagToSet);
	}

}
