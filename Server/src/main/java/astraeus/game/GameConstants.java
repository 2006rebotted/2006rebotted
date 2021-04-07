package astraeus.game;

import com.google.common.collect.ImmutableList;

import astraeus.game.model.Position;
import astraeus.game.model.location.Area;
import astraeus.game.model.location.impl.SquareArea;

/**
 * The class containing game-related constants.
 * 
 * @author Vult-R
 */
public final class GameConstants {

	private GameConstants() {

	}

	/**
	 * The rate in milliseconds in which the game thread processes logic.
	 */
	public static final int CYLCE_RATE = 600;

	/**
	 * The default spawn for entities.
	 */
	public static final Position DEFAULT_LOCATION = new Position(3159, 3485);

	/**
	 * The collection of areas that resemble the wilderness area.
	 */
	public static final ImmutableList<Area> WILDERNESS = ImmutableList
			.of(new SquareArea("Wilderness", 2941, 3518, 3392, 3966));

	/**
	 * The collection of locations representing multi-combat areas.
	 */
	public static final ImmutableList<Area> MULTIPLE_COMBAT = ImmutableList.of();
	
	/**
	 * The collection of items that cannot be sold/bought from shops.
	 */
	public static final ImmutableList<Integer> INVALID_SHOP_ITEMS  = ImmutableList.of(995);

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGIN_LIMIT = 50;

	/**
	 * The maximum amount of players that can be logged in on a single game
	 * sequence.
	 */
	public static final int LOGOUT_LIMIT = 50;

	/**
	 * How far an npc will walk away from its origin.
	 */
	public static final int NPC_RANDOM_WALK_DISTANCE = 5;

	/**
	 * The distance in tiles that an npc will follow another entity.
	 */
	public static final int NPC_FOLLOW_DISTANCE = 10;

	/**
	 * The maximum amount of players that can be held within the game world.
	 */
	public static final int MAX_PLAYERS = 1024;

	/**
	 * The maximum amount of mobs that can be held within the game world.
	 */
	public static final int MAX_NPC_SPAWNS = 3000;

}
