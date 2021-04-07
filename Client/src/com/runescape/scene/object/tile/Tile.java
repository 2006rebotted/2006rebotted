package com.runescape.scene.object.tile;
import com.runescape.collection.Linkable;
import com.runescape.entity.GroundItemTile;
import com.runescape.scene.object.GroundDecoration;
import com.runescape.scene.object.WallObject;
import com.runescape.scene.object.WallDecoration;
import com.runescape.entity.GameObject;

public final class Tile extends Linkable {
	public Tile(int i, int j, int k){
		gameObjects = new GameObject[5];
        tiledObjectMasks = new int[5];
		anInt1310 = z1AnInt1307 = i;
		anInt1308 = j;
		anInt1309 = k;
	}

	public int z1AnInt1307;
	public final int anInt1308;
	public final int anInt1309;
	public final int anInt1310;
	public SimpleTile mySimpleTile;
	public ShapedTile myShapedTile;
	public WallObject wallObject;
	public WallDecoration wallDecoration;
	public GroundDecoration groundDecoration;
	public GroundItemTile groundItemTile;
	public int gameObjectIndex;
	public final GameObject[] gameObjects;
	public final int[] tiledObjectMasks;
	public int totalTiledObjectMask;
	public int logicHeight;
	public boolean aBoolean1322;
	public boolean aBoolean1323;
	public boolean aBoolean1324;
	public int someTileMask;
	public int anInt1326;
	public int anInt1327;
	public int anInt1328;
	public Tile firstFloorTile;
}
