package com.scorpions.bcp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.scorpions.bcp.event.Event;
import com.scorpions.bcp.event.EventHandler;
import com.scorpions.bcp.event.Listener;
import com.scorpions.bcp.world.World;

public class Game {
	
	private Queue<Event> eventQueue, nextQueue;
	private Set<RegisteredListener> listeners;
	private boolean queueLocked;
	private World gameWorld;
	
	private static Game game;
	
	public Game(World w) {
		queueLocked = false;
		eventQueue = new LinkedList<Event>();
		nextQueue = new LinkedList<Event>();
		listeners = new HashSet<RegisteredListener>();
		gameWorld = w;
		Game.game = this;
		System.out.println(gameWorld);
	}
	
	public World getWorld() {
		return this.gameWorld;
	}
	
	public boolean registerListener(Listener l) {
		Method[] classMethods = l.getClass().getDeclaredMethods();
		Map<Class<? extends Event>, Method> methodMap = new HashMap<Class<? extends Event>, Method>();
		for(Method m : classMethods) {
			if(m.getAnnotation(EventHandler.class) != null && m.getParameterTypes().length == 1) {
				//Nice
				Class<?> methodParam = m.getParameterTypes()[0];
				if(Event.class.isAssignableFrom(methodParam)) {
					try {
						@SuppressWarnings("unchecked")
						Class<? extends Event> methodParamIsEvent = (Class<? extends Event>)methodParam;
						methodMap.put(methodParamIsEvent, m);
					} catch (ClassCastException ex) {
						//oof
						ex.printStackTrace();
						return false;
					}
				}
			}
		}
		RegisteredListener newListener = new RegisteredListener(l, methodMap);
		return this.listeners.add(newListener);
	}
	
	public void queueEvent(Event e) {
		//todo
		if(queueLocked) {
			nextQueue.add(e);
		} else {
			this.eventQueue.add(e);
		}
	}
	
	public void eventCycle() {
		queueLocked = true;
		for(Event e : eventQueue) {
			for(RegisteredListener l : listeners) {
				l.eventFired(e);
			}
			e.doEvent(this);
		}
		for(Event e : eventQueue) {
			e.doPostCompleteTasks(false);
		}
		eventQueue.clear();
		eventQueue.addAll(nextQueue);
		queueLocked = false;
		nextQueue.clear();
	}

	public static Game getGame() {
		return Game.game;
	}
	
	class RegisteredListener {
		
		private final Listener listenerInstance;
		private final Map<Class<? extends Event>, Method> eventMap;
		
		public RegisteredListener(Listener listenerInstance, Map<Class<? extends Event>, Method> eventMap) {
			this.listenerInstance = listenerInstance;
			this.eventMap = eventMap;
		}
		
		public void eventFired(Event e) {
			try {
				if(eventMap.get(e.getClass()) != null) {
					eventMap.get(e.getClass()).invoke(listenerInstance, e);
				}
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
