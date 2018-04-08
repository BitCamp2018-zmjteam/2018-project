package com.scorpions.bcp.net;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.creature.NPC;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.event.interact.PlayerInteractCreatureEvent;
import com.scorpions.bcp.event.interact.PlayerSentMessageEvent;

public class ConnectedClient extends Thread {

	private Socket clientSocket;
	private GameServer gameServer;
	private ObjectInputStream clientIn;
	private ObjectOutputStream clientOut;
	private Player player;
	
	public ConnectedClient(Socket s, GameServer gameServer) throws IOException {
		this.clientSocket = s;
		this.gameServer = gameServer;
		this.clientOut = new ObjectOutputStream(s.getOutputStream());
		this.clientIn = new ObjectInputStream(s.getInputStream());
		player = null;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	@Override
	public void run() {
		System.out.println("Thread start");
		while (clientSocket.isConnected()) {
			System.out.println("yeeeehaw");
			Object received;
			try {
				received = receive();
				System.out.println("Received ");
			} catch (ClassNotFoundException e) {
				// skip
				// end connection
				e.printStackTrace();
				if (!clientSocket.isClosed()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						System.out.println("Failed to close socket");
						e1.printStackTrace();
					}
				}
				gameServer.disconnected(this);
				received = null;
			} catch (IOException e) {
				// skip
				// end connection
				if(!(e instanceof SocketException)) e.printStackTrace();
				if (!clientSocket.isClosed()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						System.out.println("Failed to close socket");
						e1.printStackTrace();
					}
				}
				gameServer.disconnected(this);
				received = null;
			} catch (TimeoutException e) {
				// end connection
				e.printStackTrace();
				if (!clientSocket.isClosed()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						System.out.println("Failed to close socket");
						e1.printStackTrace();
					}
				}
				gameServer.disconnected(this);
				received = null;
			}
			if(received == null) {
				System.out.println("received is null");
				if(!gameServer.containsClient(this)) {
					break;
				} 
			} else {
				System.out.println("REQUEST ALIVE");
				if(received instanceof Request) {
					try {
						handleRequest((Request)received);
					} catch (Exception e) {
						System.out.println("Error while handling request from client");
						e.printStackTrace();
					}
				} else {
					end("Invalid request");
				}
			}
		}
	}

	
	protected void handleRequest(Request r) {
		switch(r.getType()) {
		case PLAYER_INTERACT:
			String name = (String)r.getValues().get("name");
			Point posit = (Point)r.getValues().get("interactedPos");
			String interType = (String)r.getValues().get("interactType");
			gameServer.playerInteract(player, name);
			break;
		case PLAYER_JOIN:
			player = (Player)r.getValues().get("player");
			try {
				send(gameServer.playerJoined(this, player));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			break;
		case PLAYER_LEAVE:
			System.out.println("Client dc'd");
			if (!clientSocket.isClosed()) {
				try {
					clientSocket.close();
				} catch (IOException e1) {
					System.out.println("Failed to close socket");
					e1.printStackTrace();
				}
			}
			gameServer.disconnected(this);
			break;
		case PLAYER_MOVE:
			Map<String,Object> moveMap = r.getValues();
			gameServer.playerMove(moveMap);
			break;
		case WORLD_INFO:
			Response res = gameServer.worldInfo(this);
			try {
				send(res);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case GAME_INFO:
			Map<String,Object> map = gameServer.infoRequest(player);
			Response response2 = new Response(ResponseType.GAME_INFO, map);
			try {
				send(response2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case MESSAGE_SENT:
			String targetId = (String)r.getValues().get("target");
			String msg = (String)r.getValues().get("message");
			gameServer.playerSentMessage(targetId, msg, player);
			break;
		default:
			break;
		
		}
	}
	
	public Socket getSocket() {
		return this.clientSocket;
	}

	private Object receive() throws ClassNotFoundException, IOException, TimeoutException, SocketException {
		System.out.println("READ START");
		return clientIn.readObject();
	}
	
	public void end(String reason) {
		Map<String,Object> kickMap = new HashMap<String,Object>();
		kickMap.put("reason", reason);
		try {
			send(new Response(ResponseType.PLAYER_KICK, kickMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameServer.disconnected(this);
	}
	
	public void send(Response s) throws IOException {
		clientOut.writeObject(s);
		clientOut.flush();
		clientOut.reset();
		System.out.println("SENT RESPONSE");
	}

}
