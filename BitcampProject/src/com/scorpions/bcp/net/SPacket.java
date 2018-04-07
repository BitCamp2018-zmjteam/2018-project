package com.scorpions.bcp.net;

import java.util.Map;

public abstract class SPacket {

	protected Map<String,Object> values;
	
	public SPacket(Map<String,Object> values) {
		this.values = values;
	}
	
	public Map<String,Object> getValues() {
		return this.values;
	}
	
}
