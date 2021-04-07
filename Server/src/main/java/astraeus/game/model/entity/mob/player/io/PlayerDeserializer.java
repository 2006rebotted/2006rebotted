package astraeus.game.model.entity.mob.player.io;

import astraeus.game.model.entity.mob.player.Player;

/**
 * The class that contains a static-utility method to deserialize a {@code player}.
 * 
 * @author Seven
 */
public final class PlayerDeserializer {

	private PlayerDeserializer() {

	}
	
	/**
	 * Decodes the serialized objects for a {@code player).
	 * 
	 * @param player
	 * 		The player who's file is being decoded.
	 * 
	 * @return The decoded information.
	 */
	public static synchronized boolean deserialize(Player player) throws Exception {
		if (!PlayerDetails.deserialize(player)) {
			return false;
		}
		if (!PlayerContainer.deserialize(player)) {
			return false;
		}
		return true;		
	}

}
