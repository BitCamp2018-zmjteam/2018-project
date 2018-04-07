package com.scorpions.bcp.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

public class ConnectedClient extends Thread {

	private Socket clientSocket;
	private GameServer gameServer;
	private ObjectInputStream clientIn;
	private ObjectOutputStream clientOut;

	public ConnectedClient(Socket s, GameServer gameServer) throws IOException {
		this.clientSocket = s;
		this.gameServer = gameServer;
		this.clientIn = new ObjectInputStream(s.getInputStream());
		this.clientOut = new ObjectOutputStream(s.getOutputStream());
	}

	@Override
	public void run() {
		while (clientSocket.isConnected()) {
			Object received;
			try {
				received = receive();
			} catch (ClassNotFoundException e) {
				// skip
				// end connection
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
				if(!gameServer.containsClient(this)) {
					break;
				} 
			} else {
				
			}
		}
	}

	public Socket getSocket() {
		return this.clientSocket;
	}

	private Object receive() throws ClassNotFoundException, IOException, TimeoutException {
		return clientIn.readObject();
	}

}
