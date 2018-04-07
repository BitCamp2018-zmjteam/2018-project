package com.scorpions.bcp;

public class Driver {

	public static void main(String[] args) {
		Game g = new Game();
		g.queueEvent(new TestEvent());
		g.eventCycle();
		g.queueEvent(new TestEvent());
		g.registerListener(new TestListener());
		g.eventCycle();
	}

}
