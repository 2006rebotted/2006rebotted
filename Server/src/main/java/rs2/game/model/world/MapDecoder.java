package rs2.game.model.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import rs2.game.model.Position;
import rs2.game.model.entity.object.GameObject;
import rs2.net.codec.ByteBufUtils;
import rs2.net.codec.game.ByteBufReader;
import rs2.util.FileUtils;
import rs2.util.LoggerUtils;
import rs2.util.StringUtils;

/**
 * The class that decodes map data.
 * 
 * @author Vult-R		
 */
public final class MapDecoder {
	
	/**
	 * The single logger for this class.
	 */
	private static final Logger logger = LoggerUtils.getLogger(MapDecoder.class);
	
	/**
	 * Loads the maps
	 */
	public static void load() {
		try {
			File file = new File("./data/map/map_index.dat");			
			
			if (!file.exists()) {
				return;
			}

			ByteBufReader reader = ByteBufReader.wrap(FileUtils.fileToByteArray(file));
			
			int size = reader.readShort(false);
			
			logger.info(String.format("Unpacking %s maps.", StringUtils.format(size)));			
			
			int successfull = 0;
			
			List<MapDefinition> definitions = new ArrayList<>();
			
			for (int region = 0; region < size; region++) {	
				
				int regionId = reader.readShort(false);
				
				Region.getRegions().add(new Region(regionId));
				
				int terrainFileId = reader.readShort(false);
				
				int objectFileId = reader.readShort(false);
				
				definitions.add(new MapDefinition(regionId, terrainFileId, objectFileId));				
				
			}
			
			for (MapDefinition definition : definitions) {
				
				byte[] objectData = FileUtils.decompressStoreEntry(new File("./data/map/mapdata/" + definition.getObjectFileId() + ".gz"));
				byte[] terrainData = FileUtils.decompressStoreEntry(new File("./data/map/mapdata/" + definition.getTerrainFileId() + ".gz"));
				
				if (objectData == null || terrainData == null) {
					continue;
				}
				
				try {
					MapDecoder.decodeRegion(definition.getRegionId(), ByteBufReader.wrap(objectData), ByteBufReader.wrap(terrainData));					
					successfull++;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error loading map region: " + definition.getRegionId() + ", ids: " + definition.getObjectFileId() + " and " + definition.getTerrainFileId());
				}				
			}
			
			for(Region r : Region.getRegions()) {
				
				if (r.objects.size() > 0)				
				System.out.println("region: " + r.getId() + " has: " + r.objects.size() + " objects");
			}

			logger.info(String.format("Unpacked: %s maps, %d map definitions", StringUtils.format(successfull), definitions.size()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Decodes the regions
	 * 
	 * @param regionId
	 * 		The region to decode
	 * 
	 * @param objectData
	 * 		The data that contains object information
	 * 
	 * @param terrainData
	 * 		The data that contains terrain information
	 * 
	 * @throws Exception
	 */
	private static void decodeRegion(int regionId, ByteBufReader objectData, ByteBufReader terrainData) throws Exception {		
		
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		
		int[][][] regions = new int[4][64][64];
		
		for (int plane = 0; plane < 4; plane++) {			
			for (int x = 0; x < 64; x++) {				
				for (int y = 0; y < 64; y++) {					
					while (true) {
						
						int type = terrainData.readByte(false);
						
						if (type == 0) {
							break;
						} else if (type == 1) {
							terrainData.skipBytes(1);
							break;
						} else if (type <= 49) {
							terrainData.skipBytes(1);
						} else if (type <= 81) {
							regions[plane][x][y] = type - 49;							
						}
					}
				}
			}
		}
		for (int plane = 0; plane < 4; plane++) {			
			for (int x = 0; x < 64; x++) {				
				for (int y = 0; y < 64; y++) {					
					if ((regions[plane][x][y] & 1) == 1) {
						int height = plane;
						if ((regions[1][x][y] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							//addClipping(true, absX + x, absY + y, height, 0x200000);
						}
					}
				}
			}
		}

		int id = -1;

		int idOffset = ByteBufUtils.getUSmart(objectData.getPayload());

		while (idOffset != 0) {
			id += idOffset;

			int packed = 0;
			int positionOffset = ByteBufUtils.getUSmart(objectData.getPayload());

			while (positionOffset != 0) {
				
				packed += positionOffset - 1;

				int localY = packed & 0x3F;
				int localX = packed >> 6 & 0x3F;
				int height = packed >> 12 & 0x3;

				int attributes = objectData.readByte(false);
				int type = attributes >> 2;
				int orientation = attributes & 0x3;

				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}

				if ((regions[1][localX][localY] & 2) == 2) {
					height--;
				}

				if (height >= 0 && height <= 3) {
					
					GameObject object = new GameObject(id, type, new Position(absX + localX, absY + localY, height), orientation);
					
					Region.lookup(object.getPosition()).ifPresent(it -> it.addGameObject(object));
				}

				positionOffset = ByteBufUtils.getUSmart(objectData.getPayload());
			}
			
			idOffset = ByteBufUtils.getUSmart(objectData.getPayload());
		}
	}

}
