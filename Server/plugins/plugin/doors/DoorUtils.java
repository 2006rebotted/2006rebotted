package plugin.doors;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;

import astraeus.game.model.Direction;
import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.object.GameObject;
import astraeus.net.packet.out.AddObjectPacket;
import astraeus.net.packet.out.RemoveRegionalObjectPacket;

/**
 * The class that contains static-utility methods for doors.
 *
 * @author Seven
 */
public final class DoorUtils {

	/**
	 * The set door ids that are single doors.
	 */
	public static final ImmutableSet<Integer> SINGLE_DOORS = ImmutableSet.of(1530, 1531, 1533, 1534);

	/**
	 * Gets a {@link Door} object from a {@link GameObject}.
	 *
	 * @param object The object to get.
	 * @return The optional describing the object.
	 */
	public static Optional<Door> getDoor(GameObject object) {
		return Doors.getDoors().stream().filter(Objects::nonNull).filter($it -> $it.getId() == object.getId() && !$it.isActivated() ? $it.getPosition().equals(object.getPosition()) : $it.getCurrentPosition().equals(object.getPosition())).findFirst();
	}

	/**
	 * Handles the action of opening, or closing a door.
	 *
	 * @param player The player performing this action.
	 * @param object The object that is being interacted with.
	 */
	public static void handleDoor(Player player, GameObject object) {
		// get the door from the game object
		Optional<Door> d = getDoor(object);

		// we have the right door we want so now we need to perform the actions of the door.
		if (d.isPresent()) {
			// create a temporary variable so we can manipulate the local door object.
			Door door = d.get();

			// remove the previous door right away.
			Doors.getDoors().remove(door);

			// set a default value for translating.
			Position translate = door.getCurrentPosition();

			// set a default value for the next orientation
			Direction nextOrientation = door.getEnumeratedOrientation();

			// if the door has been activated already then create the original door again.
			if (door.isActivated()) {

				// reset the next orientation
				door.setNextOrientation(door.getEnumeratedOrientation());

				// reset the doors translated Position back to the original.
				door.setCurrentPosition(door.getPosition());

				// This door has not been activated yet.
			} else {
				// now we need to translate the door to a specified coordinate.
				translate = DoorUtils.translate(player, door);

				// assign the translated coordinate to the door object.
				door.setCurrentPosition(translate);

				// So lets grab the next orientation for this door.
				nextOrientation = DoorUtils.getNextOrientation(door);

				// assign the orientation to this door object.
				door.setNextOrientation(nextOrientation);
			}

			// This is to display when doors are open "close" option when the door is closed "open" option.
			if (door.getCurrentId() == door.getId()) {
				door.setCurrentId(door.isOpened() ? door.getCurrentId() - 1 : door.getCurrentId() + 1);
			} else if (door.getCurrentId() != door.getId()) {
				door.setCurrentId(door.isOpened() ? door.getCurrentId() + 1 : door.getCurrentId() - 1);
			}

			// Remove the previous door
			DoorUtils.removeDoor(player, new GameObject(door.isActivated() ? door.getCurrentId() : door.getId(), door.getEnumeratedObjectType(), door.isActivated() ? translate : door.getPosition(), door.isActivated() ? door.getNextOrientation() : door.getEnumeratedOrientation()));

			// Create the new door
			DoorUtils.createDoor(player, new GameObject(door.isActivated() ? door.getId() : door.getCurrentId(), door.getEnumeratedObjectType(), door.isActivated() ? door.getPosition() : translate, door.isActivated() ? door.getEnumeratedOrientation() : nextOrientation));

			// Set the status of door.
			door.setOpened(door.isOpened() ? false : true);

			// Set the flag if this door has been activated or not.
			door.setActivated(!door.isActivated());

			// Add the modified door object back into the list of active doors.
			Doors.getDoors().add(door);
		}
	}

	/**
	 * Translates a doors current Position to a specified Position depending on its orientation.
	 *
	 * @param player The player to translate this door for.
	 * @param door   The door to translate.
	 */
	public static Position translate(Player player, Door door) {
		switch (door.getEnumeratedObjectType()) {
			case STRAIGHT:
				switch (door.getEnumeratedOrientation()) {
					case NORTH:
						return new Position(door.getCurrentPosition().getX() + (door.isOpened() ? 1 : 0), door.getCurrentPosition().getY() + (door.isOpened() ? 0 : 1), player.getPosition().getHeight());
					case EAST:
						return new Position(door.getCurrentPosition().getX() + (door.isOpened() ? 0 : 1), door.getCurrentPosition().getY() + (door.isOpened() ? -1 : 0), player.getPosition().getHeight());
					case SOUTH:
						return new Position(door.getCurrentPosition().getX() + (door.isOpened() ? -1 : 0), door.getCurrentPosition().getY() + (door.isOpened() ? 0 : -1), player.getPosition().getHeight());
					case WEST:
						return new Position(door.getCurrentPosition().getX() + (door.isOpened() ? 0 : -1), door.getCurrentPosition().getY() + (door.isOpened() ? 1 : 0), player.getPosition().getHeight());
				default:
					break;
				}
				break;

			case DIAGONAL:
				switch (door.getEnumeratedOrientation()) {
					case NORTH:
						return new Position(door.getCurrentPosition().getX() + 1, door.getCurrentPosition().getY(), player.getPosition().getHeight());
					case EAST:
						return new Position(door.getCurrentPosition().getX() - 1, door.getCurrentPosition().getY(), player.getPosition().getHeight());
					case SOUTH:
						return new Position(door.getCurrentPosition().getX() - 1, door.getCurrentPosition().getY(), player.getPosition().getHeight());
					case WEST:
						return new Position(door.getCurrentPosition().getX() + 1, door.getCurrentPosition().getY(), player.getPosition().getHeight());
				default:
					break;
				}
				break;
		default:
			break;
		}
		return door.getCurrentPosition();
	}

	/**
	 * Gets the next orientation of this door based on its current orientation.
	 *
	 * @param door The door to get the next orientation of.
	 * @return The next orientation for this door.
	 */
	public static Direction getNextOrientation(Door door) {
		switch (door.getEnumeratedObjectType()) {
			case STRAIGHT:
				switch (door.getEnumeratedOrientation()) {
					case NORTH:
						return door.isOpened() ? Direction.WEST : Direction.EAST;
					case EAST:
						return door.isOpened() ? Direction.NORTH : Direction.SOUTH;
					case SOUTH:
						return door.isOpened() ? Direction.EAST : Direction.WEST;
					case WEST:
						return door.isOpened() ? Direction.SOUTH : Direction.NORTH;
				default:
					break;
				}
			case DIAGONAL:
				switch (door.getEnumeratedOrientation()) {
					case NORTH:
						return door.isOpened() ? Direction.WEST : Direction.EAST;
					case EAST:
						return Direction.NORTH;
					case SOUTH:
						return door.isOpened() ? Direction.EAST : Direction.WEST;
					case WEST:
						return Direction.SOUTH;
				default:
					break;
				}
				break;
		default:
			break;
		}

		return door.getEnumeratedOrientation();
	}

	/**
	 * Creates a door object for a player and players in his/her local player list.
	 * <p>
	 * This is called when a player clicks the door.
	 *
	 * @param player The player creating objects.
	 * @param object The object to create for everyone near thisPlayer.
	 */

	public static void createDoor(Player player, GameObject object) {
		player.getLocalPlayers().stream().filter(Objects::nonNull).forEach($it -> $it.queuePacket(new AddObjectPacket(object, false)));
		player.queuePacket(new AddObjectPacket(object, false));
	}

	/**
	 * Creates the translated door or default door and removes the previous door or translated door upon loading a region for a {@code player}.
	 * <p>
	 * This is called when players enter the region (also logging in).
	 *
	 * @param player The player to create and remove the doors for
	 */
	public static void createDoors(Player player) {
		Stream<Door> stream = Doors.getDoors().stream().filter(Objects::nonNull).filter($it -> $it.getCurrentPosition().isWithinDistance(player.getPosition(), Position.VIEWING_DISTANCE)).filter($it -> $it.isActivated());

		stream.forEach($it -> {
			player.queuePacket(new RemoveRegionalObjectPacket(new GameObject($it.getId(), $it.getEnumeratedObjectType(), $it.getPosition(), $it.getEnumeratedOrientation()), false));
			player.queuePacket(new AddObjectPacket(new GameObject($it.getCurrentId(), $it.getEnumeratedObjectType(), $it.getCurrentPosition(), $it.getNextOrientation()), false));
		});
	}

	/**
	 * Removes the previous door for a player and players in his/her local player list.
	 * <p>
	 * This is called when a player clicks the door.
	 *
	 * @param player The player to remove the door for.
	 * @param object The object to remove.
	 */
	public static void removeDoor(Player player, GameObject object) {
		player.getLocalPlayers().stream().filter(Objects::nonNull).forEach($it -> $it.queuePacket(new RemoveRegionalObjectPacket(object, false)));
		player.queuePacket(new RemoveRegionalObjectPacket(object, false));
	}

	/**
	 * Determines if this {@code objectId} is a door object.
	 *
	 * @param objectId The objectId to check.
	 * @return {@code true} If this objectId is a door. {@code false} otherwise.
	 */
	public static boolean isDoor(int objectId) {
		return Doors.getDoors().stream().anyMatch($it -> $it.getId() == objectId || $it.getCurrentId() == objectId);
	}
}
