package rs2.game.model.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import rs2.game.model.Position;
import rs2.game.model.entity.object.GameObject;

/**
 * Represents a region in the game world.
 * 
 * @author Vult-R
 */
public final class Region implements Comparable<Region> {

	/**
	 * The id of this region
	 */
	private final int id;
	
	/**
	 * The collection of positions mapped to their objects.
	 */
	public final Map<Position, GameObject> objects = new HashMap<>();
	
	/**
	 * The collection of regions.
	 */
	public static final List<Region> regions = new ArrayList<>();

	/**
	 * Creates a new {@link Region}.
	 * 
	 * @param id
	 * 		The id of this region.
	 */
	public Region(int id) {
		this.id = id;
	}

	/**
	 * Adds an object to this region.
	 * 
	 * @param object
	 * 		The object to add.
	 */
	public void addGameObject(GameObject object) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;

		int z = object.getPosition().getHeight();

		if (z > 3) {
			z = z % 4;
		}
		
		objects.put(new Position(object.getX() - regionAbsX, object.getY() - regionAbsY, z), object);
	}

	/**
	 * Removes an object from this region.
	 * 
	 * @param object
	 * 		The object to remove.
	 */
	public static void removeGameObject(GameObject object) {
		Optional<Region> r = Region.lookup(object.getPosition());
		
		r.ifPresent(it -> it.objects.put(object.getPosition(), null));	}

	/**
	 * Determines if an object exists at a specified position.
	 * 
	 * @param objectId
	 * 		The object identifier
	 * 
	 * @param position
	 * 		The coordinate the object is located at.
	 */
	public boolean objectExists(int objectId, Position position) {
		Optional<Region> r = lookup(position);

		if (!r.isPresent()) {
			return false;
		}
		
		Region region = r.get();

		int regionAbsX = (region.id >> 8) << 6;
		int regionAbsY = (region.id & 0xff) << 6;
		
		int tempZ = position.getHeight();

		if (tempZ > 3) {
			tempZ = tempZ % 4;
		}

		GameObject object = region.objects.get(new Position(position.getX() - regionAbsX, position.getY() - regionAbsY, tempZ));

		return object.getId() == objectId;
	}

	/**
	 * Gets an optional describing the result of retrieving an object from a specified position.
	 * 
	 * @param position
	 * 		The position the object is located at.
	 */
	public Optional<GameObject> getObject(Position position) {
		Optional<Region> optional = lookup(position);
		

		if (!optional.isPresent()) {
			return Optional.empty();
		}
		
		Region region = optional.get();

		int regionAbsX = (region.id >> 8) << 6;
		int regionAbsY = (region.id & 0xff) << 6;
		
		int tempZ = position.getHeight();

		if (tempZ > 3) {
			tempZ = tempZ % 4;
		}
		
		return Optional.of(region.objects.get(new Position(position.getX() - regionAbsX, position.getY() - regionAbsY, tempZ)));
	}

	/**
	 * Gets an optional describing the result of retrieving a region from a coordinate.
	 * 
	 * @param position
	 * 		The position that is inside a region.
	 */
	public static Optional<Region> lookup(Position position) {
		int regionX = position.getX() >> 3;
		int regionY = position.getY() >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);

		if (regionId > regions.size() - 1) {
			return Optional.empty();
		}

		if (regionId < 0) {
			System.out.println("FATAL CLIPPING ERROR: regionId < 0");
			System.exit(0);
		}

		if (regions.get(regionId) == null) {
			return Optional.empty();
		}

		return Optional.of(regions.get(regionId));		
	}
	
	/**
	 * Gets an optional describing the result of retrieving a {@link Region} by its id.
	 * 
	 * @param id
	 * 		The id of the region to retrieve.
	 */
	public static Optional<Region> getRegionById(int id) {
		return regions.stream().filter(Objects::nonNull).filter(it -> id == it.getId()).findFirst();
	}

	/**
	 * Gets the id of this region.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the collection of regions.
	 */
	public static List<Region> getRegions() {
		return regions;
	}

	@Override
	public int compareTo(Region o) {
		return id > o.getId() ? 1 : -1;
	}
	
}
