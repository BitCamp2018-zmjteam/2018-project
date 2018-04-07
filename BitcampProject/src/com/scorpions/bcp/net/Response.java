package com.scorpions.bcp.net;

import java.util.Map;

public class Response extends SPacket {

	private final ResponseType t;
	
	public Response(ResponseType t, Map<String,Object> v) {
		super(v);
		this.t = t;
	}
	
	public ResponseType getType() {
		return this.t;
	}
	
}
