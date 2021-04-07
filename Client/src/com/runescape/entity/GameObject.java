package com.runescape.entity;

/**
 * ObjectGenre = 2
 */
public final class GameObject {

	public int zLoc;
	public int tileHeight;
	public int xPos;
	public int yPos;
	public Renderable renderable;
	public int turnValue;
	public int xLocLow;
	public int xLocHigh;
	public int yLocHigh;
	public int yLocLow;
	public int anInt527;
	public int anInt528;
	public int uid;
    /**
     * mask = (byte)((objectRotation << 6) + objectType);
     */
	public byte mask;
}
