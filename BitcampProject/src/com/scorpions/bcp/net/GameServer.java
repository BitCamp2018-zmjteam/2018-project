package com.scorpions.bcp.net;

import java.awt.Point;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.scorpions.bcp.Game;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.PlayerMoveEvent;
import com.scorpions.bcp.event.PostEventTask;
import com.scorpions.bcp.gui.DMGUI;
import com.scorpions.bcp.world.Tile;
import com.scorpions.bcp.world.TileDirection;

public class GameServer extends Thread {

	private final Game game;
	private ServerSocket socket;
	private boolean running;
	private Set<ConnectedClient> clients;
	private Map<String, Player> players;
	private DMGUI gui;
	
	public GameServer(Game g) {
		this.game = g;
		this.running = false;
		this.players = new HashMap<String,Player>();
	}

	@Override
	public void run() {
		try {
			this.socket = new ServerSocket(3252);
			GameServer s = this;
			clients = new HashSet<ConnectedClient>();
			Thread clientAccepter = new Thread() {
				@Override
				public void run() {
					while (running) {
						try {
							Socket newSocket = socket.accept();
							ConnectedClient cc = new ConnectedClient(newSocket, s);
							clients.add(cc);
							cc.start();
						} catch (IOException e) {
							if (socket.isClosed()) {
								break;
							} else {
								e.printStackTrace();
							}
						}
					}
				}
			};

			running = true;
			clientAccepter.start();
			// game loop thing
			final int nsPerTick = (int) 1e9 / 20;
			double timePassed = 0;
			double currentTime = System.nanoTime();
			double lastTime = System.nanoTime();

			while (running) {
				currentTime = System.nanoTime();
				timePassed += (currentTime - lastTime) / nsPerTick;

				if (timePassed >= 1) {
					// Tick!!!
					tick();
					timePassed -= 1;
				}

				lastTime = currentTime;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Map<String,Object> infoRequest(Player p) {
		System.out.println(p);
		Map<String,Object> toReturn = new HashMap<String,Object>();
		toReturn.put("playerMap", players);
		toReturn.put("selfId", p.getUUID().toString());
		return toReturn;
	}
	
	public Response playerJoined(ConnectedClient c, Player p) {
		Map<String,Object> acceptMap = new HashMap<String,Object>();
		Point spawnPoint = game.getWorld().getRandomSpawn();
		acceptMap.put("location", spawnPoint);
		p.setX(spawnPoint.x);
		p.setY(spawnPoint.y);
		players.put(p.getUUID().toString(), p);
		Response r = new Response(ResponseType.PLAYER_ACCEPT, acceptMap);
		gui.addPlayer(p);
		return r;
	}
	
	
	public Player getPlayer(String uuid) {
		return this.players.get(uuid);
	}
	
	public void queueStop() {
		this.running = false;
	}
	
	public void playerMove(Map<String,Object> map) {
		TileDirection td = (TileDirection)map.get("direction");
		String id = (String)map.get("playerid");
		Player p = getPlayer(id);
		int newX = p.getPos().x;
		int newY = p.getPos().y;
		switch(td) {
		case BOTTOM:
			//y-1
			newY++;
			break;
		case LEFT:
			//x-1
			newX--;
			break;
		case RIGHT:
			//x+1
			newX++;
			break;
		case TOP:
			//y+1
			newY--;
			break;
		default:
			break;
		}
		if(newX < game.getWorld().getWorldWidth() && newY < game.getWorld().getWorldHeight() && newX >= 0 && newY>=0) {
			Tile destination = game.getWorld().getTile(newX, newY);
			if(destination.isNavigable() && destination.getCreature() == null) {
				//A ok move
				PlayerMoveEvent pme = new PlayerMoveEvent(p,new Point(newX,newY));
				pme.addPostCompleteTask(new PostEventTask(pme) {
					@Override
					public void run() {

						Map<String,Object> moveMap = new HashMap<String,Object>();
						Player player = ((PlayerMoveEvent)event).getPlayer();
						moveMap.put("playerid", player.getUUID().toString());
						moveMap.put("location", player.getPos());
						Response r = new Response(ResponseType.PLAYER_MOVE, moveMap);
						System.out.println(player.getPos().toString() + "  " + moveMap.get("location"));
						for(ConnectedClient cc : clients) {
							try {
								cc.send(r);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
				game.queueEvent(pme);
			}
		}
	}
	
	public void forceStop() {
		this.running = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void tick() {
		game.eventCycle();
	}

	/*
	 * Thread for each connected client main thread for game
	 */

	public void disconnected(ConnectedClient c) {
		this.clients.remove(c);
		System.out.println("Removed " + c);
	}
	
	public void kick(ConnectedClient c) {
		c.end("Kicked");
		System.out.println("Kicked " + c);
	}
	
	public void kick(ConnectedClient c, String reason) {
		c.end(reason);
		System.out.println("Kicked " + c + " -- " + reason);
	}

	public boolean containsClient(ConnectedClient c) {
		return clients.contains(c);
	}
	
	public Game getGame() {
		return this.game;
	}

	public void setGUI(DMGUI dmGUI) {
		this.gui = dmGUI;
	}

}
