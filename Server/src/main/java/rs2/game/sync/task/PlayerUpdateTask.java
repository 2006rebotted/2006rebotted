package rs2.game.sync.task;

import rs2.game.model.entity.mob.player.Player;

/**
 * A {@link Synchronizable} implementation that performs a task that synchronizes a {@link Player} with the client.
 *
 * @author Vult-R
 */
public final class PlayerUpdateTask implements Synchronizable {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PlayerUpdateTask} for the specified player.
	 *
	 * @param player The player.
	 * @param world The world.
	 */
	public PlayerUpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		player.update();
	}

}
