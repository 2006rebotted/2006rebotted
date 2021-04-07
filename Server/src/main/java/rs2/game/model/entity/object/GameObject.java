package rs2.game.model.entity.object;

import rs2.game.model.Direction;
import rs2.game.model.Position;
import rs2.game.model.entity.Entity;
import rs2.game.model.entity.EntityType;

/**
 * Represents an object that can be spawned into the game world.
 * 
 * @author Seven
 */
public class GameObject extends Entity {

	/**
	 * The packed value that stores this object's id, type, and orientation.
	 */
	private transient final int packed;

	/**
	 * The type of this object.
	 */
	private final GameObjectType type;

	/**
	 * The orientation of this object.
	 */
	private final Direction orientation;

	/**
	 * Creates a new {@link GameObject}, with a default type of {@code 10}.
	 * 
	 * @param id
	 *            The id of this object.
	 * 
	 * @param location
	 *            The location of this object.
	 */
	public GameObject(int id, Position location) {
		this(id, GameObjectType.INTERACTABLE, location, Direction.SOUTH);
	}

	/**
	 * Creates a new {@link GameObject}, with a default type of {@code 10}.
	 * 
	 * @param id
	 *            The id of this object.
	 * 
	 * @param location
	 *            The location of this object.
	 * 
	 * @param orientation
	 *            The facing direction of this object.
	 */
	public GameObject(int id, Position location, Direction orientation) {
		this(id, GameObjectType.INTERACTABLE, location, orientation);
	}
	
	/**
	 * Creates a new {@link GameObject}.
	 * 
	 * @param id
	 *            The id of this object.
	 * 
	 * @param type
	 *            The type of this object.
	 * 
	 * @param position
	 *            The location of this object.
	 * 
	 * @param orientation
	 *            The facing direction of this object.
	 */
	public GameObject(int id, int type, Position position, int orientation) {
		this(id, GameObjectType.lookup(type).get(), position, Direction.of(orientation));		
	}

	/**
	 * Creates a new {@link GameObject}.
	 * 
	 * @param id
	 *            The id of this object.
	 * 
	 * @param type
	 *            The type of this object.
	 * 
	 * @param position
	 *            The location of this object.
	 * 
	 * @param orientation
	 *            The facing direction of this object.
	 */
	public GameObject(int id, GameObjectType type, Position position, Direction orientation) {
		packed = id << 8 | (type.getValue() & 0x3F) << 2 | orientation.getId() & 0x3;
		this.type = type;
		this.orientation = orientation;
		this.setPosition(position);		
	}

	/**
	 * Gets the packed value.
	 *
	 * @return The packed value.
	 */
	public int getPacked() {
		return packed;
	}

	/**
	 * Gets the id of this object.
	 *
	 * @return The id.
	 */
	public int getId() {
		return packed >>> 8;
	}

	/**
	 * Gets the orientation of this object.
	 * 
	 * @return The orientation
	 */
	public int getOrientation() {
		return packed & 0x3;
	}

	/**
	 * Gets this object's type.
	 *
	 * @return The type.
	 */
	public int getType() {
		return packed >> 2 & 0x3F;
	}

	/**
	 * Gets the enumerated object type of this object.
	 *
	 * @return The enumerated object type.
	 */
	public GameObjectType getEnumeratedObjectType() {
		return type;
	}

	/**
	 * Gets the enumerated orientation.
	 *
	 * @return The enumerated orientation.
	 */
	public Direction getEnumeratedOrientation() {
		return orientation;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public int hashCode() {
		return packed;
	}

	@Override
	public EntityType type() {
		return EntityType.OBJECT;
	}

}
