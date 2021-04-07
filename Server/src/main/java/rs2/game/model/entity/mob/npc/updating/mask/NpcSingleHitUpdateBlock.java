package rs2.game.model.entity.mob.npc.updating.mask;

import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.game.GamePacketBuilder;

/**
 * {@link NpcUpdateBlock} implementation that displays a single hit-splat on a mob.
 * 
 * @author SeVen
 */
public class NpcSingleHitUpdateBlock extends NpcUpdateBlock {

    /**
     * Creates a new {@link NpcSingleHitUpdateBlock}.
     */
    public NpcSingleHitUpdateBlock() {
	super(0x40, UpdateFlag.SINGLE_HIT);
    }

    @Override
    public void encode(Npc entity, GamePacketBuilder builder) {

    }

}
