package com.scorpions.bcp;

import com.scorpions.bcp.event.EventHandler;
import com.scorpions.bcp.event.Listener;

public class TestListener implements Listener {

	@EventHandler
	public void testEventMethod(TestEvent e) {
		e.setCancelled(true);
	}
	
}
