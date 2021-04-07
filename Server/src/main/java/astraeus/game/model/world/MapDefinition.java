package astraeus.game.model.world;

/**
 * Represents the definition of a map.
 * 
 * @author Vult-R
 */
public final class MapDefinition {

	/**
	 * The region on this map.
	 */
	private final int regionId;

	/**
	 * The terrain file id
	 */
	private final int terrainFileId;	

	/**
	 * The object file id
	 */
	private final int objectFileId;	

	/**
	 * Creates a new {@link MapDefinition}.
	 * 
	 * @param regionId
	 * 		The region on this map
	 * 
	 * @param terrainFile
	 * 		The id of the terrain file
	 * 
	 * @param objectFileId
	 * 		The id of the object file
	 */
	public MapDefinition(int regionId, int terrainFileId, int objectFileId) {
		this.regionId = regionId;
		this.terrainFileId = terrainFileId;
		this.objectFileId = objectFileId;
	}

	/**
	 * Gets the map region on this map.
	 */
	public int getRegionId() {
		return regionId;
	}

	/**
	 * Gets the id of the terrain file.
	 */
	public int getTerrainFileId() {
		return terrainFileId;
	}

	/**
	 * Get sthe id of the object file.
	 */
	public int getObjectFileId() {
		return objectFileId;
	}

}
