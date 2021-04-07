package com.runescape.scene;
import com.runescape.collection.Deque;
import com.runescape.draw.Rasterizer2D;
import com.runescape.draw.Rasterizer3D;
import com.runescape.entity.GroundItemTile;
import com.runescape.entity.Renderable;
import com.runescape.entity.GameObject;
import com.runescape.entity.model.Model;
import com.runescape.entity.model.VertexNormal;
import com.runescape.scene.object.GroundDecoration;
import com.runescape.scene.object.WallObject;
import com.runescape.scene.object.WallDecoration;
import com.runescape.scene.object.tile.ShapedTile;
import com.runescape.scene.object.tile.SimpleTile;
import com.runescape.scene.object.tile.Tile;

public final class SceneGraph {

	public SceneGraph(int heightMap[][][]) {
		int yLocSize = 104;// was parameter
		int xLocSize = 104;// was parameter
		int zLocSize = 4;// was parameter
        gameObjectsCache = new GameObject[5000];
		anIntArray486 = new int[10000];
		anIntArray487 = new int[10000];
		numberOfZ = zLocSize;
		xRegionSize = xLocSize;
		yRegionSize = yLocSize;
		tileArray = new Tile[zLocSize][xLocSize][yLocSize];
		anIntArrayArrayArray445 = new int[zLocSize][xLocSize + 1][yLocSize + 1];
		this.heightMap = heightMap;
		initToNull();
	}

    /**
     * The class destructor.
     */
	public static void destructor() {
		interactableObjects = null;
		sceneClusterCounts = null;
		sceneClusters = null;
		tileDeque = null;
		aBooleanArrayArrayArrayArray491 = null;
		aBooleanArrayArray492 = null;
	}

	public void initToNull() {
		for (int zLoc = 0; zLoc < numberOfZ; zLoc++)
			for (int xLoc = 0; xLoc < xRegionSize; xLoc++)
				for (int yLoc = 0; yLoc < yRegionSize; yLoc++)
					tileArray[zLoc][xLoc][yLoc] = null;
		for (int plane = 0; plane < cullingClusterPlaneCount; plane++) {
			for (int j1 = 0; j1 < sceneClusterCounts[plane]; j1++)
                sceneClusters[plane][j1] = null;
            sceneClusterCounts[plane] = 0;
		}

		for (int i = 0; i < interactableObjectCacheCurrPos; i++)
			gameObjectsCache[i] = null;
		interactableObjectCacheCurrPos = 0;
		for (int i = 0; i < interactableObjects.length; i++)
            interactableObjects[i] = null;

	}

	public void method275(int zLoc) {
		zAnInt442 = zLoc;
		for (int xLoc = 0; xLoc < xRegionSize; xLoc++) {
			for (int yLoc = 0; yLoc < yRegionSize; yLoc++)
				if (tileArray[zLoc][xLoc][yLoc] == null)
					tileArray[zLoc][xLoc][yLoc] = new Tile(zLoc, xLoc, yLoc);
		}
	}

	public void applyBridgeMode(int yLoc, int xLoc) {
		Tile tileFirstFloor = tileArray[0][xLoc][yLoc];
		for (int zLoc = 0; zLoc < 3; zLoc++) {
			Tile tile = tileArray[zLoc][xLoc][yLoc] = tileArray[zLoc + 1][xLoc][yLoc];
			if (tile != null) {
				tile.z1AnInt1307--;
				for (int j1 = 0; j1 < tile.gameObjectIndex; j1++) {
					GameObject gameObject = tile.gameObjects[j1];
					if ((gameObject.uid >> 29 & 3) == 2 && gameObject.xLocLow == xLoc && gameObject.yLocHigh == yLoc)
						gameObject.zLoc--;
				}
			}
		}
		if (tileArray[0][xLoc][yLoc] == null)
			tileArray[0][xLoc][yLoc] = new Tile(0, xLoc, yLoc);
		tileArray[0][xLoc][yLoc].firstFloorTile = tileFirstFloor;
		tileArray[3][xLoc][yLoc] = null;
	}

	public static void createNewSceneCluster(int z, int lowestX, int lowestZ, int highestX, int highestY, int highestZ, int lowestY, int searchMask) {
		SceneCluster sceneCluster = new SceneCluster();
		sceneCluster.startXLoc = lowestX / 128;
		sceneCluster.endXLoc = highestX / 128;
		sceneCluster.startYLoc = lowestY / 128;
		sceneCluster.endYLoc = highestY / 128;
		sceneCluster.orientation = searchMask;
		sceneCluster.startXPos = lowestX;
		sceneCluster.endXPos = highestX;
		sceneCluster.startYPos = lowestY;
		sceneCluster.endYPos = highestY;
		sceneCluster.startZPos = highestZ;
		sceneCluster.endZPos = lowestZ;
		sceneClusters[z][sceneClusterCounts[z]++] = sceneCluster;
	}

	public void setTileLogicHeight(int zLoc, int xLoc, int yLoc, int logicHeight) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile != null)
			tileArray[zLoc][xLoc][yLoc].logicHeight = logicHeight;
	}

	public void addTile(int zLoc, int xLoc, int yLoc, int shape, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int j4, int k4, int l4) {
		if (shape == 0) {
			SimpleTile simpleTile = new SimpleTile(k2, l2, i3, j3, -1, k4, false);
			for (int lowerZLoc = zLoc; lowerZLoc >= 0; lowerZLoc--)
				if (tileArray[lowerZLoc][xLoc][yLoc] == null)
					tileArray[lowerZLoc][xLoc][yLoc] = new Tile(lowerZLoc, xLoc, yLoc);

			tileArray[zLoc][xLoc][yLoc].mySimpleTile = simpleTile;
		} else if (shape == 1) {
			SimpleTile simpleTile = new SimpleTile(k3, l3, i4, j4, j1, l4, k1 == l1 && k1 == i2 && k1 == j2);
			for (int lowerZLoc = zLoc; lowerZLoc >= 0; lowerZLoc--)
				if (tileArray[lowerZLoc][xLoc][yLoc] == null)
					tileArray[lowerZLoc][xLoc][yLoc] = new Tile(lowerZLoc, xLoc, yLoc);

			tileArray[zLoc][xLoc][yLoc].mySimpleTile = simpleTile;
		} else {
            ShapedTile shapedTile = new ShapedTile(yLoc, k3, j3, i2, j1, i4, i1, k2, k4, i3, j2, l1, k1, shape, j4, l3, l2, xLoc, l4);
            for (int k5 = zLoc; k5 >= 0; k5--)
                if (tileArray[k5][xLoc][yLoc] == null)
                    tileArray[k5][xLoc][yLoc] = new Tile(k5, xLoc, yLoc);

            tileArray[zLoc][xLoc][yLoc].myShapedTile = shapedTile;
        }
	}

	public void addGroundDecoration(int zLoc, int zPos, int yLoc, Renderable renderable, byte objectRotationType, int uid, int xLoc) {
		if (renderable == null)
			return;
		GroundDecoration groundDecoration = new GroundDecoration();
		groundDecoration.renderable = renderable;
		groundDecoration.xPos = xLoc * 128 + 64;
		groundDecoration.yPos = yLoc * 128 + 64;
		groundDecoration.zPos = zPos;
		groundDecoration.uid = uid;
		groundDecoration.mask = objectRotationType;
		if (tileArray[zLoc][xLoc][yLoc] == null)
			tileArray[zLoc][xLoc][yLoc] = new Tile(zLoc, xLoc, yLoc);
		tileArray[zLoc][xLoc][yLoc].groundDecoration = groundDecoration;
	}

	public void addGroundItemTile(int xLoc, int uid, Renderable firstNode, int zPos, Renderable secondNode, Renderable thirdNode, int zLoc, int yLoc) {
		GroundItemTile groundItemTile = new GroundItemTile();
		groundItemTile.topNode = thirdNode;
		groundItemTile.xPos = xLoc * 128 + 64;
		groundItemTile.yPos = yLoc * 128 + 64;
		groundItemTile.zPos = zPos;
		groundItemTile.uid = uid;
		groundItemTile.lowerNode = firstNode;
		groundItemTile.middleNode = secondNode;
		int largestItemDropHeight = 0;
		Tile parentTile = tileArray[zLoc][xLoc][yLoc];
		if (parentTile != null) {
			for (int i = 0; i < parentTile.gameObjectIndex; i++)
				if (parentTile.gameObjects[i].renderable instanceof Model) {
					int objectItemDropHeight = ((Model) parentTile.gameObjects[i].renderable).itemDropHeight;
					if (objectItemDropHeight > largestItemDropHeight)
						largestItemDropHeight = objectItemDropHeight;
				}

		}
		groundItemTile.itemDropHeight = largestItemDropHeight;
		if (tileArray[zLoc][xLoc][yLoc] == null)
			tileArray[zLoc][xLoc][yLoc] = new Tile(zLoc, xLoc, yLoc);
		tileArray[zLoc][xLoc][yLoc].groundItemTile = groundItemTile;
	}

	public void addWallObject(int orientation1, Renderable renderable1, int uid, int yLoc, byte objectFaceType, int xLoc, Renderable renderable2, int zPos, int orientation2, int zLoc) {
		if (renderable1 == null && renderable2 == null)
			return;
		WallObject wallObject = new WallObject();
		wallObject.uid = uid;
		wallObject.mask = objectFaceType;
		wallObject.xPos = xLoc * 128 + 64;
		wallObject.yPos = yLoc * 128 + 64;
		wallObject.zPos = zPos;
		wallObject.renderable1 = renderable1;
		wallObject.renderable2 = renderable2;
		wallObject.orientation1 = orientation1;
		wallObject.orientation2 = orientation2;
		for (int z = zLoc; z >= 0; z--)
			if (tileArray[z][xLoc][yLoc] == null)
				tileArray[z][xLoc][yLoc] = new Tile(z, xLoc, yLoc);

		tileArray[zLoc][xLoc][yLoc].wallObject = wallObject;
	}

	public void addWallDecoration(int uid, int yLoc, int orientation2, int zLoc, int xOffset, int zPos, Renderable renderable, int xLoc, byte objectRotationType, int yOffset, int orientation) {
		if (renderable == null)
			return;
		WallDecoration wallDecoration = new WallDecoration();
		wallDecoration.uid = uid;
		wallDecoration.mask = objectRotationType;
		wallDecoration.xPos = xLoc * 128 + 64 + xOffset;
		wallDecoration.yPos = yLoc * 128 + 64 + yOffset;
		wallDecoration.zPos = zPos;
		wallDecoration.renderable = renderable;
		wallDecoration.orientation = orientation;
		wallDecoration.orientation2 = orientation2;
		for (int z = zLoc; z >= 0; z--)
			if (tileArray[z][xLoc][yLoc] == null)
				tileArray[z][xLoc][yLoc] = new Tile(z, xLoc, yLoc);

		tileArray[zLoc][xLoc][yLoc].wallDecoration = wallDecoration;
	}

	public boolean addTiledObject(int uid, byte objectRotationType, int tileHeight, int sizeY, Renderable renderable, int sizeX, int zLoc, int turnValue, int yLoc, int xLoc) {
		if (renderable == null) {
			return true;
		} else {
			int xPos = xLoc * 128 + 64 * sizeX;
			int yPos = yLoc * 128 + 64 * sizeY;
			return addAnimableC(zLoc, xLoc, yLoc, sizeX, sizeY, xPos, yPos, tileHeight, renderable, turnValue, false, uid, objectRotationType);
		}
	}

	public boolean addAnimableA(int zLoc, int turnValue, int k, int uid, int yPos, int halfSizePos, int xPos, Renderable animable, boolean flag) {
		if (animable == null)
			return true;
		int startXLoc = xPos - halfSizePos;
		int startYLoc = yPos - halfSizePos;
		int endXLoc = xPos + halfSizePos;
		int endYLoc = yPos + halfSizePos;
		if (flag) {
			if (turnValue > 640 && turnValue < 1408)
				endYLoc += 128;
			if (turnValue > 1152 && turnValue < 1920)
				endXLoc += 128;
			if (turnValue > 1664 || turnValue < 384)
				startYLoc -= 128;
			if (turnValue > 128 && turnValue < 896)
				startXLoc -= 128;
		}
        startXLoc /= 128;
        startYLoc /= 128;
		endXLoc /= 128;
        endYLoc /= 128;
		return addAnimableC(zLoc, startXLoc, startYLoc, (endXLoc - startXLoc) + 1, (endYLoc - startYLoc) + 1, xPos, yPos, k, animable, turnValue, true, uid, (byte) 0);
	}

	public boolean addToScenePlayerAsObject(int zLoc, int playerYPos, Renderable playerAsObject, int playerTurnValue, int objectEndYLoc, int playerXPos, int playerHeight, int objectStartXLoc, int objectEndXLoc, int uid, int objectStartYLoc) {
		return playerAsObject == null || addAnimableC(zLoc, objectStartXLoc, objectStartYLoc, (objectEndXLoc - objectStartXLoc) + 1, (objectEndYLoc - objectStartYLoc) + 1, playerXPos, playerYPos, playerHeight, playerAsObject, playerTurnValue, true, uid, (byte) 0);
	}

	private boolean addAnimableC(int zLoc, int xLoc, int yLoc, int sizeX, int sizeY, int xPos, int yPos, int tileHeight, Renderable renderable, int turnValue, boolean isDynamic, int uid, byte objectRotationType) {
		for (int x = xLoc; x < xLoc + sizeX; x++) {
			for (int y = yLoc; y < yLoc + sizeY; y++) {
				if (x < 0 || y < 0 || x >= xRegionSize || y >= yRegionSize)
					return false;
				Tile tile = tileArray[zLoc][x][y];
				if (tile != null && tile.gameObjectIndex >= 5)
					return false;
			}

		}

		GameObject gameObject = new GameObject();
		gameObject.uid = uid;
		gameObject.mask = objectRotationType;
		gameObject.zLoc = zLoc;
		gameObject.xPos = xPos;
		gameObject.yPos = yPos;
		gameObject.tileHeight = tileHeight;
		gameObject.renderable = renderable;
		gameObject.turnValue = turnValue;
		gameObject.xLocLow = xLoc;
		gameObject.yLocHigh = yLoc;
		gameObject.xLocHigh = (xLoc + sizeX) - 1;
		gameObject.yLocLow = (yLoc + sizeY) - 1;
		for (int x = xLoc; x < xLoc + sizeX; x++) {
			for (int y = yLoc; y < yLoc + sizeY; y++) {
				int mask = 0;
				if (x > xLoc)
					mask++;
				if (x < (xLoc + sizeX) - 1)
					mask += 4;
				if (y > yLoc)
					mask += 8;
				if (y < (yLoc + sizeY) - 1)
					mask += 2;
				for (int z = zLoc; z >= 0; z--)
					if (tileArray[z][x][y] == null)
						tileArray[z][x][y] = new Tile(z, x, y);

				Tile tile = tileArray[zLoc][x][y];
				tile.gameObjects[tile.gameObjectIndex] = gameObject;
				tile.tiledObjectMasks[tile.gameObjectIndex] = mask;
				tile.totalTiledObjectMask |= mask;
				tile.gameObjectIndex++;
			}

		}

		if (isDynamic) {
			gameObjectsCache[interactableObjectCacheCurrPos++] = gameObject;
		}
		
		return true;
	}

	public void clearGameObjectCache() {
		for (int i = 0; i < interactableObjectCacheCurrPos; i++) {
			GameObject object5 = gameObjectsCache[i];
			remove(object5);
			gameObjectsCache[i] = null;
		}

		interactableObjectCacheCurrPos = 0;
	}

	private void remove(GameObject gameObject) {
		for (int x = gameObject.xLocLow; x <= gameObject.xLocHigh; x++) {
			for (int y = gameObject.yLocHigh; y <= gameObject.yLocLow; y++) {
				Tile tile = tileArray[gameObject.zLoc][x][y];
				if (tile != null) {
					for (int i = 0; i < tile.gameObjectIndex; i++) {
						if (tile.gameObjects[i] != gameObject)
							continue;
						tile.gameObjectIndex--;
						for (int i1 = i; i1 < tile.gameObjectIndex; i1++) {
							tile.gameObjects[i1] = tile.gameObjects[i1 + 1];
							tile.tiledObjectMasks[i1] = tile.tiledObjectMasks[i1 + 1];
						}

						tile.gameObjects[tile.gameObjectIndex] = null;
						break;
					}

					tile.totalTiledObjectMask = 0;
					for (int i = 0; i < tile.gameObjectIndex; i++)
						tile.totalTiledObjectMask |= tile.tiledObjectMasks[i];
				}
			}
		}
	}

	public void method290(int yLoc, int k, int xLoc, int zLoc) { //TODO scale position?
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return;
		WallDecoration wallDecoration = tile.wallDecoration;
		if (wallDecoration != null) {
			int xPos = xLoc * 128 + 64;
			int yPos = yLoc * 128 + 64;
			wallDecoration.xPos = xPos + ((wallDecoration.xPos - xPos) * k) / 16;
			wallDecoration.yPos = yPos + ((wallDecoration.yPos - yPos) * k) / 16;
		}
	}

	public void removeWallObject(int xLoc, int zLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile != null)
			tile.wallObject = null;
	}

	public void removeWallDecoration(int yLoc, int zLoc, int xLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile != null)
			tile.wallDecoration = null;
	}

	public void removeTiledObject(int zLoc, int xLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return;
		for (int j1 = 0; j1 < tile.gameObjectIndex; j1++) {
			GameObject gameObject = tile.gameObjects[j1];
			if ((gameObject.uid >> 29 & 3) == 2 && gameObject.xLocLow == xLoc && gameObject.yLocHigh == yLoc) {
				remove(gameObject);
				return;
			}
		}

	}

	public void removeGroundDecoration(int zLoc, int yLoc, int xLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return;
		tile.groundDecoration = null;
	}

	public void removeGroundItemTile(int zLoc, int xLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile != null)
			tile.groundItemTile = null;
	}

	public WallObject getWallObject(int zLoc, int xLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return null;
		else
			return tile.wallObject;
	}

	public WallDecoration getWallDecoration(int xLoc, int yLoc, int zLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return null;
		else
			return tile.wallDecoration;
	}

	public GameObject getGameObject(int xLoc, int yLoc, int zLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return null;
		for (int i = 0; i < tile.gameObjectIndex; i++) {
			GameObject gameObject = tile.gameObjects[i];
			if ((gameObject.uid >> 29 & 3) == 2 && gameObject.xLocLow == xLoc && gameObject.yLocHigh == yLoc)
				return gameObject;
		}
		return null;
	}

	public GroundDecoration getGroundDecoration(int yLoc, int xLoc, int zLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null || tile.groundDecoration == null)
			return null;
		else
			return tile.groundDecoration;
	}

	public int getWallObjectUid(int zLoc, int xLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null || tile.wallObject == null)
			return 0;
		else
			return tile.wallObject.uid;
	}

	public int getWallDecorationUid(int zLoc, int xLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null || tile.wallDecoration == null)
			return 0;
		else
			return tile.wallDecoration.uid;
	}

	public int getGameObjectUid(int zLoc, int xLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return 0;
		for (int i = 0; i < tile.gameObjectIndex; i++) {
			GameObject gameObject = tile.gameObjects[i];
			if ((gameObject.uid >> 29 & 3) == 2 && gameObject.xLocLow == xLoc && gameObject.yLocHigh == yLoc)
				return gameObject.uid;
		}
		return 0;
	}

	public int getGroundDecorationUid(int zLoc, int xLoc, int yLoc) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null || tile.groundDecoration == null)
			return 0;
		else
			return tile.groundDecoration.uid;
	}

    /**
     * Retrieves the mask of the object with the given uid at the given location.
     * -1 if there's no object.
     * @param zLoc The zLoc.
     * @param xLoc The xLoc.
     * @param yLoc The yLoc.
     * @param uid The object's Uid.
     * @return The mask, which is comprised out of the rotation (shifted 6 to the left) and the type (which has a maximum value of 22).
     */
	public int getMask(int zLoc, int xLoc, int yLoc, int uid) {
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return -1;
		if (tile.wallObject != null && tile.wallObject.uid == uid)
			return tile.wallObject.mask & 0xff;
		if (tile.wallDecoration != null && tile.wallDecoration.uid == uid)
			return tile.wallDecoration.mask & 0xff;
		if (tile.groundDecoration != null && tile.groundDecoration.uid == uid)
			return tile.groundDecoration.mask & 0xff;
		for (int i = 0; i < tile.gameObjectIndex; i++)
			if (tile.gameObjects[i].uid == uid)
				return tile.gameObjects[i].mask & 0xff;
		return -1;
	}

	public void shadeModels(int lightY, int lightX, int lightZ) {
		int intensity = 64;// was parameter
		int diffusion = 768;// was parameter
		int lightDistance = (int) Math.sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ);
		int someLightQualityVariable = diffusion * lightDistance >> 8;
		for (int zLoc = 0; zLoc < numberOfZ; zLoc++) {
			for (int xLoc = 0; xLoc < xRegionSize; xLoc++) {
				for (int yLoc = 0; yLoc < yRegionSize; yLoc++) {
					Tile tile = tileArray[zLoc][xLoc][yLoc];
					if (tile != null) {
						WallObject wallObject = tile.wallObject;
						if (wallObject != null && wallObject.renderable1 != null && wallObject.renderable1.vertexNormals != null) {
							method307(zLoc, 1, 1, xLoc, yLoc, (Model) wallObject.renderable1);
							if (wallObject.renderable2 != null && wallObject.renderable2.vertexNormals != null) {
								method307(zLoc, 1, 1, xLoc, yLoc, (Model) wallObject.renderable2);
								mergeNormals((Model) wallObject.renderable1, (Model) wallObject.renderable2, 0, 0, 0, false);
								((Model) wallObject.renderable2).doShading(intensity, someLightQualityVariable, lightX, lightY, lightZ);
							}
							((Model) wallObject.renderable1).doShading(intensity, someLightQualityVariable, lightX, lightY, lightZ);
						}
						for (int k2 = 0; k2 < tile.gameObjectIndex; k2++) {
							GameObject interactableObject = tile.gameObjects[k2];
							if (interactableObject != null && interactableObject.renderable != null && interactableObject.renderable.vertexNormals != null) {
								method307(zLoc, (interactableObject.xLocHigh - interactableObject.xLocLow) + 1, (interactableObject.yLocLow - interactableObject.yLocHigh) + 1, xLoc, yLoc, (Model) interactableObject.renderable);
								((Model) interactableObject.renderable).doShading(intensity, someLightQualityVariable, lightX, lightY, lightZ);
							}
						}

						GroundDecoration groundDecoration = tile.groundDecoration;
						if (groundDecoration != null && groundDecoration.renderable.vertexNormals != null) {
							method306GroundDecorationOnly(xLoc, zLoc, (Model) groundDecoration.renderable, yLoc);
							((Model) groundDecoration.renderable).doShading(intensity, someLightQualityVariable, lightX, lightY, lightZ);
						}
					}
				}
			}
		}
	}

	private void method306GroundDecorationOnly(int modelXLoc, int modelZLoc, Model model, int modelYLoc) { //TODO figure it out
		if (modelXLoc < xRegionSize) {
			Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null)
				mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 0, true);
		}
		if (modelYLoc < xRegionSize) {
			Tile tile = tileArray[modelZLoc][modelXLoc][modelYLoc + 1];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null)
				mergeNormals(model, (Model) tile.groundDecoration.renderable, 0, 0, 128, true);
		}
		if (modelXLoc < xRegionSize && modelYLoc < yRegionSize) {
			Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc + 1];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null)
				mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 128, true);
		}
		if (modelXLoc < xRegionSize && modelYLoc > 0) {
			Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc - 1];
			if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.vertexNormals != null)
				mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, -128, true);
		}
	}

	private void method307(int modelZLoc, int modelXSize, int modelYSize, int modelXLoc, int modelYLoc, Model model) {
		boolean flag = true;
		int startX = modelXLoc;
		int stopX = modelXLoc + modelXSize;
		int startY = modelYLoc - 1;
		int stopY = modelYLoc + modelYSize;
		for (int zLoc = modelZLoc; zLoc <= modelZLoc + 1; zLoc++)
			if (zLoc != numberOfZ) {//TODO Always?
				for (int xLoc = startX; xLoc <= stopX; xLoc++)
					if (xLoc >= 0 && xLoc < xRegionSize) {
						for (int yLoc = startY; yLoc <= stopY; yLoc++)
							if (yLoc >= 0 && yLoc < yRegionSize && (!flag || xLoc >= stopX || yLoc >= stopY || yLoc < modelYLoc && xLoc != modelXLoc)) {
								Tile tile = tileArray[zLoc][xLoc][yLoc];
								if (tile != null) {
									int relativeHeightToModelTile = (heightMap[zLoc][xLoc][yLoc] + heightMap[zLoc][xLoc + 1][yLoc] + heightMap[zLoc][xLoc][yLoc + 1] + heightMap[zLoc][xLoc + 1][yLoc + 1]) / 4 - (heightMap[modelZLoc][modelXLoc][modelYLoc] + heightMap[modelZLoc][modelXLoc + 1][modelYLoc] + heightMap[modelZLoc][modelXLoc][modelYLoc + 1] + heightMap[modelZLoc][modelXLoc + 1][modelYLoc + 1]) / 4;
									WallObject wallObject = tile.wallObject;
									if (wallObject != null && wallObject.renderable1 != null && wallObject.renderable1.vertexNormals != null)
										mergeNormals(model, (Model) wallObject.renderable1, (xLoc - modelXLoc) * 128 + (1 - modelXSize) * 64, relativeHeightToModelTile, (yLoc - modelYLoc) * 128 + (1 - modelYSize) * 64, flag);
									if (wallObject != null && wallObject.renderable2 != null && wallObject.renderable2.vertexNormals != null)
										mergeNormals(model, (Model) wallObject.renderable2, (xLoc - modelXLoc) * 128 + (1 - modelXSize) * 64, relativeHeightToModelTile, (yLoc - modelYLoc) * 128 + (1 - modelYSize) * 64, flag);
									for (int i = 0; i < tile.gameObjectIndex; i++) {
										GameObject gameObject = tile.gameObjects[i];
										if (gameObject != null && gameObject.renderable != null && gameObject.renderable.vertexNormals != null) {
											int tiledObjectXSize = (gameObject.xLocHigh - gameObject.xLocLow) + 1;
											int tiledObjectYSize = (gameObject.yLocLow - gameObject.yLocHigh) + 1;
											mergeNormals(model, (Model) gameObject.renderable, (gameObject.xLocLow - modelXLoc) * 128 + (tiledObjectXSize - modelXSize) * 64, relativeHeightToModelTile, (gameObject.yLocHigh - modelYLoc) * 128 + (tiledObjectYSize - modelYSize) * 64, flag);
										}
									}
								}
							}
					}
				startX--; //TODO why?
				flag = false;
			}

	}

	private void mergeNormals(Model model1, Model model2, int offsetX, int offsetY, int offsetZ, boolean flag) {
		anInt488++;
		int count = 0;
		int second[] = model2.vertexX;
		int secondVertices = model2.numVertices;
		for (int model1Vertex = 0; model1Vertex < model1.numVertices; model1Vertex++) {
            VertexNormal vertexNormal1 = model1.vertexNormals[model1Vertex];
			VertexNormal alsoVertexNormal1 = model1.alsoVertexNormals[model1Vertex];
			if (alsoVertexNormal1.magnitude != 0) {
				int dY = model1.vertexY[model1Vertex] - offsetY;
				if (dY <= model2.maximumYVertex) {
					int dX = model1.vertexX[model1Vertex] - offsetX;
					if (dX >= model2.minimumXVertex && dX <= model2.maximumXVertex) {
						int k2 = model1.vertexZ[model1Vertex] - offsetZ;
						if (k2 >= model2.minimumZVertex && k2 <= model2.maximumZVertex) {
							for (int l2 = 0; l2 < secondVertices; l2++) {
								VertexNormal vertexNormal2 = model2.vertexNormals[l2];
								VertexNormal alsoVertexNormal2 = model2.alsoVertexNormals[l2];
								if (dX == second[l2] && k2 == model2.vertexZ[l2] && dY == model2.vertexY[l2] && alsoVertexNormal2.magnitude != 0) {
									vertexNormal1.normalX += alsoVertexNormal2.normalX;
									vertexNormal1.normalY += alsoVertexNormal2.normalY;
									vertexNormal1.normalZ += alsoVertexNormal2.normalZ;
									vertexNormal1.magnitude += alsoVertexNormal2.magnitude;
									vertexNormal2.normalX += alsoVertexNormal1.normalX;
									vertexNormal2.normalY += alsoVertexNormal1.normalY;
									vertexNormal2.normalZ += alsoVertexNormal1.normalZ;
									vertexNormal2.magnitude += alsoVertexNormal1.magnitude;
									count++;
									anIntArray486[model1Vertex] = anInt488;
									anIntArray487[l2] = anInt488;
								}
							}

						}
					}
				}
			}
		}

		if (count < 3 || !flag)
			return;
		for (int k1 = 0; k1 < model1.numTriangles; k1++)
			if (anIntArray486[model1.facePointA[k1]] == anInt488 && anIntArray486[model1.facePointB[k1]] == anInt488 && anIntArray486[model1.facePointC[k1]] == anInt488)
				model1.faceDrawType[k1] = -1;

		for (int l1 = 0; l1 < model2.numTriangles; l1++)
			if (anIntArray487[model2.facePointA[l1]] == anInt488 && anIntArray487[model2.facePointB[l1]] == anInt488 && anIntArray487[model2.facePointC[l1]] == anInt488)
				model2.faceDrawType[l1] = -1;

	}

	public void drawTileOnMinimapSprite(int pixels[], int drawIndex, int zLoc, int xLoc, int yLoc) {
		int leftOverWidth = 512;// was parameter
		Tile tile = tileArray[zLoc][xLoc][yLoc];
		if (tile == null)
			return;
		SimpleTile simpleTile = tile.mySimpleTile;
		if (simpleTile != null) {
			int tileRGB = simpleTile.getColourRGB();
			if (tileRGB == 0)
				return;
			for (int i = 0; i < 4; i++) {
				pixels[drawIndex] = tileRGB;
				pixels[drawIndex + 1] = tileRGB;
				pixels[drawIndex + 2] = tileRGB;
				pixels[drawIndex + 3] = tileRGB;
				drawIndex += leftOverWidth;
			}
			return;
		}
		ShapedTile shapedTile = tile.myShapedTile;
		
		if (shapedTile == null) {
			return;
		}
		
		int shape = shapedTile.shape;
		int rotation = shapedTile.rotation;
		int underlayRGB = shapedTile.colourRGB;
		int overlayRGB = shapedTile.colourRGBA;
		int shapePoints[] = tileVertices[shape];
		int shapePointIndices[] = tileVertexIndices[rotation];
		int shapePtr = 0;
		if (underlayRGB != 0) {
			for (int i = 0; i < 4; i++) {
				pixels[drawIndex] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				pixels[drawIndex + 1] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				pixels[drawIndex + 2] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				pixels[drawIndex + 3] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
				drawIndex += leftOverWidth;
			}
			return;
		}
		for (int i = 0; i < 4; i++) {
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex] = overlayRGB;
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex + 1] = overlayRGB;
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex + 2] = overlayRGB;
			if (shapePoints[shapePointIndices[shapePtr++]] != 0)
				pixels[drawIndex + 3] = overlayRGB;
			drawIndex += leftOverWidth;
		}
	}

	public static void setupViewport(int i, int j, int viewportWidth, int viewportHeight, int ai[]) {
		anInt495 = 0;
		anInt496 = 0;
		SceneGraph.viewportWidth = viewportWidth;
        SceneGraph.viewportHeight = viewportHeight;
		viewportHalfWidth = viewportWidth / 2;
		viewportHalfHeight = viewportHeight / 2;
		boolean aflag[][][][] = new boolean[9][32][53][53];
		for (int zAngle = 128; zAngle <= 384; zAngle += 32) {
			for (int xyAngle = 0; xyAngle < 2048; xyAngle += 64) {
                camUpDownY = Model.SINE[zAngle];
                camUpDownX = Model.COSINE[zAngle];
                camLeftRightY = Model.SINE[xyAngle];
                camLeftRightX = Model.COSINE[xyAngle];
				int angularZSegment = (zAngle - 128) / 32;
				int angularXYSegment = xyAngle / 64;
				for (int xRelativeToCamera = -26; xRelativeToCamera <= 26; xRelativeToCamera++) {
					for (int yRelativeToCamera = -26; yRelativeToCamera <= 26; yRelativeToCamera++) {
						int xRelativeToCameraPos = xRelativeToCamera * 128;
						int yRelativeToCameraPos = yRelativeToCamera * 128;
						boolean flag2 = false;
						for (int k4 = -i; k4 <= j; k4 += 128) {
							if (!method311(ai[angularZSegment] + k4, yRelativeToCameraPos, xRelativeToCameraPos))
								continue;
							flag2 = true;
							break;
						}
						aflag[angularZSegment][angularXYSegment][xRelativeToCamera + 25 + 1][yRelativeToCamera + 25 + 1] = flag2;
					}
				}
			}
		}

		for (int angularZSegment = 0; angularZSegment < 8; angularZSegment++) {
			for (int angularXYSegment = 0; angularXYSegment < 32; angularXYSegment++) {
				for (int xRelativeToCamera = -25; xRelativeToCamera < 25; xRelativeToCamera++) {
					for (int yRelativeToCamera = -25; yRelativeToCamera < 25; yRelativeToCamera++) {
						boolean flag1 = false;
						label0: for (int l3 = -1; l3 <= 1; l3++) {
							for (int j4 = -1; j4 <= 1; j4++) {
								if (aflag[angularZSegment][angularXYSegment][xRelativeToCamera + l3 + 25 + 1][yRelativeToCamera + j4 + 25 + 1])
									flag1 = true;
								else if (aflag[angularZSegment][(angularXYSegment + 1) % 31][xRelativeToCamera + l3 + 25 + 1][yRelativeToCamera + j4 + 25 + 1])
									flag1 = true;
								else if (aflag[angularZSegment + 1][angularXYSegment][xRelativeToCamera + l3 + 25 + 1][yRelativeToCamera + j4 + 25 + 1]) {
									flag1 = true;
								} else {
									if (!aflag[angularZSegment + 1][(angularXYSegment + 1) % 31][xRelativeToCamera + l3 + 25 + 1][yRelativeToCamera + j4 + 25 + 1])
										continue;
									flag1 = true;
								}
								break label0;
							}
						}
						aBooleanArrayArrayArrayArray491[angularZSegment][angularXYSegment][xRelativeToCamera + 25][yRelativeToCamera + 25] = flag1;
					}
				}
			}
		}
	}

	private static boolean method311(int i, int j, int k) {
		int l = j * camLeftRightY + k * camLeftRightX >> 16;
		int i1 = j * camLeftRightX - k * camLeftRightY >> 16;
		int j1 = i * camUpDownY + i1 * camUpDownX >> 16;
		int k1 = i * camUpDownX - i1 * camUpDownY >> 16;
		if (j1 < 50 || j1 > 3500)
			return false;
		int l1 = viewportHalfWidth + (l << viewDistance) / j1;
		int i2 = viewportHalfHeight + (k1 << viewDistance) / j1;
		return l1 >= anInt495 && l1 <= viewportWidth && i2 >= anInt496 && i2 <= viewportHeight;
	}

    /**
     * Clicks on the screen and requests recomputation of the clicked tile.
     * @param clickY The click's Y-coordinate on the applet.
     * @param clickX The click's X-coordinate on the applet.
     */
	public void clickTile(int clickY, int clickX) {
		clicked = true;
		clickScreenX = clickX;
		clickScreenY = clickY;
		clickedTileX = -1;
		clickedTileY = -1;
	}

    /**
     * Renders the terrain.
     * The coordinates use the WorldCoordinate Axes but the modelWorld coordinates.
     * @param cameraXPos The cameraViewpoint's X-coordinate.
     * @param cameraYPos The cameraViewpoint's Y-coordinate.
     * @param camAngleXY The cameraAngle in the XY-plain.
     * @param cameraZPos The cameraViewpoint's X-coordinate.
     * @param planeZ The plain the camera's looking at.
     * @param camAngleZ  The cameraAngle on the Z-axis.
     */
	public void render(int cameraXPos, int cameraYPos, int camAngleXY, int cameraZPos, int planeZ, int camAngleZ) {
		if (cameraXPos < 0)
            cameraXPos = 0;
		else if (cameraXPos >= xRegionSize * 128)
			cameraXPos = xRegionSize * 128 - 1;
		if (cameraYPos < 0)
			cameraYPos = 0;
		else if (cameraYPos >= yRegionSize * 128)
			cameraYPos = yRegionSize * 128 - 1;
		anInt448++;
		camUpDownY = Model.SINE[camAngleZ];
		camUpDownX = Model.COSINE[camAngleZ];
		camLeftRightY = Model.SINE[camAngleXY];
		camLeftRightX = Model.COSINE[camAngleXY];
		aBooleanArrayArray492 = aBooleanArrayArrayArrayArray491[(camAngleZ - 128) / 32][camAngleXY / 64];
		xCameraPos = cameraXPos;
		zCameraPos = cameraZPos;
		yCameraPos = cameraYPos;
		xCameraTile = cameraXPos / 128;
		yCameraTile = cameraYPos / 128;
		currentRenderPlane = planeZ;
		cameraLowTileX = xCameraTile - 25;
		if (cameraLowTileX < 0)
            cameraLowTileX = 0;
        cameraLowTileY = yCameraTile - 25;
		if (cameraLowTileY < 0)
			cameraLowTileY = 0;
        cameraHighTileX = xCameraTile + 25;
		if (cameraHighTileX > xRegionSize)
			cameraHighTileX = xRegionSize;
        cameraHighTileY = yCameraTile + 25;
		if (cameraHighTileY > yRegionSize)
			cameraHighTileY = yRegionSize;
		method319();
		anInt446 = 0;
		for (int zLoc = zAnInt442; zLoc < numberOfZ; zLoc++) {
			Tile planeTiles[][] = tileArray[zLoc];
			for (int xLoc = cameraLowTileX; xLoc < cameraHighTileX; xLoc++) {
				for (int yLoc = cameraLowTileY; yLoc < cameraHighTileY; yLoc++) {
					Tile tile = planeTiles[xLoc][yLoc];
					if (tile != null)
						if (tile.logicHeight > planeZ || !aBooleanArrayArray492[(xLoc - xCameraTile) + 25][(yLoc - yCameraTile) + 25] && heightMap[zLoc][xLoc][yLoc] - cameraZPos < 2000) {
							tile.aBoolean1322 = false;
							tile.aBoolean1323 = false;
							tile.someTileMask = 0;
						} else {
							tile.aBoolean1322 = true;
							tile.aBoolean1323 = true;
							tile.aBoolean1324 = tile.gameObjectIndex > 0;
							anInt446++;
						}
				}
			}
		}

		for (int zLoc = zAnInt442; zLoc < numberOfZ; zLoc++) {
			Tile plane[][] = tileArray[zLoc];
			for (int dX = -25; dX <= 0; dX++) {
				int xLoc1 = xCameraTile + dX;
				int xLoc2 = xCameraTile - dX;
				if (xLoc1 >= cameraLowTileX || xLoc2 < cameraHighTileX) {
					for (int dY = -25; dY <= 0; dY++) {
						int yLoc1 = yCameraTile + dY;
						int yLoc2 = yCameraTile - dY;
						if (xLoc1 >= cameraLowTileX) {
							if (yLoc1 >= cameraLowTileY) {
								Tile tile = plane[xLoc1][yLoc1];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, true);
							}
							if (yLoc2 < cameraHighTileY) {
								Tile tile = plane[xLoc1][yLoc2];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, true);
							}
						}
						if (xLoc2 < cameraHighTileX) {
							if (yLoc1 >= cameraLowTileY) {
								Tile tile = plane[xLoc2][yLoc1];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, true);
							}
							if (yLoc2 < cameraHighTileY) {
								Tile tile = plane[xLoc2][yLoc2];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, true);
							}
						}
						if (anInt446 == 0) {
							clicked = false;
							return;
						}
					}
				}
			}
		}

		for (int zLoc = zAnInt442; zLoc < numberOfZ; zLoc++) {
			Tile plane[][] = tileArray[zLoc];
			for (int dX = -25; dX <= 0; dX++) {
				int xLoc1 = xCameraTile + dX;
				int xLoc2 = xCameraTile - dX;
				if (xLoc1 >= cameraLowTileX || xLoc2 < cameraHighTileX) {
					for (int dY = -25; dY <= 0; dY++) {
						int yLoc1 = yCameraTile + dY;
						int yLoc2 = yCameraTile - dY;
						if (xLoc1 >= cameraLowTileX) {
							if (yLoc1 >= cameraLowTileY) {
								Tile tile = plane[xLoc1][yLoc1];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, false);
							}
							if (yLoc2 < cameraHighTileY) {
								Tile tile = plane[xLoc1][yLoc2];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, false);
							}
						}
						if (xLoc2 < cameraHighTileX) {
							if (yLoc1 >= cameraLowTileY) {
								Tile tile = plane[xLoc2][yLoc1];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, false);
							}
							if (yLoc2 < cameraHighTileY) {
								Tile tile = plane[xLoc2][yLoc2];
								if (tile != null && tile.aBoolean1322)
									renderTileF(tile, false);
							}
						}
						if (anInt446 == 0) {
							clicked = false;
							return;
						}
					}

				}
			}

		}

		clicked = false;
	}

	private void renderTileF(Tile tile, boolean flag) {
		tileDeque.insertHead(tile);
		do {
			Tile currentTile;
			do {
				currentTile = (Tile) tileDeque.popHead();
				if (currentTile == null)
					return;
			} while (!currentTile.aBoolean1323);
			int i = currentTile.anInt1308;
			int j = currentTile.anInt1309;
			int k = currentTile.z1AnInt1307;
			int l = currentTile.anInt1310;
			Tile aclass30_sub3[][] = tileArray[k];
			if (currentTile.aBoolean1322) {
				if (flag) {
					if (k > 0) {
						Tile class30_sub3_2 = tileArray[k - 1][i][j];
						if (class30_sub3_2 != null && class30_sub3_2.aBoolean1323)
							continue;
					}
					if (i <= xCameraTile && i > cameraLowTileX) {
						Tile class30_sub3_3 = aclass30_sub3[i - 1][j];
						if (class30_sub3_3 != null && class30_sub3_3.aBoolean1323
								&& (class30_sub3_3.aBoolean1322 || (currentTile.totalTiledObjectMask & 1) == 0))
							continue;
					}
					if (i >= xCameraTile && i < cameraHighTileX - 1) {
						Tile class30_sub3_4 = aclass30_sub3[i + 1][j];
						if (class30_sub3_4 != null && class30_sub3_4.aBoolean1323
								&& (class30_sub3_4.aBoolean1322 || (currentTile.totalTiledObjectMask & 4) == 0))
							continue;
					}
					if (j <= yCameraTile && j > cameraLowTileY) {
						Tile class30_sub3_5 = aclass30_sub3[i][j - 1];
						if (class30_sub3_5 != null && class30_sub3_5.aBoolean1323
								&& (class30_sub3_5.aBoolean1322 || (currentTile.totalTiledObjectMask & 8) == 0))
							continue;
					}
					if (j >= yCameraTile && j < cameraHighTileY - 1) {
						Tile class30_sub3_6 = aclass30_sub3[i][j + 1];
						if (class30_sub3_6 != null && class30_sub3_6.aBoolean1323
								&& (class30_sub3_6.aBoolean1322 || (currentTile.totalTiledObjectMask & 2) == 0))
							continue;
					}
				} else {
					flag = true;
				}
				currentTile.aBoolean1322 = false;
				if (currentTile.firstFloorTile != null) {
					Tile class30_sub3_7 = currentTile.firstFloorTile;
					if (class30_sub3_7.mySimpleTile != null) {
						if (!method320(0, i, j))
							method315(class30_sub3_7.mySimpleTile, 0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i, j);
					} else if (class30_sub3_7.myShapedTile != null && !method320(0, i, j))
						method316(i, camUpDownY, camLeftRightY,
								class30_sub3_7.myShapedTile, camUpDownX, j,
                                camLeftRightX);
					WallObject class10 = class30_sub3_7.wallObject;
					if (class10 != null)
						class10.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
								class10.xPos - xCameraPos, class10.zPos - zCameraPos, class10.yPos - yCameraPos,
								class10.uid);
					for (int i2 = 0; i2 < class30_sub3_7.gameObjectIndex; i2++) {
						GameObject class28 = class30_sub3_7.gameObjects[i2];
						if (class28 != null)
							class28.renderable.renderAtPoint(class28.turnValue, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, class28.xPos - xCameraPos, class28.tileHeight - zCameraPos, class28.yPos - yCameraPos, class28.uid);
					}

				}
				boolean flag1 = false;
				if (currentTile.mySimpleTile != null) {
					if (!method320(l, i, j)) {
						flag1 = true;
						method315(currentTile.mySimpleTile, l, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i, j);
					}
				} else if (currentTile.myShapedTile != null && !method320(l, i, j)) {
					flag1 = true;
					method316(i, camUpDownY, camLeftRightY,
							currentTile.myShapedTile, camUpDownX, j, camLeftRightX);
				}
				int j1 = 0;
				int j2 = 0;
				WallObject class10_3 = currentTile.wallObject;
				WallDecoration class26_1 = currentTile.wallDecoration;
				if (class10_3 != null || class26_1 != null) {
					if (xCameraTile == i)
						j1++;
					else if (xCameraTile < i)
						j1 += 2;
					if (yCameraTile == j)
						j1 += 3;
					else if (yCameraTile > j)
						j1 += 6;
					j2 = anIntArray478[j1];
					currentTile.anInt1328 = anIntArray480[j1];
				}
				if (class10_3 != null) {
					if ((class10_3.orientation1 & anIntArray479[j1]) != 0) {
						if (class10_3.orientation1 == 16) {
							currentTile.someTileMask = 3;
							currentTile.anInt1326 = anIntArray481[j1];
							currentTile.anInt1327 = 3 - currentTile.anInt1326;
						} else if (class10_3.orientation1 == 32) {
							currentTile.someTileMask = 6;
							currentTile.anInt1326 = anIntArray482[j1];
							currentTile.anInt1327 = 6 - currentTile.anInt1326;
						} else if (class10_3.orientation1 == 64) {
							currentTile.someTileMask = 12;
							currentTile.anInt1326 = anIntArray483[j1];
							currentTile.anInt1327 = 12 - currentTile.anInt1326;
						} else {
							currentTile.someTileMask = 9;
							currentTile.anInt1326 = anIntArray484[j1];
							currentTile.anInt1327 = 9 - currentTile.anInt1326;
						}
					} else {
						currentTile.someTileMask = 0;
					}
					if ((class10_3.orientation1 & j2) != 0 && !method321(l, i, j, class10_3.orientation1))
						class10_3.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
								class10_3.xPos - xCameraPos, class10_3.zPos - zCameraPos,
								class10_3.yPos - yCameraPos, class10_3.uid);
					if ((class10_3.orientation2 & j2) != 0 && !method321(l, i, j, class10_3.orientation2))
						class10_3.renderable2.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
								class10_3.xPos - xCameraPos, class10_3.zPos - zCameraPos,
								class10_3.yPos - yCameraPos, class10_3.uid);
				}
				if (class26_1 != null && !method322(l, i, j, class26_1.renderable.modelBaseY))
					if ((class26_1.orientation & j2) != 0)
						class26_1.renderable.renderAtPoint(class26_1.orientation2, camUpDownY, camUpDownX, camLeftRightY,
                                camLeftRightX, class26_1.xPos - xCameraPos, class26_1.zPos - zCameraPos,
								class26_1.yPos - yCameraPos, class26_1.uid);
					else if ((class26_1.orientation & 0x300) != 0) {
						int j4 = class26_1.xPos - xCameraPos;
						int l5 = class26_1.zPos - zCameraPos;
						int k6 = class26_1.yPos - yCameraPos;
						int i8 = class26_1.orientation2;
						int k9;
						if (i8 == 1 || i8 == 2)
							k9 = -j4;
						else
							k9 = j4;
						int k10;
						if (i8 == 2 || i8 == 3)
							k10 = -k6;
						else
							k10 = k6;
						if ((class26_1.orientation & 0x100) != 0 && k10 < k9) {
							int i11 = j4 + anIntArray463[i8];
							int k11 = k6 + anIntArray464[i8];
							class26_1.renderable.renderAtPoint(i8 * 512 + 256, camUpDownY, camUpDownX, camLeftRightY,
                                    camLeftRightX, i11, l5, k11, class26_1.uid);
						}
						if ((class26_1.orientation & 0x200) != 0 && k10 > k9) {
							int j11 = j4 + anIntArray465[i8];
							int l11 = k6 + anIntArray466[i8];
							class26_1.renderable.renderAtPoint(i8 * 512 + 1280 & 0x7ff, camUpDownY, camUpDownX,
                                    camLeftRightY, camLeftRightX, j11, l5, l11, class26_1.uid);
						}
					}
				if (flag1) {
					GroundDecoration class49 = currentTile.groundDecoration;
					if (class49 != null)
						class49.renderable.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
								class49.xPos - xCameraPos, class49.zPos - zCameraPos, class49.yPos - yCameraPos,
								class49.uid);
					GroundItemTile object4_1 = currentTile.groundItemTile;
					if (object4_1 != null && object4_1.itemDropHeight == 0) {
						if (object4_1.lowerNode != null)
							object4_1.lowerNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
									object4_1.xPos - xCameraPos, object4_1.zPos - zCameraPos,
									object4_1.yPos - yCameraPos, object4_1.uid);
						if (object4_1.middleNode != null)
							object4_1.middleNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
									object4_1.xPos - xCameraPos, object4_1.zPos - zCameraPos,
									object4_1.yPos - yCameraPos, object4_1.uid);
						if (object4_1.topNode != null)
							object4_1.topNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
									object4_1.xPos - xCameraPos, object4_1.zPos - zCameraPos,
									object4_1.yPos - yCameraPos, object4_1.uid);
					}
				}
				int k4 = currentTile.totalTiledObjectMask;
				if (k4 != 0) {
					if (i < xCameraTile && (k4 & 4) != 0) {
						Tile class30_sub3_17 = aclass30_sub3[i + 1][j];
						if (class30_sub3_17 != null && class30_sub3_17.aBoolean1323)
							tileDeque.insertHead(class30_sub3_17);
					}
					if (j < yCameraTile && (k4 & 2) != 0) {
						Tile class30_sub3_18 = aclass30_sub3[i][j + 1];
						if (class30_sub3_18 != null && class30_sub3_18.aBoolean1323)
							tileDeque.insertHead(class30_sub3_18);
					}
					if (i > xCameraTile && (k4 & 1) != 0) {
						Tile class30_sub3_19 = aclass30_sub3[i - 1][j];
						if (class30_sub3_19 != null && class30_sub3_19.aBoolean1323)
							tileDeque.insertHead(class30_sub3_19);
					}
					if (j > yCameraTile && (k4 & 8) != 0) {
						Tile class30_sub3_20 = aclass30_sub3[i][j - 1];
						if (class30_sub3_20 != null && class30_sub3_20.aBoolean1323)
							tileDeque.insertHead(class30_sub3_20);
					}
				}
			}
			if (currentTile.someTileMask != 0) {
				boolean flag2 = true;
				for (int k1 = 0; k1 < currentTile.gameObjectIndex; k1++) {
					if (currentTile.gameObjects[k1].anInt528 == anInt448 || (currentTile.tiledObjectMasks[k1]
							& currentTile.someTileMask) != currentTile.anInt1326)
						continue;
					flag2 = false;
					break;
				}

				if (flag2) {
					WallObject class10_1 = currentTile.wallObject;
					if (!method321(l, i, j, class10_1.orientation1))
						class10_1.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
								class10_1.xPos - xCameraPos, class10_1.zPos - zCameraPos,
								class10_1.yPos - yCameraPos, class10_1.uid);
					currentTile.someTileMask = 0;
				}
			}
			if (currentTile.aBoolean1324)
				try {
					int i1 = currentTile.gameObjectIndex;
					currentTile.aBoolean1324 = false;
					int l1 = 0;
					label0: for (int k2 = 0; k2 < i1; k2++) {
						GameObject class28_1 = currentTile.gameObjects[k2];
						if (class28_1.anInt528 == anInt448)
							continue;
						for (int k3 = class28_1.xLocLow; k3 <= class28_1.xLocHigh; k3++) {
							for (int l4 = class28_1.yLocHigh; l4 <= class28_1.yLocLow; l4++) {
								Tile class30_sub3_21 = aclass30_sub3[k3][l4];
								if (class30_sub3_21.aBoolean1322) {
									currentTile.aBoolean1324 = true;
								} else {
									if (class30_sub3_21.someTileMask == 0)
										continue;
									int l6 = 0;
									if (k3 > class28_1.xLocLow)
										l6++;
									if (k3 < class28_1.xLocHigh)
										l6 += 4;
									if (l4 > class28_1.yLocHigh)
										l6 += 8;
									if (l4 < class28_1.yLocLow)
										l6 += 2;
									if ((l6 & class30_sub3_21.someTileMask) != currentTile.anInt1327)
										continue;
									currentTile.aBoolean1324 = true;
								}
								continue label0;
							}

						}

						interactableObjects[l1++] = class28_1;
						int i5 = xCameraTile - class28_1.xLocLow;
						int i6 = class28_1.xLocHigh - xCameraTile;
						if (i6 > i5)
							i5 = i6;
						int i7 = yCameraTile - class28_1.yLocHigh;
						int j8 = class28_1.yLocLow - yCameraTile;
						if (j8 > i7)
							class28_1.anInt527 = i5 + j8;
						else
							class28_1.anInt527 = i5 + i7;
					}

					while (l1 > 0) {
						int i3 = -50;
						int l3 = -1;
						for (int j5 = 0; j5 < l1; j5++) {
							GameObject class28_2 = interactableObjects[j5];
							if (class28_2.anInt528 != anInt448)
								if (class28_2.anInt527 > i3) {
									i3 = class28_2.anInt527;
									l3 = j5;
								} else if (class28_2.anInt527 == i3) {
									int j7 = class28_2.xPos - xCameraPos;
									int k8 = class28_2.yPos - yCameraPos;
									int l9 = interactableObjects[l3].xPos - xCameraPos;
									int l10 = interactableObjects[l3].yPos - yCameraPos;
									if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10)
										l3 = j5;
								}
						}

						if (l3 == -1)
							break;
						GameObject class28_3 = interactableObjects[l3];
						class28_3.anInt528 = anInt448;
						if (!method323(l, class28_3.xLocLow, class28_3.xLocHigh, class28_3.yLocHigh,
								class28_3.yLocLow, class28_3.renderable.modelBaseY))
							class28_3.renderable.renderAtPoint(class28_3.turnValue, camUpDownY, camUpDownX, camLeftRightY,
                                    camLeftRightX, class28_3.xPos - xCameraPos, class28_3.tileHeight - zCameraPos,
									class28_3.yPos - yCameraPos, class28_3.uid);
						for (int k7 = class28_3.xLocLow; k7 <= class28_3.xLocHigh; k7++) {
							for (int l8 = class28_3.yLocHigh; l8 <= class28_3.yLocLow; l8++) {
								Tile class30_sub3_22 = aclass30_sub3[k7][l8];
								if (class30_sub3_22.someTileMask != 0)
									tileDeque.insertHead(class30_sub3_22);
								else if ((k7 != i || l8 != j) && class30_sub3_22.aBoolean1323)
									tileDeque.insertHead(class30_sub3_22);
							}

						}

					}
					if (currentTile.aBoolean1324)
						continue;
				} catch (Exception _ex) {
					currentTile.aBoolean1324 = false;
				}
			if (!currentTile.aBoolean1323 || currentTile.someTileMask != 0)
				continue;
			if (i <= xCameraTile && i > cameraLowTileX) {
				Tile class30_sub3_8 = aclass30_sub3[i - 1][j];
				if (class30_sub3_8 != null && class30_sub3_8.aBoolean1323)
					continue;
			}
			if (i >= xCameraTile && i < cameraHighTileX - 1) {
				Tile class30_sub3_9 = aclass30_sub3[i + 1][j];
				if (class30_sub3_9 != null && class30_sub3_9.aBoolean1323)
					continue;
			}
			if (j <= yCameraTile && j > cameraLowTileY) {
				Tile class30_sub3_10 = aclass30_sub3[i][j - 1];
				if (class30_sub3_10 != null && class30_sub3_10.aBoolean1323)
					continue;
			}
			if (j >= yCameraTile && j < cameraHighTileY - 1) {
				Tile class30_sub3_11 = aclass30_sub3[i][j + 1];
				if (class30_sub3_11 != null && class30_sub3_11.aBoolean1323)
					continue;
			}
			currentTile.aBoolean1323 = false;
			anInt446--;
			GroundItemTile object4 = currentTile.groundItemTile;
			if (object4 != null && object4.itemDropHeight != 0) {
				if (object4.lowerNode != null)
					object4.lowerNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
							object4.xPos - xCameraPos, object4.zPos - zCameraPos - object4.itemDropHeight,
							object4.yPos - yCameraPos, object4.uid);
				if (object4.middleNode != null)
					object4.middleNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
							object4.xPos - xCameraPos, object4.zPos - zCameraPos - object4.itemDropHeight,
							object4.yPos - yCameraPos, object4.uid);
				if (object4.topNode != null)
					object4.topNode.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
							object4.xPos - xCameraPos, object4.zPos - zCameraPos - object4.itemDropHeight,
							object4.yPos - yCameraPos, object4.uid);
			}
			if (currentTile.anInt1328 != 0) {
				WallDecoration class26 = currentTile.wallDecoration;
				if (class26 != null && !method322(l, i, j, class26.renderable.modelBaseY))
					if ((class26.orientation & currentTile.anInt1328) != 0)
						class26.renderable.renderAtPoint(class26.orientation2, camUpDownY, camUpDownX, camLeftRightY,
                                camLeftRightX, class26.xPos - xCameraPos, class26.zPos - zCameraPos,
								class26.yPos - yCameraPos, class26.uid);
					else if ((class26.orientation & 0x300) != 0) {
						int l2 = class26.xPos - xCameraPos;
						int j3 = class26.zPos - zCameraPos;
						int i4 = class26.yPos - yCameraPos;
						int k5 = class26.orientation2;
						int j6;
						if (k5 == 1 || k5 == 2)
							j6 = -l2;
						else
							j6 = l2;
						int l7;
						if (k5 == 2 || k5 == 3)
							l7 = -i4;
						else
							l7 = i4;
						if ((class26.orientation & 0x100) != 0 && l7 >= j6) {
							int i9 = l2 + anIntArray463[k5];
							int i10 = i4 + anIntArray464[k5];
							class26.renderable.renderAtPoint(k5 * 512 + 256, camUpDownY, camUpDownX, camLeftRightY,
                                    camLeftRightX, i9, j3, i10, class26.uid);
						}
						if ((class26.orientation & 0x200) != 0 && l7 <= j6) {
							int j9 = l2 + anIntArray465[k5];
							int j10 = i4 + anIntArray466[k5];
							class26.renderable.renderAtPoint(k5 * 512 + 1280 & 0x7ff, camUpDownY, camUpDownX,
                                    camLeftRightY, camLeftRightX, j9, j3, j10, class26.uid);
						}
					}
				WallObject class10_2 = currentTile.wallObject;
				if (class10_2 != null) {
					if ((class10_2.orientation2 & currentTile.anInt1328) != 0
							&& !method321(l, i, j, class10_2.orientation2))
						class10_2.renderable2.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
								class10_2.xPos - xCameraPos, class10_2.zPos - zCameraPos,
								class10_2.yPos - yCameraPos, class10_2.uid);
					if ((class10_2.orientation1 & currentTile.anInt1328) != 0
							&& !method321(l, i, j, class10_2.orientation1))
						class10_2.renderable1.renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX,
								class10_2.xPos - xCameraPos, class10_2.zPos - zCameraPos,
								class10_2.yPos - yCameraPos, class10_2.uid);
				}
			}
			if (k < numberOfZ - 1) {
				Tile class30_sub3_12 = tileArray[k + 1][i][j];
				if (class30_sub3_12 != null && class30_sub3_12.aBoolean1323)
					tileDeque.insertHead(class30_sub3_12);
			}
			if (i < xCameraTile) {
				Tile class30_sub3_13 = aclass30_sub3[i + 1][j];
				if (class30_sub3_13 != null && class30_sub3_13.aBoolean1323)
					tileDeque.insertHead(class30_sub3_13);
			}
			if (j < yCameraTile) {
				Tile class30_sub3_14 = aclass30_sub3[i][j + 1];
				if (class30_sub3_14 != null && class30_sub3_14.aBoolean1323)
					tileDeque.insertHead(class30_sub3_14);
			}
			if (i > xCameraTile) {
				Tile class30_sub3_15 = aclass30_sub3[i - 1][j];
				if (class30_sub3_15 != null && class30_sub3_15.aBoolean1323)
					tileDeque.insertHead(class30_sub3_15);
			}
			if (j > yCameraTile) {
				Tile class30_sub3_16 = aclass30_sub3[i][j - 1];
				if (class30_sub3_16 != null && class30_sub3_16.aBoolean1323)
					tileDeque.insertHead(class30_sub3_16);
			}
		} while (true);
	}

	private void method315(SimpleTile simpleTile, int i, int j, int k, int l, int i1, int j1, int k1) {
		int l1;
		int i2 = l1 = (j1 << 7) - xCameraPos;
		int j2;
		int k2 = j2 = (k1 << 7) - yCameraPos;
		int l2;
		int i3 = l2 = i2 + 128;
		int j3;
		int k3 = j3 = k2 + 128;
		int l3 = heightMap[i][j1][k1] - zCameraPos;
		int i4 = heightMap[i][j1 + 1][k1] - zCameraPos;
		int j4 = heightMap[i][j1 + 1][k1 + 1] - zCameraPos;
		int k4 = heightMap[i][j1][k1 + 1] - zCameraPos;
		int l4 = k2 * l + i2 * i1 >> 16;
		k2 = k2 * i1 - i2 * l >> 16;
		i2 = l4;
		l4 = l3 * k - k2 * j >> 16;
		k2 = l3 * j + k2 * k >> 16;
		l3 = l4;
		if (k2 < 50)
			return;
		l4 = j2 * l + i3 * i1 >> 16;
		j2 = j2 * i1 - i3 * l >> 16;
		i3 = l4;
		l4 = i4 * k - j2 * j >> 16;
		j2 = i4 * j + j2 * k >> 16;
		i4 = l4;
		if (j2 < 50)
			return;
		l4 = k3 * l + l2 * i1 >> 16;
		k3 = k3 * i1 - l2 * l >> 16;
		l2 = l4;
		l4 = j4 * k - k3 * j >> 16;
		k3 = j4 * j + k3 * k >> 16;
		j4 = l4;
		if (k3 < 50)
			return;
		l4 = j3 * l + l1 * i1 >> 16;
		j3 = j3 * i1 - l1 * l >> 16;
		l1 = l4;
		l4 = k4 * k - j3 * j >> 16;
		j3 = k4 * j + j3 * k >> 16;
		k4 = l4;
		if (j3 < 50)
			return;
		int i5 = Rasterizer3D.originViewX + (i2 << viewDistance) / k2;
		int j5 = Rasterizer3D.originViewY + (l3 << viewDistance) / k2;
		int k5 = Rasterizer3D.originViewX + (i3 << viewDistance) / j2;
		int l5 = Rasterizer3D.originViewY + (i4 << viewDistance) / j2;
		int i6 = Rasterizer3D.originViewX + (l2 << viewDistance) / k3;
		int j6 = Rasterizer3D.originViewY + (j4 << viewDistance) / k3;
		int k6 = Rasterizer3D.originViewX + (l1 << viewDistance) / j3;
		int l6 = Rasterizer3D.originViewY + (k4 << viewDistance) / j3;
		Rasterizer3D.alpha = 0;
		if ((i6 - k6) * (l5 - l6) - (j6 - l6) * (k5 - k6) > 0) {
			Rasterizer3D.textureOutOfDrawingBounds = i6 < 0 || k6 < 0 || k5 < 0 || i6 > Rasterizer2D.lastX || k6 > Rasterizer2D.lastX || k5 > Rasterizer2D.lastX;
			if (clicked && method318(clickScreenX, clickScreenY, j6, l6, l5, i6, k6, k5)) {
				clickedTileX = j1;
				clickedTileY = k1;
			}
			if (simpleTile.getTexture() == -1) {
				if (simpleTile.getCenterColor() != 0xbc614e)
					Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor(), k3, j3, j2);
			} else if (!lowMem) {
				if (simpleTile.isFlat())
					Rasterizer3D.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor(), i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.getTexture(), k3, j3, j2);
				else
					Rasterizer3D.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, simpleTile.getCenterColor(), simpleTile.getEastColor(), simpleTile.getNorthColor(), l2, l1, i3, j4, k4, i4, k3, j3, j2, simpleTile.getTexture(), k3, j3, j2);
			} else {				
				int textureColor = TEXTURE_COLORS[simpleTile.getTexture()];				
				Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, light(textureColor, simpleTile.getCenterColor()), light(textureColor, simpleTile.getEastColor()), light(textureColor, simpleTile.getNorthColor()), k3, j3, j2);
			}
		}
		if ((i5 - k5) * (l6 - l5) - (j5 - l5) * (k6 - k5) > 0) {
			Rasterizer3D.textureOutOfDrawingBounds = i5 < 0 || k5 < 0 || k6 < 0 || i5 > Rasterizer2D.lastX || k5 > Rasterizer2D.lastX || k6 > Rasterizer2D.lastX;
			if (clicked && method318(clickScreenX, clickScreenY, j5, l5, l6, i5, k5, k6)) {
				clickedTileX = j1;
				clickedTileY = k1;
			}
			if (simpleTile.getTexture() == -1) {
				if (simpleTile.getNorthEastColor() != 0xbc614e) {
					Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, simpleTile.getNorthEastColor(), simpleTile.getNorthColor(),
							simpleTile.getEastColor(), k2, j2, j3);
				}
			} else {
				if (!lowMem) {
					Rasterizer3D.drawTexturedTriangle(j5, l5, l6, i5, k5, k6, simpleTile.getNorthEastColor(), simpleTile.getNorthColor(),
							simpleTile.getEastColor(), i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.getTexture(), k2, j2, j3);
					return;
				}
				int j7 = TEXTURE_COLORS[simpleTile.getTexture()];
				Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, light(j7, simpleTile.getNorthEastColor()), light(j7, simpleTile.getNorthColor()), light(j7, simpleTile.getEastColor()), k2, j2, j3);
			}
		}
	}

	private void method316(int i, int j, int k, ShapedTile class40, int l, int i1, int j1) {
		int k1 = class40.anIntArray673.length;
		for (int l1 = 0; l1 < k1; l1++) {
			int i2 = class40.anIntArray673[l1] - xCameraPos;
			int k2 = class40.anIntArray674[l1] - zCameraPos;
			int i3 = class40.anIntArray675[l1] - yCameraPos;
			int k3 = i3 * k + i2 * j1 >> 16;
			i3 = i3 * j1 - i2 * k >> 16;
			i2 = k3;
			k3 = k2 * l - i3 * j >> 16;
			i3 = k2 * j + i3 * l >> 16;
			k2 = k3;
			if (i3 < 50)
				return;
			if (class40.anIntArray682 != null) {
				ShapedTile.anIntArray690[l1] = i2;
				ShapedTile.anIntArray691[l1] = k2;
				ShapedTile.anIntArray692[l1] = i3;
			}
			ShapedTile.anIntArray688[l1] = Rasterizer3D.originViewX + (i2 << viewDistance) / i3;
			ShapedTile.anIntArray689[l1] = Rasterizer3D.originViewY + (k2 << viewDistance) / i3;
			ShapedTile.depthPoint[l1] = i3;
		}

		Rasterizer3D.alpha = 0;
		k1 = class40.anIntArray679.length;
		for (int j2 = 0; j2 < k1; j2++) {
			int l2 = class40.anIntArray679[j2];
			int j3 = class40.anIntArray680[j2];
			int l3 = class40.anIntArray681[j2];
			int i4 = ShapedTile.anIntArray688[l2];
			int j4 = ShapedTile.anIntArray688[j3];
			int k4 = ShapedTile.anIntArray688[l3];
			int l4 = ShapedTile.anIntArray689[l2];
			int i5 = ShapedTile.anIntArray689[j3];
			int j5 = ShapedTile.anIntArray689[l3];
			if ((i4 - j4) * (j5 - i5) - (l4 - i5) * (k4 - j4) > 0) {
				Rasterizer3D.textureOutOfDrawingBounds = i4 < 0 || j4 < 0 || k4 < 0 || i4 > Rasterizer2D.lastX
						|| j4 > Rasterizer2D.lastX || k4 > Rasterizer2D.lastX;
				if (clicked && method318(clickScreenX, clickScreenY, l4, i5, j5, i4, j4, k4)) {
					clickedTileX = i;
					clickedTileY = i1;
				}
				Rasterizer3D.drawDepthTriangle(i4, j4, k4, l4, i5, j5, ShapedTile.depthPoint[l2], ShapedTile.depthPoint[j3],
						ShapedTile.depthPoint[l3]);
				if (class40.anIntArray682 == null || class40.anIntArray682[j2] == -1) {
					if (class40.anIntArray676[j2] != 0xbc614e)
						Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, class40.anIntArray676[j2],
								class40.anIntArray677[j2], class40.anIntArray678[j2], ShapedTile.depthPoint[l2],
								ShapedTile.depthPoint[j3], ShapedTile.depthPoint[l3]);
				} else if (!lowMem) {
					if (class40.flat)
						Rasterizer3D.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, class40.anIntArray676[j2],
								class40.anIntArray677[j2], class40.anIntArray678[j2], ShapedTile.anIntArray690[0],
								ShapedTile.anIntArray690[1], ShapedTile.anIntArray690[3], ShapedTile.anIntArray691[0],
								ShapedTile.anIntArray691[1], ShapedTile.anIntArray691[3], ShapedTile.anIntArray692[0],
								ShapedTile.anIntArray692[1], ShapedTile.anIntArray692[3], class40.anIntArray682[j2],
								ShapedTile.depthPoint[l2], ShapedTile.depthPoint[j3], ShapedTile.depthPoint[l3]);
					else
						Rasterizer3D.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, class40.anIntArray676[j2],
								class40.anIntArray677[j2], class40.anIntArray678[j2], ShapedTile.anIntArray690[l2],
								ShapedTile.anIntArray690[j3], ShapedTile.anIntArray690[l3], ShapedTile.anIntArray691[l2],
								ShapedTile.anIntArray691[j3], ShapedTile.anIntArray691[l3], ShapedTile.anIntArray692[l2],
								ShapedTile.anIntArray692[j3], ShapedTile.anIntArray692[l3], class40.anIntArray682[j2],
								ShapedTile.depthPoint[l2], ShapedTile.depthPoint[j3], ShapedTile.depthPoint[l3]);
				} else {
					int k5 = TEXTURE_COLORS[class40.anIntArray682[j2]];
					Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, light(k5, class40.anIntArray676[j2]),
							light(k5, class40.anIntArray677[j2]), light(k5, class40.anIntArray678[j2]),
							ShapedTile.depthPoint[l2], ShapedTile.depthPoint[j3], ShapedTile.depthPoint[l3]);
				}
			}
		}
	}

	private int light(int j, int k) {		
		k = 127 - k;
		k = (k * (j & 0x7f)) / 160;
		if (k < 2)
			k = 2;
		else if (k > 126)
			k = 126;
		return (j & 0xff80) + k;
	}

	private boolean method318(int i, int j, int k, int l, int i1, int j1, int k1, int l1) {
		if (j < k && j < l && j < i1)
			return false;
		if (j > k && j > l && j > i1)
			return false;
		if (i < j1 && i < k1 && i < l1)
			return false;
		if (i > j1 && i > k1 && i > l1)
			return false;
		int i2 = (j - k) * (k1 - j1) - (i - j1) * (l - k);
		int j2 = (j - i1) * (j1 - l1) - (i - l1) * (k - i1);
		int k2 = (j - l) * (l1 - k1) - (i - k1) * (i1 - l);
		return i2 * k2 > 0 && k2 * j2 > 0;
	}

	private void method319() {
		int sceneClusterCount = sceneClusterCounts[currentRenderPlane];
		SceneCluster sceneClusters[] = SceneGraph.sceneClusters[currentRenderPlane];
		anInt475 = 0;
		for (int sceneIndex = 0; sceneIndex < sceneClusterCount; sceneIndex++) {
			SceneCluster sceneCluster = sceneClusters[sceneIndex];
			if (sceneCluster.orientation == 1) { //YZ-plane
				int relativeX = (sceneCluster.startXLoc - xCameraTile) + 25;
				if (relativeX < 0 || relativeX > 50)
					continue;
				int minRelativeY = (sceneCluster.startYLoc - yCameraTile) + 25;
				if (minRelativeY < 0)
					minRelativeY = 0;
				int maxRelativeY = (sceneCluster.endYLoc - yCameraTile) + 25;
				if (maxRelativeY > 50)
					maxRelativeY = 50;
				boolean flag = false;
				while (minRelativeY <= maxRelativeY)
					if (aBooleanArrayArray492[relativeX][minRelativeY++]) {
						flag = true;
						break;
					}
				if (!flag)
					continue;
				int dXPos = xCameraPos - sceneCluster.startXPos;
				if (dXPos > 32) {
					sceneCluster.cullDirection = 1;
				} else {
					if (dXPos >= -32)
						continue;
					sceneCluster.cullDirection = 2;
					dXPos = -dXPos;
				}
				sceneCluster.anInt801 = (sceneCluster.startYPos - yCameraPos << 8) / dXPos;
				sceneCluster.anInt802 = (sceneCluster.endYPos - yCameraPos << 8) / dXPos;
				sceneCluster.anInt803 = (sceneCluster.startZPos - zCameraPos << 8) / dXPos;
				sceneCluster.anInt804 = (sceneCluster.endZPos - zCameraPos << 8) / dXPos;
				aClass47Array476[anInt475++] = sceneCluster;
				continue;
			}
			if (sceneCluster.orientation == 2) { //XZ-plane
				int relativeY = (sceneCluster.startYLoc - yCameraTile) + 25;
				if (relativeY < 0 || relativeY > 50)
					continue;
				int minRelativeX = (sceneCluster.startXLoc - xCameraTile) + 25;
				if (minRelativeX < 0)
					minRelativeX = 0;
				int maxRelativeX = (sceneCluster.endXLoc - xCameraTile) + 25;
				if (maxRelativeX > 50)
					maxRelativeX = 50;
				boolean flag1 = false;
				while (minRelativeX <= maxRelativeX)
					if (aBooleanArrayArray492[minRelativeX++][relativeY]) {
						flag1 = true;
						break;
					}
				if (!flag1)
					continue;
				int dYPos = yCameraPos - sceneCluster.startYPos;
				if (dYPos > 32) {
					sceneCluster.cullDirection = 3;
				} else if (dYPos < -32){
					sceneCluster.cullDirection = 4;
					dYPos = -dYPos;
				} else {
                    continue;
                }
				sceneCluster.anInt799 = (sceneCluster.startXPos - xCameraPos << 8) / dYPos;
				sceneCluster.anInt800 = (sceneCluster.endXPos - xCameraPos << 8) / dYPos;
				sceneCluster.anInt803 = (sceneCluster.startZPos - zCameraPos << 8) / dYPos;
				sceneCluster.anInt804 = (sceneCluster.endZPos - zCameraPos << 8) / dYPos;
				aClass47Array476[anInt475++] = sceneCluster;
			} else if (sceneCluster.orientation == 4) { //XY-plane
				int relativeZ = sceneCluster.startZPos - zCameraPos;
				if (relativeZ > 128) {
					int minRelativeY = (sceneCluster.startYLoc - yCameraTile) + 25;
					if (minRelativeY < 0)
						minRelativeY = 0;
					int maxRelativeY = (sceneCluster.endYLoc - yCameraTile) + 25;
					if (maxRelativeY > 50)
						maxRelativeY = 50;
					if (minRelativeY <= maxRelativeY) {
						int minRelativeX = (sceneCluster.startXLoc - xCameraTile) + 25;
						if (minRelativeX < 0)
							minRelativeX = 0;
						int maxRelativeX = (sceneCluster.endXLoc - xCameraTile) + 25;
						if (maxRelativeX > 50)
							maxRelativeX = 50;
						boolean flag2 = false;
						label0: for (int i4 = minRelativeX; i4 <= maxRelativeX; i4++) {
							for (int j4 = minRelativeY; j4 <= maxRelativeY; j4++) {
								if (!aBooleanArrayArray492[i4][j4])
									continue;
								flag2 = true;
								break label0;
							}

						}

						if (flag2) {
							sceneCluster.cullDirection = 5;
							sceneCluster.anInt799 = (sceneCluster.startXPos - xCameraPos << 8) / relativeZ;
							sceneCluster.anInt800 = (sceneCluster.endXPos - xCameraPos << 8) / relativeZ;
							sceneCluster.anInt801 = (sceneCluster.startYPos - yCameraPos << 8) / relativeZ;
							sceneCluster.anInt802 = (sceneCluster.endYPos - yCameraPos << 8) / relativeZ;
							aClass47Array476[anInt475++] = sceneCluster;
						}
					}
				}
			}
		}

	}

	private boolean method320(int zLoc, int xLoc, int yLoc) {
		int l = anIntArrayArrayArray445[zLoc][xLoc][yLoc];
		if (l == -anInt448)
			return false;
		if (l == anInt448)
			return true;
		int xPos = xLoc << 7;
		int yPos = yLoc << 7;
		if (method324(xPos + 1, heightMap[zLoc][xLoc][yLoc], yPos + 1) && method324((xPos + 128) - 1, heightMap[zLoc][xLoc + 1][yLoc], yPos + 1) && method324((xPos + 128) - 1, heightMap[zLoc][xLoc + 1][yLoc + 1], (yPos + 128) - 1) && method324(xPos + 1, heightMap[zLoc][xLoc][yLoc + 1], (yPos + 128) - 1)) {
			anIntArrayArrayArray445[zLoc][xLoc][yLoc] = anInt448;
			return true;
		} else {
			anIntArrayArrayArray445[zLoc][xLoc][yLoc] = -anInt448;
			return false;
		}
	}

	private boolean method321(int i, int j, int k, int l) {
		if (!method320(i, j, k))
			return false;
		int i1 = j << 7;
		int j1 = k << 7;
		int k1 = heightMap[i][j][k] - 1;
		int l1 = k1 - 120;
		int i2 = k1 - 230;
		int j2 = k1 - 238;
		if (l < 16) {
			if (l == 1) {
				if (i1 > xCameraPos) {
					if (!method324(i1, k1, j1))
						return false;
					if (!method324(i1, k1, j1 + 128))
						return false;
				}
				if (i > 0) {
					if (!method324(i1, l1, j1))
						return false;
					if (!method324(i1, l1, j1 + 128))
						return false;
				}
				return method324(i1, i2, j1) && method324(i1, i2, j1 + 128);
			}
			if (l == 2) {
				if (j1 < yCameraPos) {
					if (!method324(i1, k1, j1 + 128))
						return false;
					if (!method324(i1 + 128, k1, j1 + 128))
						return false;
				}
				if (i > 0) {
					if (!method324(i1, l1, j1 + 128))
						return false;
					if (!method324(i1 + 128, l1, j1 + 128))
						return false;
				}
				return method324(i1, i2, j1 + 128) && method324(i1 + 128, i2, j1 + 128);
			}
			if (l == 4) {
				if (i1 < xCameraPos) {
					if (!method324(i1 + 128, k1, j1))
						return false;
					if (!method324(i1 + 128, k1, j1 + 128))
						return false;
				}
				if (i > 0) {
					if (!method324(i1 + 128, l1, j1))
						return false;
					if (!method324(i1 + 128, l1, j1 + 128))
						return false;
				}
				return method324(i1 + 128, i2, j1) && method324(i1 + 128, i2, j1 + 128);
			}
			if (l == 8) {
				if (j1 > yCameraPos) {
					if (!method324(i1, k1, j1))
						return false;
					if (!method324(i1 + 128, k1, j1))
						return false;
				}
				if (i > 0) {
					if (!method324(i1, l1, j1))
						return false;
					if (!method324(i1 + 128, l1, j1))
						return false;
				}
				return method324(i1, i2, j1) && method324(i1 + 128, i2, j1);
			}
		}
		if (!method324(i1 + 64, j2, j1 + 64))
			return false;
		if (l == 16)
			return method324(i1, i2, j1 + 128);
		if (l == 32)
			return method324(i1 + 128, i2, j1 + 128);
		if (l == 64)
			return method324(i1 + 128, i2, j1);
		if (l == 128) {
			return method324(i1, i2, j1);
		} else {
			System.out.println("Warning unsupported wall type"); //TODO
			return true;
		}
	}

	private boolean method322(int i, int j, int k, int l) {
		if (!method320(i, j, k))
			return false;
		int i1 = j << 7;
		int j1 = k << 7;
		return method324(i1 + 1, heightMap[i][j][k] - l, j1 + 1)
				&& method324((i1 + 128) - 1, heightMap[i][j + 1][k] - l, j1 + 1)
				&& method324((i1 + 128) - 1, heightMap[i][j + 1][k + 1] - l, (j1 + 128) - 1)
				&& method324(i1 + 1, heightMap[i][j][k + 1] - l, (j1 + 128) - 1);
	}

	private boolean method323(int i, int j, int k, int l, int i1, int j1) {
		if (j == k && l == i1) {
			if (!method320(i, j, l))
				return false;
			int k1 = j << 7;
			int i2 = l << 7;
			return method324(k1 + 1, heightMap[i][j][l] - j1, i2 + 1)
					&& method324((k1 + 128) - 1, heightMap[i][j + 1][l] - j1, i2 + 1)
					&& method324((k1 + 128) - 1, heightMap[i][j + 1][l + 1] - j1, (i2 + 128) - 1)
					&& method324(k1 + 1, heightMap[i][j][l + 1] - j1, (i2 + 128) - 1);
		}
		for (int l1 = j; l1 <= k; l1++) {
			for (int j2 = l; j2 <= i1; j2++)
				if (anIntArrayArrayArray445[i][l1][j2] == -anInt448)
					return false;

		}

		int k2 = (j << 7) + 1;
		int l2 = (l << 7) + 2;
		int i3 = heightMap[i][j][l] - j1;
		if (!method324(k2, i3, l2))
			return false;
		int j3 = (k << 7) - 1;
		if (!method324(j3, i3, l2))
			return false;
		int k3 = (i1 << 7) - 1;
		return method324(k2, i3, k3) && method324(j3, i3, k3);
	}

	private boolean method324(int i, int j, int k) {
		for (int l = 0; l < anInt475; l++) {
			SceneCluster class47 = aClass47Array476[l];
			if (class47.cullDirection == 1) {
				int i1 = class47.startXPos - i;
				if (i1 > 0) {
					int j2 = class47.startYPos + (class47.anInt801 * i1 >> 8);
					int k3 = class47.endYPos + (class47.anInt802 * i1 >> 8);
					int l4 = class47.startZPos + (class47.anInt803 * i1 >> 8);
					int i6 = class47.endZPos + (class47.anInt804 * i1 >> 8);
					if (k >= j2 && k <= k3 && j >= l4 && j <= i6)
						return true;
				}
			} else if (class47.cullDirection == 2) {
				int j1 = i - class47.startXPos;
				if (j1 > 0) {
					int k2 = class47.startYPos + (class47.anInt801 * j1 >> 8);
					int l3 = class47.endYPos + (class47.anInt802 * j1 >> 8);
					int i5 = class47.startZPos + (class47.anInt803 * j1 >> 8);
					int j6 = class47.endZPos + (class47.anInt804 * j1 >> 8);
					if (k >= k2 && k <= l3 && j >= i5 && j <= j6)
						return true;
				}
			} else if (class47.cullDirection == 3) {
				int k1 = class47.startYPos - k;
				if (k1 > 0) {
					int l2 = class47.startXPos + (class47.anInt799 * k1 >> 8);
					int i4 = class47.endXPos + (class47.anInt800 * k1 >> 8);
					int j5 = class47.startZPos + (class47.anInt803 * k1 >> 8);
					int k6 = class47.endZPos + (class47.anInt804 * k1 >> 8);
					if (i >= l2 && i <= i4 && j >= j5 && j <= k6)
						return true;
				}
			} else if (class47.cullDirection == 4) {
				int l1 = k - class47.startYPos;
				if (l1 > 0) {
					int i3 = class47.startXPos + (class47.anInt799 * l1 >> 8);
					int j4 = class47.endXPos + (class47.anInt800 * l1 >> 8);
					int k5 = class47.startZPos + (class47.anInt803 * l1 >> 8);
					int l6 = class47.endZPos + (class47.anInt804 * l1 >> 8);
					if (i >= i3 && i <= j4 && j >= k5 && j <= l6)
						return true;
				}
			} else if (class47.cullDirection == 5) {
				int i2 = j - class47.startZPos;
				if (i2 > 0) {
					int j3 = class47.startXPos + (class47.anInt799 * i2 >> 8);
					int k4 = class47.endXPos + (class47.anInt800 * i2 >> 8);
					int l5 = class47.startYPos + (class47.anInt801 * i2 >> 8);
					int i7 = class47.endYPos + (class47.anInt802 * i2 >> 8);
					if (i >= j3 && i <= k4 && k >= l5 && k <= i7)
						return true;
				}
			}
		}

		return false;
	}

	public static boolean lowMem = true;
	private final int numberOfZ;
	private final int xRegionSize;
	private final int yRegionSize;
	private final int[][][] heightMap;
	private final Tile[][][] tileArray;
	private int zAnInt442;
	private int interactableObjectCacheCurrPos;
	private final GameObject[] gameObjectsCache;
	private final int[][][] anIntArrayArrayArray445;
	private static int anInt446;
	private static int currentRenderPlane;
	private static int anInt448;
	private static int cameraLowTileX;
	private static int cameraHighTileX;
	private static int cameraLowTileY;
	private static int cameraHighTileY;
	private static int xCameraTile;
	private static int yCameraTile;
	private static int xCameraPos;
	private static int zCameraPos;
	private static int yCameraPos;
	private static int camUpDownY;
	private static int camUpDownX;
	private static int camLeftRightY;
	private static int camLeftRightX;
	private static GameObject[] interactableObjects = new GameObject[100];
	private static final int[] anIntArray463 = { 53, -53, -53, 53 };
	private static final int[] anIntArray464 = { -53, -53, 53, 53 };
	private static final int[] anIntArray465 = { -45, 45, 45, -45 };
	private static final int[] anIntArray466 = { 45, 45, -45, -45 };
	private static boolean clicked;
	private static int clickScreenX;
	private static int clickScreenY;
	public static int clickedTileX = -1;
	public static int clickedTileY = -1;
	private static final int cullingClusterPlaneCount;
	private static int[] sceneClusterCounts;
	private static SceneCluster[][] sceneClusters;
	private static int anInt475;
	private static final SceneCluster[] aClass47Array476 = new SceneCluster[500];
	private static Deque tileDeque = new Deque();
	private static final int[] anIntArray478 = { 19, 55, 38, 155, 255, 110, 137, 205, 76 };
	private static final int[] anIntArray479 = { 160, 192, 80, 96, 0, 144, 80, 48, 160 };
	private static final int[] anIntArray480 = { 76, 8, 137, 4, 0, 1, 38, 2, 19 };
	private static final int[] anIntArray481 = { 0, 0, 2, 0, 0, 2, 1, 1, 0 };
	private static final int[] anIntArray482 = { 2, 0, 0, 2, 0, 0, 0, 4, 4 };
	private static final int[] anIntArray483 = { 0, 4, 4, 8, 0, 0, 8, 0, 0 };
	private static final int[] anIntArray484 = { 1, 1, 0, 0, 0, 8, 0, 0, 8 };
	private static final int[] TEXTURE_COLORS = { 41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086,
			41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41,
			41, 41, 41, 41, 41, 3131, 41, 41, 41 };
	private final int[] anIntArray486;
	private final int[] anIntArray487;
	private int anInt488;
	private final int[][] tileVertices = { new int[16], { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1 } };
	private final int[][] tileVertexIndices = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
			{ 12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3 },
			{ 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 },
			{ 3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12 } };
	private static boolean[][][][] aBooleanArrayArrayArrayArray491 = new boolean[8][32][51][51];
	private static boolean[][] aBooleanArrayArray492;
	private static int viewportHalfWidth;
	private static int viewportHalfHeight;
	private static int anInt495;
	private static int anInt496;
	private static int viewportWidth;
	private static int viewportHeight;
	public static int viewDistance = 9;

	static {
		cullingClusterPlaneCount = 4;
		sceneClusterCounts = new int[cullingClusterPlaneCount];
		sceneClusters = new SceneCluster[cullingClusterPlaneCount][500];
	}
}
