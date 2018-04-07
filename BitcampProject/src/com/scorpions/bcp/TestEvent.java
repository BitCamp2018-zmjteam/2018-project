package com.scorpions.bcp;

import com.scorpions.bcp.event.Event;

public class TestEvent extends Event {

	@Override
	protected void enact(Game g) {
		System.out.println("YEEET");
	}

}
