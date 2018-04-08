package com.scorpions.bcp.event.interact;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;
import com.scorpions.bcp.net.Response;
import com.scorpions.bcp.net.ResponseType;

public class PlayerMessageEvent extends Event {
	private static final long serialVersionUID = -1101816794250950761L;
	private Player target;
	private String message;
	private String sender;
	private Response r;
	
	public PlayerMessageEvent(Player target, String message, UUID sender) {
		this.target = target;
		this.message = message;
		this.sender = sender.toString();
		Map<String,Object> respoMap = new HashMap<>();
		respoMap.put("uuid", sender);
		respoMap.put("message", message);
		r = new Response(ResponseType.PLAYER_MESSAGE,respoMap);
	}
	public Player getPlayerTarget() {
		return target;
	}
	public String getMessage() {
		return message;
	}
	public String getSender() {
		return sender;
	}
	public Response getResponse() {
		return r;
	}
	protected void enact(Game g) {
		// TODO Auto-generated method stub
		// Send a response to the client with the Player_Message Protocol
	}

}
