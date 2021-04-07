package rs2.game.model.entity.mob.npc.updating.mask;

import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.game.GamePacketBuilder;

public class NpcDoubleHitUpdateBlock extends NpcUpdateBlock {

    public NpcDoubleHitUpdateBlock() {
	super(8, UpdateFlag.DOUBLE_HIT);
    }

    @Override
    public void encode(Npc entity, GamePacketBuilder builder) {

    }

}
