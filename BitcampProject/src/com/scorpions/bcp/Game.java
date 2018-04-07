package com.scorpions.bcp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.scorpions.bcp.event.Event;
import com.scorpions.bcp.event.Listener;

public class Game {
	
	private Queue<Event> eventQueue;
	private Set<RegisteredListener> listeners;

	
	
	
	
	public static Game getGame() {
		return null;
	}
	
	class RegisteredListener {
		
		private final Listener listenerInstance;
		private final Map<? extends Event, Method> eventMap;
		
		public RegisteredListener(Listener listenerInstance, Map<? extends Event, Method> eventMap) {
			this.listenerInstance = listenerInstance;
			this.eventMap = eventMap;
		}
		
		public void eventFired(Event e) {
			try {
				if(eventMap.get(e) != null) eventMap.get(e).invoke(listenerInstance, e);
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
