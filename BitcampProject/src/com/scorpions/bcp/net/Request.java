package com.scorpions.bcp.net;

import java.util.Map;

public class Request extends SPacket {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6307752582728635103L;
	private final RequestType type;
	
	public Request(RequestType t, Map<String,Object> values) {
		super(values);
		this.type = t;
	}
	
	public final RequestType getType() {
		return this.type;
	}
	

}
