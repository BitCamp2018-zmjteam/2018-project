package com.scorpions.bcp.net;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.scorpions.bcp.creature.Creature;
import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.gui.PlayerGUI;
import com.scorpions.bcp.world.Tile;

public class GameClientPlayer {

	private Player p;
	private Socket selfSocket;
	private boolean connected;

	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private PlayerGUI gui;
	private Map<String,Player> players;

	public GameClientPlayer(Player p) {
		this.p = p;
		selfSocket = null;
		connected = false;
		gui = null;
		players = new HashMap<String,Player>();
	}

	public void connect(InetAddress addr, int port) {
		try {
			selfSocket = new Socket(addr.getHostAddress(), port);
			connected = true;
			outStream = new ObjectOutputStream(selfSocket.getOutputStream());
			inStream = new ObjectInputStream(selfSocket.getInputStream());
			Thread socketListener = new Thread() {
				@Override
				public void run() {

					sendGreeting();
					while (connected) {
						try {
							Response r = readResponse();
							if (r != null) {
								evalResponse(r);
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							if (!(e instanceof SocketException)) {
								e.printStackTrace();
							} else {
								connected = false;
								System.exit(0);
							}
						}
					}
				}
			};
			socketListener.start();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (!selfSocket.isClosed()) {
						sendRequest(new Request(RequestType.PLAYER_LEAVE, new HashMap<String, Object>()));
					}
				}
			});
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void sendGreeting() {
		Map<String, Object> greetingMap = new HashMap<String, Object>();
		greetingMap.put("player", p);
		this.sendRequest(new Request(RequestType.PLAYER_JOIN, greetingMap));
	}

	protected Response readResponse() throws ClassNotFoundException, IOException {
		Object o = inStream.readObject();
		if (o instanceof Response) {
			return (Response) o;
		}
		return null;
	}

	protected void evalResponse(Response r) {
		System.out.println("eval Start "+r.getType());
		switch (r.getType()) {
		case PLAYER_ACCEPT:
			gui.updateLog("Location: " + ((Point) (r.getValues().get("location"))).getX() + ", " + ((Point) (r.getValues().get("location"))).getY());
			Point point = (Point) r.getValues().get("location");
			p.setX(point.x);
			p.setY(point.y);
			this.sendRequest(new Request(RequestType.GAME_INFO,null));
			break;
		case GAME_INFO:
			Map<String,Player> playerMap = (Map<String,Player>)r.getValues().get("playerMap");
			System.out.println(r.getValues());
			String selfID = ((String)r.getValues().get("selfId"));
			assert(selfID.equals(p.getUUID().toString()));
			gui.updateLog("You have UUID " + selfID);
			gui.updateLog("Also in the world are:");
			for (String u : playerMap.keySet()) {
				gui.updateLog(playerMap.get(u) + " with UUID " + u);
			}
			HashMap<String,Object> worldInfoMap = new HashMap<>();
			worldInfoMap.put("location", new Point((int)p.getPos().getX(),(int)p.getPos().getY()));
			this.sendRequest(new Request(RequestType.WORLD_INFO,worldInfoMap));
			break;
		case INTERACT_RESPONSE:
			boolean success = (Boolean)r.getValues().get("success");
			Object result = r.getValues().get("result");
			System.out.print("Interaction ");
			System.out.print(success?"succeeded":"failed");
			if (result != null)
				gui.updateLog(" - "+result.toString());
			else
				gui.updateLog("");
			break;
		case PLAYER_KICK:
			gui.updateLog("You were kicked from the server.");
			break;
		case PLAYER_MOVE:
			String playerID = (String)r.getValues().get("playerid");
			Point location = (Point)r.getValues().get("location");
			if(this.players.containsKey(playerID)) {
				Player moved = this.players.get(playerID);
				moved.setX(location.x);
				moved.setY(location.y);
				if(this.p.getUUID().toString().equals(playerID)) {
					gui.updateLog("You are now at ("+location.getX()+","+location.getY()+").");
				}
			}
			break;
		case WORLD_INFO:
			Tile[][] area = ((Tile[][])r.getValues().get("area"));
			String update = "";
			for (Tile[] row : area) {
				for (Tile t : row) {
					update += t.getCreature()==null?t.isNavigable()?" ":"#":"@";
				}
				update += "\n";
			}
			update += "# is a barrier, @ is a creature\n";
			gui.updateLog(update);
			break;
		case PLAYER_MESSAGE:
			String sender = Creature.getCreature((String)r.getValues().get("uuid")).getName();
			String message = (String)r.getValues().get("message");
			gui.updateLog("("+sender+")\n"+message);
		default:
			break;
		}
	}

	public void disconnect() {
		connected = false;
		sendRequest(new Request(RequestType.PLAYER_LEAVE, new HashMap<String, Object>()));
		try {
			selfSocket.close();
		} catch (IOException e) {
			System.out.println("Failed to close socket");
			e.printStackTrace();
		}
	}

	public void sendRequest(Request s) {
		try {
			outStream.writeObject(s);
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setGUI(PlayerGUI p) {
		this.gui = p;
	}

	public Player getPlayer() {
		return p;
	}
}
