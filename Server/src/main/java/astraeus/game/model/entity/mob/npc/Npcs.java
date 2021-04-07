package astraeus.game.model.entity.mob.npc;

import astraeus.game.GameConstants;
import astraeus.game.model.Position;
import astraeus.game.model.World;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.util.RandomUtils;

import java.util.concurrent.TimeUnit;

/**
 * A static-utility class that contains useful methods for npcs.
 * 
 * @author Vult-R
 */
public final class Npcs {
	
	/**
	 * The private constructor to prevent instantiation of this class.
	 */
	private Npcs() {
		
	}

	/**
	 * Spawns a {@link Npc} into the game world.
	 * 
	 * @param spawn
	 *            The mob to spawn.
	 */
	public static void createSpawn(NpcSpawn spawn) {
		final Npc npc = new Npc(spawn.getId());
		
		if (World.world.getMobs().add(npc)) {
			npc.setPosition(spawn.getPosition());
			npc.setCreatedLocation(new Position(spawn.getPosition()));

			npc.setFacingDirection(spawn.getFacing());
			npc.setRandomWalk(spawn.isRandomWalk());
			npc.setRegistered(true);
			npc.setVisible(true);
			npc.getUpdateFlags().add(UpdateFlag.APPEARANCE);
		}
				
	}

	/**
	 * Handles randomly walking movement for a mob.
	 * 
	 * @param mob
	 *            The mob to handle.
	 */
	public static void handleRandomWalk(Npc mob) {
		if (mob.getRandomWalkTimer().elapsed(TimeUnit.SECONDS) >= 1) {

			if ((RandomUtils.random(5)) == 1) {

				int randomX = RandomUtils.random(-1, 1);
				int randomY = RandomUtils.random(-1, 1);

				Position nextLocation = new Position(mob.getPosition().getX() + randomX, mob.getPosition().getY() + randomY, mob.getPosition().getHeight());

				int distance = Position.getDistance(mob.getCreatedLocation(), nextLocation);

				if (mob.getInteractingEntity() == null && distance <= GameConstants.NPC_RANDOM_WALK_DISTANCE) {
					mob.getMovement().walk(nextLocation);
				} else if (mob.getInteractingEntity() == null) {
					mob.getMovement().walk(mob.getCreatedLocation());
				}
				mob.getRandomWalkTimer().reset();
			}
		}
	}

	/**
	 * Resets the facing direction of an entity to its default direction.
	 * 
	 * @param mob
	 *            The mob to reset.
	 */
	public static void resetFacingDirection(Npc mob) {
		if (mob.isRandomWalk() && mob.getInteractingEntity() == null) {
			return;
		}

		mob.setFacingDirection(mob.getFacingDirection());
	}

}
