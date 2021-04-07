package astraeus.game.sync.task;

import astraeus.game.model.entity.mob.npc.Npc;

/**
 * The {@link Synchronizable} implementation that executes a task before a {@link Npc} is synchronized with a client.
 * 
 * @author Vult-R
 */
public final class PreNpcUpdateTask implements Synchronizable {
	
	/**
	 * The npc this task will be executed for.
	 */
	private Npc npc;
	
	/**
	 * Creates a new {@link PreNpcUpdateTask}.
	 * 
	 * @param npc
	 * 		The npc this task will be executed for.
	 */
	public PreNpcUpdateTask(Npc npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		npc.preUpdate();
	}

}
