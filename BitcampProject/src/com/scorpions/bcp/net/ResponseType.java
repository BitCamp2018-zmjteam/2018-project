package com.scorpions.bcp.net;

public enum ResponseType {

	PLAYER_ACCEPT(false), WORLD_INFO(false), PLAYER_MOVE(true),GAME_INFO(false),INTERACT_RESPONSE(false);

	
	private boolean showAll;
	
	/**
	 * Response type
	 * @param showAll Show to all server members or just one
	 */
	ResponseType(boolean showAll) {
		this.showAll = showAll;
	}
	
	public boolean showAll() {
		return this.showAll;
	}
	
	/*
	 * PLAYER_ACCEPT:
	 *   map:
	 *     - location: x,y pair
	 * WORLD_INFO:
	 *   map:
	 *     - area: tile[][] | area around the player
	 *     - offset: x,y pair | offset in the world
	 * PLAYER_MOVE:
	 *   map:
	 *     playerId: player who moved
	 *     location: x,y | new location
	 *     
	 * GAME_INFO:
	 *   map:
	 *     playerMap: map of player ids to full player objects
	 *     selfId: id of player
	 * 
	 * INTERACT_RESPONSE:
	 *   map:
	 *     success: true or false
	 *     result: possible result, probably null
	 *     
	 */
	
}
