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
import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.Event;
import com.scorpions.bcp.event.PlayerMoveEvent;
import com.scorpions.bcp.event.PostEventTask;
import com.scorpions.bcp.event.interact.PlayerInteractCreatureEvent;
import com.scorpions.bcp.event.interact.PlayerInteractEvent;
import com.scorpions.bcp.event.interact.PlayerSentMessageEvent;
import com.scorpions.bcp.gui.DMGUI;
import com.scorpions.bcp.world.Interactable;
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
		g.registerListener(new GameServerListener(this));
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
	
	public Response worldInfo(ConnectedClient c) {
		Player p = c.getPlayer();
		Tile[][] areaTiles = retrieveTiles(p.getPos().x,p.getPos().y);
		Map<Interactable,Point> possibleTargets = new HashMap<>();
		for (int i=0;i<areaTiles.length;i++) {
			for (int j=0;j<areaTiles[0].length;j++) {
				if (areaTiles[i][j] instanceof Interactable) {
					possibleTargets.put((Interactable)areaTiles[i][j],new Point(i,j));
				}
				if (areaTiles[i][j].getCreature() != null) {
					possibleTargets.put((areaTiles[i][j].getCreature()),new Point(i,j));
				}
			}
		}
		System.out.println(possibleTargets + "\n" + possibleTargets.size());
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("area", areaTiles);
		returnMap.put("targets", possibleTargets);
		Response r = new Response(ResponseType.WORLD_INFO, returnMap);
		return r;
	}
	
	public Tile[][] retrieveTiles(int x, int y) {
		int minX, minY, maxX, maxY;
		int lookDistance = 12;
		int lookRadius = lookDistance/2;
		minX = x-lookRadius;
		minY = y-lookRadius;
		maxX = x+lookRadius;
		maxY = y+lookRadius;
		if(minX <= 0) {
			minX = 0;
		}
		if(maxX > game.getWorld().getWorldWidth()) {
			maxX=game.getWorld().getWorldWidth();
		}
		if(minY <= 0) {
			minY = 0;
		}
		if(maxY > game.getWorld().getWorldHeight()) {
			maxY = game.getWorld().getWorldHeight();
		}
		int xrange = maxX-minX;
		int yrange = maxY-minY;
		Tile[][] areaTiles = new Tile[xrange][yrange];
		for(int i = 0; i < xrange; i++) {
			for(int k = 0; k < yrange; k++) {
				areaTiles[i][k] = game.getWorld().getTile(i+minX, k+minY);
			}
		}
		return areaTiles;
	}
	
	public Map<String,Object> infoRequest(Player p) {
		System.out.println(p);
		Map<String,Object> toReturn = new HashMap<String,Object>();
		toReturn.put("playerMap", players);
		toReturn.put("selfId", p.getUUID().toString());
		return toReturn;
	}
	
	public ConnectedClient getClient(Player p) {
		for(ConnectedClient c : clients) {
			if(c.getPlayer().equals(p)) return c;
		}
		return null;
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
	
	public void playerSentMessage(String targetId, String msg, Player sender) {
		PlayerSentMessageEvent event = new PlayerSentMessageEvent(targetId, msg, sender);
		game.queueEvent(event);
  }
  
	public void playerInteract(Player interactor, String name) {
		Tile[][] around = retrieveTiles(interactor.getPos().x,interactor.getPos().y);
		Map<String,Interactable> possibleTargets = new HashMap<>();
		for (Tile[] row : around) {
			for (Tile t : row) {
				if (t instanceof Interactable) {
					possibleTargets.put(((Interactable)t).getName(),(Interactable)t);
				}
				if (t.getCreature() != null) {
					possibleTargets.put(((Interactable)t.getCreature()).getName(), t.getCreature());
				}
			}
		}
		Interactable interactableTarget = possibleTargets.get(name);
		if (interactableTarget != null) {
			Event e;
			if (interactableTarget instanceof Tile) {
				e = new PlayerInteractEvent(interactor,interactableTarget);
			} else {
				e = new PlayerInteractCreatureEvent(interactor,(Creature)interactableTarget);
			}
			getGame().queueEvent(e);
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
	
	public Set<ConnectedClient> getClients() {
		return this.clients;
	}
	
	protected void tick() {
		game.eventCycle();
	}

	/*
	 * Thread for each connected client main thread for game
	 */

	public void disconnected(ConnectedClient c) {
		this.clients.remove(c);
		this.players.remove(c.getPlayer().getUUID().toString());
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
