package astraeus.game.model.entity.mob.npc.updating.mask;

import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.game.GamePacketBuilder;

public class NpcDoubleHitUpdateBlock extends NpcUpdateBlock {

    public NpcDoubleHitUpdateBlock() {
	super(8, UpdateFlag.DOUBLE_HIT);
    }

    @Override
    public void encode(Npc entity, GamePacketBuilder builder) {

    }

}
