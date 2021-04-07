package rs2.game.sync.task;

import rs2.game.model.entity.mob.npc.Npc;

/**
 * The {@link Synchronizable} implementation that performs a task after a {@link Npc} has been synchronized with a client.
 * 
 * @author Vult-R
 */
public final class PostNpcUpdateTask implements Synchronizable {
	
	/**
	 * The npc this task will be executed for.
	 */
	private final Npc npc;
	
	/**
	 * Creates a new {@link PostNpcUpdateTask}.
	 * 
	 * @param npc
	 * 		The npc to execute this task for.
	 */
	public PostNpcUpdateTask(Npc npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		npc.postUpdate();
	}


}
