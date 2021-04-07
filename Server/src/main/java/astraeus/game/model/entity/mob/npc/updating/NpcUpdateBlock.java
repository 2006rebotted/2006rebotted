package astraeus.game.model.entity.mob.npc.updating;

import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.update.UpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;

/**
 * The {@link UpdateBlock} implementation for mobs.
 * 
 * @author SeVen
 */
public abstract class NpcUpdateBlock extends UpdateBlock<Npc> {

	/**
	 * Creates a new {@link NpcUpdateBlock}.
	 * 
	 * @param flag
	 *            The enumerated type for this update.
	 */
	public NpcUpdateBlock(int mask, UpdateFlag flag) {
		super(mask, flag);
	}

}
