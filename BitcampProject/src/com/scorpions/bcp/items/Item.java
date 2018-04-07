package com.scorpions.bcp.items;

public class Item {
	private int buyPrice, sellPrice;
	private String name, description;
	
	public Item(String name, int bP, int sP) {
		this.buyPrice = bP;
		this.sellPrice = sP;
		this.name = name;
	}
	
	public int getBuyPrice() {
		return this.buyPrice;
	}
	
	public int getSellPrice() {
		return this.sellPrice;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setDesc(String desc) {
		this.description = desc;
	}
	
	public String getDesc() {
		return this.description;
	}
	
	@Override
	public String toString() {
		return this.name + ": \n" + this.description;
	}
	
}
