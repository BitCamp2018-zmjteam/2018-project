package com.scorpions.bcp.net;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.scorpions.bcp.creature.Player;
import com.scorpions.bcp.gui.PlayerGUI;

public class GameClientPlayer {

	private Player p;
	private Socket selfSocket;
	private boolean connected;
	
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	
	public GameClientPlayer(Player p, PlayerGUI gui) {
		this.p = p;
		selfSocket = null;
		connected = false;
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
					while(connected) {
						try {
							Response r = readResponse();
							if(r!=null) {
								evalResponse(r);
							}
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			socketListener.start();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					sendRequest(new Request(RequestType.PLAYER_LEAVE, new HashMap<String,Object>()));
				}
			});
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void sendGreeting() {
		Map<String,Object> greetingMap = new HashMap<String,Object>();
		greetingMap.put("player", p);
		this.sendRequest(new Request(RequestType.PLAYER_JOIN, greetingMap));
	}
	
	
	protected Response readResponse() throws ClassNotFoundException, IOException  {
		Object o = inStream.readObject();
		if(o instanceof Response) {
			return (Response)o;
		} 
		return null;
	}
	
	protected void evalResponse(Response r) {
		switch(r.getType()) {
		case PLAYER_ACCEPT:
			System.out.println(((Point)r.getValues().get("location")).toString());
		case GAME_INFO:
			break;
		case INTERACT_RESPONSE:
			break;
		case PLAYER_KICK:
			break;
		case PLAYER_MOVE:
			break;
		case WORLD_INFO:
			break;
		default:
			break;
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
	
}
