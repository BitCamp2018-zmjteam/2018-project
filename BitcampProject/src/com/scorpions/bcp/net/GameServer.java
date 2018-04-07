package com.scorpions.bcp.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import com.scorpions.bcp.Game;

public class GameServer {

	private final Game game;
	private ServerSocket socket;
	private boolean running;
	private Set<ConnectedClient> clients;

	public GameServer(Game g) {
		this.game = g;
		this.running = false;
	}

	public void start() {
		try {
			this.socket = new ServerSocket(3252);
			GameServer s = this;
			clients = new HashSet<ConnectedClient>();
			Thread clientAccepter = new Thread() {
				@Override
				public void run() {
					System.out.println("Started client watcher");
					while (running) {
						System.out.println("in loop");
						try {
							System.out.println("try accept");
							Socket newSocket = socket.accept();
							System.out.println("accepted - - trying cleint");
							ConnectedClient cc = new ConnectedClient(newSocket, s);
							System.out.println("finish client");
							clients.add(cc);
							System.out.println("Accepted client");
							cc.start();
							System.out.println("Client start");
						} catch (IOException e) {
							if (socket.isClosed()) {
								// okay
								System.out.println("Client thread closed");
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
	
	public void queueStop() {
		this.running = false;
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

}
