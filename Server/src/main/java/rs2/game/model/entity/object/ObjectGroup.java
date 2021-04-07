package rs2.game.model.entity.object;

/**
 * The enumerated types of groups {@link GameObject}s can belong to.
 *
 * @author Seven | Apollo Development Team
 */
public enum ObjectGroup {

	/**
	 * The ground decoration object group, which may block a tile.
	 */
	GROUND_DECORATION,

	/**
	 * The interactable object group, for objects that can be clicked and interacted with.
	 */
	INTERACTABLE,

	/**
	 * The wall object group, which may block a tile.
	 */
	WALL,

	/**
	 * The wall decoration object group, which never blocks a tile.
	 */
	WALL_DECORATION,

	/**
	 * The roof object group, for objects that are above the players head, or on the ceiling.
	 */
	ROOF

}
