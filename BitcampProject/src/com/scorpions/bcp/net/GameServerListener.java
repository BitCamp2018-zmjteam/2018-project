package com.scorpions.bcp.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.scorpions.bcp.event.EventHandler;
import com.scorpions.bcp.event.Listener;
import com.scorpions.bcp.event.PostEventTask;
import com.scorpions.bcp.event.interact.PlayerMessageEvent;
import com.scorpions.bcp.event.interact.PlayerSentMessageEvent;

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
	
	@EventHandler
	public void onPlayerSendMessage(PlayerSentMessageEvent e) {
		e.addPostCompleteTask(new PostEventTask(e) {
			@Override
			public void run() {
				if(e.getTargetId() == null) {
					//send to everyone
					for (ConnectedClient c : s.getClients()) {
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("uuid", e.getSender().getUUID().toString());
						map.put("message", e.getMessage());
						Response r = new Response(ResponseType.PLAYER_MESSAGE, map);
						try {
							c.send(r);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}
	
}
