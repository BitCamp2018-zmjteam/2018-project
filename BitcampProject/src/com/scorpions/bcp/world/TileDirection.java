package com.scorpions.bcp.world;

import java.io.Serializable;

public enum TileDirection implements Serializable {
	TOP,RIGHT,BOTTOM,LEFT;
	
	public static TileDirection fromString(String str) {
		if(str.equals("north")) {
			return TileDirection.TOP;
		}
		else if(str.equals("south")) {
			return TileDirection.BOTTOM;
		}
		else if(str.equals("east")) {
			return TileDirection.RIGHT;
		}
		else {
			return TileDirection.LEFT;
		}
	}
}
