package com.scorpions.bcp.net;

import java.io.Serializable;
import java.util.Map;

public abstract class SPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4611963287033478283L;
	protected Map<String,Object> values;
	
	public SPacket(Map<String,Object> values) {
		this.values = values;
	}
	
	public Map<String,Object> getValues() {
		return this.values;
	}
	
}
