package plugin.doors;

import rs2.game.model.Direction;
import rs2.game.model.Position;
import rs2.game.model.entity.object.GameObject;
import rs2.game.model.entity.object.GameObjectType;

import java.util.Objects;

/**
 * Represents a door object in the game world.
 *
 * @author SeVen
 */
public final class Door extends GameObject {

	/**
	 * The modified id of this door.
	 */
	private int currentId;

	/**
	 * The modified Position of this door.
	 */
	private Position currentPosition;

	/**
	 * The flag that denotes the door is open.
	 */
	private boolean open;

	/**
	 * The next orientation.
	 */
	private Direction nextOrientation;

	/**
	 * The flag that denotes this door has already been clicked.
	 */
	private boolean activated;

	/**
	 * Creates a new {@link Door}.
	 *
	 * @param id       The id of the door.
	 * @param type     The type of door.
	 * @param Position The Position of this door.
	 */
	public Door(int id, GameObjectType type, Position Position, boolean open, Direction orientation) {
		super(id, type, Position, orientation);
		this.currentId = id;
		this.currentPosition = Position;
		this.open = open;
		this.nextOrientation = orientation;
	}

	/**
	 * Gets the modified id of this door.
	 *
	 * @return The modified id.
	 */
	public int getCurrentId() {
		return currentId;
	}

	/**
	 * Sets the id of this object.
	 *
	 * @param id The new id of this object.
	 */
	public void setCurrentId(int id) {
		this.currentId = id;
	}

	/**
	 * Gets the modified Position of this door.
	 *
	 * @return The modified Position.
	 */
	public Position getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Sets the Position of this door.
	 *
	 * @param Position The new Position.
	 */
	public void setCurrentPosition(Position Position) {
		this.currentPosition = Position;
	}

	/**
	 * Determines if this door is open.
	 *
	 * @return {@code true} If this door is open. {@code false} The door is closed.
	 */
	public boolean isOpened() {
		return open;
	}

	/**
	 * Sets the flag that denotes this door is opened.
	 *
	 * @param opened The flag to set.
	 */
	public void setOpened(boolean opened) {
		this.open = opened;
	}

	/**
	 * Gets the next orientation of this door.
	 *
	 * @return The next orientation.
	 */
	public Direction getNextOrientation() {
		return nextOrientation;
	}

	/**
	 * Sets the next orientation of this door.
	 *
	 * @param nextOrientation The next orientation to set.
	 */
	public void setNextOrientation(Direction nextOrientation) {
		this.nextOrientation = nextOrientation;
	}

	/**
	 * Determines if this door has already been clicked.
	 *
	 * @return {@code true} If this door has been clicked. {@code false} otherwise.
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * Sets the flag that denotes this door has been clicked.
	 *
	 * @param activated The flag to set.
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	/**
	 * Determines if this door is a single door.
	 *
	 * @return {@code true} If this door is a single door. {@code false} otherwise.
	 */
	public boolean isSingle() {
		return DoorUtils.SINGLE_DOORS.stream().anyMatch($it -> $it == getCurrentId());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Door) {
			Door other = (Door) o;
			return (this.hashCode() == other.hashCode());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(currentId, getType(), currentPosition, open, nextOrientation);
	}

	@Override
	public String toString() {
		return String.format("[DOOR] [id= %d] [type= %s] [loc= %s] [open= %s] [orientation= %s]", currentId, getEnumeratedObjectType().name(), currentPosition.toString(), Boolean.toString(open), nextOrientation.name());
	}

}