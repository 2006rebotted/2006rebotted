package rs2.game.model.location;

import java.util.Arrays;

import rs2.game.GameConstants;
import rs2.game.model.Position;
import rs2.game.model.entity.Entity;

/**
 * Resembles an area or region of coordinates.
 * 
 * @author SeVen
 */
public abstract class Area {

	/**
	 * Determines if a specified location is within an area.
	 * 
	 * @param location
	 *            The location to check.
	 * 
	 * @return {@code true} If the specified location is within an area.
	 *         {@code false} otherwise.
	 */
	public abstract boolean inArea(Position location);

	/**
	 * Determines if an entity is in all of these areas.
	 * 
	 * @param entity
	 *            The entity to check.
	 *
	 * @param area
	 *            The areas to check.
	 * 
	 * @return {@code true} If an entity is in all of these areas. {@code false}
	 *         otherwise.
	 */
	public static boolean inAllArea(Entity entity, Area... area) {
		return Arrays.stream(area).allMatch(a -> a.inArea(entity.getPosition()));
	}

	/**
	 * Determines if an entity is in any of the areas.
	 * 
	 * @param entity
	 *            The entity to check.
	 *
	 * @param area
	 *            The areas to check.
	 * 
	 * @return {@code true} If an entity is in any of these areas. {@code false}
	 *         otherwise.
	 */
	public static boolean inAnyArea(Entity entity, Area... area) {
		return Arrays.stream(area).anyMatch(a -> a.inArea(entity.getPosition()));
	}

	/**
	 * Determines if an entity is in the wilderness.
	 * 
	 * @param entity
	 *            The entity to check.
	 * 
	 * @return {@code true} If this entity is in the wilderness. {@code false}
	 *         otherwise.
	 */
	public static boolean inWilderness(Entity entity) {
		return GameConstants.WILDERNESS.stream().anyMatch($it -> $it.inArea(entity.getPosition()));
	}

	/**
	 * Determines if an entity is in a multi-combat zone.
	 *
	 * @param entity
	 *            The entity to check.
	 *
	 * @return {@code true} If this entity is in the wilderness. {@code false}
	 *         otherwise.
	 */
	public static boolean inMultiCombat(Entity entity) {
		return GameConstants.MULTIPLE_COMBAT.stream().anyMatch($it -> $it.inArea(entity.getPosition()));
	}
	
}
