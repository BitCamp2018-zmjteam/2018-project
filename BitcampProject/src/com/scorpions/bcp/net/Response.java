package com.scorpions.bcp.net;

import java.util.Map;

public class Response extends SPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8428957146390885288L;
	private final ResponseType t;
	
	public Response(ResponseType t, Map<String,Object> v) {
		super(v);
		this.t = t;
	}
	
	public ResponseType getType() {
		return this.t;
	}
	
}
