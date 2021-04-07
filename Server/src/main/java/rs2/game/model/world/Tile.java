package rs2.game.model.world;

import rs2.game.model.Position;

/**
 * Represents a tile in the game world
 * 
 * @author Vult-R
 */
public final class Tile {
	
	/**
	 * The position of this tile.
	 */
	private final Position position;

	/**
	 * Creates a new {@link Position}.
	 * 
	 * @param position
	 * 		The position of this tile.
	 */
	public Tile(Position position) {
		this.position = position;
	}

	/**
	 * Gets the position of this tile.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public boolean equals(Object o) {
		if ((o instanceof Tile)) {
			Tile other = (Tile) o;
			return (other.getPosition().getX() == position.getX()) && (other.getPosition().getY() == position.getY()) && (other.getPosition().getHeight() == position.getHeight());
		}

		return false;
	}
}
