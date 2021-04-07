package rs2.game.sync.task;

import rs2.game.model.entity.mob.player.Player;

/**
 * A {@link Synchronizable} implementation that executes a task after a {@link Player} has been synchronized with a client.
 *
 * @author Vult-R
 */
public final class PostPlayerUpdateTask implements Synchronizable {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PostPlayerUpdateTask} for the specified
	 * player.
	 *
	 * @param player The player.
	 */
	public PostPlayerUpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		// reset all updating flags
		player.postUpdate();
		
		// flush all awaiting packets to be send to the client
		player.getSession().flush();
	}

}
