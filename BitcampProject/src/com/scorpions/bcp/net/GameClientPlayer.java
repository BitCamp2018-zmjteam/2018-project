package com.scorpions.bcp.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.scorpions.bcp.creature.Player;

public class GameClientPlayer {

	private Player p;
	private Socket selfSocket;
	private boolean connected;
	
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	
	public GameClientPlayer(Player p) {
		this.p = p;
		selfSocket = null;
		connected = false;
	}
	
	public void connect(InetAddress addr, int port) {
		try {
			selfSocket = new Socket(addr.getHostAddress(), port);
			connected = true;
			inStream = new ObjectInputStream(selfSocket.getInputStream());
			outStream = new ObjectOutputStream(selfSocket.getOutputStream());
			Thread socketListener = new Thread() {
				@Override
				public void run() {
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
			sendGreeting();
			socketListener.start();
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
		
	}
	
	public void sendRequest(Request s) {
		try {
			outStream.writeObject(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
