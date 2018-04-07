package com.scorpions.bcp.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ConnectedClient extends Thread {

	private Socket clientSocket;
	private GameServer gameServer;
	private ObjectInputStream clientIn;
	private ObjectOutputStream clientOut;

	public ConnectedClient(Socket s, GameServer gameServer) throws IOException {
		this.clientSocket = s;
		this.gameServer = gameServer;
		this.clientOut = new ObjectOutputStream(s.getOutputStream());
		this.clientIn = new ObjectInputStream(s.getInputStream());
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
					handleRequest((Request)received);
				} else {
					end("Invalid request");
				}
			}
		}
	}

	
	protected void handleRequest(Request r) {
		switch(r.getType()) {
		case PLAYER_INTERACT:
			break;
		case PLAYER_JOIN:
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
			break;
		case PLAYER_MOVE:
			break;
		case WORLD_INFO:
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
		System.out.println("SENT RESPONSE");
	}

}
