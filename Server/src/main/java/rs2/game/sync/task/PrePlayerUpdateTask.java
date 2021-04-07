package rs2.game.sync.task;

import rs2.game.model.entity.mob.player.Player;

/**
 * A {@link Synchronizable} implementation that executes a task before a {@link Player} is synchronized with a client.
 *
 * @author Vult-R
 */
public final class PrePlayerUpdateTask implements Synchronizable {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PrePlayerUpdateTask} for the specified
	 * player.
	 *
	 * @param player
	 *            The player.
	 */
	public PrePlayerUpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		player.preUpdate();
	}

}