package rs2.game.model.entity.object;

import rs2.game.model.Position;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.out.AddGroundItemPacket;
import rs2.net.packet.out.AddObjectPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The class that provides static utility methods for {@link GameObject}s.
 *
 * @author Seven
 */
public final class GameObjects {

	/**
	 * The list of global object spawned in the game world.
	 */
	private static final List<GameObject> global_objects = new ArrayList<>();	

	/**
	 * A map of ground items and their positions in the world.
	 */
	private static final Map<Position, Item> ground_items = new HashMap<>();	

	/**
	 * The method that creates global objects for a user.
	 * 
	 * @param player
	 *            The player to create the global objects for.
	 */
	public static final void createGlobalObjects(Player player) {
		global_objects.stream().filter(Objects::nonNull)
				.filter($it -> $it.getPosition().isWithinDistance(player.getPosition(), 32))
				.forEach($it -> player.queuePacket(new AddObjectPacket($it, true)));
	}
	
	public static final void createGlobalItems(Player player) {
		if (ground_items.isEmpty()) {
			return;
		}
		
		ground_items.values().stream().filter(Objects::nonNull).filter($it -> $it.getPosition().isWithinDistance(player.getPosition(), 32)).forEach($it -> player.queuePacket(new AddGroundItemPacket($it)));
	}

	/**
	 * Gets the custom object spawns.
	 * 
	 * @return The custom objects.
	 */
	public static final List<GameObject> getGlobalObjects() {
		return global_objects;
	}

	public static Map<Position, Item> getGroundItems() {
		return ground_items;
	}

}
