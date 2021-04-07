package astraeus.game.model;

import astraeus.game.model.entity.object.GameObject;

/**
 * Represents the enumerated directions an entity can walk or face.
 * 
 * @author Vult-R
 */
public enum Direction {

	NORTH(0, 1, 3),

	NORTH_EAST(1, 1, -1),

	EAST(1, 0, 0),

	SOUTH_EAST(1, -1, -1),

	SOUTH(0, -1, 1),

	SOUTH_WEST(-1, -1, -1),

	WEST(-1, 0, 2),

	NORTH_WEST(-1, 1, -1),

	NONE(0, 0, -1);

	private final int directionX;

	private final int directionY;

	private final int id;

	/**
	 * Creates a {@link Direction}.
	 * 
	 * @param directionX
	 *            The value that represents a direction.
	 */
	Direction(int directionX, int directionY, int id) {
		this.directionX = directionX;
		this.directionY = directionY;
		this.id = id;
	}

	/**
	 * Gets the direction between two locations.
	 * 
	 * @param location
	 *            The location that will be the viewpoint.
	 * 
	 * @param other
	 *            The other location to get the direction of.
	 * 
	 * @return The direction of the other location.
	 */
	public static Direction getDirection(Position location, Position other) {

		final int deltaX = other.getX() - location.getX();
		final int deltaY = other.getY() - location.getY();

		if (deltaY >= 1) {
			if (deltaX >= 1) {
				return NORTH_EAST;
			} else if (deltaX == 0) {
				return NORTH;
			} else if (deltaX <= -1) {
				return NORTH_WEST;
			}
		} else if (deltaY == 0) {
			if (deltaX >= 1) {
				return Direction.EAST;
			} else if (deltaX == 0) {
				return Direction.NONE;
			} else if (deltaX <= -1) {
				return Direction.WEST;
			}
		} else if (deltaY <= -1) {
			if (deltaX >= 1) {
				return SOUTH_EAST;
			} else if (deltaX == 0) {
				return SOUTH;
			} else if (deltaX <= -1) {
				return SOUTH_WEST;
			}
		}

		return Direction.NONE;

	}

	/**
	 * Gets the direction between two locations, ignoring corners.
	 * 
	 * @param location
	 *            The location that will be the viewpoint.
	 * 
	 * @param other
	 *            The other location to get the direction of.
	 * 
	 * @return The direction of the other location.
	 */
	public static Direction getManhattanDirection(Position location, Position other) {

		final int deltaX = other.getX() - location.getX();
		final int deltaY = other.getY() - location.getY();

		if (deltaY >= 1) {
			if (deltaX == 0) {
				return NORTH;
			}
		} else if (deltaY == 0) {
			if (deltaX >= 1) {
				return Direction.EAST;
			} else if (deltaX == 0) {
				return Direction.NONE;
			} else if (deltaX <= -1) {
				return Direction.WEST;
			}
		} else if (deltaY <= -1) {
			if (deltaX == 0) {
				return SOUTH;
			}
		}

		return Direction.NONE;

	}

	/**
	 * Gets the opposite direction of the given direction.
	 * 
	 * @param direction
	 */
	public static Direction getOppositeDirection(Direction direction) {
		switch (direction) {

		case EAST:
			return WEST;

		case NORTH:
			return SOUTH;

		case NORTH_EAST:
			return SOUTH_WEST;

		case NORTH_WEST:
			return SOUTH_EAST;

		case SOUTH:
			return NORTH;

		case SOUTH_EAST:
			return NORTH_WEST;

		case SOUTH_WEST:
			return NORTH_EAST;

		case WEST:
			return EAST;

		default:
			return NONE;

		}
	}

	/**
	 * Gets the orientation of door {@link GameObject}s.
	 * 
	 * @param direction
	 *            The direction of this object.
	 *
	 * @return The orientation.
	 */
	public static int getDoorOrientation(Direction direction) {
		switch (direction) {

		case WEST:
			return 0;

		case NORTH:
			return 1;

		case EAST:
			return 2;

		case SOUTH:
			return 3;

		default:
			return 3;
		}
	}
	
	/**
	 * Gets the Manhattan direction of an orientation. Manhattan direction does not support diagnal directions, so the orientation must be between [0, 3] inclusive.
	 * @param orientation
	 * @return
	 */
	public static Direction ofManhattan(int orientation) {
		
		assert(orientation >= 0 && orientation <= 3);
		
		switch (orientation) {
		
		case 0:
			return EAST;
			
		case 1:
			return SOUTH;
			
		case 2:
			return WEST;
			
		case 3:
			return NORTH;
		
		}
		
		return null;
	}
	
	public static Direction of(int orientation) {
		for (Direction direction : values()) {
			if (direction.id == orientation) {
				return direction;
			}
		}
		
		return null;
	}

	public static int getPackedOrientation(GameObject object) {
		return object.getOrientation();
	}

	/**
	 * @return the directionX
	 */
	public int getDirectionX() {
		return directionX;
	}

	/**
	 * @return the directionY
	 */
	public int getDirectionY() {
		return directionY;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
