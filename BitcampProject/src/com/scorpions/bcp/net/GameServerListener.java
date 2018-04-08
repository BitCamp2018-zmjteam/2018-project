package com.scorpions.bcp.net;

import java.io.IOException;

import com.scorpions.bcp.event.EventHandler;
import com.scorpions.bcp.event.Listener;
import com.scorpions.bcp.event.PostEventTask;
import com.scorpions.bcp.event.interact.PlayerMessageEvent;

public class GameServerListener implements Listener {

	private GameServer s;
	
	public GameServerListener(GameServer s) {
		this.s = s;
	}
	
	
	@EventHandler
	public void onPlayerMessageEvent(PlayerMessageEvent e) {
		e.addPostCompleteTask(new PostEventTask(e) {
			@Override
			public void run() {
				PlayerMessageEvent pme = (PlayerMessageEvent)e;
				ConnectedClient cc = s.getClient(e.getPlayerTarget());
				if(cc != null) {
					try {
						cc.send(pme.getResponse());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
}
