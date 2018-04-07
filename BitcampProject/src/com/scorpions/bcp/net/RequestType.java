package com.scorpions.bcp.net;

import java.io.Serializable;

public enum RequestType implements Serializable {

	PLAYER_JOIN,PLAYER_LEAVE,WORLD_INFO,PLAYER_MOVE,PLAYER_INTERACT, GAME_INFO;
	
	/*
	 * PLAYER_JOIN:
	 *   request map:
	 *     - player: playerobject
	 *   response type:
	 *     - PLAYER_ACCEPT
	 * PLAYER_LEAVE:
	 *   request map:
	 *     -
	 *   response type:
	 *     -
	 * GAME_INFO:
	 *   request map:
	 *     - 
	 *   response type:
	 *     - GAME_INFO
	 * WORLD_INFO:
	 *   request map:
	 *     - x,y pair
	 *     - OPTIONAL -- distance (default 12)
	 *   response type:
	 *     - WORLD_INFO
	 * PLAYER_MOVE:
	 *   request map:
	 *     - direction: TileDirection direction
	 *   response type:
	 *     - PLAYER_MOVE
	 * PLAYER_INTERACT:
	 *   request map:
	 *     - location: x,y pair
	 *     - interactedPos: x,y pair
	 *     - interactType: TILE, CREATURE
	 *   response type:
	 *     - INTERACT_RESPONSE
	 *   
	 */
	
}
